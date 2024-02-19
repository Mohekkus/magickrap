package compose

import androidx.compose.runtime.Composable
import certificate
import login


enum class MainRoute {
    LOGIN{
        @Composable
        override fun get(navigate: (MainRoute) -> Unit): @Composable () -> Unit = {
            login.main {
                navigate.invoke(CERTIFICATE)
            }
        }
    },
    CERTIFICATE {
        @Composable
        override fun get(navigate: (MainRoute) -> Unit): @Composable () -> Unit = {
            certificate.main {
                navigate.invoke(LOGIN)
            }
        }
    };

    @Composable
    abstract fun get(navigate: (MainRoute) -> Unit): @Composable () -> Unit
}