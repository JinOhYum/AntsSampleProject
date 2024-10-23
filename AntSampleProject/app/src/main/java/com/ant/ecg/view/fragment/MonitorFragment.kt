package com.ant.ecg.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ant.ecg.R
import com.ant.ecg.databinding.FragmentMonitorBinding

/**
 * 모니터링 화면
 * **/
class MonitorFragment : Fragment() {
    companion object {
        fun newInstance() = MonitorFragment()
    }

    private lateinit var binding : FragmentMonitorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMonitorBinding.inflate(inflater)
        return binding.root
    }

}