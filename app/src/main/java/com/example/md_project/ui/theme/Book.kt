package com.example.md_project.ui.theme

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStreamReader

data class Book(
    var title: String,
    var cover: String,
    var author: String,
    var description: String
)
