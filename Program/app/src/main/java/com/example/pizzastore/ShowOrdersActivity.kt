package com.example.pizzastore

import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.charset.Charset

class ShowOrdersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_orders)

        val buttonClose = findViewById<Button>(R.id.button_Show_End)
        val buttonUpdate = findViewById<Button>(R.id.button_Update)
        val editText= findViewById<EditText>(R.id.editText_Orders)

        val filename = "Orders"
        val file = File(this.filesDir, filename)

        readFromFile(file, editText)

        buttonClose.setOnClickListener {
            finish()
        }
        buttonUpdate.setOnClickListener{
            updateFile(file, editText)
        }

    }

    private fun readFromFile(file: File, editText: EditText) {
        try {
            val content = file.readText(Charset.defaultCharset()).trim()
            if (content.isEmpty()) {
                Toast.makeText(this, "File is empty", Toast.LENGTH_SHORT).apply {
                    setGravity(Gravity.CENTER, 0, 0)
                }.show()
            }
            editText.setText(content)
        } catch(ex: IOException) {
            Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFile(file: File, editText: EditText){
        try {
            val newText = editText.text.toString()
            file.writeText(newText, Charset.defaultCharset())
            Toast.makeText(this, "File updated", Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.CENTER, 0, 0)
            }.show()
        } catch (ex: IOException) {
            Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
        }
    }
}