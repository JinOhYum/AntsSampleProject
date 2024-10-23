package com.ant.ecg.view

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.ant.ecg.R
import com.ant.ecg.adapter.MainAdapter
import com.ant.ecg.databinding.ActivityMainBinding
import com.ant.ecg.databinding.DialogBottomDeviceBinding
import com.ant.ecg.view.fragment.HistoryFragment
import com.ant.ecg.view.fragment.MonitorFragment
import com.ant.ecg.view.fragment.SettingFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private lateinit var binding : ActivityMainBinding

    //viewPager2 에서 사용한 프래그먼트 list (모니터링,이력관리,설정)
    private var fragmentList = ArrayList<Fragment>()
    //하단 네비게이션바 관리용 list
    private var bottomLayoutList = ArrayList<LinearLayout>()
    //viewPager2 어뎁터
    private lateinit var adapter : MainAdapter

    //모니터링 프래그먼트
    private val monitorFragment = MonitorFragment.newInstance()
    //이력관리 프래그먼트
    private val historyFragment = HistoryFragment.newInstance()
    //설정 프래그먼트
    private val settingFragment = SettingFragment.newInstance()

    //하단 팝업 다이얼로그(기기연결 팝업)
    private lateinit var bottomSheetDialogDevice : BottomSheetDialog
    //하단 팝업 UI(기기연결 UI)
    private lateinit var bottomSheetDialogDeviceBinding : DialogBottomDeviceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        /**
         * apply = 레이아웃을 초기화 시킨 이후 셋팅 할 값들
         * **/
        setContentView(binding.root).apply {
            fragmentList.add(monitorFragment)
            fragmentList.add(historyFragment)
            fragmentList.add(settingFragment)

            bottomLayoutList.add(binding.layoutMoitor)
            bottomLayoutList.add(binding.layoutHistory)
            bottomLayoutList.add(binding.layoutSetting)

            /**
             * 하단 팝업 다이얼로그(기기연결 팝업) 셋팅
             * **/
            bottomSheetDialogDevice = BottomSheetDialog(this@MainActivity)
            bottomSheetDialogDeviceBinding = DialogBottomDeviceBinding.inflate(layoutInflater)
            bottomSheetDialogDevice.setContentView(bottomSheetDialogDeviceBinding.root)

        }

        init()

    }

    //기본 셋팅 함수
    private fun init(){

        /**
         * 코루틴을 이용해 MainThread 에서 하단 팝업 노출 시키기
         * 기존 java 에서 runOnUIThread 같은 개념이며
         * 안전하게 UI 가 노출 될수 있도록 MainThread 감싸기
         * **/
        lifecycleScope.launch(Dispatchers.Main) {
            bottomSheetDialogDevice.show()
        }

        //viewPager2 어뎁터 셋팅
        adapter = MainAdapter(supportFragmentManager , lifecycle , fragmentList)
        binding.viewPager.adapter = adapter

        //viewPager2 스크롤 관련 이벤트 처리
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                updateBottomUi(position)

            }
        })

        //하단 네비게이션바 모니터링 클릭
        binding.layoutMoitor.setOnClickListener {
            updateBottomState(binding.layoutMoitor)
        }
        //하단 네비게이션바 이력관리 클릭
        binding.layoutHistory.setOnClickListener {
            updateBottomState(binding.layoutHistory)
        }

        //하단 네비게이션바 설정 클릭
        binding.layoutSetting.setOnClickListener {
            updateBottomState(binding.layoutSetting)

        }

        //하단 팝업 확인 버튼 클릭
        bottomSheetDialogDeviceBinding.btOk.setOnClickListener {
            binding.layoutTopBar.tvConnection.text = "기기 연결 중"
            binding.layoutTopBar.progressbar.visibility = View.VISIBLE
            bottomSheetDialogDevice.dismiss()
        }

        //하단 팝업 닫기 버튼 클릭
        bottomSheetDialogDeviceBinding.ivClose.setOnClickListener {
            bottomSheetDialogDevice.dismiss()
        }

        //상단 액션바 기기연결 클릭하기
        binding.layoutTopBar.layoutConnection.setOnClickListener {
            /**
             * setOnClickListener 는 이미 MainThread 에서 동작 하기 때문에
             * 별도의 코루틴으로 감쌀 필요 없음
             * **/
            bottomSheetDialogDevice.show()
        }

    }

    //하단 네비게이션바 클릭 이벤트 처리 함수
    private fun updateBottomState(layout: LinearLayout) {

        for(i in 0 until bottomLayoutList.size){
            if(bottomLayoutList[i] == layout){
                updateBottomUi(i)
            }
        }

        // ViewPager2 UI 업데이트
        when (layout) {
            binding.layoutMoitor -> binding.viewPager.currentItem = 0
            binding.layoutHistory -> binding.viewPager.currentItem = 1
            binding.layoutSetting -> binding.viewPager.currentItem = 2
        }
    }

    //하단 네비게이션바 UI 변경 함수
    private fun updateBottomUi(bottomPosition : Int){
        /**
         * 하단 네비게이션바 position 에 맞춰 UI 변경
         * **/
        when(bottomPosition){
            0->{//모니터링
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
            1->{//이력관리
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
            2->{//설정
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