package com.example.md_project
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.md_project.ui.theme.readBooksFromAssets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import coil.transform.RoundedCornersTransformation
import com.example.md_project.ui.theme.findBookByTitle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberImagePainter
import com.example.md_project.ui.theme.BookViewModel

class MainActivity : ComponentActivity() {
    private lateinit var bookViewModel: BookViewModel

    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the BookViewModel with the application instance
        bookViewModel = ViewModelProvider(this, ViewModelFactory(application)).get(BookViewModel::class.java)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Navigation(bookViewModel = bookViewModel)
            }

        }


    }

    override fun onStop() {
        super.onStop()
        bookViewModel.saveChanges()
    }
}

class ViewModelFactory(private val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookViewModel(application) as T
    }
}



@Composable


fun Navigation(bookViewModel: BookViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomePage(navController, bookViewModel)
        }
        composable("profileDetails") {
            ProfilePage(navController, bookViewModel)
        }
        composable("bookDetails/{bookTitle}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("bookTitle") ?: ""
            val book = findBookByTitle(title)
            BookDetailsPage(book = book, navController = navController, bookViewModel = bookViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController, bookViewModel: BookViewModel) {
    // Read book information from the books.txt file
    val books = readBooksFromAssets(LocalContext.current, "books.txt")

    var searchText by remember { mutableStateOf("") }

    // Filtered list based on search text
    val filteredBooks = if (searchText.isNotBlank()) {
        books.filter { it.title.contains(searchText, ignoreCase = true) }
    } else {
        books
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Search bar and profile button in a Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp) // Adjust the height
        ) {

            // Search bar
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp), // Round the corners
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent, // Remove the underline
                    focusedIndicatorColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(
                    color = LocalContentColor.current
                ),
                placeholder = {
                    Text("Search", color = Color.Gray) // Use placeholder instead of label
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            )

            // Button with profile icon
            IconButton(
                onClick = {
                    navController.navigate("profileDetails")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))


// Conditionally display the LazyRow only when searching
        if (searchText.isNotBlank()) {
            // Displaying the filtered books
            if (filteredBooks.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(filteredBooks) { book ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    // Navigate to book details activity
                                    navController.navigate("bookDetails/${book.title}")
                                }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .width(142.dp)
                                    .wrapContentHeight()
                            ) {
                                // Book cover image
                                Image(
                                    painter = rememberImagePainter(
                                        data = LocalContext.current.resources.getIdentifier(
                                            book.cover,
                                            "drawable",
                                            LocalContext.current.packageName
                                        ),
                                        builder = {
                                            crossfade(true)
                                            transformations(RoundedCornersTransformation(8f))
                                        }
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(142.dp) // Square dimensions
                                        .clip(shape = RoundedCornerShape(8.dp))
                                )

                                // Book title with padding
                                Text(
                                    text = book.title,
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp) // Adjust the padding as needed
                                        .padding(top = 8.dp),
                                    style = TextStyle.Default.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            } else {
                // Display a message when no books match the search
                Text(
                    text = "No matching books found",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    style = TextStyle.Default.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                )
            }
        }

        // Displaying the recommended books
        Column {
            Text(
                text = "Recommended", // Add your desired title here
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 16.dp, top = 8.dp),
                style = TextStyle.Default.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )

            // Carousel with book covers and titles
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(books) { book ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                // Navigate to book details activity
                                navController.navigate("bookDetails/${book.title}")
                            }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(8.dp)
                                .width(142.dp)
                                .wrapContentHeight()
                        ) {
                            // Book cover image
                            Image(
                                painter = rememberImagePainter(
                                    data = LocalContext.current.resources.getIdentifier(
                                        book.cover,
                                        "drawable",
                                        LocalContext.current.packageName
                                    ),
                                    builder = {
                                        crossfade(true)
                                        transformations(RoundedCornersTransformation(8f))
                                    }
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(142.dp) // Square dimensions
                                    .clip(shape = RoundedCornerShape(8.dp))
                            )

                            // Book title with padding
                            Text(
                                text = book.title,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp) // Adjust the padding as needed
                                    .padding(top = 8.dp),
                                style = TextStyle.Default.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        // Second row
        Column {
            Text(
                text = "Featured Books", // Add your desired title here
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 16.dp, top = 8.dp),
                style = TextStyle.Default.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )

            // Carousel with book covers and titles
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(books) { book ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                // Navigate to book details activity
                                navController.navigate("bookDetails/${book.title}")
                            }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(8.dp)
                                .width(142.dp)
                                .wrapContentHeight()
                        ) {
                            // Book cover image
                            Image(
                                painter = rememberImagePainter(
                                    data = LocalContext.current.resources.getIdentifier(
                                        book.cover,
                                        "drawable",
                                        LocalContext.current.packageName
                                    ),
                                    builder = {
                                        crossfade(true)
                                        transformations(RoundedCornersTransformation(8f))
                                    }
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(142.dp) // Square dimensions
                                    .clip(shape = RoundedCornerShape(8.dp))
                            )

                            // Book title with padding
                            Text(
                                text = book.title,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp) // Adjust the padding as needed
                                    .padding(top = 8.dp),
                                style = TextStyle.Default.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        // Third row
        Column {
            Text(
                text = "Classics", // Add your desired title here
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 16.dp, top = 8.dp),
                style = TextStyle.Default.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )

            // Carousel with book covers and titles
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                items(books) { book ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                // Navigate to book details activity
                                navController.navigate("bookDetails/${book.title}")
                            }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(8.dp)
                                .width(142.dp)
                                .wrapContentHeight()
                        ) {
                            // Book cover image
                            Image(
                                painter = rememberImagePainter(
                                    data = LocalContext.current.resources.getIdentifier(
                                        book.cover,
                                        "drawable",
                                        LocalContext.current.packageName
                                    ),
                                    builder = {
                                        crossfade(true)
                                        transformations(RoundedCornersTransformation(8f))
                                    }
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(142.dp) // Square dimensions
                                    .clip(shape = RoundedCornerShape(8.dp))
                            )

                            // Book title with padding
                            Text(
                                text = book.title,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp) // Adjust the padding as needed
                                    .padding(top = 8.dp),
                                style = TextStyle.Default.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}