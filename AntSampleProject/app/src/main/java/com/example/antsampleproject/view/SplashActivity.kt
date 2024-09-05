package com.example.antsampleproject.view

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.window.SplashScreen
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.antsampleproject.databinding.ActivitySplashBinding
import com.example.antsampleproject.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * AndroidEntryPoint = Hilt 에서 제공되는 DI 함수로 Activity 위에 어노테이션으로 지정 해주면된다
 * **/
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding

    private val TAG = "SplashActivity"

    /**
     * viewModel 선언
     * **/
    private val viewModel : SplashViewModel by viewModels<SplashViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * OS 12이상 부터 스플래쉬 화면을 지정할 때 사용
         * **/
        installSplashScreen()

        binding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)

        init()
        onObserve()
        onLottiPlay()
    }

    private fun init(){
        /**
         * 서버에서 API 조회
         * **/
        viewModel.getTestApi()
    }

    /**
     * 옵저버 관리 함수
     * **/
    private fun onObserve(){

        /**
         * 서버에서 API 응답이 와서 viewModel 에 testApiResponse 값이 변경이 되었을때 호출
         * **/
        viewModel.testApiResponse.observe(this){
            Log.d("옵저버","왔다 "+it)
        }
    }

    private fun onLottiPlay(){
        /**
         * lottie 셋팅
         * **/
        binding.lvSplash.addAnimatorListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator) {
                /**
                 * 애니메이션 시작 될떄
                 **/

                Log.d(TAG,"애니메이션 시작 될떄")
            }

            override fun onAnimationEnd(p0: Animator) {
                /**
                 * 애니메이션 종료 될떄
                 **/
                Log.d(TAG,"애니메이션 종료 될떄")
            }

            override fun onAnimationCancel(p0: Animator) {
                /**
                 * 애니메이션 취소 될떄
                 **/
                Log.d(TAG,"애니메이션 취소 될떄")
            }

            override fun onAnimationRepeat(p0: Animator) {
                /**
                 * 애니메이션 시작 이후 반복 될떄
                 **/
                Log.d(TAG,"애니메이션 시작 이후 반복 될떄")

                /**
                 * API 통신이 완료되고 + 스플래쉬 애니메이션 끝났을때 Login 페이지 이동
                 **/
                if(viewModel.isApiSuccess){
                    binding.lvSplash.pauseAnimation()
                    val intent  = Intent(this@SplashActivity , LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        })
    }
}