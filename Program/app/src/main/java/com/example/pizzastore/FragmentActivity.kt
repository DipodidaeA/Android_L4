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

        val data = arguments?.getStringArrayList("data") ?: listOf("Text Error", "", "")
        val type = arguments?.getString("type")?: "alert"

        val message: String

        // змінюємо колір тексту
        if (type == "alert") {
            message = "${data[0]}\n"

            messageText.text = message
            msgTextType.text = getString(R.string.order_error)

            messageText.setTextColor(Color.RED)
            msgTextType.setTextColor(Color.RED)
        } else {
            message = "Type: ${data[0]}\n" +
                    "Size: ${data[1]}\n" +
                    "Count: ${data[2]}"

            messageText.text = message
            msgTextType.text = getString(R.string.order_success)

            messageText.setTextColor(Color.GREEN)
            msgTextType.setTextColor(Color.GREEN)

            (activity as? MainActivity)?.saveDataInFile(view ,data)
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
        fun newInstance(message: List<String>, type: String): FragmentActivity {
            val fragment = FragmentActivity()
            val args = Bundle()
            args.putStringArrayList("data", ArrayList(message))
            args.putString("type", type)
            fragment.arguments = args
            return fragment
        }
    }
}