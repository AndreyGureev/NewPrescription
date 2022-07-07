package ru.netology.newprescription.activity.repository

import androidx.lifecycle.LiveData
import ru.netology.newprescription.activity.Recipe

interface RecipesOfList {

    val data: LiveData<List<Recipe>>

    fun addRecipe(recipe: Recipe)

    fun deleteRecipe(recipe: Recipe)

    fun editRecipe(recipe: Recipe)

    fun getRecipe(recipeId: Int): Recipe

    fun getAllRecipes(): List<Recipe>

    fun isFavorite(recipeId: Int)

    companion object {
        const val CANCEL_SEARCH = "cancel search"
    }
}