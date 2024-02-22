import androidx.compose.runtime.Composable
import compose.ui.login.LoginComposable
import compose.ui.login.component.normalLoginComposable


enum class MainRoute {
    LOGIN{
        @Composable
        override fun get(navigate: () -> Unit): @Composable () -> Unit = {
            login.main {
                navigate.invoke()
            }
        }
    },
    CERTIFICATE {
        @Composable
        override fun get(navigate: () -> Unit): @Composable () -> Unit = {
            certificate.main {
                navigate.invoke()
            }
        }
    };

    @Composable
    abstract fun get(navigate: () -> Unit): @Composable () -> Unit
}