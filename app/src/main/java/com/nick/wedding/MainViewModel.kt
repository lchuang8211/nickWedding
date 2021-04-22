package com.nick.wedding

import androidx.lifecycle.MutableLiveData
import com.nick.wedding.base.BaseViewModel

class MainViewModel : BaseViewModel() {

    val stringData = MutableLiveData<String>("LiveData")

}