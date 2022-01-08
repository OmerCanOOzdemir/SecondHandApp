package com.example.secondhandapplication.activities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.secondhandapp.data.user.UserViewModel
import com.example.secondhandapplication.R
import com.example.secondhandapplication.data.product.Product
import com.example.secondhandapplication.data.product.ProductViewModel
import com.google.firebase.auth.FirebaseAuth

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var productViewModel: ProductViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var auth :FirebaseAuth
    private var own_product = false
    private lateinit var product: Product
    private var productId :Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        //Back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        auth = FirebaseAuth.getInstance()
        productId = intent.getIntExtra("id",0)

        if(productId !=0 ){
            setProductInfo(productId)
        }else{

            finish()
        }

    }

    private fun setProductInfo(id:Int){

        val title = findViewById<TextView>(R.id.details_title_card)
        val description = findViewById<TextView>(R.id.details_description_card)
        val phone = findViewById<TextView>(R.id.details_phone_card)
        val image = findViewById<ImageView>(R.id.details_image_card)
        val price = findViewById<TextView>(R.id.details_price_card)
        val address = findViewById<TextView>(R.id.details_address_card)
        val date = findViewById<TextView>(R.id.details_date_card)
        val goMap = findViewById<Button>(R.id.address_button)



        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        productViewModel.getProductById(id).observe(this, Observer {
            if(it == null){
                finish()
            }else{
                product = it
                if(product.user_id == auth.currentUser!!.email){
                    own_product = true
                    this.invalidateOptionsMenu()
                }
                title.text = product.title
                description.text = product.description
                date.text = product.date.toString()
                image.setImageBitmap(product.image)
                price.text = product.price.toString() +"€"

                goMap.setOnClickListener {
                    val gmmIntentUri =
                        Uri.parse("geo:0,0?q="+address.text)
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    startActivity(mapIntent)

                }

                userViewModel.getUserByEmail(it.user_id).observe(this, Observer {
                    val user = it
                    phone.text = user.phone
                    address.text = user.address.streetname +" "+ user.address.streetnumber.toString() + ","+user.address.country + ", "+user.address.city

                })
            }



        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(own_product){
            menuInflater.inflate(R.menu.product_details_menu,menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit_product_menu_btn ->{
                val intent = Intent(this,EditProductActivity::class.java)
                intent.putExtra("id",productId)
                startActivity(intent)
                return true
            }
            R.id.delete_product_menu_btn ->{
                productViewModel.deleteProduct(product)
                Toast.makeText(this,getString(R.string.product_deleted),Toast.LENGTH_SHORT).show()
                finish()
                return true
            }
        }
        return true
    }

}