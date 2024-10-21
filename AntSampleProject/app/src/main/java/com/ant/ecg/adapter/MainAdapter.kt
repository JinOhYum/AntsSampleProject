package com.ant.ecg.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val fragments : ArrayList<Fragment>) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {

       return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }


}