package com.shanemaglangit.a2020.lite


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.shanemaglangit.a2020.R
import com.shanemaglangit.a2020.databinding.FragmentLiteBinding

class LiteFragment : Fragment() {
    private lateinit var binding: FragmentLiteBinding
    private lateinit var liteViewModel: LiteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lite, container, false)
        liteViewModel = ViewModelProvider(this, LiteViewModelFactory(activity!!.application)).get(LiteViewModel::class.java)

        binding.liteViewModel = liteViewModel
        binding.lifecycleOwner = this

        // TEMPORARY
        binding.buttonSave.setOnClickListener {
            liteViewModel.savePreferences()
            findNavController().navigate(R.id.action_setupFragment_to_restFragment)
        }

        return binding.root
    }
}
