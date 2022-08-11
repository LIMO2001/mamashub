package com.intellisoft.kabarakmhis.new_designs.present_pregnancy

import android.app.Application
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.intellisoft.kabarakmhis.R
import com.intellisoft.kabarakmhis.helperclass.DbObservationLabel
import com.intellisoft.kabarakmhis.helperclass.DbObservationValues
import com.intellisoft.kabarakmhis.helperclass.FormatterClass
import com.intellisoft.kabarakmhis.new_designs.antenatal_profile.FragmentAntenatal1
import com.intellisoft.kabarakmhis.new_designs.antenatal_profile.FragmentAntenatal2
import com.intellisoft.kabarakmhis.new_designs.data_class.*
import com.intellisoft.kabarakmhis.new_designs.roomdb.KabarakViewModel
import kotlinx.android.synthetic.main.fragment_present_preg_1.view.*
import kotlinx.android.synthetic.main.fragment_present_preg_1.view.etDiastolicBp
import kotlinx.android.synthetic.main.fragment_present_preg_1.view.etGestation
import kotlinx.android.synthetic.main.fragment_present_preg_1.view.etSystolicBp
import kotlinx.android.synthetic.main.fragment_present_preg_1.view.linearUrine
import kotlinx.android.synthetic.main.fragment_present_preg_1.view.navigation
import kotlinx.android.synthetic.main.fragment_present_preg_1.view.radioGrpHb
import kotlinx.android.synthetic.main.fragment_present_preg_1.view.radioGrpUrineResults
import kotlinx.android.synthetic.main.navigation.view.*
import java.util.*


class FragmentPresentPregnancy1 : Fragment(), AdapterView.OnItemSelectedListener {

    private val formatter = FormatterClass()

    var contactNumberList = arrayOf("","1st", "2nd", "3rd", "4th", "5th", "6th", "7th")
    private var spinnerContactNumberValue  = contactNumberList[0]

    private var observationList = mutableMapOf<String, DbObservationLabel>()
    private lateinit var kabarakViewModel: KabarakViewModel

    private lateinit var rootView: View

    private lateinit var calendar : Calendar
    private var year = 0
    private  var month = 0
    private  var day = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        rootView = inflater.inflate(R.layout.fragment_present_preg_1, container, false)

        kabarakViewModel = KabarakViewModel(requireContext().applicationContext as Application)

        formatter.saveCurrentPage("1", requireContext())
        getPageDetails()

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        initSpinner()

        rootView.tvDate.setOnClickListener { createDialog(999) }

        rootView.radioGrpUrineResults.setOnCheckedChangeListener { radioGroup, checkedId ->
            val checkedRadioButton = radioGroup.findViewById<RadioButton>(checkedId)
            val isChecked = checkedRadioButton.isChecked
            if (isChecked) {
                val checkedBtn = checkedRadioButton.text.toString()
                if (checkedBtn == "Yes") {
                    changeVisibility(rootView.linearUrine, true)
                } else {
                    changeVisibility(rootView.linearUrine, false)
                }
            }
        }
        rootView.radioGrpHb.setOnCheckedChangeListener { radioGroup, checkedId ->
            val checkedRadioButton = radioGroup.findViewById<RadioButton>(checkedId)
            val isChecked = checkedRadioButton.isChecked
            if (isChecked) {
                val checkedBtn = checkedRadioButton.text.toString()
                if (checkedBtn == "Yes") {
                    changeVisibility(rootView.linearHbReading, true)
                } else {
                    changeVisibility(rootView.linearHbReading, false)
                }
            }
        }

        rootView.etMuac.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val value = rootView.etMuac.text.toString()

