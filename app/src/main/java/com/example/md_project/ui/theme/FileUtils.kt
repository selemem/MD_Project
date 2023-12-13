package com.example.md_project.ui.theme

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


// Reads the books from txt file (Json Array
fun readBooksFromAssets(context: Context, fileName: String): List<Book> {
    val books = mutableListOf<Book>()
    try {
        // Access the asset manager and open the specific file
        val assetManager = context.assets
        val inputStream = assetManager.open(fileName)

        // Creates buffer read to read the contents fo the file
        val reader = BufferedReader(InputStreamReader(inputStream))

        // Read content inside Json Array
        val jsonArray = JSONArray(reader.readText())

        // Loops through each element of the array  and create a book object
        for (i in 0 until jsonArray.length()) {
            val jsonBook = jsonArray.getJSONObject(i)
            val book = Book(
                title = jsonBook.getString("Title"),
                cover = jsonBook.getString("Cover"),
                author = jsonBook.getString("Author"),
                description = jsonBook.getString("Description"),
                status = BookStatus.NONE, // Default status is NONE
                stars = 0 // Default stars is 0
            )
            // Adds all the objects to 'book'
            books.add(book)
        }
        reader.close()
        inputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: JSONException) {
        e.printStackTrace()
    }
    return books
}


@Composable
fun findBookByTitle(title: String): Book {
    // Read books from assets
    val books = readBooksFromAssets(LocalContext.current, "books.txt")
    // Returns teh books based on its title
    return books.find { it.title == title } ?: Book(
        title = "Unknown Book",
        cover = "default_cover_placeholder",
        author = "Unknown Author",
        description = "No description available"
    )
}


