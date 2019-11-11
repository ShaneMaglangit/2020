package com.shanemaglangit.a2020.setting


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
        settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(activity!!.application)).get(
                SettingViewModel::class.java
            )

        binding.settingViewModel = settingViewModel
        binding.lifecycleOwner = this

        settingViewModel.duration.setFieldChangeObserver()
        settingViewModel.work.setFieldChangeObserver()
        settingViewModel.showDisabledSnackbar.setDisabledSnackbarObserver()
        settingViewModel.invalidFields.setInvalidFieldObserver()

        return binding.root
    }

    private fun LiveData<Boolean>.setInvalidFieldObserver() {
        this.observe(viewLifecycleOwner, Observer {
            if (it) {
                Snackbar.make(binding.root, "Fields cannot be set to 0", Snackbar.LENGTH_SHORT)
                    .show()
                settingViewModel.invalidFieldsSnackbarComplete()
            }
        })
    }

    private fun LiveData<Boolean>.setDisabledSnackbarObserver() {
        this.observe(viewLifecycleOwner, Observer {
            if (it) {
                Snackbar.make(binding.root, "Breaks are now disabled", Snackbar.LENGTH_SHORT).show()
                settingViewModel.disabledSnackbarComplete()
            }
        })
    }

    private fun MutableLiveData<Int>.setFieldChangeObserver() {
        this.observe(viewLifecycleOwner, Observer {
            settingViewModel.fieldsChanged()
        })
    }
}
