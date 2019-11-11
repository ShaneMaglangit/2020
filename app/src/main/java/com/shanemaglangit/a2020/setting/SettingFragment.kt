package com.shanemaglangit.a2020.setting


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
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

        binding.seekbarDuration.setOnChangeListener()
        binding.seekbarWork.setOnChangeListener()
        binding.settingViewModel = settingViewModel
        binding.lifecycleOwner = this

        settingViewModel.showDisabledSnackbar.observe(this, Observer {
            if(it) {
                Snackbar.make(binding.root, "Breaks are now disabled", Snackbar.LENGTH_SHORT).show()
                settingViewModel.disabledSnackbarComplete()
            }
        })

        settingViewModel.invalidFields.observe(this, Observer {
            if(it) {
                Snackbar.make(binding.root, "Fields cannot be set to 0", Snackbar.LENGTH_SHORT).show()
                settingViewModel.invalidFieldsSnackbarComplete()
            }
        })

        return binding.root
    }

    private fun SeekBar.setOnChangeListener() {
        this.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar?, progress: Int, isUser: Boolean) {
                Log.i("SettingFragment", "Progress is now ${progress * 5}")
            }
            override fun onStartTrackingTouch(seek: SeekBar?) {}
            override fun onStopTrackingTouch(seek: SeekBar?) {
                if(settingViewModel.isEnabled.value!!) settingViewModel.disableBreaks()
            }
        })
    }
}
