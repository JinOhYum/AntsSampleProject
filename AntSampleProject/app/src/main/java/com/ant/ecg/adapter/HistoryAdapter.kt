package com.ant.ecg.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ant.ecg.R
import com.ant.ecg.data.model.HistoryModel
import com.ant.ecg.databinding.ItemLayoutMyChatBinding
import com.ant.ecg.databinding.ListItemHistoryBinding
import com.ant.ecg.interfaces.HistoryAdapterInterFace

/**
 * 이력관리 리스트 어뎁터
 * */
class HistoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listItem : ArrayList<HistoryModel> = ArrayList()

    private lateinit var historyAdapterInterFace : HistoryAdapterInterFace

    /**
     * 아이템 클릭 이벤트 용 interface
     * **/
    fun setHistoryAdapterInterFace(historyAdapterInterFace : HistoryAdapterInterFace){
        this.historyAdapterInterFace = historyAdapterInterFace
    }

    /**
     * 어뎁터에 전달받을 데이터
     * **/
    fun setData(listItem: ArrayList<HistoryModel>){
        this.listItem = listItem
        notifyDataSetChanged()
    }


    /**
     * List Item 레이아웃 셋팅
     * **/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemLayout : ListItemHistoryBinding = ListItemHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HistoryViewHolder(itemLayout)
    }

    /**
     * 리스트 아이템 갯수
     * **/
    override fun getItemCount(): Int {
        return listItem.size
    }

    /**
     * 리스트 아이템 ViewHolder 셋팅 부분
     * **/
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HistoryViewHolder){
            holder.bind(listItem[position])
        }
    }

    /**
     * ViewHolder 셋팅
     * 이력관리 ViewHolder 같은경우 복잡한 UI 및 기능이 없기 때문에
     * 내부에서 사용하도록 inner class 로 감싸서 사용
     * **/
    private inner class HistoryViewHolder(val binding : ListItemHistoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data : HistoryModel){
            /**
             * 자세히보기 클릭 이벤트 + interface 에 넘겨 프래그먼트에서 클릭이벤트 처리 할수있게
             * **/
            binding.layoutDetail.setOnClickListener {
                historyAdapterInterFace.onClick(adapterPosition , data)
            }
        }
    }
}