package ru.netology.newprescription.demo.userInterface

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.addCallback
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.newprescription.R
import ru.netology.newprescription.activity.Cuisine
import ru.netology.newprescription.databinding.OptionsFragmentBinding
import ru.netology.newprescription.demo.adapter.OptionsAdapter
import ru.netology.newprescription.utils.MyApp
import java.lang.reflect.Type


class OptionsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = OptionsFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        val selectedKitchenList = initSettings()

        val adapter = OptionsAdapter()

        binding.settingsList.adapter = adapter


        adapter.selectedKitchenList = Cuisine.selectedKitchenList

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            saveSettings(selectedKitchenList)
            findNavController().popBackStack()
        }


    }.root

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_bar_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        with(menu) {
            findItem(R.id.edit_button).isVisible = false
            findItem(R.id.delete_button).isVisible = false
            findItem(R.id.search_button).isVisible = false
            findItem(R.id.filter_button).isVisible = false
            findItem(R.id.add_button).isVisible = false
            findItem(R.id.cancel_button).isVisible = false
            findItem(R.id.ok_button).isVisible = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ok_button -> {
                saveSettings(Cuisine.selectedKitchenList)
                findNavController().popBackStack()
                true
            }
            else -> false
        }
    }

    private fun saveSettings(kitchenList: List<Cuisine>) {
        val prefs = context?.getSharedPreferences(SAVED_SETTINGS, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(kitchenList)
        prefs?.edit {
            putString(SAVED_SETTINGS, json)
        }
        Cuisine.selectedKitchenList = kitchenList as MutableList<Cuisine>
    }

    companion object {
        const val SAVED_SETTINGS = "savedContent"
        private const val DEFAULT_VALUE_FOR_SAVED_SETTINGS = ""

        fun initSettings(): List<Cuisine> {
            val gson = Gson()
            val prefs = MyApp.appContext?.getSharedPreferences(SAVED_SETTINGS, Context.MODE_PRIVATE)
            val type: Type = object : TypeToken<List<Cuisine>?>() {}.type
            val list = prefs?.getString(SAVED_SETTINGS, DEFAULT_VALUE_FOR_SAVED_SETTINGS)
            val selectedKitchenList =
                gson.fromJson<List<Cuisine>>(list, type) ?: Cuisine.selectedKitchenList.toList()
            Cuisine.selectedKitchenList = selectedKitchenList as MutableList<Cuisine>
            return selectedKitchenList
        }
    }
}