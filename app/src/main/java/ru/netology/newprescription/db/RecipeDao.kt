package ru.netology.newprescription.db

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.netology.newprescription.utils.DeleteTypeConverter

@Dao
interface RecipeDao {

    @Insert
    fun addRecipe(recipe: RecipeEntity)

    @Query("DELETE FROM recipes WHERE id= :recipe")
    @TypeConverters(DeleteTypeConverter::class)
    fun deleteRecipe(recipe: RecipeEntity)

    @Update
    fun editRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM recipes WHERE id= :recipeId")
    fun getRecipe(recipeId: Int): RecipeEntity

    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun getAllRecipes(): LiveData<List<RecipeEntity>>

    @Query(
        """
        UPDATE recipes SET
            favorite = CASE WHEN favorite THEN 0 ELSE 1 END
        WHERE id = :recipeId;
            """
    )
    fun favorite(recipeId: Int)
}