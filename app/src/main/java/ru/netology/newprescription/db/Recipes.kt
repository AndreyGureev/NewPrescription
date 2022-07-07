package ru.netology.newprescription.db

import ru.netology.newprescription.activity.Recipe

fun RecipeEntity.toModel() = Recipe(
    id = id,
    title = title,
    author = author,
    authorId = authorId,
    cuisineCategory = cuisineCategory,
    dishTime = dishTime,
    ingredientsList = ingredientsList,
    cookingList = cookingList,
    previewUri = previewUri,
    favorite = favorite
)

fun Recipe.toEntity() = RecipeEntity(
    id = id,
    title = title,
    author = author,
    authorId = authorId,
    cuisineCategory = cuisineCategory,
    dishTime = dishTime,
    ingredientsList = ingredientsList,
    cookingList = cookingList,
    previewUri = previewUri,
    favorite = favorite
)