package ru.netology.newprescription.data

import androidx.lifecycle.MutableLiveData
import ru.netology.newprescription.activity.CookingStage
import ru.netology.newprescription.activity.Ingredient
import ru.netology.newprescription.activity.Recipe
import ru.netology.newprescription.activity.repository.RecipesOfList
import ru.netology.newprescription.utils.ContentRecipe
import kotlin.random.Random

object RecipeRepositoryImpl : RecipesOfList { // Список рецептов

    private const val CREATURE_RECIPE_COUNT = 10
    private const val CREATURE_INGREDIENTS_COUNT = 5
    private const val CREATURE_COOKING_STAGE_COUNT = 5

    private var nextId = CREATURE_RECIPE_COUNT

    override val data: MutableLiveData<List<Recipe>> = MutableLiveData(
        List(CREATURE_RECIPE_COUNT) { index ->
            Recipe(
                id = index + 1,
                title = "Recipe №${index + 1}",
                author = "Favourites",
                authorId = Random.nextInt(1, 5),
                cuisineCategory = ContentRecipe.getRandomCuisineCategory(),
                dishTime = "0h\n50min",
                ingredientsList =
                List(CREATURE_INGREDIENTS_COUNT) { ingredientIndex ->
                    Ingredient(
                        "Ingredient $ingredientIndex",
                        "${Random.nextInt(10, 100)} шт.",
                        ingredientIndex
                    )
                },
                cookingList =
                List(CREATURE_COOKING_STAGE_COUNT) { cookingStageIndex ->
                    CookingStage(
                        descript = "Description $cookingStageIndex",
                        stageImageUri = ContentRecipe.setRandomCookingStepImage(),
                        id = cookingStageIndex
                    )
                },
                previewUri = ContentRecipe.setRandomImagePreview()
            )
        }
    )

    private val recipeList
        get() = checkNotNull(data.value) {
            "enter the amount of funds"
        }

    override fun addRecipe(recipe: Recipe) {
        if (recipe.id == Recipe.IDENT) {
            data.value = listOf(recipe.copy(id = ++nextId)) + recipeList
        } else editRecipe(recipe)
    }

    override fun deleteRecipe(recipe: Recipe) {
        data.value = recipeList.filterNot { it.id == recipe.id }
    }

    override fun editRecipe(recipe: Recipe) {
        data.value = recipeList.map {
            if (it.id == recipe.id) recipe else it
        }
    }

    override fun getRecipe(recipeId: Int): Recipe {
        return recipeList.find {
            it.id == recipeId
        } ?: throw RuntimeException("The recipe's $recipeId is not in the list!")
    }

    override fun getAllRecipes(): List<Recipe> {
        return recipeList
    }

    override fun isFavorite(recipeId: Int) {
        data.value = recipeList.map { if (it.id == recipeId) it.copy(favorite = !it.favorite) else it }
    }
}