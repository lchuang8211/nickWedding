package com.nick.wedding.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel(){

    // 載入共同使用的 Data 或 Function

    // 取消訂閱防止 Rxkotlin/RxJava 記憶體洩漏
//    val compositeDisposable = CompositeDisposable()


    override fun onCleared() {
        super.onCleared()
        dispose()
    }

    fun dispose() {
//        compositeDisposable.dispose()
    }

}