package ru.netology.newprescription.activity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cuisine(
    var title: String,
    var isChecked: Boolean = true
) : Parcelable {

    companion object {
        var selectedKitchenList = mutableListOf(
            Cuisine("European", true),
            Cuisine("Asian", true),
            Cuisine("Pan-Asian", true),
            Cuisine("Eastern", true),
            Cuisine("American", true),
            Cuisine("Mediterranean", true),
            Cuisine("Undefined category", true)
        )
    }
}