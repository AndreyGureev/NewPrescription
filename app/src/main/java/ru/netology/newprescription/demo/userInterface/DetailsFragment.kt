package ru.netology.newprescription.demo.userInterface

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import ru.netology.newprescription.R
import ru.netology.newprescription.activity.Cuisine
import ru.netology.newprescription.activity.Recipe
import ru.netology.newprescription.databinding.RecipeFunctionsFragmentBinding
import ru.netology.newprescription.demo.adapter.display.RecipeDetailsCookingStage
import ru.netology.newprescription.demo.adapter.display.RecipeDetailsIngredients
import ru.netology.newprescription.demo.viewModel.DetailsViewModel

class DetailsFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModels()
    private val args by navArgs<DetailsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.navigateToRecipeEditorScreen.observe(this) { recipe ->
            val direction = DetailsFragmentDirections.toRecipeEditorFragment(recipe)
            findNavController().navigate(direction)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = RecipeFunctionsFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        setFragmentResultListener(requestKey = RecipeEditorFragment.ORDER_KEY) { requestKey, bundle ->
            if (requestKey !== RecipeEditorFragment.ORDER_KEY) return@setFragmentResultListener
            val recipe =
                bundle.getParcelable<Recipe>(RecipeEditorFragment.RESULT_KEY_NEW_STAGE)
            if (recipe != null)
                viewModel.editRecipe(recipe)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }

        viewModel.data.observe(viewLifecycleOwner) { recipeList ->
            val recipe = recipeList.first { it.id == args.recipeId }
            with(binding) {
                recipeItems.author.text = recipe.author
                recipeItems.title.text = recipe.title
                if (recipe.previewUri !== null) {
                    Glide.with(this@DetailsFragment)
                        .asDrawable()
                        .load(recipe.previewUri)
                        .error(R.drawable.ic_baseline_image_not_supported_24)
                        .into(recipeItems.recipeOverview)
                } else {
                    recipeItems.recipeOverview.setImageResource(R.drawable.ic_baseline_image_not_supported_24)
                }
                if (recipe.cuisineCategory == Cuisine.selectedKitchenList.last().title) {
                    recipeItems.cuisineCategory.visibility = View.GONE
                } else recipeItems.cuisineCategory.text = recipe.cuisineCategory
                if (recipe.dishTime == null) {
                    recipeItems.dishTime.visibility = View.GONE
                } else recipeItems.dishTime.text = recipe.dishTime
                recipeItems.favoriteButton.setImageResource(
                    when (recipe.favorite) {
                        true -> R.drawable.ic_baseline_favorite_24
                        false -> R.drawable.ic_baseline_favorite_border_24
                    }
                )

                val ingredientsAdapter = RecipeDetailsIngredients()
                binding.ingredientView.adapter = ingredientsAdapter
                ingredientsAdapter.submitList(recipe.ingredientsList)

                val cookingStepsAdapter = RecipeDetailsCookingStage()
                binding.cookingCompositionView.adapter = cookingStepsAdapter
                cookingStepsAdapter.submitList(recipe.cookingList)

                cookingCompositionButton.setOnClickListener {
                    if (cookingCompositionView.visibility == View.VISIBLE) {
                        cookingCompositionView.visibility = View.GONE
                        cookingCompositionButton.setIconResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    } else {
                        cookingCompositionView.visibility = View.VISIBLE
                        cookingCompositionButton.setIconResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                    }
                }

                ingredientsButton.setOnClickListener {
                    if (ingredientView.visibility == View.VISIBLE) {
                        ingredientView.visibility = View.GONE
                        ingredientsButton.setIconResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    } else {
                        ingredientView.visibility = View.VISIBLE
                        ingredientsButton.setIconResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                    }
                }

                recipeItems.favoriteButton.setOnClickListener {
                    viewModel.onFavoriteClicked(recipe)
                }

            }
        }
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
            findItem(R.id.filter_button).isVisible = false
            findItem(R.id.cancel_button).isVisible = false
            findItem(R.id.add_button).isVisible = false
            findItem(R.id.ok_button).isVisible = false
            val myId = 2
            Log.d("TAG", "recipe id is ${args.recipeId}")
            val recipe = viewModel.getRecipeById(args.recipeId)
            Log.d("TAG", "recipe id is $recipe")
            if (recipe.authorId == myId) {
                findItem(R.id.edit_button).isVisible = true
                findItem(R.id.delete_button).isVisible = true
            } else {
                findItem(R.id.edit_button).isVisible = false
                findItem(R.id.delete_button).isVisible = false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_button -> {
                val recipe = viewModel.data.value?.find { it.id == args.recipeId } ?: return false
                viewModel.onDeleteClicked(recipe)
                findNavController().popBackStack()
                true
            }
            R.id.edit_button -> {
                val recipe = viewModel.data.value?.find { it.id == args.recipeId } ?: return false
                viewModel.onEditClicked(recipe)
                true
            }
            R.id.cancel_button -> {
                findNavController().popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}