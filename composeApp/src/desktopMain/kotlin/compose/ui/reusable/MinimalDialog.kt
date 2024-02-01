package compose.ui.reusable

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Preview
@Composable
fun minimalDialog(message: String, onDismissed: () -> Unit) {
    AlertDialog(
        text = {
            Text(message)
        },
        onDismissRequest = {
            onDismissed()
        },
        buttons =  {
            TextButton(
                onClick = {
                    onDismissed()
                }
            ) {
                Text("Ok")
            }
        }
    )
}