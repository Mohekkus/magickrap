package etc

import java.util.regex.Pattern
import java.util.regex.Pattern.compile
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

object Global {

    val URL_PUBLIC_IP = "https://checkip.amazonaws.com"
    val emailRegex: Pattern = compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )


    fun List<String>.extend(collection: List<String>): List<String> {
        return toMutableList().apply {
            addAll(collection)
        }.toList()
    }
    inline fun <reified T : Any> T.toMutableMap(): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()

        T::class.memberProperties.forEach { property ->
            property.isAccessible = true
            if (property.get(this) != null)
                map[property.name] = property.get(this).toString()
        }

        return map
    }

    fun getResourcesString(alias: String) {

    }
}