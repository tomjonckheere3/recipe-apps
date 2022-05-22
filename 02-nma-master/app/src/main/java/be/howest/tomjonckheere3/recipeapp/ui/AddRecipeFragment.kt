package be.howest.tomjonckheere3.recipeapp.ui

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import be.howest.tomjonckheere3.recipeapp.R
import be.howest.tomjonckheere3.recipeapp.databinding.FragmentAddRecipeBinding
import be.howest.tomjonckheere3.recipeapp.viewmodels.AddRecipeViewModel
import java.io.ByteArrayOutputStream
import java.lang.Exception

class AddRecipeFragment: Fragment() {

    private val viewModel: AddRecipeViewModel by lazy {
        ViewModelProvider(this).get(AddRecipeViewModel::class.java)
    }

    private var image: Bitmap? = null

    private var totalIngredients = 1

    private var totalSteps = 1

    @SuppressLint("SetTextI18n", "ShowToast")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<FragmentAddRecipeBinding>(inflater,
            R.layout.fragment_add_recipe, container, false)

        binding.lifecycleOwner = this

        binding.addIngredientButton.setOnClickListener {
            totalIngredients += 1
            val newIngredientView = EditText(requireContext())
            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
            newIngredientView.layoutParams = params
            newIngredientView.hint = "Ingredient $totalIngredients"
            binding.addIngredientsLinearLayout.addView(newIngredientView)
        }

        binding.addStepButton.setOnClickListener {
            totalSteps += 1
            val newStepView = EditText(requireContext())
            val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
            newStepView.layoutParams = params
            newStepView.hint = "Step $totalSteps"
            binding.addStepsLinearLayout.addView(newStepView)
        }

        binding.addRecipeImageButton.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
        }

        binding.addRecipeButton.setOnClickListener {
            val recipeName = binding.addRecipeName.text.toString()
            val description = binding.addDescription.text.toString()
            val mealTypeId = binding.mealTypeOptions.checkedRadioButtonId
            val mealType = resources.getResourceEntryName(mealTypeId)
            val servings = binding.addServings.text.toString().toInt()
            val prepareTime = binding.addPrepareTime.text.toString().toInt()
            val price = binding.addPrice.text.toString().toInt()
            val ingredientStrings = getIngredientValues(binding)
            val stepEntries = getStepValues(binding)
            val base64Image = image?.let { image -> imageToBase64String(image) }
            viewModel.addRecipe(recipeName, mealType, servings, prepareTime, price, description,
                    base64Image, ingredientStrings, stepEntries)
        }

        viewModel.navigateToAddedRecipe.observe(viewLifecycleOwner, Observer{
            try {
                this.findNavController().navigate(
                        AddRecipeFragmentDirections.actionAddRecipeFragmentToDetailedRecipeFragment(it))
                viewModel.displayRecipeDetailsComplete()
            } catch (exc: Exception) {
                exc.message?.let { it1 -> Log.e("error", it1) }
            }
        })

        return binding.root
    }

    private fun imageToBase64String(image: Bitmap): String {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun getIngredientValues(binding: FragmentAddRecipeBinding) : MutableList<String> {
        val ingredientStrings = emptyList<String>().toMutableList()
        for (i in 0 until binding.addIngredientsLinearLayout.childCount) {
            val ingredientEditView : EditText = binding.addIngredientsLinearLayout.getChildAt(i) as EditText
            ingredientStrings += ingredientEditView.text.toString()
        }
        return ingredientStrings
    }

    private fun getStepValues(binding: FragmentAddRecipeBinding) : Map<Int, String> {
        val stepStrings = emptyMap<Int, String>().toMutableMap()
        for (i in 0 until binding.addStepsLinearLayout.childCount) {
            val stepEditView : EditText = binding.addStepsLinearLayout.getChildAt(i) as EditText
            val step = i+1
            stepStrings[step] = stepEditView.text.toString()
        }
        return stepStrings
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            val imageUri = data?.data
            image = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
        }
    }
}