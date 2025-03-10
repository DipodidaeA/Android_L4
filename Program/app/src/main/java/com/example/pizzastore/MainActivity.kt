package com.example.pizzastore

import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentOrder = FragmentOrderActivity()
        val fragmentMessage = FragmentActivity()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_order_create, fragmentOrder)
            .replace(R.id.fragment_message_view, fragmentMessage)
            .commit()
    }

    fun showMessage(message: String, type: String) {
        val fragmentMessage = FragmentActivity.newInstance(message, type)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_message_view, fragmentMessage)
            .commit()

        findViewById<View>(R.id.fragment_message_view).visibility = View.VISIBLE
    }

    fun clearPizzaParameter() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_order_create) as? FragmentOrderActivity
        fragment?.clearParameter()
    }
}