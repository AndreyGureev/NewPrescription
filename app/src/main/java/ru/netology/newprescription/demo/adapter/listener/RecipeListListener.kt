package ru.netology.newprescription.demo.adapter.listener

import ru.netology.newprescription.activity.Recipe

interface RecipeListListener {

    fun onFavoriteClicked(recipe: Recipe)

    fun onRecipeClicked(recipe: Recipe)

    fun onAddClicked()

}