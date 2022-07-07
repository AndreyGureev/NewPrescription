package ru.netology.newprescription.data

import androidx.lifecycle.map
import ru.netology.newprescription.activity.Recipe
import ru.netology.newprescription.activity.repository.RecipesOfList
import ru.netology.newprescription.db.RecipeDao
import ru.netology.newprescription.db.toEntity
import ru.netology.newprescription.db.toModel

class RoomRepository(
    private val dao: RecipeDao
) : RecipesOfList {

    override val data = dao.getAllRecipes().map { entities ->
        entities.map { it.toModel() }
    }

    override fun addRecipe(recipe: Recipe) {
        dao.addRecipe(recipe.toEntity())
    }

    override fun deleteRecipe(recipe: Recipe) {
        dao.deleteRecipe(recipe.toEntity())
    }

    override fun editRecipe(recipe: Recipe) {
        dao.editRecipe(recipe.toEntity())
    }

    override fun getRecipe(recipeId: Int): Recipe {
        return dao.getRecipe(recipeId).toModel()
    }

    override fun getAllRecipes(): List<Recipe> {
        return dao.getAllRecipes().value?.map { it.toModel() } ?: emptyList()
    }

    override fun isFavorite(recipeId: Int) {
        dao.favorite(recipeId)
    }
}