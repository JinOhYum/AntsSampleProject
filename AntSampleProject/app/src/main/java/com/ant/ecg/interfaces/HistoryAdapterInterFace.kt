package com.ant.ecg.interfaces

import com.ant.ecg.data.model.HistoryModel

interface HistoryAdapterInterFace {

    fun onClick(position : Int , data : HistoryModel)
}