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

    /**
     * 伙食區
    #餅乾、巧克力 3
    #飲飲 5
    #冰淇淋 10
    #一品香 13
    #M 15
    #燕麥克 15
    #指定伙食 20
    生活區
    #順毛 1
    #逛街工具熊 3
    #陪唱歌看電影 5
    #一小按摩 10
    #專業造型熊 15
    #一日男僕 20
    羞羞區
    #親十次 1
    #潛艇堡 1
    #龜龜爬山 3
    #導演模式 5
    #全身濕嗒嗒 10
    #小朋友齊打交 20
     */
    enum class One(val value: String, val price: Int){
        item_1(value = "餅乾、巧克力", price = 3),
        item_2(value = "飲飲", price = 5),
        item_3(value = "冰淇淋", price = 10),
        item_4(value = "一品香", price = 13),
        item_5(value = "M", price = 15),
        item_6(value = "燕麥克", price = 15),
        item_7(value = "指定伙食", price = 20)

    }

    enum class Two(val value: String, val price: Int){
        item_1(value = "順毛", price = 1),
        item_2(value = "逛街工具熊", price = 3),
        item_3(value = "陪唱歌看電影", price = 5),
        item_4(value = "一小按摩", price = 10),
        item_5(value = "專業造型熊", price = 15),
        item_6(value = "一日男僕", price = 20),
        item_7(value = "一日公狗腰", price = 30)
    }

    enum class Three(val value: String, val price: Int){
        item_1(value = "親十次", price = 1),
        item_2(value = "潛艇堡", price = 1),
        item_3(value = "龜龜爬山", price = 3),
        item_4(value = "導演模式", price = 5),
        item_5(value = "全身濕嗒嗒", price = 10),
        item_6(value = "小朋友齊打交", price = 20)
    }
}