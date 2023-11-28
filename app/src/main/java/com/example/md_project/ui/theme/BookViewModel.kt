package com.example.md_project.ui.theme
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson


class BookViewModel : ViewModel() {
    var selectedButton by mutableStateOf("none")
    var selectedStars by mutableStateOf(0)

    // New state variables for each category
    val toReadBooks = mutableStateOf(mutableListOf<Book>())
    val readingBooks = mutableStateOf(mutableListOf<Book>())
    val readBooks = mutableStateOf(mutableListOf<Book>())

    // Functions to add books to different categories
    fun addToToRead(book: Book) {
        toReadBooks.value.add(book)
        readingBooks.value.remove(book)
        readBooks.value.remove(book)
        selectedButton = "To Read"
        updateBookStatus(book, BookStatus.TO_READ)
    }

    fun addToReading(book: Book) {
        toReadBooks.value.remove(book)
        readingBooks.value.add(book)
        readBooks.value.remove(book)
        selectedButton = "Reading"
        updateBookStatus(book, BookStatus.READING)
    }

    fun addToRead(book: Book) {
        toReadBooks.value.remove(book)
        readingBooks.value.remove(book)
        readBooks.value.add(book)
        selectedButton = "Read"
        updateBookStatus(book, BookStatus.READ)
    }

    // Function to update book status
    fun updateBookStatus(book: Book, newStatus: BookStatus) {
        // Remove the book from other lists
        toReadBooks.value.remove(book)
        readingBooks.value.remove(book)
        readBooks.value.remove(book)

        // Update the book status
        when (newStatus) {
            BookStatus.TO_READ -> toReadBooks.value.add(book)
            BookStatus.READING -> readingBooks.value.add(book)
            BookStatus.READ -> readBooks.value.add(book)
            else -> Unit
        }

        // Update the book status
        book.status = newStatus

        // Update selectedButton and selectedStars
        selectedButton = newStatus.name
        selectedStars = book.stars
    }

    // Other existing code...
}