package com.ant.ecg.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ant.ecg.data.model.HistoryModel
import com.ant.ecg.data.model.MessageModel
import com.ant.ecg.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 이력관리 ViewModel
 * **/
@HiltViewModel
class HistoryViewModel @Inject constructor() : ViewModel() {


    /**
     * Room 에서 값을 전달 받기 위한 변수로 Room 은 백그라운드 쓰레드에서 접근이 가능하기에 Flow 사용
     * **/
    private var _historyList : MutableSharedFlow<ArrayList<HistoryModel>> = MutableSharedFlow(replay = 1)

    /**
     * Room 에서 가져온 데이터를 UI 로 표시하기위해 LiveData 로 치환
     * */
    val historyList : LiveData<List<HistoryModel>> get() = _historyList.asLiveData()

    /**
     * 상단 날짜 년,월 가져오기
     * */
    val dateList : List<String> = DateUtil().getYearMonthList()

    /**
     * 상단 날짜 이동 에 사용할 position 값
     * */
    var datePosition = dateList.size - 1


    fun setHistoryData(){
        viewModelScope.launch(Dispatchers.IO) {
            val item = ArrayList<HistoryModel>()
            for (i in 0 until 30){
                item.add(HistoryModel(i.toLong() , "","","","",""))
            }
            _historyList.emit(item)
        }
    }

}