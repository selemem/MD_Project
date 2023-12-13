package com.example.md_project.ui.theme

data class Book(
    //Defining the structure of a book
    var title: String,
    var cover: String,
    var author: String,
    var description: String,
    var status: BookStatus = BookStatus.NONE,
    var stars: Int = 0
)

