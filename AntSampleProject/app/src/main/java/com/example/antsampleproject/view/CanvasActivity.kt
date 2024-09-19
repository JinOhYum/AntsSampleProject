package com.example.antsampleproject.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antsampleproject.R
import com.example.antsampleproject.adapter.CanvasAdapter
import com.example.antsampleproject.databinding.ActivityCanvasBinding
import com.example.antsampleproject.databinding.ActivityMainBinding
import com.example.antsampleproject.util.DefineConfig
import com.example.antsampleproject.util.DrawingView
import com.example.antsampleproject.util.SnackBarOption
import com.example.antsampleproject.viewmodel.CanvasViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URISyntaxException

@AndroidEntryPoint
class CanvasActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCanvasBinding

    private val viewModel : CanvasViewModel by viewModels<CanvasViewModel>()

    private val snackBarOption = SnackBarOption()

    private lateinit var snackBar : Snackbar

    private lateinit var adapter: CanvasAdapter

    private lateinit var deviceId : String

    //신규 뒤로가기 기존 onBackPressed 는 Deprecated 되었음
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        this.onBackPressedDispatcher.addCallback(this , onBackPressedCallback)

        binding = ActivityCanvasBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel.setSocketConnect()
        viewModel.setSocketSecondConnect()

        initLayout()
        adapterInit()
        onObserve()
    }

    override fun onDestroy() {
        super.onDestroy()
        /**
         * 소켓서버 연결 종료하기
         * **/
        viewModel.setSocketDisConnect()
    }

    @SuppressLint("HardwareIds")
    private fun initLayout(){

        /**
         * androidId 값 같은경우 context 가 필요하기에 ViewModel 이 아닌 Activity 에서 가져와 ViewModel 에 전달
         * **/
        deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        viewModel.setDeviceId(deviceId)

        /**
         * 그림판 모두 지우기
         * **/
        binding.btClear.setOnClickListener {
            binding.drawing.clearCanvas()
        }

        /**
         * 본인의 그림판 그리기 셋팅
         * **/
        binding.drawing.touchListener = object : DrawingView.OnTouchListener{
            override fun onTouch(event: MotionEvent) {
                viewModel.setDataFrom(event.x , event.y , event.action)
            }
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.persistentBottomSheet)

        /**
         * BottomSheet 상태 관리 인터페이스
         * **/
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            @SuppressLint("ClickableViewAccessibility")
            override fun onStateChanged(p0: View, p1: Int) {

                when (p1) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        Log.d("여기 ","STATE_EXPANDED")
                        // BottomSheet가 펼쳐졌을 때 터치 이벤트 차단
                        binding.drawing.setOnTouchListener { _, _ -> true }
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        Log.d("여기 ","STATE_COLLAPSED")
                        // BottomSheet가 접혔을 때 터치 이벤트 허용
                        binding.drawing.setOnTouchListener(null)
                    }
                    else -> {
                        Log.d("여기 ","else")
                        // 다른 상태일 경우에도 터치 이벤트 허용
                        binding.drawing.setOnTouchListener(null)
                    }
                }

            }

            override fun onSlide(p0: View, p1: Float) {
            }

        })

        /**
         * 하단 대화방 클릭시 대화방 전체 노출
         * **/
        binding.persistentBottomSheet.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        binding.chatLayout.btSend.setOnClickListener {
            viewModel.setMessage(binding.chatLayout.etChat.text.toString()).apply {
                binding.chatLayout.etChat.text =  null
            }
        }

    }

    /**
     * 대화 리사이클러뷰,어뎁터 초기화
     * **/
    private fun adapterInit(){
        adapter = CanvasAdapter()
        binding.chatLayout.rvMessage.setHasFixedSize(true)
        binding.chatLayout.rvMessage.layoutManager = LinearLayoutManager(this)
        binding.chatLayout.rvMessage.adapter = adapter
    }

    /**
     * 옵저버 관리
     * **/
    private fun onObserve(){
        viewModel.dataFrom.observe(this){jsonObject->
            /**
             * key 값에 빈값 확인
             * **/
            if (jsonObject.has("drawing_x") && jsonObject.has("drawing_y") && jsonObject.has("action")) {
                val x = jsonObject.getString("drawing_x").toFloat()
                val y = jsonObject.getString("drawing_y").toFloat()
                val action = jsonObject.getString("action").toInt()

                binding.drawing.drawAt(x, y, action)

            } else {
                Log.d("여기", "필수 키가 누락되었습니다: $jsonObject")
            }
        }

        /**
         * Socket 연결상태 관리 옵저버
         * **/
        viewModel.networkCheck.observe(this){
            if(it != -1){
                showSnackBar(it,0)
            }
        }
        viewModel.secondNetworkCheck.observe(this){
            if(it != -1){
                showSnackBar(it,1)
            }
        }

        viewModel.messageList.observe(this){
            adapter.setData(it)
            binding.chatLayout.rvMessage.scrollToPosition(adapter.itemCount-1)
        }

    }

    /**
     * Socket 연결상태에 따른 SnackBar 노출
     * **/
    private fun showSnackBar(isNetworkCheck : Int , type : Int){
        val networkCheckText : String =
            if(type == 0){
                when(isNetworkCheck){
                    1 -> "서버 연결 성공"
                    2 -> "서버 연결 종료"
                    3 -> "서버 연결 에러"
                    else -> "서버 연결 대기"
                }
            }
            else{
                when(isNetworkCheck){
                    1 -> "세컨드 서버 연결 성공"
                    2 -> "세컨드 서버 연결 종료"
                    3 -> "세컨드 서버 연결 에러"
                    else -> "세컨드 서버 연결 대기"
                }
            }

        snackBar = Snackbar.make(binding.root , networkCheckText , Snackbar.LENGTH_SHORT)

        snackBarOption.setSnackBarOption(snackBar)
        if(!snackBar.isShown){
            snackBar.show()
        }
    }

}