package com.example.secondhandapplication.activities

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.secondhandapp.data.user.UserViewModel
import com.example.secondhandapplication.R
import com.example.secondhandapplication.data.product.ProductViewModel
import com.google.firebase.auth.FirebaseAuth

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var productViewModel: ProductViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var auth :FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        auth = FirebaseAuth.getInstance()
        val productId = intent.getIntExtra("id",0)

        if(productId !=0 ){
            setProductInfo(productId)
            //Edit product
            setProductInfo(productId)

        }else{
            val intent = Intent(this,MainActivity::class.java)
            Toast.makeText(this,getString(R.string.error_details_product),Toast.LENGTH_SHORT).show()
            startActivity(intent)
            finish()
        }

    }
    private fun setProductInfo(id:Int){

        var edit_button: Button = findViewById(R.id.edit_product_button)
        var delete_button:Button = findViewById(R.id.delete_product_button)
        val title = findViewById<TextView>(R.id.details_title_card)
        val description = findViewById<TextView>(R.id.details_description_card)
        val phone = findViewById<TextView>(R.id.details_phone_card)
        val image = findViewById<ImageView>(R.id.details_image_card)
        val price = findViewById<TextView>(R.id.details_price_card)
        val address = findViewById<TextView>(R.id.details_address_card)
        val date = findViewById<TextView>(R.id.details_date_card)
        val goMap = findViewById<Button>(R.id.address_button)


        goMap.setOnClickListener {
            val intent = Intent(this,MapViewActivity::class.java)
            startActivity(intent)
        }

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        productViewModel.getProductById(id).observe(this, Observer {

            val product = it
            if(product.user_id != auth.currentUser!!.email){
                edit_button.visibility = View.GONE
                delete_button.visibility = View.GONE
            }
            title.text = product.title
            description.text = product.description
            date.text = product.date.toString()
            image.setImageBitmap(product.image)
            price.text = product.price.toString() +"€"

            delete_button.setOnClickListener {
                productViewModel.deleteProduct(product)
                Toast.makeText(this,getString(R.string.product_deleted),Toast.LENGTH_SHORT).show()

                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()

            }

            edit_button.setOnClickListener {
                val intent = Intent(this,EditProductActivity::class.java)
                intent.putExtra("id",product.id)
                startActivity(intent)


            }

            userViewModel.getUserByEmail(it.user_id).observe(this, Observer {
                val user = it
                phone.text = user.phone
                address.text = user.address.streetname + user.address.streetnumber.toString()

            })

        })
    }
}