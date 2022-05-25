package com.intellisoft.kabarakmhis.fhir.screens

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.intellisoft.kabarakmhis.R
import com.intellisoft.kabarakmhis.databinding.FragmentDashboardBinding
import com.intellisoft.kabarakmhis.helperclass.ImageUtils


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mBitmapCache: SparseArray<Bitmap?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentDashboardBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = resources.getString(R.string.app_name)
//            setDisplayHomeAsUpEnabled(true)
        }

//        setHasOptionsMenu(true)
//        (activity as MainActivity).setDrawerEnabled(true)

        binding.btnAddPatient.setOnClickListener {
            proceedNext("0")
        }
        binding.previousPregnancy.setOnClickListener {
            proceedNext("1")
        }
        binding.relativeLyt.setOnClickListener {
            proceedNext("2")
        }
//        binding.rltPostNatal.setOnClickListener {
//            proceedNext("3")
//        }
//        binding.rltAllPatients.setOnClickListener {
//            proceedNext("4")
//        }
//        binding.rltHumanMilk.setOnClickListener {
//            proceedNext("5")
//        }
//        binding.rltMonitoring.setOnClickListener {
//            proceedNext("6")
//        }


    }

    private fun proceedNext(s: String) {
        findNavController().navigate(DashboardFragmentDirections.actionDashboardToPatientList(s))
    }


    private fun drawIcon(imageView: ImageView?, drawableId: Int) {
        mBitmapCache = SparseArray()
        if (view != null) {
            createImageBitmap(drawableId, imageView!!.layoutParams)
            imageView.setImageBitmap(mBitmapCache!![drawableId])
        }
    }


    private fun createImageBitmap(key: Int, layoutParams: ViewGroup.LayoutParams) {
        if (mBitmapCache!![key] == null) {
            mBitmapCache!!.put(
                key, ImageUtils.decodeBitmapFromResource(
                    resources, key,
                    layoutParams.width, layoutParams.height
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
//                (requireActivity() as MainActivity).openNavigationDrawer()
                true
            }
            else -> false
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}