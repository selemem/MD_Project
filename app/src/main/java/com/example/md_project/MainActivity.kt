package com.example.md_project
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.painterResource
import coil.transform.RoundedCornersTransformation
import com.example.md_project.ui.theme.findBookByTitle
import androidx.compose.foundation.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Navigation()
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomePage(navController)
        }
        composable("profileDetails") {
            ProfilePage(navController = navController)
        }
        composable("bookDetails/{bookTitle}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("bookTitle") ?: ""
            val book = findBookByTitle(title)
            BookDetailsPage(book = book, onBack = { navController.popBackStack() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {
    // Read book information from the books.txt file
    val books = readBooksFromAssets(LocalContext.current, "books.txt")

    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search bar and profile button in a Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp) // Adjust the height
        ) {
            // Search bar
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
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

        // Carousel with book covers and titles
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
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
                            .width(142.dp)
                            .height(250.dp)
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
                                .width(142.dp)
                                .height(200.dp)
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

        // Other existing code...
    }
}












