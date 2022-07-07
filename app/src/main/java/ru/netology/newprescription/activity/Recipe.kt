package ru.netology.newprescription.activity

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val title: String,
    val author: String,
    val authorId: Int,
    val cuisineCategory: String,
    val dishTime: String?,
    val ingredientsList: List<Ingredient>,
    val cookingList: List<CookingStage>,
    val previewUri: Uri? = null,
    val favorite: Boolean = false,
    val id: Int
) : Parcelable {

    companion object {
        const val IDENT = 0
    }
}
