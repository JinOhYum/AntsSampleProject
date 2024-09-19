package com.example.antsampleproject.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.antsampleproject.data.model.MessageModel
import com.example.antsampleproject.util.DefineConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URISyntaxException
import javax.inject.Inject

@HiltViewModel
class CanvasViewModel @Inject constructor(): ViewModel() {


    private var mSocket : Socket? = null
    private var mSecondSocket : Socket? = null

    private var _messageList : MutableSharedFlow<MessageModel> = MutableSharedFlow<MessageModel>()
    val messageList : LiveData<MessageModel> get() = _messageList.asLiveData()

    /**
     * 서버 연결 확인용 flow
     * StateFlow: 상태를 관리하고 싶을 때(현재 값이 항상 유지됨). UI 상태 관리에 적합.
     * SharedFlow: 이벤트를 전달하고 싶을 때(상태 유지 없음). 일회성 이벤트 처리에 적합.
     * 서버 연결 확인 같은경우 항상 값을 갖고있어야되고 상태값을 확인해 SnackBar 를 띄우기 위해 StateFlow 사용
     * **/
    private val _networkCheck = MutableStateFlow<Int>(-1) //-1:네트워크 대기 , 1:네트워크 연결 , 2:네트워크 종료 , 3:네트워크 에러
    val networkCheck : LiveData<Int> get() = _networkCheck.asLiveData()

    /**
     * 대화방 소켓 네트워크 체크용
     * **/
    private val _secondNetworkCheck = MutableStateFlow<Int>(-1) //-1:네트워크 대기 , 1:네트워크 연결 , 2:네트워크 종료 , 3:네트워크 에러
    val secondNetworkCheck : LiveData<Int> get() = _networkCheck.asLiveData()


    /**
     * Socket 에서 받은 데이터를 그림판에서 사용할 flow
     * LiveData 가 아닌 Flow 로 받는 이유 :
     * LiveData 와 flow 의 동작 방식은 비슷하되
     * LiveData 같은경우 데이터의 수집을 onStop 이 되면 멈춘다
     * Flow 의 경우 View 가 onDestroy 일때 까지 데이터를 수집 받는다
     * **/
    private val _dataFrom = MutableSharedFlow<JSONObject>()
    val dataFrom : LiveData<JSONObject> get() = _dataFrom.asLiveData()


    private var deviceId = ""//Device 고유 값

    private var isFirstNetworkCheck :Boolean = false //네트워크 최초 연결시도 파악 여부

    private var isFirstSecondNetworkCheck :Boolean = false //대화방 네트워크 최초 연결시도 파악 여부


    /**
     * Activity 에서 AndroidId 전달 받는 함수
     * **/
    fun setDeviceId(deviceId : String){
        this.deviceId = deviceId
    }

