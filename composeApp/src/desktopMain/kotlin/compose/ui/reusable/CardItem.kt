package compose.ui.reusable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource


@Composable
fun cartItem(url: String? = null, name: String?) {
    Box(
        modifier = Modifier.padding(
            start = 16.dp, top = 8.dp, bottom = 8.dp, end = 16.dp
        )
    ) {
        Card (
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
                KamelImage(
                    contentDescription = url,
                    resource =  asyncPainterResource(url.toString()),
                    modifier = Modifier.size(32.dp)
                )

                Text(
                    name.toString(),
                    style = MaterialTheme.typography.subtitle1
                )

            }
        }
    }
}