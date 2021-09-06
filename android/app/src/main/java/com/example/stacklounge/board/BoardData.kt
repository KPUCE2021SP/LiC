package com.example.stacklounge.board

// 게시판 data
data class BoardData(
    var title: String,
    var boardContents : String,
    var boardTime : String,
    var userId : String,
    var uId : String,
    var boardNumber : Int = 0,
    var commentNum : Int
)
