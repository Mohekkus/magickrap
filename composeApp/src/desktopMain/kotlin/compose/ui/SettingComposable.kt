package compose.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import compose.MainRoute

class SettingComposable {

    companion object {
        val instance = SettingComposable()
    }

    @Composable
    fun main(callback: (MainRoute) -> Unit) {
        Window(
            onCloseRequest =  {}
        ) {

            Column {
                Row {
                    Column {
                        // OPTION
                    }

                    Column {
                        Card {
                            // View
                        }
                    }
                }

                Row {
                    //BUTTON
                }
            }
        }
    }
}