                if (!TextUtils.isEmpty(value)){
                    try {
                        validateMuac(rootView.etMuac, value.toInt())

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }

            }

        })

        rootView.etSystolicBp.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val value = rootView.etSystolicBp.text.toString()
                if (!TextUtils.isEmpty(value)){
                    try {
                        validateSystolicBloodPressure(rootView.etSystolicBp, value.toInt())

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }

            }

        })
        rootView.etDiastolicBp.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val value = rootView.etDiastolicBp.text.toString()
                if (!TextUtils.isEmpty(value)){
                    try {
                        validateDiastolicBloodPressure(rootView.etDiastolicBp, value.toInt())

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }

            }

        })
        rootView.etHbReading.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val value = rootView.etHbReading.text.toString()
                if (!TextUtils.isEmpty(value)){
                    try {
                        validateHbReading(rootView.etHbReading, value.toInt())

                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }

            }

        })


        handleNavigation()

        return rootView
    }
    private fun validateHbReading(editText: EditText, value: Int){

        if (value < 11){
            editText.setBackgroundColor(resources.getColor(R.color.yellow))
        }else if (value >= 11.5 && value <= 13){
            editText.setBackgroundColor(resources.getColor(R.color.low_risk))
        } else {
            editText.setBackgroundColor(resources.getColor(R.color.moderate_risk))
        }



    }
    private fun validateSystolicBloodPressure(editText: EditText, value: Int){

        if (value <= 70){
            editText.setBackgroundColor(resources.getColor(R.color.moderate_risk))
        }else if (value <= 80){
            editText.setBackgroundColor(resources.getColor(R.color.orange))
        }else if (value <= 110){
            editText.setBackgroundColor(resources.getColor(R.color.yellow))
        }else if (value <= 130)
            editText.setBackgroundColor(resources.getColor(android.R.color.holo_green_light))
        else {
            editText.setBackgroundColor(resources.getColor(R.color.moderate_risk))
        }



    }
    private fun validateDiastolicBloodPressure(editText: EditText, value: Int){

        if (value <= 60){
            editText.setBackgroundColor(resources.getColor(R.color.yellow))
        }else if (value <= 90){
            editText.setBackgroundColor(resources.getColor(R.color.low_risk))
        }else {
            editText.setBackgroundColor(resources.getColor(R.color.moderate_risk))
        }

    }
    private fun validateMuac(editText: EditText, value: Int){

        if (value < 23){
            editText.setBackgroundColor(resources.getColor(R.color.yellow))
        }else if (value in 24..30){
            editText.setBackgroundColor(resources.getColor(R.color.low_risk))
        }else {
            editText.setBackgroundColor(resources.getColor(R.color.moderate_risk))
        }

    }

    private fun handleNavigation() {

        rootView.navigation.btnNext.text = "Next"
        rootView.navigation.btnPrevious.text = "Cancel"

        rootView.navigation.btnNext.setOnClickListener { saveData() }
        rootView.navigation.btnPrevious.setOnClickListener { activity?.onBackPressed() }

    }

    private fun changeVisibility(linearLayout: LinearLayout, showLinear: Boolean){
        if (showLinear){
            linearLayout.visibility = View.VISIBLE
        }else{
            linearLayout.visibility = View.GONE
        }

    }

    

    private fun saveData() {

        val dbDataList = ArrayList<DbDataList>()

        val systolic = rootView.etSystolicBp.text.toString()
        val diastolic = rootView.etDiastolicBp.text.toString()
        val gestation = rootView.etGestation.text.toString()
        val fundalHeight = rootView.etFundal.text.toString()
        val muac = rootView.etMuac.text.toString()


        if (rootView.linearHbReading.visibility == View.VISIBLE){
            val text = rootView.etHbReading.text.toString()
            addData("Hb Testing Done",text, DbObservationValues.HB_TEST.name)
        }else{
            val text = formatter.getRadioText(rootView.radioGrpHb)
            addData("Hb Testing Done",text, DbObservationValues.HB_TEST.name)
        }
        val date = rootView.tvDate.text.toString()


        if (
            !TextUtils.isEmpty(systolic) && !TextUtils.isEmpty(diastolic)
            && !TextUtils.isEmpty(fundalHeight) && !TextUtils.isEmpty(gestation)
            && !TextUtils.isEmpty(date) && !TextUtils.isEmpty(muac)
        ){

            if (gestation.toInt() in 33..42) {

                if (formatter.validateMuac(muac)){

                    if (rootView.linearUrine.visibility == View.VISIBLE){
                        val text = rootView.etUrineResults.text.toString()
                        addData("Urine Results",text, DbObservationValues.URINALYSIS_RESULTS.name)
                    }else{
                        val text = formatter.getRadioText(rootView.radioGrpUrineResults)
                        addData("Urine Results",text, DbObservationValues.URINALYSIS_RESULTS.name)
                    }
                    addData("MUAC",muac, DbObservationValues.MUAC.name)
                    addData("Pregnancy Contact",spinnerContactNumberValue, DbObservationValues.CONTACT_NUMBER.name)
                    for (items in observationList){

                        val key = items.key
                        val dbObservationLabel = observationList.getValue(key)

                        val value = dbObservationLabel.value
                        val label = dbObservationLabel.label

                        val data = DbDataList(key, value, "Current Pregnancy Details", DbResourceType.Observation.name, label)
                        dbDataList.add(data)

                    }
                    observationList.clear()


                    addData("Systolic Blood Pressure",systolic, DbObservationValues.SYSTOLIC_BP.name)
                    addData("Diastolic Blood Pressure",diastolic, DbObservationValues.DIASTOLIC_BP.name)
                    for (items in observationList){

                        val key = items.key
                        val dbObservationLabel = observationList.getValue(key)

                        val value = dbObservationLabel.value
                        val label = dbObservationLabel.label

                        val data = DbDataList(key, value, "Blood Pressure", DbResourceType.Observation.name, label)
                        dbDataList.add(data)

                    }
                    observationList.clear()

                    addData("Gestation (Weeks)",gestation, DbObservationValues.GESTATION.name)
                    addData("Fundal Height (cm)",fundalHeight, DbObservationValues.FUNDAL_HEIGHT.name)
                    addData("Date",date, DbObservationValues.NEXT_VISIT_DATE.name)
                    for (items in observationList){

                        val key = items.key
                        val dbObservationLabel = observationList.getValue(key)

                        val value = dbObservationLabel.value
                        val label = dbObservationLabel.label

                        val data = DbDataList(key, value, "Hb Test", DbResourceType.Observation.name, label)
                        dbDataList.add(data)

                    }
                    observationList.clear()


                    val dbDataDetailsList = ArrayList<DbDataDetails>()
                    val dbDataDetails = DbDataDetails(dbDataList)
                    dbDataDetailsList.add(dbDataDetails)
                    val dbPatientData = DbPatientData(DbResourceViews.PRESENT_PREGNANCY.name, dbDataDetailsList)
                    kabarakViewModel.insertInfo(requireContext(), dbPatientData)

                    val ft = requireActivity().supportFragmentManager.beginTransaction()
                    ft.replace(R.id.fragmentHolder, FragmentPresentPregnancy2())
                    ft.addToBackStack(null)
                    ft.commit()

                }else{
                    Toast.makeText(requireContext(), "Please enter valid MUAC", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(activity, "Gestation is not in range of 34 - 42 weeks", Toast.LENGTH_SHORT).show()
            }





        }else{
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
        }

    }

    private fun addData(key: String, value: String, codeLabel: String) {

        val dbObservationLabel = DbObservationLabel(value, codeLabel)
        observationList[key] = dbObservationLabel
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getPageDetails() {

        val totalPages = formatter.retrieveSharedPreference(requireContext(), "totalPages")
        val currentPage = formatter.retrieveSharedPreference(requireContext(), "currentPage")

        if (totalPages != null && currentPage != null){

            formatter.progressBarFun(requireContext(), currentPage.toInt(), totalPages.toInt(), rootView)

        }


    }

    private fun initSpinner() {


        val kinRshp = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, contactNumberList)
        kinRshp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rootView.spinnerContact!!.adapter = kinRshp

        rootView.spinnerContact.onItemSelectedListener = this


    }

    override fun onItemSelected(arg0: AdapterView<*>, p1: View?, p2: Int, p3: Long) {
        when (arg0.id) {
            R.id.spinnerContact -> { spinnerContactNumberValue = rootView.spinnerContact.selectedItem.toString() }
            else -> {}
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun createDialog(id: Int) {
        // TODO Auto-generated method stub

        when (id) {
            999 -> {
                val datePickerDialog = DatePickerDialog( requireContext(),
                    myDateDobListener, year, month, day)
                datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
                datePickerDialog.show()

            }

            else -> null
        }


    }

    private val myDateDobListener =
        DatePickerDialog.OnDateSetListener { arg0, arg1, arg2, arg3 -> // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            val date = showDate(arg1, arg2 + 1, arg3)
            rootView.tvDate.text = date

        }

    private fun showDate(year: Int, month: Int, day: Int) :String{

        var dayDate = day.toString()
        if (day.toString().length == 1){
            dayDate = "0$day"
        }
        var monthDate = month.toString()
        if (month.toString().length == 1){
            monthDate = "0$monthDate"
        }

        val date = StringBuilder().append(year).append("-")
            .append(monthDate).append("-").append(dayDate)

        return date.toString()

    }
}