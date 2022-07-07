package ru.netology.newprescription.demo.userInterface

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import ru.netology.newprescription.R
import ru.netology.newprescription.activity.CookingStage
import ru.netology.newprescription.databinding.RecipeEditorFragmentBinding
import ru.netology.newprescription.activity.Ingredient
import ru.netology.newprescription.activity.Recipe
import ru.netology.newprescription.demo.adapter.display.*
import ru.netology.newprescription.demo.adapter.listener.CookingStageActionListener
import ru.netology.newprescription.demo.adapter.listener.IngredientActionListener
import ru.netology.newprescription.demo.viewModel.ListViewModel
import ru.netology.newprescription.utils.CookingTimeConverter


class RecipeEditorFragment : Fragment() {

    private val args by navArgs<RecipeEditorFragmentArgs>()
    private val viewModel: ListViewModel by viewModels()

    private val ingredientService: IngredientService = IngredientService
    private val cookingStepsService: CookingStageService = CookingStageService

    private var selectedRecipePreviewImageUri: Uri? = null

    private val selectImages: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri: Uri? ->
            selectedRecipePreviewImageUri = imageUri
            imageRecipePreviewIsEmpty = false
            view?.findViewById<ImageView>(R.id.recipe_preview)?.setImageURI(imageUri)
            view?.findViewById<ImageButton>(R.id.add_preview_button)?.visibility = View.GONE
            view?.findViewById<ImageButton>(R.id.clear_preview_button)?.visibility = View.VISIBLE
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeEditorFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            clearCache()
            imageRecipePreviewIsEmpty = false
            selectedRecipePreviewImageUri = null
            findNavController().popBackStack()
        }

        val recipe = args.recipe?.id?.let { viewModel.getRecipeById(it) }

        binding.addPreviewButton.visibility = View.VISIBLE
        binding.clearPreviewButton.visibility = View.GONE

        if (recipe !== null) {
            val editedCookingStepList = cookingStepsService.getCookingSteps()
            val editedIngredientsList = ingredientService.getIngredients()
            when (editedCookingStepList) {
                emptyList<CookingStage>() -> cookingStepsService.setCookingStepsList(recipe.cookingList as MutableList<CookingStage>)
                else -> cookingStepsService.setCookingStepsList(editedCookingStepList as MutableList<CookingStage>)
            }
            when (editedIngredientsList) {
                emptyList<Ingredient>() -> ingredientService.setIngredientsList(recipe.ingredientsList as MutableList<Ingredient>)
                else -> ingredientService.setIngredientsList(editedIngredientsList as MutableList<Ingredient>)
            }

            if (savedInstanceState == null) {
                binding.title.setText(recipe.title)
                binding.cookingTimeHours.setText(CookingTimeConverter.convertToHours(recipe.dishTime))
                binding.cookingTimeMinutes.setText(CookingTimeConverter.convertToMinutes(recipe.dishTime))
                binding.kitchenCategoryTitle.text = recipe.cuisineCategory
                if (recipe.previewUri !== null) {
                    imageRecipePreviewIsEmpty = false
                    binding.addPreviewButton.visibility = View.GONE
                    binding.clearPreviewButton.visibility = View.VISIBLE
                    Glide.with(this)
                        .asDrawable()
                        .load(recipe.previewUri)
                        .error(R.drawable.ic_baseline_image_not_supported_24)
                        .into(binding.recipePreview)
                }
            }
        } else binding.recipePreview.setImageResource(R.drawable.ic_baseline_image_not_supported_24)

        if (selectedRecipePreviewImageUri !== null) {
            Glide.with(this)
                .asDrawable()
                .load(selectedRecipePreviewImageUri)
                .error(R.drawable.ic_baseline_image_not_supported_24)
                .into(binding.recipePreview)
            binding.addPreviewButton.visibility = View.GONE
            binding.clearPreviewButton.visibility = View.VISIBLE
        }

        if (selectedKitchen !== null) binding.kitchenCategoryTitle.text = selectedKitchen

        val ingredientsAdapter = IngredientsAdapter(object : IngredientActionListener {
            override fun onIngredientUp(ingredient: Ingredient, moveBy: Int) {
                ingredientService.moveIngredient(ingredient, moveBy)
            }

            override fun onIngredientDown(ingredient: Ingredient, moveBy: Int) {
                ingredientService.moveIngredient(ingredient, moveBy)
            }

            override fun onIngredientEdit(ingredient: Ingredient) {
                ingredientService.targetIngredient = ingredient
                binding.ingredientEditGroup.visibility = View.VISIBLE
                binding.addIngredientButton.text = getString(R.string.save_ingredient)
                binding.newIngredientNameEditText.setText(ingredient.title)
                binding.newIngredientValueEditText.setText(ingredient.value)
            }

            override fun onIngredientDelete(ingredient: Ingredient) {
                ingredientService.deleteIngredient(ingredient)
            }
        })

        val cookingInstructionStepsAdapter =
            this.context?.let {
                CookingInstructionStepsAdapter(it, object : CookingStageActionListener {
                    override fun onCookingStageUp(cookingStage: CookingStage, moveBy: Int) {
                        cookingStepsService.moveCookingStep(cookingStage, moveBy)
                    }

                    override fun onCookingStageDown(cookingStage: CookingStage, moveBy: Int) {
                        cookingStepsService.moveCookingStep(cookingStage, moveBy)
                    }

                    override fun onCookingStageEdit(cookingStage: CookingStage) {
                        cookingStepsService.targetCookingStep = cookingStage
                        val direction =
                            RecipeEditorFragmentDirections.toCookingStageEditorFragment(cookingStage)
                        findNavController().navigate(direction)
                    }

                    override fun onCookingStageDelete(cookingStage: CookingStage) {
                        cookingStepsService.deleteCookingStep(cookingStage)
                    }
                })
            }

        binding.ingredientEditGroup.visibility = View.GONE

        binding.ingredientsList.adapter = ingredientsAdapter
        binding.cookingInstructionList.adapter = cookingInstructionStepsAdapter

        val ingredientsListener: IngredientListener = {
            ingredientsAdapter.ingredients = ingredientService.getIngredients()
        }
        val cookingStepsListener: CookingStepListener = {
            cookingInstructionStepsAdapter?.cookingStages = cookingStepsService.getCookingSteps()
        }

        ingredientService.addListener(ingredientsListener)
        cookingStepsService.addListener(cookingStepsListener)

        binding.clearPreviewButton.setOnClickListener {
            binding.addPreviewButton.visibility = View.VISIBLE
            binding.clearPreviewButton.visibility = View.GONE
            binding.recipePreview.setImageResource(R.drawable.ic_baseline_image_not_supported_24)
            selectedRecipePreviewImageUri = null
            imageRecipePreviewIsEmpty = true
        }

        binding.addPreviewButton.setOnClickListener {
            selectImages.launch(RESULT_IMAGES)
        }

        binding.cancelEditIngredientButton.setOnClickListener {
            binding.newIngredientNameEditText.text.clear()
            binding.newIngredientValueEditText.text.clear()
            binding.addIngredientButton.text = getString(R.string.add_ingredient)
            binding.ingredientEditGroup.visibility = View.GONE
        }

        binding.kitchenCategoryButton.setOnClickListener {
            PopupMenu(binding.root.context, binding.kitchenCategoryButton).apply {
                inflate(R.menu.dish_category_menu)
                setOnMenuItemClickListener {
                    binding.kitchenCategoryTitle.text = it.title
                    selectedKitchen = it.title.toString()
                    true
                }
            }.show()
        }

        binding.addIngredientButton.setOnClickListener {
            val ingredientEditGroupVisibility = binding.ingredientEditGroup.visibility
            val targetIngredient =
                ingredientService.targetIngredient

            if (ingredientEditGroupVisibility == View.GONE) {
                binding.ingredientEditGroup.visibility = View.VISIBLE
                binding.addIngredientButton.text = getString(R.string.save_ingredient)
            }
            if (ingredientEditGroupVisibility == View.VISIBLE && binding.newIngredientNameEditText.text.isNullOrBlank()) {
                toastMaker("Ingredient name field is empty")
                return@setOnClickListener
            }
            if (ingredientEditGroupVisibility == View.VISIBLE && targetIngredient !== null) {
                ingredientService.targetIngredient = targetIngredient.copy(
                    title = binding.newIngredientNameEditText.text.toString(),
                    value = binding.newIngredientValueEditText.text.toString()
                )
                ingredientService.targetIngredient?.let { ingredient ->
                    ingredientService.editIngredient(ingredient)
                }
                ingredientService.targetIngredient = null
                binding.newIngredientNameEditText.text.clear()
                binding.newIngredientValueEditText.text.clear()
                binding.ingredientEditGroup.visibility = View.GONE
                binding.addIngredientButton.text = getString(R.string.add_ingredient)
                return@setOnClickListener
            }
            if (ingredientEditGroupVisibility == View.VISIBLE && targetIngredient == null) {
                ingredientService.targetIngredient = Ingredient(
                    title = binding.newIngredientNameEditText.text.toString(),
                    value = binding.newIngredientValueEditText.text.toString(),
                    id = Ingredient.UNDEFINED_ID
                )
                ingredientService.targetIngredient?.let { ingredient ->
                    ingredientService.addIngredient(ingredient)
                }
                ingredientService.targetIngredient = null
                binding.newIngredientNameEditText.text.clear()
                binding.newIngredientValueEditText.text.clear()
                binding.ingredientEditGroup.visibility = View.GONE
                binding.addIngredientButton.text = getString(R.string.add_ingredient)
                return@setOnClickListener
            }

        }
        binding.addCookingStepButton.setOnClickListener {
            val direction = RecipeEditorFragmentDirections.toCookingStageEditorFragment(null)
            findNavController().navigate(direction)
        }
        Log.d("TAG", "step list editor fragment ${CookingStageService.getCookingSteps()}")

    }.root

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_bar_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val toolBarEditText = activity?.findViewById(R.id.toolBarEditText) as EditText
        toolBarEditText.visibility = View.GONE
        super.onPrepareOptionsMenu(menu)
        with(menu) {
            findItem(R.id.search_button).isVisible = false
            findItem(R.id.add_button).isVisible = false
            findItem(R.id.filter_button).isVisible = false
            findItem(R.id.edit_button).isVisible = false
            findItem(R.id.delete_button).isVisible = false
            findItem(R.id.ok_button).isVisible = true
            findItem(R.id.cancel_button).isVisible = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ok_button -> {
                val resultBundle = Bundle(1)
                val newRecipe = Recipe(
                    id = args.recipe?.id ?: Recipe.IDENT,
                    title = view?.findViewById<EditText>(R.id.title)?.text.toString(),
                    author = "Unknown",
                    authorId = 2,
                    cuisineCategory = view?.findViewById<TextView>(R.id.kitchen_category_title)?.text.toString(),
                    dishTime = CookingTimeConverter.convertToString(
                        view?.findViewById<EditText>(R.id.cooking_time_hours)?.text.toString(),
                        view?.findViewById<EditText>(R.id.cooking_time_minutes)?.text.toString()
                    ),
                    ingredientsList = IngredientService.getIngredients(),
                    cookingList = CookingStageService.getCookingSteps(),
                    previewUri = when {
                        imageRecipePreviewIsEmpty -> null
                        selectedRecipePreviewImageUri !== null -> selectedRecipePreviewImageUri
                        args.recipe?.previewUri !== null -> args.recipe?.previewUri
                        else -> null
                    },
                    favorite = false

                )
                if (checkRecipeForEmptyFields(newRecipe)) {
                    clearCache()
                    resultBundle.putParcelable(RESULT_KEY_NEW_STAGE, newRecipe)
                    setFragmentResult(ORDER_KEY, resultBundle)
                    findNavController().popBackStack()
                }
                true
            }
            R.id.cancel_button -> {
                clearCache()
                findNavController().popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkRecipeForEmptyFields(recipe: Recipe): Boolean {
        return when {
            recipe.title.isBlank() -> {
                toastMaker("title shouldn't be empty")
                false
            }
            recipe.ingredientsList.isEmpty() -> {
                toastMaker("Please add at least one ingredient")
                false
            }
            recipe.cookingList.isEmpty() -> {
                toastMaker("Please add at least one cooking instruction step")
                false
            }
            else -> true
        }
    }

    private fun toastMaker(value: String) {
        Toast.makeText(
            context,
            value,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun clearCache() {
        selectedKitchen = null
        ingredientService.targetIngredient = null
        cookingStepsService.targetCookingStep = null
        ingredientService.setIngredientsList(mutableListOf())
        cookingStepsService.setCookingStepsList(mutableListOf())
    }

    companion object {
        const val RESULT_KEY_NEW_STAGE = "add new recipe"
        const val ORDER_KEY = "requestKey"
        const val RESULT_IMAGES = "image/*"
        var imageRecipePreviewIsEmpty: Boolean = false
        var selectedKitchen: String? = null
    }
}