package com.ant.ecg.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ant.ecg.adapter.HistoryAdapter
import com.ant.ecg.data.model.HistoryModel
import com.ant.ecg.databinding.FragmentHistoryBinding
import com.ant.ecg.interfaces.HistoryAdapterInterFace
import com.ant.ecg.view.DetailHistoryActivity
import com.ant.ecg.viewmodel.HistoryViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * 이력관리 화면
 * **/
@AndroidEntryPoint
class HistoryFragment : Fragment() {
    companion object {
        fun newInstance() = HistoryFragment()
    }

    private lateinit var binding : FragmentHistoryBinding

    private val adapter : HistoryAdapter = HistoryAdapter()

    private val viewModel : HistoryViewModel by  viewModels<HistoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHistoryBinding.inflate(inflater)


        init()
        setData()
        setObserve()

        return binding.root
    }

    /**
     * 기본셋팅 및 초기화 관련 함수
     * **/
    private fun init(){
        /**
         * RecyclerView 셋팅 및 Adapter 셋팅
         * **/
        binding.rvHistory.setHasFixedSize(true)
        binding.rvHistory.layoutManager = LinearLayoutManager(context)
        binding.rvHistory.adapter = adapter

        binding.ivRight.visibility = View.GONE

        /**
         * 상단 날짜 표시
         * **/
        binding.tvDate.text = viewModel.dateList[viewModel.dateList.size-1]


        /**
         * RecyclerView 아이템 클릭 이벤트 처리 (자세히보기 클릭)
         * **/
        adapter.setHistoryAdapterInterFace(object : HistoryAdapterInterFace{
            override fun onClick(position: Int, data: HistoryModel) {
                val intent : Intent = Intent(context , DetailHistoryActivity::class.java)
                startActivity(intent)
            }

        })

        /**
         * 상단 왼쪽 화솰표 클릭이벤트 처리
         * **/
        binding.ivLeft.setOnClickListener {
            viewModel.datePosition--
            binding.tvDate.text = viewModel.dateList[viewModel.datePosition]
            binding.ivRight.visibility = View.VISIBLE
        }

        /**
         * 상단 오른쪽 화솰표 클릭이벤트 처리
         * **/
        binding.ivRight.setOnClickListener {
            if(viewModel.datePosition != viewModel.dateList.size - 1){
                viewModel.datePosition++
                binding.tvDate.text = viewModel.dateList[viewModel.datePosition]
                if(viewModel.datePosition == viewModel.dateList.size - 1){
                    binding.ivRight.visibility = View.GONE
                }
                else{
                    binding.ivRight.visibility = View.VISIBLE
                }
            }
        }

    }

    /**
     * 데이터 셋팅 함수
     * **/
    private fun setData(){
        viewModel.setHistoryData()
    }

    /**
     * viewModel 에 있는 옵저버 관리 함수
     * **/
    private fun setObserve(){

        viewModel.historyList.observe(viewLifecycleOwner){data ->
            if(data.isNotEmpty()){
                adapter.setData(data as ArrayList<HistoryModel>)
            }
        }
    }
}