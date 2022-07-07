package ru.netology.newprescription.demo.userInterface

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import ru.netology.newprescription.R
import ru.netology.newprescription.activity.CookingStage
import ru.netology.newprescription.databinding.CookingStageEditorBinding
import ru.netology.newprescription.demo.adapter.display.CookingStageService

class CookingStageEditorFragment : Fragment() {

    private val args = navArgs<CookingStageEditorFragmentArgs>()

    private val cookingStepService = CookingStageService

    private var selectedCookingStageImageUri: Uri? = null

    private val selectImages: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri: Uri? ->
            selectedCookingStageImageUri = imageUri
            imageCookingStagePreview = false
            view?.findViewById<ImageView>(R.id.stage_preview)?.setImageURI(imageUri)
            view?.findViewById<ImageButton>(R.id.add_preview_button)?.visibility = View.GONE
            view?.findViewById<ImageButton>(R.id.clear_preview_button)?.visibility = View.VISIBLE
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = CookingStageEditorBinding.inflate(layoutInflater, container, false).also { binding ->

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            imageCookingStagePreview = false
            selectedCookingStageImageUri = null
            findNavController().popBackStack()
        }

        if (args.value.cookingStage !== null) {
            val step = args.value.cookingStage
            binding.descriptionStageEditText.setText(step?.descript)
            if (step?.stageImageUri == null) {
                binding.stagePreview.setImageResource(R.drawable.ic_baseline_image_not_supported_24)
                binding.addPreviewButton.visibility = View.VISIBLE
                binding.clearPreviewButton.visibility = View.GONE
            } else {
                binding.addPreviewButton.visibility = View.GONE
                binding.clearPreviewButton.visibility = View.VISIBLE
                Glide.with(binding.stagePreview)
                    .asDrawable()
                    .load(step.stageImageUri)
                    .error(R.drawable.ic_baseline_image_not_supported_24)
                    .into(binding.stagePreview)
            }
        } else {
            binding.clearPreviewButton.visibility = View.GONE
            binding.stagePreview.setImageResource(R.drawable.ic_baseline_image_not_supported_24)
        }

        binding.clearPreviewButton.setOnClickListener {
            binding.stagePreview.setImageResource(R.drawable.ic_baseline_image_not_supported_24)
            binding.addPreviewButton.visibility = View.VISIBLE
            binding.clearPreviewButton.visibility = View.GONE
            selectedCookingStageImageUri = null
            imageCookingStagePreview = true
        }

        binding.cancelButton.setOnClickListener {
            selectedCookingStageImageUri = null
            imageCookingStagePreview = true
            findNavController().popBackStack()
        }

        binding.clearDescriptionStepButton.setOnClickListener {
            binding.descriptionStageEditText.text?.clear()
        }

        binding.addPreviewButton.setOnClickListener {
            selectImages.launch(RESULT_IMAGES)
        }

        binding.okButton.setOnClickListener {
            val newDescription = binding.descriptionStageEditText.text
            if (!newDescription.isNullOrBlank()) {
                val step = CookingStage(
                    descript = newDescription.toString(),
                    stageImageUri = when {
                        imageCookingStagePreview -> null
                        selectedCookingStageImageUri !== null -> selectedCookingStageImageUri
                        args.value.cookingStage?.stageImageUri !== null -> args.value.cookingStage?.stageImageUri
                        else -> null
                    },
                    id = args.value.cookingStage?.id ?: CookingStage.UNDEFINED_ID
                )
                cookingStepService.addCookingStep(step)
                cookingStepService.targetCookingStep = null
                imageCookingStageUri = null
                findNavController().popBackStack()
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.fill_in_the_content_text),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }.root

    companion object {
        const val RESULT_IMAGES = "image/*"
        var imageCookingStageUri: Uri? = null
        var imageCookingStagePreview: Boolean = false
    }
}