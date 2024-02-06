import androidx.compose.runtime.Composable

class AuxonodeDesktop {

    @Composable
    fun main() {

    }

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
}