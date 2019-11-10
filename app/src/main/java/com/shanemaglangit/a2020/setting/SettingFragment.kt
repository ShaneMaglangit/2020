package com.shanemaglangit.a2020.setting


import android.content.SharedPreferences
import android.os.Bundle
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
import org.koin.android.ext.android.inject

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var settingViewModel: SettingViewModel
    private val sharedPreferences: SharedPreferences by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        settingViewModel = ViewModelProvider(this, SettingViewModelFactory(activity!!.application, sharedPreferences)).get(SettingViewModel::class.java)

        binding.seekbarDuration.setOnChangeListener()
        binding.seekbarWork.setOnChangeListener()
        binding.settingViewModel = settingViewModel
        binding.lifecycleOwner = this

        settingViewModel.showDisabledSnackbar.observe(this, Observer {
            if(it) {
                Snackbar.make(binding.linearParent, "Breaks are now disabled", Snackbar.LENGTH_SHORT).show()
                settingViewModel.disabledSnackbarComplete()
            }
        })

        settingViewModel.invalidFields.observe(this, Observer {
            if(it) {
                Snackbar.make(binding.linearParent, "Fields cannot be set to 0", Snackbar.LENGTH_SHORT).show()
                settingViewModel.invalidFieldsSnackbarComplete()
            }
        })

        return binding.root
    }

    private fun SeekBar.setOnChangeListener() {
        this.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar?, p1: Int, p2: Boolean) {
                settingViewModel.disableBreaks()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }
}
