package com.example.md_project.ui.theme
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class BookViewModel : ViewModel() {

    // Existing code...

    // New state variables for each category
    val toReadBooks = mutableStateOf(mutableListOf<Book>())
    val readingBooks = mutableStateOf(mutableListOf<Book>())
    val readBooks = mutableStateOf(mutableListOf<Book>())

    // Functions to add books to different categories
    fun addToToRead(book: Book) {
        toReadBooks.value.add(book)
        // Update other necessary logic
    }

    fun addToReading(book: Book) {
        readingBooks.value.add(book)
        // Update other necessary logic
    }

    fun addToRead(book: Book) {
        readBooks.value.add(book)
        // Update other necessary logic
    }

    // Other existing code...
}