    /**
     * Socket.IO 연결 함수
     * Socket.IO 라이브러리 같은 경우 기본적으로 백그라운드 쓰레드를 이용해 동작하기에
     * viewModelScope 를 이용해 코루틴을 감쌀 필요없음
     * 감싸게 될경우 백그라운드 쓰레드가 중복으로 등록될수있음
     * **/
    fun setSocketConnect() {
        viewModelScope
        if (mSocket?.connected() == true) {
            Log.d("여기", "이미 소켓에 연결되어있음")
            return
        }

        try {
            mSocket = IO.socket(DefineConfig.DOMAIN_SOCKET)

            mSocket?.apply {
                on(Socket.EVENT_CONNECT) {
                    Log.d("여기", "서버 연결 됨")
                    viewModelScope.launch(Dispatchers.IO) {
                        if(isFirstNetworkCheck){
                            _networkCheck.emit(1)
                        }
                        isFirstNetworkCheck = true
                    }
                }

                on(Socket.EVENT_DISCONNECT) {
                    Log.d("여기", "서버 연결 종료")
                    viewModelScope.launch(Dispatchers.IO) {
                        _networkCheck.emit(2)
                    }
                }

                on(Socket.EVENT_CONNECT_ERROR) { args ->
                    Log.d("여기", "서버 연결 에러 : ${args[0]}")
                    viewModelScope.launch(Dispatchers.IO) {
                        if(_networkCheck.value != 3){
                            _networkCheck.emit(3)
                        }
                    }
                }

                /**
                 * 서버에서 dataFrom 의 key 값으로 보내는 데이터 받는곳
                 * **/
                on("dataFrom") { args ->

                    val jsonObject = JSONObject(args[0].toString())

                    Log.d("여기"," "+args[0])

                    if(jsonObject.get("deviceId") != deviceId){
                        viewModelScope.launch(Dispatchers.IO) {
                            _dataFrom.emit(jsonObject)
                        }
                    }
                }
                connect()
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            Log.d("여기", "URI Syntax Error: ${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("여기", "Error: ${e.message}")
        }

    }

    /**
     * Socket.IO 대화방 연결 함수
     * **/
    fun setSocketSecondConnect() {
        if (mSecondSocket?.connected() == true) {
            Log.d("여기", "이미 세컨드 소켓에 연결되어있음")
            return
        }

        try {
            mSecondSocket = IO.socket(DefineConfig.DOMAIN_SECOND_SOCKET)

            mSecondSocket?.apply {
                on(Socket.EVENT_CONNECT) {
                    Log.d("여기", "대화방 서버 연결 됨")
                    viewModelScope.launch(Dispatchers.IO) {
                        if(isFirstSecondNetworkCheck){
                            _secondNetworkCheck.emit(1)
                        }
                        isFirstSecondNetworkCheck = true
                    }
                }

                on(Socket.EVENT_DISCONNECT) {
                    Log.d("여기", "대화방 서버 연결 종료")
                    viewModelScope.launch(Dispatchers.IO) {
                        _secondNetworkCheck.emit(2)
                    }
                }

                on(Socket.EVENT_CONNECT_ERROR) { args ->
                    Log.d("여기", "대화방 서버 연결 에러 : ${args[0]}")
                    viewModelScope.launch(Dispatchers.IO) {
                        if(_secondNetworkCheck.value != 3){
                            _secondNetworkCheck.emit(3)
                        }
                    }
                }

                /**
                 * 서버에서 dataFrom 의 key 값으로 보내는 데이터 받는곳
                 * **/
                on("secondDataFrom") { args ->

                    val jsonObject = JSONObject(args[0].toString())
//
                    Log.d("여기"," 대화방 데이터 = "+args[0])

                    /**
                     * 내가 보낸 데이터가 아닐때만 _messageList 에 데이터 담기
                     * **/
                    if(jsonObject.get("deviceId") != deviceId){
                        viewModelScope.launch(Dispatchers.IO) {
                            val viewType = if(deviceId == jsonObject.get("deviceId").toString()){
                                0
                            } else{
                                1
                            }
                            _messageList.emit(MessageModel(viewType,jsonObject.get("deviceId").toString(),jsonObject.get("message").toString()))
                        }
                    }
                }
                connect()
            }
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            Log.d("여기", "URI Syntax Error: ${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("여기", "Error: ${e.message}")
        }

    }

    /**
     * 본인의 그림판 에서 받은 x,y 값을 socket 서버에 전달
     * **/
    fun setDataFrom(x : Float , y :Float , action : Int){
        if (mSocket?.connected() == true) {
            val jsonObject = JSONObject()
            jsonObject.put("deviceId",deviceId)
            jsonObject.put("drawing_x",x.toString())
            jsonObject.put("drawing_y",y.toString())
            jsonObject.put("action",action.toString())

            /**
             * emit 을 통해 connectReceive의 이벤트로 socket 서버 전달
             * connectReceive 는 서버에서 데이터 받을 key 값이라고 생각하면됨
             * **/
            mSocket?.emit("connectReceive",jsonObject)
        }

    }

    /**
     * 소켓서버로 보낼 메세지 데이터
     * **/
    fun setMessage(message : String){

        /**
         * 본인이 보낸 데이터는 viewModel에 선언한 _messageList 변수에 담고 그다음 소켓 서버에 데이터를 보낸다
         * 본인이 보낸 데이터는 소켓서버에서 받는 부분에서는 사용을 안하기에 이부분에서 데이터를 담아준다
         * **/
        viewModelScope.launch(Dispatchers.IO){
            _messageList.emit(MessageModel(0,deviceId,message))
        }

        if (mSecondSocket?.connected() == true) {
            val jsonObject = JSONObject()
            jsonObject.put("deviceId",deviceId)
            jsonObject.put("message",message)
            mSecondSocket?.emit("connectSecondReceive",jsonObject)
        }
    }

    /**
     * 소켓 연결 종료하기
     * **/
    fun setSocketDisConnect(){
        if (mSocket != null && mSocket?.connected() == true){
            mSocket?.disconnect()
        }
        if (mSecondSocket != null && mSecondSocket?.connected() == true){
            mSecondSocket?.disconnect()
        }
    }

}