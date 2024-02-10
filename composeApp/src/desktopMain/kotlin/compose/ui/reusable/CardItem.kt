package compose.ui.reusable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource


@Composable
fun serverCard(url: String? = null, text: String?) {
    Box(
        modifier = Modifier.padding(
            start = 16.dp, top = 8.dp, bottom = 8.dp, end = 0.dp
        )
    ) {
        Card (
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (url != null)
                    KamelImage(
                        contentDescription = url,
                        resource =  asyncPainterResource(url.toString()),
                        modifier = Modifier.size(32.dp)
                    )

                Text(
                    text.toString(),
                    style = MaterialTheme.typography.subtitle1
                )

            }
        }
    }
}

@Composable
fun componentCard(composable: @Composable () -> Unit) {
    Box(
        modifier = Modifier.padding(
            start = 8.dp, top = 8.dp, bottom = 8.dp, end = 8.dp
        )
    ) {
        Card (
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                composable()

            }
        }
    }
}