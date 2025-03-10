package com.example.pizzastore

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class FragmentActivity : Fragment(R.layout.message_fragment) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.message_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val messageText = view.findViewById<TextView>(R.id.messageText)
        val msgTextType = view.findViewById<TextView>(R.id.messageType)
        val closeButton = view.findViewById<Button>(R.id.closeButton)

        val message = arguments?.getString("message") ?: "Text Error"
        val type = arguments?.getString("type")

        messageText.text = message

        // змінюємо колій тексту
        if (type == "alert") {
            msgTextType.text = getString(R.string.order_error)
            messageText.setTextColor(Color.RED)
        } else {
            msgTextType.text = getString(R.string.order_success)
            messageText.setTextColor(Color.GREEN)
        }

        closeButton.setOnClickListener {
            if (type != "alert") {
                (activity as? MainActivity)?.clearPizzaParameter()
            }
            parentFragmentManager.beginTransaction().remove(this).commit()
            requireActivity().findViewById<View>(R.id.fragment_message_view).visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(message: String, type: String): FragmentActivity {
            val fragment = FragmentActivity()
            val args = Bundle()
            args.putString("message", message)
            args.putString("type", type)
            fragment.arguments = args
            return fragment
        }
    }
}