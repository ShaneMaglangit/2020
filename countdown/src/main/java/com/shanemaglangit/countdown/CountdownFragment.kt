package com.shanemaglangit.countdown


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.shanemaglangit.countdown.databinding.FragmentCountdownBinding

/**
 * A simple [Fragment] subclass.
 */
class CountdownFragment : Fragment() {
    private lateinit var binding: FragmentCountdownBinding
    private lateinit var countdownViewModel: CountdownViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_countdown, container, false)
        countdownViewModel = ViewModelProvider(this, CountdownViewModelFactory(activity!!.application)).get(CountdownViewModel::class.java)

        binding.lifecycleOwner = this
        binding.countdownViewModel = countdownViewModel

        countdownViewModel.startTimer()

        countdownViewModel.timeLeft.observe(viewLifecycleOwner, Observer {
            if(it == 0) this.activity!!.finish()
        })

        return binding.root
    }
}
