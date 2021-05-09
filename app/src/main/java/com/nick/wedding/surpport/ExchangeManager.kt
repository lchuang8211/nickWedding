package com.nick.wedding.surpport

object ExchangeManager {

    fun getExchangeList(key: String, page: Int): Pair<String, Int>{
        when(page){
            1 -> {
               return Pair(One.valueOf(key).value, One.valueOf(key).price)
            }
            2 -> {
                return Pair(Two.valueOf(key).value, Two.valueOf(key).price)
            }
            3 -> {
                return Pair(Three.valueOf(key).value, Three.valueOf(key).price)
            }
            else ->
                return Pair("",0)
        }
    }

    enum class One(val value: String, val price: Int){
        a(value = "雞腿便當", price = 5),
        b(value = "排骨酥麵", price = 4),
        c(value = "牛肉丼飯", price = 3),
        d(value = "豬肉咖哩", price = 2),
        e(value = "雞排肉燥", price = 1)

    }

    enum class Two(val value: String, val price: Int){
        a(value = "八家將", price = 5),
        b(value = "鞭炮", price = 4),
        c(value = "舞龍舞獅", price = 3),
        d(value = "七爺八爺", price = 2),
        e(value = "閻羅王", price = 1)
    }

    enum class Three(val value: String, val price: Int){
        a(value = "鞭子", price = 5),
        b(value = "板子", price = 4),
        c(value = "繩子", price = 3),
        d(value = "蠟燭", price = 2),
        e(value = "眼罩", price = 1)
    }
}