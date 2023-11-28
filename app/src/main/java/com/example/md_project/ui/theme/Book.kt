package com.example.md_project.ui.theme

data class Book(
    var title: String,
    var cover: String,
    var author: String,
    var description: String,
    var status: BookStatus = BookStatus.NONE, // Default status is NON
    var stars: Int = 0
)

enum class BookStatus {
    NONE,
    TO_READ,
    READING,
    READ
}
