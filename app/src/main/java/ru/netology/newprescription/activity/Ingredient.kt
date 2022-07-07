package ru.netology.newprescription.activity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ingredient(
    val title: String,
    val value: String,
    val id: Int

) : Parcelable {

    companion object {
        const val UNDEFINED_ID = 0
    }
}