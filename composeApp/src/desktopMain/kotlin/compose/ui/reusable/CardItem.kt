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
fun serverCard(url: String? = null, text: String?, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
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

                content()
            }
        }
    }
}

@Composable
fun componentCard(modifier: Modifier? = null, composable: @Composable () -> Unit) {
    Box(
        modifier = modifier ?: Modifier,
    ) {
        Card (
            shape = RoundedCornerShape(16.dp),
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                composable()
            }
        }
    }
}