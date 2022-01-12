package com.example.costaccounting.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.costaccounting.R
import com.example.costaccounting.activities.EditCategoryActivity
import com.example.costaccounting.data.Category

class CategoriesAdapter(val context: Context): RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    private var categoriesList = emptyList<Category>()
    private var namesList = emptyList<String>()

    class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.category_row, parent, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoriesList[position]
        val name = namesList[position]
        holder.itemView.findViewById<TextView>(R.id.textViewCategoryName).text = name

        holder.itemView.findViewById<CardView>(R.id.categoryRow).setOnClickListener{
            val intent = Intent(context, EditCategoryActivity::class.java)
            intent.putExtra("category", category)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    fun setData(categories: List<Category>) {
        categoriesList = categories
        notifyDataSetChanged()
    }

    fun setLabels(names: List<String>){
        namesList = names
        notifyDataSetChanged()
    }

}