package com.example.md_project.ui.theme
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson


class BookViewModel(application: Application) : AndroidViewModel(application) {

    var selectedButton by mutableStateOf("none")
    var selectedStars by mutableStateOf(0)

    // New state variables for each category
    val toReadBooks = mutableStateOf(mutableListOf<Book>())
    val readingBooks = mutableStateOf(mutableListOf<Book>())
    val readBooks = mutableStateOf(mutableListOf<Book>())

    fun updateSelectedStars(stars: Int) {
        selectedStars = stars
    }

    // Functions to add books to different categories
//    fun addToToRead(book: Book) {
//        toReadBooks.value.add(book)
//        readingBooks.value.remove(book)
//        readBooks.value.remove(book)
//        selectedButton = "To Read"
//        updateBookStatus(book, BookStatus.TO_READ)
//        saveBookData()
//    }
//
//    fun addToReading(book: Book) {
//        toReadBooks.value.remove(book)
//        readingBooks.value.add(book)
//        readBooks.value.remove(book)
//        selectedButton = "Reading"
//        updateBookStatus(book, BookStatus.READING)
//        saveBookData()
//    }
//
//    fun addToRead(book: Book) {
//        toReadBooks.value.remove(book)
//        readingBooks.value.remove(book)
//        readBooks.value.add(book)
//        selectedButton = "Read"
//        updateBookStatus(book, BookStatus.READ)
//        saveBookData()
//    }

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


    private val sharedPreferencesKey = "BOOK_DATA_KEY"

    // Function to save book data to SharedPreferences
    // Function to save book data to SharedPreferences
    private fun saveBookData() {
        val bookData = mapOf(
            "toRead" to toReadBooks.value,
            "reading" to readingBooks.value,
            "read" to readBooks.value,
            "selectedButton" to selectedButton,
            "selectedStars" to selectedStars
        )

        val jsonString = Gson().toJson(bookData)

        val sharedPref = getApplication<Application>().getSharedPreferences(
            sharedPreferencesKey, Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            putString("bookData", jsonString)
            apply()
            Log.d("SaveBookData", "Saved book data successfully.")
        }
    }

    // Function to load book data from SharedPreferences
    private fun loadBookData() {
        val sharedPref = getApplication<Application>().getSharedPreferences(
            sharedPreferencesKey, Context.MODE_PRIVATE
        )

        val jsonString = sharedPref.getString("bookData", null)
        jsonString?.let {
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val bookData: Map<String, Any> = Gson().fromJson(jsonString, type)

            toReadBooks.value = (bookData["toRead"] as List<Map<String, Any>>?)?.map { book ->
                createBookFromMap(book)
            }?.toMutableList() ?: mutableListOf()

            readingBooks.value = (bookData["reading"] as List<Map<String, Any>>?)?.map { book ->
                createBookFromMap(book)
            }?.toMutableList() ?: mutableListOf()

            readBooks.value = (bookData["read"] as List<Map<String, Any>>?)?.map { book ->
                createBookFromMap(book)
            }?.toMutableList() ?: mutableListOf()

            selectedButton = bookData["selectedButton"] as String? ?: "none"
            selectedStars = (bookData["selectedStars"] as Double?)?.toInt() ?: 0
        }
    }


    private fun createBookFromMap(bookMap: Map<String, Any>): Book {
        return Book(
            title = bookMap["title"] as String,
            author = bookMap["author"] as String,
            cover = bookMap["cover"] as String,
            description = bookMap["description"] as String,
            stars = (bookMap["stars"] as Double?)?.toInt() ?: 0,
            status = BookStatus.valueOf(bookMap["status"] as String)
            // Add any other properties of your Book class here
        )
    }

    init {
        // Load book data when the ViewModel is initialized
        loadBookData()
    }

    // Call this function whenever you want to save changes to book data
    fun saveChanges() {
        saveBookData()
    }


}