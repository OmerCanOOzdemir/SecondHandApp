package com.example.secondhandapplication.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.secondhandapplication.R
import com.example.secondhandapplication.data.category.CategoryViewModel
import com.example.secondhandapplication.data.product.Product
import com.example.secondhandapplication.data.product.ProductViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class EditProductActivity : AppCompatActivity() {

    private lateinit var productViewModel: ProductViewModel

    private var image_from_galery: Bitmap? = null
    private var old_image :Bitmap? = null
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var auth :FirebaseAuth


    // https://developer.android.com/training/basics/intents/result
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        //getBitmap is deprecated
        if(Build.VERSION.SDK_INT <28){
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,uri)
            if(bitmap != null){
                image_from_galery = bitmap
            }
        }else{
            val source = uri?.let { ImageDecoder.createSource(this.contentResolver, it) }
            if(source != null){
                image_from_galery = ImageDecoder.decodeBitmap(source)
            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)
        //Back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()
        val id = intent.getIntExtra("id",0)

        if(id != 0){
            //Set product information
            setProductInformations(id)
        }else{
            Toast.makeText(this,getString(R.string.error_details_product),Toast.LENGTH_SHORT).show()
            val intent = Intent(this,ProductDetailsActivity::class.java)
            startActivity(intent)
            finish()
        }


    }



    private fun setProductInformations(id:Int) {
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        categoryViewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)

        val titleProduct = findViewById<EditText>(R.id.edit_title_product_input)
        val descriptionProduct = findViewById<EditText>(R.id.edit_description_product_input)
        val priceProduct = findViewById<EditText>(R.id.edit_price_product_input)
        val editProductBtn = findViewById<Button>(R.id.edit_product_btn_)
        val category = findViewById<AutoCompleteTextView>(R.id.edit_choose_category)

        //Get all categories
        categoryViewModel.allCategories.observe(this, androidx.lifecycle.Observer {
            val adapter = ArrayAdapter(this,R.layout.dropdown_category,it)
            category.setAdapter(adapter)
        })

        productViewModel.getProductById(id).observe(this, Observer {
            titleProduct.setText(it.title)
            descriptionProduct.setText(it.description)
            priceProduct.setText(it.price.toString())
            old_image = it.image
        })
        //edit product
        editProductBtn.setOnClickListener {
            editProduct(id)
        }
    }
    private fun editProduct(id:Int){
        val titleProduct = findViewById<EditText>(R.id.edit_title_product_input)
        val descriptionProduct = findViewById<EditText>(R.id.edit_description_product_input)
        val priceProduct = findViewById<EditText>(R.id.edit_price_product_input)
        val category = findViewById<AutoCompleteTextView>(R.id.edit_choose_category)


        if (titleProduct.text.isBlank()){
            titleProduct.error = getString(R.string.title_product_empty)
            titleProduct.requestFocus()
        } else if (descriptionProduct.text.isBlank()){
            descriptionProduct.error = getString(R.string.description_product_empty)
            descriptionProduct.requestFocus()
        } else if (priceProduct.text.isEmpty()){
            priceProduct.error = getString(R.string.price_empty_product)
            priceProduct.requestFocus()
        } else if(category.text.toString() == getString(R.string.choose_category)){
            category.error = getString(R.string.choose_category)
            category.requestFocus()
        }else{
            if(image_from_galery == null){
                image_from_galery =old_image
            }
            categoryViewModel.getIdOfCategory(category.text.toString()).observe(this,
                androidx.lifecycle.Observer {
                    val date = Date()
                    val product = Product(id,titleProduct.text.toString(),descriptionProduct.text.toString(),priceProduct.text.toString().toDouble(),
                        image_from_galery!!,date,auth.currentUser!!.email!!,
                        it
                    )
                    productViewModel.updateProduct(product)
                    //Redirect to details page
                    Toast.makeText(this,getString(R.string.product_updated),Toast.LENGTH_LONG).show()
                    finish()
                })

        }
        }

}
