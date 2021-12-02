package com.example.secondhandapp.entities

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.secondhandapplication.R
import com.example.secondhandapplication.data.product.Product
import com.example.secondhandapplication.data.relations.UserWithProducts
import com.google.firebase.auth.FirebaseAuth


class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {


    private lateinit var auth:FirebaseAuth
    private var productList = emptyList<Product>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var image : ImageView
        var title : TextView
        var price : TextView
        init {
            auth = FirebaseAuth.getInstance()
            image = itemView.findViewById(R.id.image_card)
            title = itemView.findViewById(R.id.title_card)
            price = itemView.findViewById(R.id.price_card)

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

            val currentProduct = productList[position]

            holder.image.setImageBitmap(currentProduct.image)
            holder.title.text = currentProduct.title
            holder.price.text = currentProduct.price.toString()+"€"
            holder.itemView.findViewById<Button>(R.id.edit_product_button).visibility = View.GONE



            //Go to product details
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