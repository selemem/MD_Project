package com.example.md_project
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.example.md_project.ui.theme.Book

@SuppressLint("DiscouragedApi")
@Composable
fun BookDetailsPage(book: Book, onBack: () -> Unit) {
    // Your book details page content here
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
                .height(250.dp)
                .clip(shape = RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Book title
        Text(
            text = book.title,
            style = TextStyle.Default.copy(
                fontWeight = FontWeight.Bold
            )


        )

        Spacer(modifier = Modifier.height(16.dp))


        // Button to go back to the main activity
        Button(onClick = onBack) {
            Text(text = "Back to Home")
        }
    }
}
