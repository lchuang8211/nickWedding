package com.nick.wedding

import androidx.lifecycle.MutableLiveData
import com.nick.wedding.base.BaseViewModel
import com.nick.wedding.data.DateSign
import java.util.*

class MerryMeViewModel : BaseViewModel() {

    val stringData = MutableLiveData<String>("LiveData")
    val mDate = MutableLiveData<List<DateSign>>()


    fun initDate(){
        val cl = Calendar.getInstance()

    }

}