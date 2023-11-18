package com.example.md_project.ui.theme

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


// Update your readBooksFromAssets function
fun readBooksFromAssets(context: Context, fileName: String): List<Book> {
    val books = mutableListOf<Book>()
    try {
        val assetManager = context.assets
        val inputStream = assetManager.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))

        val jsonArray = JSONArray(reader.readText())

        for (i in 0 until jsonArray.length()) {
            val jsonBook = jsonArray.getJSONObject(i)
            val book = Book(
                title = jsonBook.getString("Title"),
                cover = jsonBook.getString("Cover"),
                author = jsonBook.getString("Author"),
                description = jsonBook.getString("Description")
            )
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
    val books = readBooksFromAssets(LocalContext.current, "books.txt")
    return books.find { it.title == title } ?: Book(
        title = "Unknown Book",
        cover = "default_cover_placeholder",
        author = "Unknown Author",
        description = "No description available"
    )
}


