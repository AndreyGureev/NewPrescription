package ru.netology.newprescription.utils

import ru.netology.newprescription.activity.CuisineCategory
import kotlin.random.Random

object ContentRecipe {

    fun getRandomCuisineCategory(): String {
        return CuisineCategory.cuisineCategory.random()
    }

    fun setRandomImagePreview(): String? {
        val imgUrl = listOf<String?>(
            "https://s1.1zoom.ru/big0/513/The_second_dishes_Bell_pepper_Potato_Wood_planks_607727_1280x853.jpg",
            "https://s1.1zoom.ru/big0/537/The_second_dishes_Meat_products_Potato_Dill_Plate_606882_1280x853.jpg",
            "https://s1.1zoom.ru/big0/329/The_second_dishes_Vegetables_Fruit_Porridge_601269_1280x853.jpg",
            "https://s1.1zoom.ru/big0/676/The_second_dishes_Porridge_Meat_products_Cucumbers_590404_1219x1024.jpg",
            "https://s1.1zoom.ru/big0/59/Porridge_Fruit_The_second_dishes_Grain_570869_1280x853.jpg",
            "https://s1.1zoom.ru/big0/261/The_second_dishes_Meat_products_Potato_Vegetables_568230_1280x853.jpg",
            "https://s1.1zoom.ru/big0/153/The_second_dishes_Rice_Vegetables_Zucchini_Plate_567162_1238x1024.jpg",
            "https://s1.1zoom.ru/big0/335/The_second_dishes_Tomatoes_Cabbage_rolls_Plate_568391_1280x853.jpg",
            "https://s1.1zoom.ru/big0/500/The_second_dishes_Fish_Food_French_fries_566470_1280x853.jpg",
            "https://s1.1zoom.ru/big0/106/The_second_dishes_Potato_Meat_products_Vegetables_566163_1280x853.jpg",
            null
        )
        val urlIndex = Random.nextInt(0, imgUrl.size)
        return imgUrl[urlIndex]
    }

    fun setRandomCookingStepImage(): String? {
        val imgUrl = listOf<String?>(
            "https://www.art-active.ru/images/upload/wok-03.jpg",
            "https://funpanda.ru/wp-content/uploads/2012/08/IMG_8021.jpg",
            "https://vid.alarabiya.net/images/2014/12/09/78d05207-3efd-4145-a948-be21e29c6281/78d05207-3efd-4145-a948-be21e29c6281_16x9_1200x676.jpg?width=1138",
            "https://object.pscloud.io/cms/cms/Uploads/image_AhMwNn.png",
            "https://ae04.alicdn.com/kf/Hc379a69d227c4bec84927dd6b4e9233aa.jpg",
            null
        )
        val urlIndex = Random.nextInt(0, imgUrl.size)
        return imgUrl[urlIndex]
    }
}