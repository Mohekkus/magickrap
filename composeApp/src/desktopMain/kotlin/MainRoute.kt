import androidx.compose.runtime.Composable
import compose.ui.login.LoginComposable
import compose.ui.login.component.normalLoginComposable


enum class MainRoute {
    LOGIN{
        @Composable
        override fun get(): @Composable () -> Unit = {
            login.main()
        }
    },
    CERTIFICATE {
        @Composable
        override fun get(): @Composable () -> Unit = {
            certificate.main()
        }
    };

    @Composable
    abstract fun get(): @Composable () -> Unit
}