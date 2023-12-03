package com.example.md_project.ui.theme

data class Book(
    var title: String,
    var cover: String,
    var author: String,
    var description: String,
    var status: BookStatus = BookStatus.NONE, // Default status is NONE
    var stars: Int = 0
)

