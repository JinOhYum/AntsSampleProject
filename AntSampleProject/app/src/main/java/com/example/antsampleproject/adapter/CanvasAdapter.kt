package com.example.antsampleproject.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.antsampleproject.data.model.MessageModel
import com.example.antsampleproject.databinding.ItemLayoutMyChatBinding
import com.example.antsampleproject.databinding.ItemLayoutOppositeChatBinding

class CanvasAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var messageList : ArrayList<MessageModel> = ArrayList()

    fun setData(messageList : MessageModel){
        this.messageList.add(messageList)
        notifyItemInserted(this.messageList.size)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

       return when(viewType){
            0->{//내가 보낸 메세지
                val itemLayout : ItemLayoutMyChatBinding = ItemLayoutMyChatBinding.
                inflate(LayoutInflater.from(parent.context),parent,false)
                CanvasViewHolder(itemLayout)
            }
            else->{//상대방 보낸 메세지
                val itemLayout : ItemLayoutOppositeChatBinding = ItemLayoutOppositeChatBinding.
                inflate(LayoutInflater.from(parent.context),parent,false)
                CanvasOppositeViewHolder(itemLayout)
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return messageList[position].viewType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is CanvasViewHolder){
            holder.bind(messageList[position])
        }
        else if(holder is CanvasOppositeViewHolder){
            holder.bind(messageList[position])
        }
    }

    /**
     * 내가 보낸 메세지 UI 를 보여줄 ViewHolder
     * **/
    private inner class CanvasViewHolder(val binding : ItemLayoutMyChatBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : MessageModel){
            binding.tvOpposite.text = item.message
        }
    }

    /**
     * 상대방이 보낸 메세지 UI 를 보여줄 ViewHolder
     * **/
    private inner class CanvasOppositeViewHolder(val binding : ItemLayoutOppositeChatBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : MessageModel){
            val name = "user_"+item.deviceId.substring(0 until 5)
            binding.tvName.text = name
            binding.tvOppositeMessage.text = item.message
        }
    }
}