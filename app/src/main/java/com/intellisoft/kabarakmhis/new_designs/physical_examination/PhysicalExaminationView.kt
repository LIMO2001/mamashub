package com.intellisoft.kabarakmhis.new_designs.physical_examination

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intellisoft.kabarakmhis.R
import com.intellisoft.kabarakmhis.helperclass.FormatterClass
import com.intellisoft.kabarakmhis.network_request.requests.RetrofitCallsFhir
import com.intellisoft.kabarakmhis.new_designs.adapter.ObservationAdapter
import com.intellisoft.kabarakmhis.new_designs.adapter.ViewDetailsAdapter
import com.intellisoft.kabarakmhis.new_designs.data_class.DbObserveValue
import com.intellisoft.kabarakmhis.new_designs.data_class.DbResourceViews
import com.intellisoft.kabarakmhis.new_designs.roomdb.KabarakViewModel
import kotlinx.android.synthetic.main.activity_physical_examination_view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PhysicalExaminationView : AppCompatActivity() {

    private val retrofitCallsFhir = RetrofitCallsFhir()
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var kabarakViewModel: KabarakViewModel
    private val formatter = FormatterClass()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_physical_examination_view)

        title = "Physical Examination Details"

        btnPhysicalExamination.setOnClickListener {
            val intent = Intent(this, PhysicalExamination::class.java)
            startActivity(intent)
        }
        kabarakViewModel = KabarakViewModel(this.applicationContext as Application)

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)


    }

    override fun onStart() {
        super.onStart()



        CoroutineScope(Dispatchers.IO).launch {

            val observationId = formatter.retrieveSharedPreference(this@PhysicalExaminationView,"observationId")
            if (observationId != null) {
                val observationList = retrofitCallsFhir.getObservationDetails(this@PhysicalExaminationView, observationId)

                CoroutineScope(Dispatchers.Main).launch {
                    val configurationListingAdapter = ObservationAdapter(
                        observationList,this@PhysicalExaminationView)
                    recyclerView.adapter = configurationListingAdapter
                }

            }



        }


    }

}