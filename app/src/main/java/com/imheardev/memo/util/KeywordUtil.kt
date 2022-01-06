package com.imheardev.memo.util

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import java.util.regex.Pattern


object KeywordUtil {
    /**
     * 关键字高亮变色
     * @param color
     * 变化的色值
     * @param text
     * 文字
     * @param keyword
     * 文字中的关键字
     * @return
     */
    fun matcherSearchTitle(
        color: Int, text: String?,
        keyword: String?
    ): SpannableString {
        val s = SpannableString(text)
        val p = Pattern.compile(keyword)
        val m = p.matcher(s)
        while (m.find()) {
            val start = m.start()
            val end = m.end()
            s.setSpan(
                ForegroundColorSpan(color), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return s
    }

    /**
     * 多个关键字高亮变色
     * @param color
     * 变化的色值
     * @param text
     * 文字
     * @param keyword
     * 文字中的关键字数组
     * @return
     */
    fun matcherSearchTitle(
        color: Int, text: String?,
        keyword: Array<String?>
    ): SpannableString {
        val s = SpannableString(text)
        for (i in keyword.indices) {
            val p = Pattern.compile(keyword[i])
            val m = p.matcher(s)
            while (m.find()) {
                val start = m.start()
                val end = m.end()
                s.setSpan(
                    ForegroundColorSpan(color), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return s
    }
}