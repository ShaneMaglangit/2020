package com.shanemaglangit.a2020.setting


import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.shanemaglangit.a2020.R
import com.shanemaglangit.a2020.databinding.FragmentSettingBinding
import com.shanemaglangit.a2020.setRatingText

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

        binding.textRating.startTextAnimation(
            settingViewModel.userRating.value!!,
            2500,
            1000,
            true
        )

        binding.textTotal.startTextAnimation(
            settingViewModel.totalBreak.value!!,
            1500,
            1000
        )

        binding.textCompleted.startTextAnimation(
            settingViewModel.completedBreak.value!!,
            1500,
            1000
        )

        binding.textSkipped.startTextAnimation(
            settingViewModel.skippedBreak.value!!,
            1500,
            1000
        )

        binding.settingViewModel = settingViewModel
        binding.lifecycleOwner = this

        settingViewModel.duration.setFieldChangeObserver()
        settingViewModel.work.setFieldChangeObserver()
        settingViewModel.showEnabledSnackbar.setEnabledSnackbarObserver()
        settingViewModel.showDisabledSnackbar.setDisabledSnackbarObserver()
        settingViewModel.invalidFields.setInvalidFieldObserver()

        return binding.root
    }

    private fun TextView.startTextAnimation(
        endValue: Int,
        duration: Long,
        delay: Long = 0,
        isRating: Boolean = false
    ) {
        val valueAnimator = ValueAnimator.ofInt(0, endValue)
        valueAnimator.addUpdateListener {
            if (isRating) setRatingText(this, valueAnimator.animatedValue.toString())
            else this.text = valueAnimator.animatedValue.toString()
        }
        valueAnimator.duration = duration
        valueAnimator.startDelay = delay
        valueAnimator.start()
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

    private fun LiveData<Boolean>.setEnabledSnackbarObserver() {
        this.observe(viewLifecycleOwner, Observer {
            if (it) {
                Snackbar.make(binding.root, "Breaks are now enabled", Snackbar.LENGTH_SHORT).show()
                settingViewModel.enabledSnackbarComplete()
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
