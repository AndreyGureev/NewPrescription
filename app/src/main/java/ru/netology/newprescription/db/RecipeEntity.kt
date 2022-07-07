package ru.netology.newprescription.db

import android.net.Uri
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.jetbrains.annotations.NotNull
import ru.netology.newprescription.activity.CookingStage
import ru.netology.newprescription.activity.Ingredient
import ru.netology.newprescription.utils.CookingStageListTypeConverter
import ru.netology.newprescription.utils.ImageUriConverter
import ru.netology.newprescription.utils.IngredientListTypeConverter

@Entity(tableName = "recipes")
@TypeConverters(IngredientListTypeConverter::class, CookingStageListTypeConverter::class,
    ImageUriConverter::class)
class RecipeEntity(
    @ColumnInfo(name = "title")
    @NotNull
    val title: String,

    @ColumnInfo(name = "author")
    @NotNull
    val author: String,

    @ColumnInfo(name = "authorId")
    @NotNull
    val authorId: Int,

    @ColumnInfo(name = "cuisineCategory")
    @Nullable
    val cuisineCategory: String,

    @ColumnInfo(name = "dishTime")
    @Nullable
    val dishTime: String?,

    @ColumnInfo(name = "ingredientsList")
    @NotNull
    val ingredientsList: List<Ingredient>,

    @ColumnInfo(name = "cookingList")
    @NotNull
    val cookingList: List<CookingStage>,

    @ColumnInfo(name = "previewUri")
    @Nullable
    val previewUri: Uri?,

    @ColumnInfo(name = "favorite")
    val favorite: Boolean,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @NotNull
    val id: Int,
)