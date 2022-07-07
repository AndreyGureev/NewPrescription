package ru.netology.newprescription.activity

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CookingStage(
    val descript: String,
    val uri: String? = null,
    val stageImageUri: Uri? = null,
    val id: Int

) : Parcelable {

    companion object {
        const val UNDEFINED_ID = 0
    }
}