package com.intellisoft.kabarakmhis.new_designs.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.intellisoft.kabarakmhis.R
import com.intellisoft.kabarakmhis.network_request.requests.RetrofitCallsFhir
import com.intellisoft.kabarakmhis.new_designs.data_class.DbObservationData
import com.intellisoft.kabarakmhis.new_designs.data_class.DbObservationValue
import com.intellisoft.kabarakmhis.new_designs.data_class.DbResourceViews
import kotlinx.android.synthetic.main.activity_physical_examination.*

class PhysicalExamination : AppCompatActivity() {

    private val retrofitCallsFhir = RetrofitCallsFhir()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_physical_examination)

        btnSave.setOnClickListener {

            saveData()

        }

    }

    private fun saveData() {

        val generalExam = etExam.text.toString()
        val systolicBp = etSysolicBp.text.toString()
        val diastolicBp = etDiastolicBp.text.toString()
        val pulseRate = etPulseRate.text.toString()
        val cvs = etCVs.text.toString()
        val respiratory = etRespiratory.text.toString()
        val oximetry = etOximetry.text.toString()
        val breasts = etBreasts.text.toString()
        val abdomen = etAbdomen.text.toString()
        val externalGenetalia = etExternalGenitelia.text.toString()
        val genitalUlcer = etDischargeUlcer.text.toString()

        if (!TextUtils.isEmpty(generalExam) && !TextUtils.isEmpty(systolicBp) && !TextUtils.isEmpty(diastolicBp) &&
            !TextUtils.isEmpty(pulseRate) && !TextUtils.isEmpty(cvs) && !TextUtils.isEmpty(respiratory) &&
            !TextUtils.isEmpty(oximetry) && !TextUtils.isEmpty(breasts) && !TextUtils.isEmpty(abdomen) &&
            !TextUtils.isEmpty(externalGenetalia) && !TextUtils.isEmpty(genitalUlcer)){

            val hashSet = HashSet<DbObservationData>()
            val generalExamData = getList(generalExam,"General Exam")
            val systolicBpData = getList(systolicBp,"Systolica BP")
            val diastolicBpData = getList(diastolicBp,"Diastolic BP")
            val pulseRateData = getList(pulseRate,"Pulse Rate")
            val cvsData = getList(cvs,"CVS")
            val respiratoryData = getList(respiratory,"Respiratory ")
            val oximetryData = getList(oximetry,"Oximetry")
            val breastsData = getList(breasts,"Breasts Exam")
            val abdomenData = getList(abdomen,"Abdomen Exam")
            val externalGenetaliaData = getList(externalGenetalia,"External Genitalia Examination")
            val genitalUlcerData = getList(genitalUlcer,"Genital Ulcer")

            hashSet.add(generalExamData)
            hashSet.add(systolicBpData)
            hashSet.add(diastolicBpData)
            hashSet.add(pulseRateData)
            hashSet.add(cvsData)
            hashSet.add(respiratoryData)
            hashSet.add(oximetryData)
            hashSet.add(breastsData)
            hashSet.add(abdomenData)
            hashSet.add(externalGenetaliaData)
            hashSet.add(genitalUlcerData)

            val dbObservationValue = DbObservationValue(hashSet)

            retrofitCallsFhir.createFhirEncounter(this, dbObservationValue,
                DbResourceViews.PHYSICAL_EXAMINATION.name)


        }else{
            Toast.makeText(this, "Please fill all values", Toast.LENGTH_SHORT).show()
        }


    }

    private fun getList(value: String, code: String): DbObservationData {

        val valueList = HashSet<String>()
        valueList.add(value)

        return DbObservationData(code, valueList)

    }
}