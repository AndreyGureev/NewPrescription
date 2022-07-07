package ru.netology.newprescription.demo.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.newprescription.activity.*
import ru.netology.newprescription.data.RoomRepository
import ru.netology.newprescription.db.AppDb
import ru.netology.newprescription.demo.adapter.listener.RecipeListListener
import ru.netology.newprescription.utils.MultipleDevelopment

class ListViewModel(

    application: Application
) : AndroidViewModel(application), RecipeListListener {

    private val repository: RoomRepository = RoomRepository(
        dao = AppDb.getInstance(context = application).recipeDao
    )

    val data by repository::data

    val navigateToRecipeDetailsScreen = MultipleDevelopment<Recipe>()

    val navigateToRecipeEditorScreen = MultipleDevelopment<Recipe?>()

    fun addRecipe(recipe: Recipe) {
        repository.addRecipe(recipe)
    }

    fun getRecipeById(recipeId: Int): Recipe {
        return repository.getRecipe(recipeId)
    }

    override fun onFavoriteClicked(recipe: Recipe) = repository.isFavorite(recipe.id)

    override fun onRecipeClicked(recipe: Recipe) {
        navigateToRecipeDetailsScreen.value = recipe
    }

    override fun onAddClicked() {
        navigateToRecipeEditorScreen.value = null
    }
}