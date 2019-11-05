package com.shanemaglangit.a2020.setting


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shanemaglangit.a2020.R
import com.shanemaglangit.a2020.databinding.FragmentSettingBinding
import com.shanemaglangit.a2020.setAlarmManager

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var settingViewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        settingViewModel = ViewModelProvider(this, SettingViewModelFactory(activity!!.application)).get(SettingViewModel::class.java)

        setAlarmManager(this.context!!)

        settingViewModel.duration.observe(viewLifecycleOwner, Observer {
            if(settingViewModel.isEnabled.value!!) settingViewModel.stopBreaks()
        })
        settingViewModel.work.observe(viewLifecycleOwner, Observer {
            if(settingViewModel.isEnabled.value!!) settingViewModel.stopBreaks()
        })

        binding.settingViewModel = settingViewModel
        binding.lifecycleOwner = this

        return binding.root
    }
}
