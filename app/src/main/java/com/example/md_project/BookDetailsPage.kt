package com.example.md_project
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.example.md_project.ui.theme.Book
import com.example.md_project.ui.theme.BookViewModel


@SuppressLint("DiscouragedApi")
@Composable
fun BookDetailsPage(book: Book, navController: NavController, bookViewModel: BookViewModel) {
    var selectedButton by remember { mutableStateOf("none") }
    var selectedStars by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.End
        ) {
        // Home button in the top-left corner
        IconButton(
            onClick = { navController.popBackStack() },
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        // Profile button in the top-right corner
        IconButton(
            onClick = { navController.navigate("profileDetails") },
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }}


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
                .height(200.dp)
                .clip(shape = RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Book title
        Text(
            text = book.title,
            style = TextStyle.Default.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )

        // Author's name in 16sp and italic
        Text(
            text = book.author,
            style = TextStyle.Default.copy(
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp
            ),
        )


        Spacer(modifier = Modifier.height(16.dp))
        Divider(modifier = Modifier.fillMaxWidth(), color = Color.Gray, thickness = 1.dp)
        Spacer(modifier = Modifier.height(8.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),

        ) {
            createBookButton(
                label = "Want to Read",
                isSelected = selectedButton == "To Read",
                onClick = {
                    selectedButton = "To Read"
                    bookViewModel.toReadBooks.value.add(book)
                },
                shape = RoundedCornerShape(8.dp)
            )

            createBookButton(
                label = "Reading",
                isSelected = selectedButton == "Reading",
                onClick = {
                    selectedButton = "Reading"
                    bookViewModel.readingBooks.value.add(book)
                },
                shape = RoundedCornerShape(8.dp)
            )

            createBookButton(
                label = "Read",
                isSelected = selectedButton == "Read",
                onClick = {
                    selectedButton = "Read"
                    bookViewModel.readBooks.value.add(book)
                },
                shape = RoundedCornerShape(8.dp)
            )
        }

        // Row of stars
            if (selectedButton == "Read") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(5) { index ->
                        // Adjust the star's opacity based on the selection
                        val starAlpha = if (index < selectedStars) 1f else 0.2f

                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .alpha(starAlpha)
                                .clickable {
                                    selectedStars = index + 1
                                    // Handle star click
                                }
                        )
                    }
                }
            }

        Spacer(modifier = Modifier.height(8.dp))
        Divider(modifier = Modifier.fillMaxWidth(), color = Color.Gray, thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = book.description, fontSize = 16.sp)


    }
}

@Composable
fun createBookButton(label: String, isSelected: Boolean, onClick: () -> Unit, shape: Shape) {
    // Adjust the button transparency based on the selection
    val buttonAlpha = if (isSelected) 1f else 0.5f

    Button(
        onClick = onClick,
        modifier = Modifier
            .alpha(buttonAlpha),
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White
        )
    ) {
        Text(label)
    }
}
