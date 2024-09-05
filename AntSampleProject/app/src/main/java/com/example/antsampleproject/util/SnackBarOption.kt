package com.example.antsampleproject.util

import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class SnackBarOption {

    fun setSnackBarOption(snackBar: Snackbar){
        snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE // 애니메이션 설정
    }

}