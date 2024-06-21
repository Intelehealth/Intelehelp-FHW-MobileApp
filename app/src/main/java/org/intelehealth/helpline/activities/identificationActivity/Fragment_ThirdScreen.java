package org.intelehealth.helpline.activities.identificationActivity;

import static org.intelehealth.helpline.utilities.StringUtils.inputFilter_Others;
import static org.intelehealth.helpline.utilities.StringUtils.switch_as_caste_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_as_economic_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_as_education_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_bn_caste_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_bn_economic_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_bn_education_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_gu_caste_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_gu_economic_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_gu_education_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_hi_caste_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_hi_economic_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_hi_education_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_kn_caste_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_kn_economic_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_kn_education_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_ml_caste_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_ml_economic_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_ml_education_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_mr_caste_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_mr_economic_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_mr_education_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_or_caste_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_or_economic_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_or_education_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_ru_caste_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_ru_economic_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_ru_education_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_ta_caste_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_ta_economic_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_ta_education_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_te_caste_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_te_economic_edit;
import static org.intelehealth.helpline.utilities.StringUtils.switch_te_education_edit;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;

import org.intelehealth.helpline.R;
import org.intelehealth.helpline.activities.patientDetailActivity.PatientDetailActivity2;
import org.intelehealth.helpline.app.IntelehealthApplication;
import org.intelehealth.helpline.database.dao.ImagesDAO;
import org.intelehealth.helpline.database.dao.ImagesPushDAO;
import org.intelehealth.helpline.database.dao.PatientsDAO;
import org.intelehealth.helpline.database.dao.SyncDAO;
import org.intelehealth.helpline.models.Patient;
import org.intelehealth.helpline.models.dto.PatientAttributesDTO;
import org.intelehealth.helpline.models.dto.PatientDTO;
import org.intelehealth.helpline.utilities.DateAndTimeUtils;
import org.intelehealth.helpline.utilities.Logger;
import org.intelehealth.helpline.utilities.NetworkConnection;
import org.intelehealth.helpline.utilities.SessionManager;
import org.intelehealth.helpline.utilities.StringUtils;
import org.intelehealth.helpline.utilities.exception.DAOException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Fragment_ThirdScreen extends Fragment {
    private static final String TAG = Fragment_ThirdScreen.class.getSimpleName();
    private PatientDTO patientDTO;
    private View view;
    SessionManager sessionManager = null;
    private ArrayAdapter<CharSequence> educationAdapter;
    private ArrayAdapter<CharSequence> casteAdapter;
    private ArrayAdapter<CharSequence> economicStatusAdapter;
    private EditText mRelationNameEditText, mOccupationEditText, mNationalIDEditText, etNoBelongsToOther;
    private Spinner mCasteSpinner, mEducationSpinner, mEconomicstatusSpinner, mNumberBelongsToSpinner;
    private ImageView personal_icon, address_icon, other_icon;
    private Button frag3_btn_back, frag3_btn_next;
    private TextView mRelationNameErrorTextView, mOccupationErrorTextView, mCasteErrorTextView, mEducationErrorTextView,
            mEconomicErrorTextView, mNoBelongsToErrorTextView, mTypeOfCallErrorTextview, mNoBelongsToErrorTvOther;
    ImagesDAO imagesDAO = new ImagesDAO();
    private Fragment_SecondScreen secondScreen;
    boolean fromThirdScreen = false, fromSecondScreen = false;
    Patient patient1 = new Patient();
    PatientsDAO patientsDAO = new PatientsDAO();
    String patientID_edit;
    boolean patient_detail = false;
    private ArrayAdapter<CharSequence> noBelongsToAdapter;
    private RadioGroup radioGroupCallType;
    private RadioButton rbIncoming, rbOutgoing;
    private String selectedCallType = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_registration_thirdscreen, container, false);
        setLocale(getContext());
        return view;
    }


    public Context setLocale(Context context) {
        SessionManager sessionManager1 = new SessionManager(context);
        String appLanguage = sessionManager1.getAppLanguage();
        Resources res = context.getResources();
        Configuration conf = res.getConfiguration();
        Locale locale = new Locale(appLanguage);
        Locale.setDefault(locale);
        conf.setLocale(locale);
        context.createConfigurationContext(conf);
        DisplayMetrics dm = res.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            conf.setLocales(new LocaleList(locale));
        } else {
            conf.locale = locale;
        }
        res.updateConfiguration(conf, dm);
        return context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManager = new SessionManager(getActivity());

        personal_icon = getActivity().findViewById(R.id.addpatient_icon);
        address_icon = getActivity().findViewById(R.id.addresslocation_icon);
        other_icon = getActivity().findViewById(R.id.other_icon);
        frag3_btn_back = getActivity().findViewById(R.id.frag3_btn_back);
        frag3_btn_next = getActivity().findViewById(R.id.frag3_btn_next);

        mRelationNameEditText = view.findViewById(R.id.relation_edittext);
        mNationalIDEditText = view.findViewById(R.id.national_ID_editText);
        //mNationalIDEditText.setFilters(new InputFilter[]{new InputFilter.AllCaps(), new InputFilter.LengthFilter(24)}); //all capital input
        mNationalIDEditText.setFilters(new InputFilter[]{
                new InputFilter.AllCaps(),
                new InputFilter.LengthFilter(24),
                emojiFilter
        });
        mOccupationEditText = view.findViewById(R.id.occupation_editText);
        mCasteSpinner = view.findViewById(R.id.caste_spinner);
        mEducationSpinner = view.findViewById(R.id.education_spinner);
        mEconomicstatusSpinner = view.findViewById(R.id.economicstatus_spinner);
        mNumberBelongsToSpinner = view.findViewById(R.id.spinner_no_belongs_to);
        radioGroupCallType = view.findViewById(R.id.radioGroup_call_type);
        rbIncoming = view.findViewById(R.id.rb_incoming);
        rbOutgoing = view.findViewById(R.id.rb_outgoing);
        etNoBelongsToOther = view.findViewById(R.id.et_no_belongs_to_other_option);

        mRelationNameErrorTextView = view.findViewById(R.id.relation_error);
        mOccupationErrorTextView = view.findViewById(R.id.occupation_error);
        mCasteErrorTextView = view.findViewById(R.id.caste_error);
        mEducationErrorTextView = view.findViewById(R.id.education_error);
        mEconomicErrorTextView = view.findViewById(R.id.economic_error);
        mNoBelongsToErrorTextView = view.findViewById(R.id.tv_error_no_belongs_to);
        mTypeOfCallErrorTextview = view.findViewById(R.id.tv_error_type_of_call);
        mNoBelongsToErrorTvOther = view.findViewById(R.id.tv_error_no_belongs_to_other);

        mRelationNameEditText.addTextChangedListener(new MyTextWatcher(mRelationNameEditText));
        mRelationNameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25), inputFilter_Others}); //maxlength 25
        mRelationNameEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25), inputFilter_Others}); //maxlength 25

        /*mNationalIDEditText.addTextChangedListener(new MyTextWatcher(mNationalIDEditText));
        mNationalIDEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18), inputFilter_Others});*/ //maxlength 25

        mOccupationEditText.addTextChangedListener(new MyTextWatcher(mOccupationEditText));
        mOccupationEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25), inputFilter_Others}); //maxlength 25
        etNoBelongsToOther.addTextChangedListener(new MyTextWatcher(etNoBelongsToOther));
        etNoBelongsToOther.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25), inputFilter_Others}); //maxlength 25

        secondScreen = new Fragment_SecondScreen();
        if (getArguments() != null) {
            patientDTO = (PatientDTO) getArguments().getSerializable("patientDTO");
            fromSecondScreen = getArguments().getBoolean("fromSecondScreen");
            patient_detail = getArguments().getBoolean("patient_detail");
            if (patient_detail) {
                frag3_btn_back.setVisibility(View.GONE);
                frag3_btn_next.setText(getString(R.string.save));
            } else {
                // do nothing...
            }
        }

        mCasteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    mCasteErrorTextView.setVisibility(View.GONE);
                    mCasteSpinner.setBackgroundResource(R.drawable.ui2_spinner_background_new);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mEducationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    mEducationErrorTextView.setVisibility(View.GONE);
                    mEducationSpinner.setBackgroundResource(R.drawable.ui2_spinner_background_new);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mEconomicstatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    mEconomicErrorTextView.setVisibility(View.GONE);
                    mEconomicstatusSpinner.setBackgroundResource(R.drawable.ui2_spinner_background_new);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mNumberBelongsToSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    mNoBelongsToErrorTextView.setVisibility(View.GONE);
                    mNumberBelongsToSpinner.setBackgroundResource(R.drawable.ui2_spinner_background_new);
                    String selectedItem = mNumberBelongsToSpinner.getSelectedItem().toString();
                    if (!selectedItem.isEmpty() && selectedItem.equalsIgnoreCase("Other")) {
                        etNoBelongsToOther.setVisibility(View.VISIBLE);
                    } else {
                        etNoBelongsToOther.setText("");
                        etNoBelongsToOther.setVisibility(View.GONE);
                        mNoBelongsToErrorTvOther.setVisibility(View.GONE);
                    }
                } else {
                    etNoBelongsToOther.setText("");
                    etNoBelongsToOther.setVisibility(View.GONE);
                    mNoBelongsToErrorTvOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        radioGroupCallType.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            boolean isChecked = checkedRadioButton.isChecked();
            if (isChecked) {
                selectedCallType = checkedRadioButton.getText().toString();
                //myProfilePOJO.setNewGender(String.valueOf(selectedValue.charAt(0)));

                switch (selectedCallType) {
                    case "Incoming":
                        rbIncoming.setButtonDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ui2_ic_selected_green));
                        rbOutgoing.setButtonDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ui2_ic_circle));
                        mTypeOfCallErrorTextview.setVisibility(View.GONE);
                        break;
                    case "Outgoing":
                        rbIncoming.setButtonDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ui2_ic_circle));
                        rbOutgoing.setButtonDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ui2_ic_selected_green));
                        mTypeOfCallErrorTextview.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    class MyTextWatcher implements TextWatcher {
        EditText editText;

        MyTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String val = editable.toString().trim();
            if (this.editText.getId() == R.id.et_no_belongs_to_other_option) {
                if (mNumberBelongsToSpinner.getSelectedItem().toString().equalsIgnoreCase("other") && val.isEmpty()) {
                    mNoBelongsToErrorTvOther.setVisibility(View.VISIBLE);
                    mNoBelongsToErrorTvOther.setText(getString(R.string.error_field_required));
                    etNoBelongsToOther.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
                } else {
                    mNoBelongsToErrorTvOther.setVisibility(View.GONE);
                    etNoBelongsToOther.setBackgroundResource(R.drawable.bg_input_fieldnew);
                    mNoBelongsToErrorTextView.setVisibility(View.GONE);
                }
            }  /*else if (this.editText.getId() == R.id.occupation_editText) {
                if (val.isEmpty()) {
                    mOccupationErrorTextView.setVisibility(View.VISIBLE);
                    mOccupationErrorTextView.setText(getString(R.string.error_field_required));
                    mOccupationEditText.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
                } else {
                    mOccupationErrorTextView.setVisibility(View.GONE);
                    mOccupationEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);
                }
            }*/
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        personal_icon.setImageDrawable(getResources().getDrawable(R.drawable.addpatient_icon_done));
        address_icon.setImageDrawable(getResources().getDrawable(R.drawable.addresslocation_icon_done));
        other_icon.setImageDrawable(getResources().getDrawable(R.drawable.other_icon));

        frag3_btn_back.setOnClickListener(v -> {
            onBackInsertIntoPatientDTO();
        });

        frag3_btn_next.setOnClickListener(v -> {
//                Intent intent = new Intent(getActivity(), PatientDetailActivity2.class);
//                startActivity(intent);
            if (isDataValid())
                onPatientCreateClicked();
            //else
            //Toast.makeText(requireActivity(), "Please fill the required fields", Toast.LENGTH_SHORT).show();
        });

        // caste spinner
        Resources res = getResources();
        try {
            String casteLanguage = "caste_" + sessionManager.getAppLanguage();
            int castes = res.getIdentifier(casteLanguage, "array", getActivity().getApplicationContext().getPackageName());
            if (castes != 0) {
                casteAdapter = ArrayAdapter.createFromResource(getActivity(),
                        castes, R.layout.simple_spinner_item_1);
                casteAdapter.setDropDownViewResource(R.layout.ui2_custome_dropdown_item_view);
            }
            mCasteSpinner.setAdapter(casteAdapter);
            mCasteSpinner.setPopupBackgroundDrawable(getActivity().getDrawable(R.drawable.popup_menu_background));

        } catch (Exception e) {
//            Toast.makeText(this, R.string.education_values_missing, Toast.LENGTH_SHORT).show();
            Logger.logE("Identification", "#648", e);
        }

        // education spinner
        try {
            String educationLanguage = "education_" + sessionManager.getAppLanguage();
            int educations = res.getIdentifier(educationLanguage, "array", getActivity().getApplicationContext().getPackageName());
            if (educations != 0) {
                educationAdapter = ArrayAdapter.createFromResource(getActivity(),
                        educations, R.layout.simple_spinner_item_1);
                educationAdapter.setDropDownViewResource(R.layout.ui2_custome_dropdown_item_view);
            }
            mEducationSpinner.setAdapter(educationAdapter);
            mEducationSpinner.setPopupBackgroundDrawable(getActivity().getDrawable(R.drawable.popup_menu_background));
        } catch (Exception e) {
//            Toast.makeText(this, R.string.education_values_missing, Toast.LENGTH_SHORT).show();
            Logger.logE("Identification", "#648", e);
        }

        // economic spinner
        try {
            String economicLanguage = "economic_" + sessionManager.getAppLanguage();
            int economics = res.getIdentifier(economicLanguage, "array", getActivity().getApplicationContext().getPackageName());
            if (economics != 0) {
                economicStatusAdapter = ArrayAdapter.createFromResource(getActivity(),
                        economics, R.layout.simple_spinner_item_1);
                economicStatusAdapter.setDropDownViewResource(R.layout.ui2_custome_dropdown_item_view);
            }
            mEconomicstatusSpinner.setAdapter(economicStatusAdapter);
            mEconomicstatusSpinner.setPopupBackgroundDrawable(getActivity().getDrawable(R.drawable.popup_menu_background));
        } catch (Exception e) {
//            Toast.makeText(this, R.string.education_values_missing, Toast.LENGTH_SHORT).show();
            Logger.logE("Identification", "#648", e);
        }

        setDataToSpinners();

        if (patientDTO.getSon_dau_wife() != null && !patientDTO.getSon_dau_wife().isEmpty())
            mRelationNameEditText.setText(patientDTO.getSon_dau_wife());
        Log.v(TAG, "relation: " + patientDTO.getSon_dau_wife());

        if (patientDTO.getOccupation() != null && !patientDTO.getOccupation().isEmpty())
            mOccupationEditText.setText(patientDTO.getOccupation());

        if (patientDTO.getNationalID() != null && !patientDTO.getNationalID().isEmpty())
            mNationalIDEditText.setText(patientDTO.getNationalID());


        // setting screen in edit for spinners...
        if (fromThirdScreen || fromSecondScreen) {
            selectedCallType = patientDTO.getTypeOfCall();
            if (selectedCallType != null && !selectedCallType.isEmpty()) {

                if (selectedCallType.equalsIgnoreCase("Incoming")) {
                    rbIncoming.setButtonDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ui2_ic_selected_green));
                    rbOutgoing.setButtonDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ui2_ic_circle));

                } else if (selectedCallType.equalsIgnoreCase("Outgoing")) {
                    rbIncoming.setButtonDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ui2_ic_circle));
                    rbOutgoing.setButtonDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ui2_ic_selected_green));
                }
            }

            //caste
            if (patientDTO.getCaste() != null) {
                if (patientDTO.getCaste().equals(getResources().getString(R.string.not_provided)))
                    mCasteSpinner.setSelection(0);
//            else
//                caste_spinner.setSelection(casteAdapter.getPosition(patientDTO.getCaste()));

                else {
                    if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                        String caste = switch_hi_caste_edit(patientDTO.getCaste());
                        mCasteSpinner.setSelection(casteAdapter.getPosition(caste));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
                        String caste = switch_or_caste_edit(patientDTO.getCaste());
                        mCasteSpinner.setSelection(casteAdapter.getPosition(caste));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("te")) {
                        String caste = switch_te_caste_edit(patientDTO.getCaste());
                        mCasteSpinner.setSelection(casteAdapter.getPosition(caste));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("mr")) {
                        String caste = switch_mr_caste_edit(patientDTO.getCaste());
                        mCasteSpinner.setSelection(casteAdapter.getPosition(caste));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("as")) {
                        String caste = switch_as_caste_edit(patientDTO.getCaste());
                        mCasteSpinner.setSelection(casteAdapter.getPosition(caste));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ml")) {
                        String caste = switch_ml_caste_edit(patientDTO.getCaste());
                        mCasteSpinner.setSelection(casteAdapter.getPosition(caste));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("kn")) {
                        String caste = switch_kn_caste_edit(patientDTO.getCaste());
                        mCasteSpinner.setSelection(casteAdapter.getPosition(caste));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ru")) {
                        String caste = switch_ru_caste_edit(patientDTO.getCaste());
                        mCasteSpinner.setSelection(casteAdapter.getPosition(caste));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("gu")) {
                        String caste = switch_gu_caste_edit(patientDTO.getCaste());
                        mCasteSpinner.setSelection(casteAdapter.getPosition(caste));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("bn")) {
                        String caste = switch_bn_caste_edit(patientDTO.getCaste());
                        mCasteSpinner.setSelection(casteAdapter.getPosition(caste));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ta")) {
                        String caste = switch_ta_caste_edit(patientDTO.getCaste());
                        mCasteSpinner.setSelection(casteAdapter.getPosition(caste));
                    } else {
                        mCasteSpinner.setSelection(casteAdapter.getPosition(patientDTO.getCaste()));
                    }
                }
            }

            //education status
            if (patientDTO.getEducation() != null) {
                if (patientDTO.getEducation().equals(getResources().getString(R.string.not_provided)))
                    mEducationSpinner.setSelection(0);
//            else
//                education_spinner.setSelection(educationAdapter != null ? educationAdapter.getPosition(patientDTO.getEducation()) : 0);

                else {
                    if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                        String education = switch_hi_education_edit(patientDTO.getEducation());
                        mEducationSpinner.setSelection(educationAdapter != null ? educationAdapter.getPosition(education) : 0);
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
                        String education = switch_or_education_edit(patientDTO.getEducation());
                        mEducationSpinner.setSelection(educationAdapter != null ? educationAdapter.getPosition(education) : 0);
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("te")) {
                        String education = switch_te_education_edit(patientDTO.getEducation());
                        mEducationSpinner.setSelection(educationAdapter != null ? educationAdapter.getPosition(education) : 0);
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("mr")) {
                        String education = switch_mr_education_edit(patientDTO.getEducation());
                        mEducationSpinner.setSelection(educationAdapter != null ? educationAdapter.getPosition(education) : 0);
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("as")) {
                        String education = switch_as_education_edit(patientDTO.getEducation());
                        mEducationSpinner.setSelection(educationAdapter != null ? educationAdapter.getPosition(education) : 0);
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("gu")) {
                        String education = switch_gu_education_edit(patientDTO.getEducation());
                        mEducationSpinner.setSelection(educationAdapter != null ? educationAdapter.getPosition(education) : 0);
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ta")) {
                        String education = switch_ta_education_edit(patientDTO.getEducation());
                        mEducationSpinner.setSelection(educationAdapter != null ? educationAdapter.getPosition(education) : 0);
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("bn")) {
                        String education = switch_bn_education_edit(patientDTO.getEducation());
                        mEducationSpinner.setSelection(educationAdapter != null ? educationAdapter.getPosition(education) : 0);
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ml")) {
                        String education = switch_ml_education_edit(patientDTO.getEducation());
                        mEducationSpinner.setSelection(educationAdapter != null ? educationAdapter.getPosition(education) : 0);
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("kn")) {
                        String education = switch_kn_education_edit(patientDTO.getEducation());
                        mEducationSpinner.setSelection(educationAdapter != null ? educationAdapter.getPosition(education) : 0);
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ru")) {
                        String education = switch_ru_education_edit(patientDTO.getEducation());
                        mEducationSpinner.setSelection(educationAdapter != null ? educationAdapter.getPosition(education) : 0);
                    } else {
                        mEducationSpinner.setSelection(educationAdapter != null ? educationAdapter.getPosition(patientDTO.getEducation()) : 0);
                    }
                }
            }


            // economic statius
            if (patientDTO.getEconomic() != null) {
                if (patientDTO.getEconomic().equals(getResources().getString(R.string.not_provided)))
                    mEconomicstatusSpinner.setSelection(0);
//            else
//                economicstatus_spinner.setSelection(economicStatusAdapter.getPosition(patientDTO.getEconomic()));

                else {
                    if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                        String economic = switch_hi_economic_edit(patientDTO.getEconomic());
                        mEconomicstatusSpinner.setSelection(economicStatusAdapter.getPosition(economic));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
                        String economic = switch_or_economic_edit(patientDTO.getEconomic());
                        mEconomicstatusSpinner.setSelection(economicStatusAdapter.getPosition(economic));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("te")) {
                        String economic = switch_te_economic_edit(patientDTO.getEconomic());
                        mEconomicstatusSpinner.setSelection(economicStatusAdapter.getPosition(economic));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("mr")) {
                        String economic = switch_mr_economic_edit(patientDTO.getEconomic());
                        mEconomicstatusSpinner.setSelection(economicStatusAdapter.getPosition(economic));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("as")) {
                        String economic = switch_as_economic_edit(patientDTO.getEconomic());
                        mEconomicstatusSpinner.setSelection(economicStatusAdapter.getPosition(economic));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ml")) {
                        String economic = switch_ml_economic_edit(patientDTO.getEconomic());
                        mEconomicstatusSpinner.setSelection(economicStatusAdapter.getPosition(economic));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("kn")) {
                        String economic = switch_kn_economic_edit(patientDTO.getEconomic());
                        mEconomicstatusSpinner.setSelection(economicStatusAdapter.getPosition(economic));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ru")) {
                        String economic = switch_ru_economic_edit(patientDTO.getEconomic());
                        mEconomicstatusSpinner.setSelection(economicStatusAdapter.getPosition(economic));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("gu")) {
                        String economic = switch_gu_economic_edit(patientDTO.getEconomic());
                        mEconomicstatusSpinner.setSelection(economicStatusAdapter.getPosition(economic));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("bn")) {
                        String economic = switch_bn_economic_edit(patientDTO.getEconomic());
                        mEconomicstatusSpinner.setSelection(economicStatusAdapter.getPosition(economic));
                    } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ta")) {
                        String economic = switch_ta_economic_edit(patientDTO.getEconomic());
                        mEconomicstatusSpinner.setSelection(economicStatusAdapter.getPosition(economic));
                    } else {
                        mEconomicstatusSpinner.setSelection(economicStatusAdapter.getPosition(patientDTO.getEconomic()));
                    }
                }


                // No belongs to
                if (patientDTO.getNumberBelongsTo() != null) {
                    Log.d(TAG, "onActivityCreated:other value:  " + patientDTO.getNumberBelongsTo());
                    if (patientDTO.getNumberBelongsTo().equals(getResources().getString(R.string.not_provided)))
                        mNumberBelongsToSpinner.setSelection(0);
//            else
//                economicstatus_spinner.setSelection(economicStatusAdapter.getPosition(patientDTO.getEconomic()));
                    else if (patientDTO.getNumberBelongsTo().contains("Other")) {
                        Log.d(TAG, "onActivityCreated: on other case");
                        mNumberBelongsToSpinner.setSelection(noBelongsToAdapter.getPosition("Other"));
                        String[] parts = patientDTO.getNumberBelongsTo().split("-");
                        etNoBelongsToOther.setText(parts[1]);
                        etNoBelongsToOther.setVisibility(View.VISIBLE);
                    } else {
                        etNoBelongsToOther.setVisibility(View.GONE);
                        if (sessionManager.getAppLanguage().equalsIgnoreCase("hi")) {
                            String noBelongsTo = switch_hi_economic_edit(patientDTO.getNumberBelongsTo());
                            mNumberBelongsToSpinner.setSelection(noBelongsToAdapter.getPosition(noBelongsTo));
                        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("or")) {
                            String noBelongsTo = switch_or_economic_edit(patientDTO.getNumberBelongsTo());
                            mNumberBelongsToSpinner.setSelection(noBelongsToAdapter.getPosition(noBelongsTo));
                        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("te")) {
                            String noBelongsTo = switch_te_economic_edit(patientDTO.getNumberBelongsTo());
                            mNumberBelongsToSpinner.setSelection(noBelongsToAdapter.getPosition(noBelongsTo));
                        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("mr")) {
                            String noBelongsTo = switch_mr_economic_edit(patientDTO.getNumberBelongsTo());
                            mNumberBelongsToSpinner.setSelection(noBelongsToAdapter.getPosition(noBelongsTo));
                        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("as")) {
                            String noBelongsTo = switch_as_economic_edit(patientDTO.getNumberBelongsTo());
                            mNumberBelongsToSpinner.setSelection(noBelongsToAdapter.getPosition(noBelongsTo));
                        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ml")) {
                            String noBelongsTo = switch_ml_economic_edit(patientDTO.getNumberBelongsTo());
                            mNumberBelongsToSpinner.setSelection(noBelongsToAdapter.getPosition(noBelongsTo));
                        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("kn")) {
                            String noBelongsTo = switch_kn_economic_edit(patientDTO.getNumberBelongsTo());
                            mNumberBelongsToSpinner.setSelection(noBelongsToAdapter.getPosition(noBelongsTo));
                        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ru")) {
                            String noBelongsTo = switch_ru_economic_edit(patientDTO.getNumberBelongsTo());
                            mNumberBelongsToSpinner.setSelection(noBelongsToAdapter.getPosition(noBelongsTo));
                        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("gu")) {
                            String noBelongsTo = switch_gu_economic_edit(patientDTO.getNumberBelongsTo());
                            mNumberBelongsToSpinner.setSelection(noBelongsToAdapter.getPosition(noBelongsTo));
                        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("bn")) {
                            String noBelongsTo = switch_bn_economic_edit(patientDTO.getNumberBelongsTo());
                            mNumberBelongsToSpinner.setSelection(noBelongsToAdapter.getPosition(noBelongsTo));
                        } else if (sessionManager.getAppLanguage().equalsIgnoreCase("ta")) {
                            String noBelongsTo = switch_ta_economic_edit(patientDTO.getNumberBelongsTo());
                            mNumberBelongsToSpinner.setSelection(noBelongsToAdapter.getPosition(noBelongsTo));
                        } else {
                            mNumberBelongsToSpinner.setSelection(noBelongsToAdapter.getPosition(patientDTO.getNumberBelongsTo()));
                        }
                    }
                }

                /*if (patientDTO.getTypeOfCall() != null && !patientDTO.getTypeOfCall().isEmpty()) {
                    if (selectedCallType.equalsIgnoreCase("incoming")) {
                        rbIncoming.setChecked(true);
                    } else {
                        rbOutgoing.setChecked(true);
                    }
                }*/
            }

        }
    }

    private boolean isDataValid() {
        String noBelongToValue = mNumberBelongsToSpinner.getSelectedItem().toString();
        Log.d(TAG, "isDataValid: noBelongToValue:" + noBelongToValue);
        if (mNumberBelongsToSpinner.getSelectedItem().toString().isEmpty() || mNumberBelongsToSpinner.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            mNoBelongsToErrorTextView.setVisibility(View.VISIBLE);
            mNumberBelongsToSpinner.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
            mNumberBelongsToSpinner.requestFocus();
            return false;
        } else if (noBelongToValue.equalsIgnoreCase("other") && etNoBelongsToOther.getText().toString().isEmpty()) {
            Log.d(TAG, "isDataValid: in else other");
            etNoBelongsToOther.setVisibility(View.VISIBLE);
            mNoBelongsToErrorTvOther.setVisibility(View.VISIBLE);
            etNoBelongsToOther.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
            return false;
        } else {
            mNoBelongsToErrorTextView.setVisibility(View.GONE);
            mNumberBelongsToSpinner.setBackgroundResource(R.drawable.ui2_spinner_background_new);
            mNoBelongsToErrorTvOther.setVisibility(View.GONE);
            etNoBelongsToOther.setBackgroundResource(R.drawable.bg_input_fieldnew);
        }
        Log.d(TAG, "isDataValid: selectedCallType : " + selectedCallType);

        if (selectedCallType == null || selectedCallType.isEmpty()) {
            mTypeOfCallErrorTextview.setVisibility(View.VISIBLE);
            return false;
        } else {
            mTypeOfCallErrorTextview.setVisibility(View.GONE);
            return true;
        }
    }

    private void setDataToSpinners() {
        Resources res = getResources();
        try {
            String noBelongsTo = "no_belongs_to_" + sessionManager.getAppLanguage();
            int noBelongsToValue = res.getIdentifier(noBelongsTo, "array", getActivity().getApplicationContext().getPackageName());
            if (noBelongsToValue != 0) {
                noBelongsToAdapter = ArrayAdapter.createFromResource(getActivity(),
                        noBelongsToValue, R.layout.simple_spinner_item_1);
                noBelongsToAdapter.setDropDownViewResource(R.layout.ui2_custome_dropdown_item_view);
            }
            mNumberBelongsToSpinner.setAdapter(noBelongsToAdapter);
            mNumberBelongsToSpinner.setPopupBackgroundDrawable(getActivity().getDrawable(R.drawable.popup_menu_background));

        } catch (Exception e) {
//            Toast.makeText(this, R.string.education_values_missing, Toast.LENGTH_SHORT).show();
            Logger.logE("Identification", "#648", e);
        }
    }

    private void onBackInsertIntoPatientDTO() {
        patientDTO.setSon_dau_wife(mRelationNameEditText.getText().toString());
        patientDTO.setOccupation(mOccupationEditText.getText().toString());
        patientDTO.setNationalID(mNationalIDEditText.getText().toString());
        patientDTO.setCaste(StringUtils.getValue(mCasteSpinner.getSelectedItem().toString()));
        patientDTO.setEducation(StringUtils.getValue(mEducationSpinner.getSelectedItem().toString()));
        patientDTO.setEconomic(StringUtils.getValue(mEconomicstatusSpinner.getSelectedItem().toString()));
        //patientDTO.setNumberBelongsTo(StringUtils.getValue(mNumberBelongsToSpinner.getSelectedItem().toString()));
        patientDTO.setTypeOfCall(selectedCallType);
        if (mNumberBelongsToSpinner.getSelectedItem().toString().equalsIgnoreCase("other"))
            patientDTO.setNumberBelongsTo(StringUtils.getValue(mNumberBelongsToSpinner.getSelectedItem().toString()) + "-" + etNoBelongsToOther.getText().toString());
        else
            patientDTO.setNumberBelongsTo(StringUtils.getValue(mNumberBelongsToSpinner.getSelectedItem().toString()));
        Bundle bundle = new Bundle();
        bundle.putSerializable("patientDTO", (Serializable) patientDTO);
        bundle.putBoolean("fromThirdScreen", true);
        bundle.putBoolean("patient_detail", patient_detail);
        secondScreen.setArguments(bundle); // passing data to Fragment

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_firstscreen, secondScreen)
                .commit();
    }

    private void onPatientCreateClicked() {
        patientDTO.setSon_dau_wife(mRelationNameEditText.getText().toString());
        patientDTO.setOccupation(mOccupationEditText.getText().toString());
        patientDTO.setNationalID(mNationalIDEditText.getText().toString());
        patientDTO.setCaste(StringUtils.getValue(mCasteSpinner.getSelectedItem().toString()));
        patientDTO.setEducation(StringUtils.getValue(mEducationSpinner.getSelectedItem().toString()));
        patientDTO.setEconomic(StringUtils.getValue(mEconomicstatusSpinner.getSelectedItem().toString()));
        patientDTO.setTypeOfCall(selectedCallType);
        //patientDTO.setNumberBelongsTo(StringUtils.getValue(mNumberBelongsToSpinner.getSelectedItem().toString()));
        if (mNumberBelongsToSpinner.getSelectedItem().toString().equalsIgnoreCase("other"))
            patientDTO.setNumberBelongsTo(StringUtils.getValue(mNumberBelongsToSpinner.getSelectedItem().toString()) + "-" + etNoBelongsToOther.getText().toString());
        else
            patientDTO.setNumberBelongsTo(StringUtils.getValue(mNumberBelongsToSpinner.getSelectedItem().toString()));
        PatientsDAO patientsDAO = new PatientsDAO();
        PatientAttributesDTO patientAttributesDTO = new PatientAttributesDTO();
        List<PatientAttributesDTO> patientAttributesDTOList = new ArrayList<>();

        Gson gson = new Gson();
        boolean cancel = false;
        View focusView = null;

        // validation - start
        /*if (mRelationNameEditText.getText().toString().equals("")) {
            mRelationNameErrorTextView.setVisibility(View.VISIBLE);
            mRelationNameErrorTextView.setText(getString(R.string.error_field_required));
            mRelationNameEditText.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
            mRelationNameEditText.requestFocus();
            return;
        } else {
            mRelationNameErrorTextView.setVisibility(View.GONE);
            mRelationNameEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);
        }

        if (mOccupationEditText.getText().toString().equals("")) {
            mOccupationErrorTextView.setVisibility(View.VISIBLE);
            mOccupationErrorTextView.setText(getString(R.string.error_field_required));
            mOccupationEditText.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
            mOccupationEditText.requestFocus();
            return;
        } else {
            mOccupationErrorTextView.setVisibility(View.GONE);
            mOccupationEditText.setBackgroundResource(R.drawable.bg_input_fieldnew);
        }

        if (mCasteSpinner.getSelectedItemPosition() == 0) {
            mCasteErrorTextView.setVisibility(View.VISIBLE);
            mCasteErrorTextView.setText(getString(R.string.error_field_required));
            mCasteSpinner.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
            mCasteSpinner.requestFocus();
            return;
        } else {
            mCasteErrorTextView.setVisibility(View.GONE);
            mCasteSpinner.setBackgroundResource(R.drawable.ui2_spinner_background_new);
        }

        if (mEducationSpinner.getSelectedItemPosition() == 0) {
            mEducationErrorTextView.setVisibility(View.VISIBLE);
            mEducationErrorTextView.setText(getString(R.string.error_field_required));
            mEducationSpinner.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
            mEducationSpinner.requestFocus();
            return;
        } else {
            mEducationErrorTextView.setVisibility(View.GONE);
            mEducationSpinner.setBackgroundResource(R.drawable.ui2_spinner_background_new);
        }

        if (mEconomicstatusSpinner.getSelectedItemPosition() == 0) {
            mEconomicErrorTextView.setVisibility(View.VISIBLE);
            mEconomicErrorTextView.setText(getString(R.string.error_field_required));
            mEconomicstatusSpinner.setBackgroundResource(R.drawable.input_field_error_bg_ui2);
            mEconomicstatusSpinner.requestFocus();
            return;
        } else {
            mEconomicErrorTextView.setVisibility(View.GONE);
            mEconomicstatusSpinner.setBackgroundResource(R.drawable.ui2_spinner_background_new);
        }*/
        // validation - end


        /**
         *  entering value in dataset start
         */

        String uuid = patientDTO.getUuid();

        if (patientDTO.getPhonenumber() != null && !patientDTO.getPhonenumber().isEmpty()) {
            // mobile no adding in patient attributes.
            patientAttributesDTO = new PatientAttributesDTO();
            patientAttributesDTO.setUuid(UUID.randomUUID().toString());
            patientAttributesDTO.setPatientuuid(uuid);
            patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Telephone Number"));
            patientAttributesDTO.setValue(StringUtils.getValue(patientDTO.getPhonenumber()));
            patientAttributesDTOList.add(patientAttributesDTO);
        }

        // son/daughter/wife of
        patientAttributesDTO = new PatientAttributesDTO();
        patientAttributesDTO.setUuid(UUID.randomUUID().toString());
        patientAttributesDTO.setPatientuuid(uuid);
        patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Son/wife/daughter"));
        patientAttributesDTO.setValue(StringUtils.getValue(mRelationNameEditText.getText().toString()));
        patientAttributesDTOList.add(patientAttributesDTO);

        patientAttributesDTO = new PatientAttributesDTO();
        patientAttributesDTO.setUuid(UUID.randomUUID().toString());
        patientAttributesDTO.setPatientuuid(uuid);
        patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("NationalID"));
        patientAttributesDTO.setValue(StringUtils.getValue(mNationalIDEditText.getText().toString()));
        patientAttributesDTOList.add(patientAttributesDTO);

        // occupation
        patientAttributesDTO = new PatientAttributesDTO();
        patientAttributesDTO.setUuid(UUID.randomUUID().toString());
        patientAttributesDTO.setPatientuuid(uuid);
        patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("occupation"));
        patientAttributesDTO.setValue(StringUtils.getValue(mOccupationEditText.getText().toString()));
        patientAttributesDTOList.add(patientAttributesDTO);

        // caste
        patientAttributesDTO = new PatientAttributesDTO();
        patientAttributesDTO.setUuid(UUID.randomUUID().toString());
        patientAttributesDTO.setPatientuuid(uuid);
        patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("caste"));
        patientAttributesDTO.setValue(StringUtils.getProvided(mCasteSpinner));
        patientAttributesDTOList.add(patientAttributesDTO);
        Log.v(TAG, "values_caste: " + patientAttributesDTO.toString());

        // education
        patientAttributesDTO = new PatientAttributesDTO();
        patientAttributesDTO.setUuid(UUID.randomUUID().toString());
        patientAttributesDTO.setPatientuuid(uuid);
        patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Education Level"));
        patientAttributesDTO.setValue(StringUtils.getProvided(mEducationSpinner));
        patientAttributesDTOList.add(patientAttributesDTO);

        // economic status
        patientAttributesDTO = new PatientAttributesDTO();
        patientAttributesDTO.setUuid(UUID.randomUUID().toString());
        patientAttributesDTO.setPatientuuid(uuid);
        patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("Economic Status"));
        patientAttributesDTO.setValue(StringUtils.getProvided(mEconomicstatusSpinner));
        patientAttributesDTOList.add(patientAttributesDTO);

        // createdDate
        patientAttributesDTO = new PatientAttributesDTO();
        patientAttributesDTO.setUuid(UUID.randomUUID().toString());
        patientAttributesDTO.setPatientuuid(uuid);
        patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("createdDate"));
        if (patientDTO.getCreatedDate() != null) {
            patientAttributesDTO.setValue(patientDTO.getCreatedDate());
        } else {
            patientAttributesDTO.setValue(DateAndTimeUtils.getTodaysDateInRequiredFormat("dd MMMM, yyyy"));
        }
        patientAttributesDTOList.add(patientAttributesDTO);

        //providerUUID
        patientAttributesDTO = new PatientAttributesDTO();
        patientAttributesDTO.setUuid(UUID.randomUUID().toString());
        patientAttributesDTO.setPatientuuid(uuid);
        patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("providerUUID"));
        if (patientDTO.getProviderUUID() != null) {
            patientAttributesDTO.setValue(patientDTO.getProviderUUID());
        } else {
            patientAttributesDTO.setValue(sessionManager.getProviderID());
        }
        patientAttributesDTOList.add(patientAttributesDTO);

        // numberBelongsTo
        patientAttributesDTO = new PatientAttributesDTO();
        patientAttributesDTO.setUuid(UUID.randomUUID().toString());
        patientAttributesDTO.setPatientuuid(uuid);
        patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("numberBelongsTo"));
        patientAttributesDTO.setValue(patientDTO.getNumberBelongsTo());
        patientAttributesDTOList.add(patientAttributesDTO);

        // numberBelongsTo
        patientAttributesDTO = new PatientAttributesDTO();
        patientAttributesDTO.setUuid(UUID.randomUUID().toString());
        patientAttributesDTO.setPatientuuid(uuid);
        patientAttributesDTO.setPersonAttributeTypeUuid(patientsDAO.getUuidForAttribute("typeOfCall"));
        patientAttributesDTO.setValue(selectedCallType);
        patientAttributesDTOList.add(patientAttributesDTO);


        patientDTO.setPatientAttributesDTOList(patientAttributesDTOList);
        patientDTO.setSyncd(false);
        //  patientDTO.setSyncd(true);
        Logger.logD("patient json : ", "Json : " + gson.toJson(patientDTO, PatientDTO.class));


        // inserting data in db and uploading to server...
        try {
            Logger.logD(TAG, "insertpatinet ");
            boolean isPatientInserted = false;
            boolean isPatientImageInserted = false;

            if (patient_detail) {
                /*isPatientInserted = patientsDAO.insertPatientToDB(patientDTO, patientID_edit);
                isPatientImageInserted = imagesDAO.insertPatientProfileImages(patientDTO.getPatientPhoto(), patientID_edit);*/

                isPatientInserted = patientsDAO.updatePatientToDB_PatientDTO(patientDTO, patientDTO.getUuid(), patientAttributesDTOList);
                isPatientImageInserted = imagesDAO.updatePatientProfileImages(patientDTO.getPatientPhoto(), patientDTO.getUuid());
            } else {
                isPatientInserted = patientsDAO.insertPatientToDB(patientDTO, patientDTO.getUuid());
                isPatientImageInserted = imagesDAO.insertPatientProfileImages(patientDTO.getPatientPhoto(), patientDTO.getUuid());
            }

            if (NetworkConnection.isOnline(getActivity().getApplication())) { // todo: uncomment later jsut for testing added.
                SyncDAO syncDAO = new SyncDAO();
                ImagesPushDAO imagesPushDAO = new ImagesPushDAO();
                boolean push = syncDAO.pushDataApi();
                boolean pushImage = imagesPushDAO.patientProfileImagesPush();
            }

            if (isPatientInserted && isPatientImageInserted) {
                Logger.logD(TAG, "inserted");
                Intent intent = new Intent(getActivity().getApplication(), PatientDetailActivity2.class);
                intent.putExtra("patientUuid", patientDTO.getUuid());
                intent.putExtra("patientName", patientDTO.getFirstname() + " " + patientDTO.getLastname());
                intent.putExtra("tag", "newPatient");
                intent.putExtra("hasPrescription", "false");
                Bundle args = new Bundle();
                args.putSerializable("patientDTO", (Serializable) patientDTO);
                intent.putExtra("BUNDLE", args);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        } catch (DAOException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }


    private void setscreen(String str) {
        SQLiteDatabase db = IntelehealthApplication.inteleHealthDatabaseHelper.getWritableDatabase();
        Log.v(TAG, "relation: " + str);

        String patientSelection = "uuid=?";
        String[] patientArgs = {str};
        String[] patientColumns = {"uuid", "first_name", "middle_name", "last_name",
                "date_of_birth", "address1", "address2", "city_village", "state_province",
                "postal_code", "country", "phone_number", "gender", "sdw", "occupation", "patient_photo",
                "economic_status", "education_status", "caste"};
        Cursor idCursor = db.query("tbl_patient", patientColumns, patientSelection, patientArgs, null, null, null);
        if (idCursor.moveToFirst()) {
            do {
                patient1.setUuid(idCursor.getString(idCursor.getColumnIndexOrThrow("uuid")));
                patient1.setFirst_name(idCursor.getString(idCursor.getColumnIndexOrThrow("first_name")));
                patient1.setMiddle_name(idCursor.getString(idCursor.getColumnIndexOrThrow("middle_name")));
                patient1.setLast_name(idCursor.getString(idCursor.getColumnIndexOrThrow("last_name")));
                patient1.setDate_of_birth(idCursor.getString(idCursor.getColumnIndexOrThrow("date_of_birth")));
                patient1.setAddress1(idCursor.getString(idCursor.getColumnIndexOrThrow("address1")));
                patient1.setAddress2(idCursor.getString(idCursor.getColumnIndexOrThrow("address2")));
                patient1.setCity_village(idCursor.getString(idCursor.getColumnIndexOrThrow("city_village")));
                patient1.setState_province(idCursor.getString(idCursor.getColumnIndexOrThrow("state_province")));
                patient1.setPostal_code(idCursor.getString(idCursor.getColumnIndexOrThrow("postal_code")));
                patient1.setCountry(idCursor.getString(idCursor.getColumnIndexOrThrow("country")));
                patient1.setPhone_number(idCursor.getString(idCursor.getColumnIndexOrThrow("phone_number")));
                patient1.setGender(idCursor.getString(idCursor.getColumnIndexOrThrow("gender")));
                patient1.setSdw(idCursor.getString(idCursor.getColumnIndexOrThrow("sdw")));
                patient1.setOccupation(idCursor.getString(idCursor.getColumnIndexOrThrow("occupation")));
                patient1.setPatient_photo(idCursor.getString(idCursor.getColumnIndexOrThrow("patient_photo")));

            } while (idCursor.moveToNext());
            idCursor.close();
        }
        String patientSelection1 = "patientuuid = ?";
        String[] patientArgs1 = {str};
        String[] patientColumns1 = {"value", "person_attribute_type_uuid"};
        final Cursor idCursor1 = db.query("tbl_patient_attribute", patientColumns1, patientSelection1, patientArgs1, null, null, null);
        String name = "";
        if (idCursor1.moveToFirst()) {
            do {
                try {
                    name = patientsDAO.getAttributesName(idCursor1.getString(idCursor1.getColumnIndexOrThrow("person_attribute_type_uuid")));
                } catch (DAOException e) {
                    FirebaseCrashlytics.getInstance().recordException(e);
                }

                if (name.equalsIgnoreCase("caste")) {
                    patient1.setCaste(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Telephone Number")) {
                    patient1.setPhone_number(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Education Level")) {
                    patient1.setEducation_level(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Economic Status")) {
                    patient1.setEconomic_status(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("occupation")) {
                    patient1.setOccupation(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }
                if (name.equalsIgnoreCase("Son/wife/daughter")) {
                    patient1.setSdw(idCursor1.getString(idCursor1.getColumnIndexOrThrow("value")));
                }

            } while (idCursor1.moveToNext());
        }
        idCursor1.close();
    }
    InputFilter emojiFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            StringBuilder filteredStringBuilder = new StringBuilder();
            for (int index = start; index < end; index++) {
                char currentChar = source.charAt(index);
                int type = Character.getType(currentChar);
                if (type != Character.SURROGATE && type != Character.OTHER_SYMBOL) {
                    // Allow non-emoji characters
                    filteredStringBuilder.append(currentChar);
                }
            }
            // Return null if the string contains no emojis, otherwise return an empty string
            return filteredStringBuilder.length() == end - start ? null : filteredStringBuilder.toString();
        }
    };
}
