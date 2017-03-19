package nl.dionsegijn.steppertouchdemo.util

import android.text.Html
import android.text.Spanned



/**
 * Created by dionsegijn on 3/19/17.
 */
class HtmlFormatter {
    fun stringToHtml(text: String) : Spanned {
        val result: Spanned
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            result = Html.fromHtml(text)
        }
        return result
    }
}
