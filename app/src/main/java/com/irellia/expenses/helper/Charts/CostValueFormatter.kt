package com.irellia.expenses.helper.Charts

import com.github.mikephil.charting.formatter.ValueFormatter
import com.irellia.expenses.model.Payment

/**
 * Created by µðšţãƒâ ™ on 4/21/2019.
 * ->
 */
class CostValueFormatter(val payment: MutableList<Payment>) : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return if (payment.size > value.toInt())
            payment[value.toInt()].name
        else
            ""
    }
}