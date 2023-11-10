package com.example.md_project.ui.theme

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

fun readBooksFromAssets(context: Context, fileName: String): List<Book> {
    val books = mutableListOf<Book>()
    try {
        val assetManager = context.assets
        val inputStream = assetManager.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))

        var currentLine: String?
        var currentBook = Book("", "", "", "")

        while (reader.readLine().also { currentLine = it } != null) {
            when {
                currentLine?.startsWith("Cover:") == true -> {
                    currentBook.cover = currentLine?.substringAfter("Cover: ")?.trim() ?: ""
                }
                currentLine?.startsWith("Title:") == true -> {
                    currentBook.title = currentLine?.substringAfter("Title: ")?.trim() ?: ""
                }
                currentLine?.startsWith("Author:") == true -> {
                    currentBook.author = currentLine?.substringAfter("Author: ")?.trim() ?: ""
                }
                currentLine?.startsWith("Description:") == true -> {
                    currentBook.description = currentLine?.substringAfter("Description: ")?.trim() ?: ""
                    books.add(currentBook)
                    currentBook = Book("", "", "", "")
                }
            }
        }

        reader.close()
        inputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return books

}
