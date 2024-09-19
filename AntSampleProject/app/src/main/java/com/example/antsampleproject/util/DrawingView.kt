package com.example.antsampleproject.util

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlinx.coroutines.flow.flow
import kotlin.math.abs

class DrawingView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private var path = Path()
    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 8f
        isAntiAlias = true
    }


    private val paths = ArrayList<Path>()

    // 기본 브러시 설정
    private val defaultPaint = Paint(paint)

    // 현재 지우기 모드인지 여부
    private var isErase = false

    // 리스너 변수
    var touchListener: OnTouchListener? = null

    private var isDataCheck = false

    // 좌표 콜백 인터페이스
    interface OnTouchListener {
        fun onTouch(event: MotionEvent)
    }

    // 지우기 기능을 설정하는 메서드
    fun setErase(erase: Boolean) {
        isErase = erase
        if (isErase) {
            paint.color = Color.WHITE  // 지우기 색상은 배경색과 동일하게 설정 (여기서는 흰색)
            paint.strokeWidth = 50f    // 지우개 크기 설정
        } else {
            paint.color = defaultPaint.color // 기본 브러시 색상
            paint.strokeWidth = defaultPaint.strokeWidth // 기본 브러시 크기
        }
    }

    // 전체 지우기 (초기화)
    fun clearCanvas() {
        paths.clear() // 기존의 모든 경로 삭제
        path.reset() // 현재 경로도 초기화
        invalidate() // 화면 갱신
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (p in paths) {
            canvas.drawPath(p, paint)
        }
        canvas.drawPath(path, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        touchListener?.onTouch(event)

        if(!isDataCheck && event.action == MotionEvent.ACTION_MOVE){
            event.action = 0
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isDataCheck = true
                path.moveTo(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                isDataCheck = false
                paths.add(path)
                path = Path()
            }
        }
        invalidate()
        return true
    }
    fun drawAt(x: Float, y: Float , action : Int?) {

        var temAction = action

        if(!isDataCheck && action == MotionEvent.ACTION_MOVE){
            temAction = 0
        }

        when (temAction) {
            MotionEvent.ACTION_DOWN -> {
                isDataCheck = true
                path.moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                isDataCheck = false
                paths.add(path)
                path = Path()
            }
        }

        // 그려진 경로를 저장하고 다시 그리기
        invalidate() // 화면 갱신


    }

}
