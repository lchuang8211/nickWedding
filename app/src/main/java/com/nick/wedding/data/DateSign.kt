package com.nick.wedding.data

data class DateSign (
    val year: Int,
    val month: Int,
    val week: Int = 0,
    val date: Int,
    val picture: Int = 0,
    var signed: Boolean
)