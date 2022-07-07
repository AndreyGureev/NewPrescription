package ru.netology.newprescription.demo.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.netology.newprescription.activity.*
import ru.netology.newprescription.data.RecipeRepositoryImpl
import ru.netology.newprescription.data.RoomRepository
import ru.netology.newprescription.db.AppDb
import ru.netology.newprescription.demo.adapter.listener.RecipeDetailsInteractionListener
import ru.netology.newprescription.utils.MultipleDevelopment

class DetailsViewModel(

    application: Application
) : AndroidViewModel(application), RecipeDetailsInteractionListener {

//    private val repository = RecipeRepositoryImpl

    private val repository: RoomRepository = RoomRepository(
        dao = AppDb.getInstance(context = application).recipeDao
    )

    val data by repository::data

    val navigateToRecipeEditorScreen = MultipleDevelopment<Recipe?>()

    fun deleteRecipe(recipe: Recipe) {
        repository.deleteRecipe(recipe)
    }

    fun editRecipe(recipe: Recipe) {
        repository.editRecipe(recipe)
    }

    fun getRecipeById(recipeId: Int): Recipe {
        return repository.getRecipe(recipeId)
    }

    override fun onFavoriteClicked(recipe: Recipe) = repository.isFavorite(recipe.id)

    override fun onDeleteClicked(recipe: Recipe) {
        deleteRecipe(recipe)
    }

    override fun onEditClicked(recipe: Recipe) {
        navigateToRecipeEditorScreen.value = recipe
    }
}