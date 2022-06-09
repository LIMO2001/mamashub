package com.intellisoft.kabarakmhis.new_designs.antenatal_profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.intellisoft.kabarakmhis.R
import com.intellisoft.kabarakmhis.helperclass.FormatterClass
import com.intellisoft.kabarakmhis.new_designs.data_class.DbResourceViews
import com.intellisoft.kabarakmhis.new_designs.new_patient.FragmentPatientDetails
import com.intellisoft.kabarakmhis.new_designs.new_patient.FragmentPatientInfo

class AntenatalProfile : AppCompatActivity() {

    private val formatter = FormatterClass()

    private val antenatal1 = DbResourceViews.ANTENATAL_1.name
    private val antenatal2 = DbResourceViews.ANTENATAL_2.name
    private val antenatal3 = DbResourceViews.ANTENATAL_3.name
    private val antenatal4 = DbResourceViews.ANTENATAL_4.name


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_antenatal_profile)

        formatter.saveSharedPreference(this, "totalPages", "4")

        if (savedInstanceState == null){
            val ft = supportFragmentManager.beginTransaction()

            when (formatter.retrieveSharedPreference(this, "FRAGMENT")) {
                antenatal1 -> {
                    ft.replace(R.id.fragmentHolder, FragmentAntenatal1())
                    formatter.saveCurrentPage("1" , this)
                }
                antenatal2 -> {
                    ft.replace(R.id.fragmentHolder, FragmentAntenatal2())
                    formatter.saveCurrentPage("2", this)
                }
                antenatal3 -> {
                    ft.replace(R.id.fragmentHolder, FragmentAntenatal3())
                    formatter.saveCurrentPage("3", this)
                }
                antenatal4 -> {
                    ft.replace(R.id.fragmentHolder, FragmentAntenatal4())
                    formatter.saveCurrentPage("4", this)
                }
                else -> {
                    ft.replace(R.id.fragmentHolder, FragmentAntenatal1())
                    formatter.saveCurrentPage("1", this)
                }
            }

            ft.commit()


        }

    }

    
}