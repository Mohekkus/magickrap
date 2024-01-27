package etc

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.regex.Pattern
import java.util.regex.Pattern.compile

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


    fun String.toDataClass(classKType: Class<*>) = Gson().fromJson(this, classKType)

    fun List<String>.extend(collection: List<String>): List<String> {
        return toMutableList().apply {
            addAll(collection)
        }.toList()
    }
}