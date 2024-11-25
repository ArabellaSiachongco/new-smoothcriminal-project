package com.example.crudifyapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditProductActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var productId: Int? =
        null // ID of the product being edited, null if adding a new product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_product)

        // Initialize DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // Retrieve the product ID from the intent
        productId = intent.getIntExtra("PRODUCT_ID", -1).takeIf { it != -1 }

        // Initialize UI elements
        val productNameEditText = findViewById<EditText>(R.id.editProductName)
        val productQuantityEditText = findViewById<EditText>(R.id.editProductQuantity)
        val saveButton = findViewById<Button>(R.id.saveButton)

        // If editing, load the product details
        if (productId != null) {
            val product = dbHelper.getProductById(productId!!)
            if (product != null) {
                productNameEditText.setText(product.name)
                productQuantityEditText.setText(product.quantity.toString())
            } else {
                Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show()
                finish() // Close the activity if the product does not exist
            }
        }

        // Save changes or add a new product
        saveButton.setOnClickListener {
            val productName =
                productNameEditText.text.toString() // Get the product name as a String
            val productQuantity = productQuantityEditText.text.toString()
                .toIntOrNull() // Get the product quantity as an Int or null

            if (productName.isNotBlank() && productQuantity != null) {
                // Pass productName (String), productQuantity (Int), and productId (Int) to insertOrUpdateProduct
                val success = dbHelper.insertOrUpdateProduct(productName, productQuantity)

                if (success) {
                    Toast.makeText(this, "Product saved successfully", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity after saving
                } else {
                    Toast.makeText(this, "Failed to save product", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

