package compose

import androidx.compose.runtime.Composable
import login
import main


enum class MainRoute {
    LOGIN{
        @Composable
        override fun get(navigate: (MainRoute) -> Unit): @Composable () -> Unit = {
            login.main {
                navigate.invoke(MAIN)
            }
        }
    },
    MAIN {
        @Composable
        override fun get(navigate: (MainRoute) -> Unit): @Composable () -> Unit = {
            main.main {
                navigate.invoke(it)
            }
        }
    };

    @Composable
    abstract fun get(navigate: (MainRoute) -> Unit): @Composable () -> Unit
}