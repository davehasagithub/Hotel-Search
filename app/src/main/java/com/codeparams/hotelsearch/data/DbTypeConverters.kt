package com.codeparams.hotelsearch.data

import android.text.TextUtils
import androidx.room.TypeConverter
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.math.BigDecimal
import java.util.*

object DbTypeConverters {

    @ToJson
    @TypeConverter
    @JvmStatic
    fun bigDecimalToString(value: BigDecimal?): String =
        if (value == null) "0" else String.format(Locale.US, "%6.2f", value.toFloat())

    @FromJson
    @TypeConverter
    @JvmStatic
    fun bigDecimalFromString(value1: String?): BigDecimal {
        if (value1.isNullOrEmpty()) {
            return BigDecimal.ZERO
        }
        var value = value1.replace("[^0-9.]".toRegex(), "")
        val s = value.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        value = s[0] + if (s.size == 1) "" else "." + TextUtils.join("", s.copyOfRange(1, s.size))
        return (if (value.isEmpty()) BigDecimal.ZERO else BigDecimal(value)).setScale(
            2,
            BigDecimal.ROUND_HALF_UP
        )
    }
}
