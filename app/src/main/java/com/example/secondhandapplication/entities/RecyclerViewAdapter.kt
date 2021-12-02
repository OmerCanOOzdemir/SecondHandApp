package com.example.secondhandapp.entities

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.secondhandapplication.R
import com.example.secondhandapplication.data.product.Product


class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {



    private var productList = emptyList<Product>()
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var image : ImageView
        var title : TextView
        var price : TextView

        init {
            image = itemView.findViewById<ImageView>(R.id.image_card)
            title = itemView.findViewById<TextView>(R.id.title_card)
            price = itemView.findViewById<TextView>(R.id.price_card)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_model, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        var currentProduct = productList[position]

        holder.image.setImageResource(R.drawable.petshop)
        holder.title.text = currentProduct.title
        holder.price.text = currentProduct.price.toString()

        holder.itemView.setOnClickListener {
            Toast.makeText(it.context,"Clicked", Toast.LENGTH_SHORT).show()
        }

    }

    override fun getItemCount(): Int {
        return productList.size
    }

     @SuppressLint("NotifyDataSetChanged")
     fun setData(products : List<Product>){
        this.productList = products
        notifyDataSetChanged()
    }
}