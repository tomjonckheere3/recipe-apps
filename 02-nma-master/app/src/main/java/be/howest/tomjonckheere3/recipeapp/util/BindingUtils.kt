package be.howest.tomjonckheere3.recipeapp.util

import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import be.howest.tomjonckheere3.recipeapp.domain.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


@BindingAdapter("listRecipes")
fun bindRecipesToRecyclerView(recyclerView: RecyclerView, data: List<NormalRecipe>?) {
    val adapter = recyclerView.adapter as RecipeListAdapter
    adapter.submitList(data)
}

@BindingAdapter("listIngredients")
fun bindIngredientsToRecyclerView(recyclerView: RecyclerView, data: List<Ingredient>?) {
    val adapter = recyclerView.adapter as IngredientListAdapter
    adapter.submitList(data)
}

@BindingAdapter("listSteps")
fun bindStepsToRecyclerView(recyclerView: RecyclerView, data: List<Step>?) {
    val adapter = recyclerView.adapter as StepListAdapter
    adapter.submitList(data)
}

@BindingAdapter("listComments")
fun bindCommentsToRecyclerView(recyclerView: RecyclerView, data: List<Comment>?) {
    val adapter = recyclerView.adapter as CommentListAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageBase64")
fun bindImage(imgView: ImageView, imgBase64: String?) {
    imgBase64?.let {
        val imgByteArray: ByteArray = Base64.decode(imgBase64, Base64.DEFAULT)
        Glide.with(imgView.context)
            .asBitmap()
            .load(imgByteArray)
            .apply(RequestOptions()
                .override(800, 600)
                .centerCrop())
            .into(imgView)
    }
}

@BindingAdapter("servingsFormatted")
fun TextView.setServings(item: NormalRecipe?) {
    item?.let {
        text = item.servings.toString()
    }
}

@BindingAdapter("prepareTimeFormatted")
fun TextView.setPrepareTime(item: NormalRecipe?) {
    item?.let {
        text = item.prepareTime.toString()
    }
}

@BindingAdapter("servingsFormattedDetailed")
fun TextView.setServingsDetailed(item: DetailedRecipe?) {
    item?.let {
        val servings = item.servings.toString()
        text = "Servings: $servings persons"
    }
}

@BindingAdapter("prepareTimeFormattedDetailed")
fun TextView.setPrepareTimeDetailed(item: DetailedRecipe?) {
    item?.let {
        val prepareTime = item.prepareTime.toString()
        text = "Prepare time: $prepareTime minutes"
    }
}

@BindingAdapter("priceFormattedDetailed")
fun TextView.setPriceDetailed(item: DetailedRecipe?) {
    item?.let {
        val price = item.price.toString()
        text = "Average cost: $price"
    }
}

@BindingAdapter("stepFormatted")
fun TextView.setStep(item: Step?) {
    item?.let {
        val step = item.step
        text = "STEP $step"
    }
}
