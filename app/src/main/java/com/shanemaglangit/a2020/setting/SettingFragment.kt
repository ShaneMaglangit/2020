package com.shanemaglangit.a2020.setting


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shanemaglangit.a2020.BreakService
import com.shanemaglangit.a2020.R
import com.shanemaglangit.a2020.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var settingViewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        settingViewModel = ViewModelProvider(this, SettingViewModelFactory(activity!!.application)).get(SettingViewModel::class.java)

        settingViewModel.isEnabled.observe(viewLifecycleOwner, Observer {
            if(it) startBreakService()
            else stopBreakService()
        })

        settingViewModel.work.observe(viewLifecycleOwner, Observer {
            if(settingViewModel.isEnabled.value!!) settingViewModel.isEnabled.value = false
        })

        settingViewModel.duration.observe(viewLifecycleOwner, Observer {
            if(settingViewModel.isEnabled.value!!) settingViewModel.isEnabled.value = false
        })

        binding.settingViewModel = settingViewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    private fun startBreakService() {
        if(Build.VERSION.SDK_INT >= 26) {
            activity!!.startForegroundService(Intent(activity!!, BreakService::class.java))
        }
        else {
            activity!!.startService(Intent(activity!!, BreakService::class.java))
        }
    }

    private fun stopBreakService() {
        activity!!.stopService(Intent(activity!!, BreakService::class.java))
    }
}
