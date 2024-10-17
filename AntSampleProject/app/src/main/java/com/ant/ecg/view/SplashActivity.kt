package com.ant.ecg.view

import android.Manifest
import android.animation.Animator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.ant.ecg.databinding.ActivitySplashBinding
import com.ant.ecg.util.dialog.CustomDefaultDialog
import com.ant.ecg.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * AndroidEntryPoint = Hilt 에서 제공되는 DI 함수로 Activity 위에 어노테이션으로 지정 해주면된다
 * **/
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    private lateinit var binding : ActivitySplashBinding

    private val TAG = "SplashActivity"

    private lateinit var customDefaultDialog : CustomDefaultDialog

    private val viewModel : SplashViewModel by viewModels<SplashViewModel>()

    // 권한 요청 결과 처리
    @RequiresApi(Build.VERSION_CODES.S)
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

        val granted = permissions[Manifest.permission.BLUETOOTH_CONNECT] == true &&
                permissions[Manifest.permission.BLUETOOTH_SCAN] == true
        if (granted) {
            // 권한이 승인되었을 때 실행할 코드
            onPermissionGranted()
        } else {
            // 권한이 거부되었을 때 실행할 코드
            onPermissionDenied()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * OS 12이상 부터 스플래쉬 화면을 지정할 때 사용
         * **/
        installSplashScreen()

        binding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)

        requestNearbyDevicesPermission()

    }

    override fun onResume() {
        super.onResume()

        /**
         * 설정 페이지 에서 돌아와 권한이 허용 되었을때 페이지 이동
         * **/
        if(checkPermissions()){
            onPermissionGranted()
        }
        else{
            /**
             * 설정 페이지 에서 돌아와 권한이 허용 안되었을때 권한 유도 팝업 노출
             * **/
            if(viewModel.isPermissions){
                showSettingsDialog()
            }
        }

    }


    /**
     * 블루투스 권한 요청
    **/
    private fun requestNearbyDevicesPermission() {
        // ActivityResultLauncher로 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!checkPermissions()) {
                // 권한이 없을때
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN
                    )
                )
            } else {
                // 권한이 이미 부여받았을때
                onPermissionGranted()
            }
        }
        else{
            // OS 12 미만 경우 권한 불필요 하기 때문에 MainActivity 로 이동
            onPermissionGranted()
        }
    }


    /**
     * 권한 승인 후 실행할 코드
     * **/
    private fun onPermissionGranted() {
        Log.d(TAG,"onPermissionGranted")
        val intent  = Intent(this@SplashActivity , MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * 권한 거부 후 실행할 코드
     * **/
    private fun onPermissionDenied() {
        Log.d(TAG,"onPermissionDenied")
        /**
         * 다이얼로그를 안전하게 노출 하기 위해 Main 쓰레드를 통해 다이얼로그 노출
         * **/
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.isPermissions = true
            showSettingsDialog()
        }
    }

    /**
     * 권한을 취소했을때 권한을 다시 요청할 다이얼로그
     * **/
    private fun showSettingsDialog() {

        customDefaultDialog = CustomDefaultDialog(this , "권한 필요" , "이 기능을 사용하려면 권한이 필요합니다. 설정에서 권한을 허용해주세요.","취소","설정이동")

        customDefaultDialog.setDialogListener(object :CustomDefaultDialog.CustomDialogListener{
            override fun onCheckClick() {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
                startActivity(intent)
            }

            override fun onNoClick() {
                showSettingsDialog()
            }

        })

        customDefaultDialog.show()

    }

    /**
     * 권한 요청 체크용
     * **/
    private fun checkPermissions() : Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        else{
            return true
        }
    }
}