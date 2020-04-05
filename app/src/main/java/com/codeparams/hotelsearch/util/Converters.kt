package com.codeparams.hotelsearch.util

import java.math.BigDecimal
import java.util.*

object Converters {

    @JvmStatic
    fun bigDecimalToString(value: BigDecimal) = String.format(Locale.US, "%,.2f", value.toFloat())

}
