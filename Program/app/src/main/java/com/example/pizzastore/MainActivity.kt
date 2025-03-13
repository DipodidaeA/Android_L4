package com.example.pizzastore

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentOrder = FragmentOrderActivity()
        val fragmentMessage = FragmentActivity()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_order_create, fragmentOrder)
            .replace(R.id.fragment_message_view, fragmentMessage)
            .addToBackStack(null)
            .commit()

    }

    fun showMessage(data: List<String>, type: String) {
        val fragmentMessage = FragmentActivity.newInstance(data, type)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_message_view, fragmentMessage)
            .commit()

        findViewById<View>(R.id.fragment_message_view).visibility = View.VISIBLE
    }

    fun clearPizzaParameter() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_order_create) as? FragmentOrderActivity
        fragment?.clearParameter()
    }

    fun saveDataInFile(view: View, data: List<String>){
        try {
            val filename = "Orders"
            val file = File(this.filesDir, filename)

            val currentTime = Calendar.getInstance().time
            val nowData = SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault()).format(currentTime)

            file.appendText("Time: $nowData\nType: ${data[0]}\nSize: ${data[1]}\nCount: ${data[2]}\n\n")

            Toast.makeText(this, "Data wrote", Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.CENTER, 0, 0)
            }.show()
        }catch(ex: IOException) {
            Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
        }
    }
}