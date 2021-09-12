package com.example.stacklounge.tool

data class ToolData(
    var id: String,
    var name: String?,
    var slug: String?,
    var title: String?,
    var description: String?,
    var imageUrl: String?,
    var ossRepo: String?,
    var canonicalUrl: String?,
    var websiteUrl: String?
)
