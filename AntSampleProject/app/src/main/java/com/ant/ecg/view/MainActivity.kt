package com.ant.ecg.view

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.ant.ecg.R
import com.ant.ecg.adapter.MainAdapter
import com.ant.ecg.databinding.ActivityMainBinding
import com.ant.ecg.databinding.DialogBottomDeviceBinding
import com.ant.ecg.view.fragment.HistoryFragment
import com.ant.ecg.view.fragment.MonitorFragment
import com.ant.ecg.view.fragment.SettingFragment
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding
    private var fragmentList = ArrayList<Fragment>()
    private var bottomLayoutList = ArrayList<LinearLayout>()
    private lateinit var adapter : MainAdapter

    private val historyFragment = HistoryFragment.newInstance()
    private val monitorFragment = MonitorFragment.newInstance()
    private val settingFragment = SettingFragment.newInstance()

    private lateinit var bottomSheetDialogDevice : BottomSheetDialog
    private lateinit var bottomSheetDialogDeviceBinding : DialogBottomDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root).apply {
            fragmentList.add(monitorFragment)
            fragmentList.add(historyFragment)
            fragmentList.add(settingFragment)

            bottomLayoutList.add(binding.layoutMoitor)
            bottomLayoutList.add(binding.layoutHistory)
            bottomLayoutList.add(binding.layoutSetting)

            bottomSheetDialogDevice = BottomSheetDialog(this@MainActivity)
            bottomSheetDialogDeviceBinding = DialogBottomDeviceBinding.inflate(layoutInflater)
            bottomSheetDialogDevice.setContentView(bottomSheetDialogDeviceBinding.root)

        }

        init()

    }


    private fun init(){

        bottomSheetDialogDevice.show()

        adapter = MainAdapter(supportFragmentManager , lifecycle , fragmentList)
        binding.viewPager.adapter = adapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                updateBottomUi(position)

            }
        })

        binding.layoutMoitor.setOnClickListener {
            updateBottomState(binding.layoutMoitor)
        }
        binding.layoutHistory.setOnClickListener {
            updateBottomState(binding.layoutHistory)
        }
        binding.layoutSetting.setOnClickListener {
            updateBottomState(binding.layoutSetting)

        }

        bottomSheetDialogDeviceBinding.btOk.setOnClickListener {
            binding.layoutTopBar.tvConnection.text = "기기 연결 중"
            binding.layoutTopBar.progressbar.visibility = View.VISIBLE
            bottomSheetDialogDevice.dismiss()
        }
        bottomSheetDialogDeviceBinding.ivClose.setOnClickListener {
            bottomSheetDialogDevice.dismiss()
        }

        binding.layoutTopBar.layoutConnection.setOnClickListener {
            bottomSheetDialogDevice.show()
        }

    }

    private fun updateBottomState(layout: LinearLayout) {

        // 모든 체크박스를 false로 초기화
        for(i in 0 until bottomLayoutList.size){
            if(bottomLayoutList[i] == layout){
                updateBottomUi(i)
            }
        }

        // ViewPager 업데이트
        when (layout) {
            binding.layoutMoitor -> binding.viewPager.currentItem = 0
            binding.layoutHistory -> binding.viewPager.currentItem = 1
            binding.layoutSetting -> binding.viewPager.currentItem = 2
        }
    }

    private fun updateBottomUi(bottomPosition : Int){

        when(bottomPosition){
            0->{
                binding.layoutTopBar.layoutTopLeft.visibility = View.VISIBLE
                binding.layoutTopBar.tvTitle.visibility = View.GONE
                binding.layoutTopBar.layoutConnection.visibility = View.VISIBLE
                binding.ivMonitor.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_monitor_on))
                binding.tvMonitor.setTextColor(ContextCompat.getColor(this,R.color.white))
                binding.ivHistory.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_history_off))
                binding.tvHistory.setTextColor(ContextCompat.getColor(this,R.color.color_0c81bf))
                binding.ivSetting.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_setting_off))
                binding.tvSetting.setTextColor(ContextCompat.getColor(this,R.color.color_0c81bf))
            }
            1->{
                binding.layoutTopBar.layoutTopLeft.visibility = View.GONE
                binding.layoutTopBar.tvTitle.visibility = View.VISIBLE
                binding.layoutTopBar.layoutConnection.visibility = View.GONE
                binding.layoutTopBar.tvTitle.text = ContextCompat.getString(this , R.string.text_history)
                binding.ivMonitor.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_monitor_off))
                binding.tvMonitor.setTextColor(ContextCompat.getColor(this,R.color.color_0c81bf))
                binding.ivHistory.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_history_on))
                binding.tvHistory.setTextColor(ContextCompat.getColor(this,R.color.white))
                binding.ivSetting.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_setting_off))
                binding.tvSetting.setTextColor(ContextCompat.getColor(this,R.color.color_0c81bf))
            }
            2->{
                binding.layoutTopBar.layoutTopLeft.visibility = View.GONE
                binding.layoutTopBar.tvTitle.visibility = View.VISIBLE
                binding.layoutTopBar.layoutConnection.visibility = View.GONE
                binding.layoutTopBar.tvTitle.text = ContextCompat.getString(this , R.string.text_setting)
                binding.ivMonitor.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_monitor_off))
                binding.tvMonitor.setTextColor(ContextCompat.getColor(this,R.color.color_0c81bf))
                binding.ivHistory.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_history_off))
                binding.tvHistory.setTextColor(ContextCompat.getColor(this,R.color.color_0c81bf))
                binding.ivSetting.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.ic_setting_on))
                binding.tvSetting.setTextColor(ContextCompat.getColor(this,R.color.white))
            }
        }
    }
}