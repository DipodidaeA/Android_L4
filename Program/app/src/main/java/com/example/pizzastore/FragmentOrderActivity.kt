package com.example.pizzastore

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment

class FragmentOrderActivity : Fragment(R.layout.order_fragment){

    private lateinit var allParam: AllCheckBoxes

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.order_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mozzarella = view.findViewById<CheckBox>(R.id.checkBox_Pizza_Type_Mozzarella)
        val hawaiian = view.findViewById<CheckBox>(R.id.checkBox_Pizza_Type_Hawaiian)
        val pepperoni = view.findViewById<CheckBox>(R.id.checkBox_Pizza_Type_Pepperoni)

        val size25 = view.findViewById<CheckBox>(R.id.checkBox_Pizza_Size_25)
        val size35 = view.findViewById<CheckBox>(R.id.checkBox_Pizza_Size_35)
        val size45 = view.findViewById<CheckBox>(R.id.checkBox_Pizza_Size_45)

        val count1 = view.findViewById<CheckBox>(R.id.checkBox_Pizza_Count_1)
        val count2 = view.findViewById<CheckBox>(R.id.checkBox_Pizza_Count_2)
        val count3 = view.findViewById<CheckBox>(R.id.checkBox_Pizza_Count_3)

        val buttonConfirm = view.findViewById<Button>(R.id.button_Confirm)
        val buttonShow = view.findViewById<Button>(R.id.button_Show_Orders)

        val pizzaTypes = listOf(mozzarella, hawaiian, pepperoni)
        val pizzaSizes = listOf(size25, size35, size45)
        val pizzaCounts = listOf(count1, count2, count3)

        allParam = AllCheckBoxes(pizzaTypes, pizzaSizes , pizzaCounts)

        clickCheckBox(pizzaTypes)
        clickCheckBox(pizzaSizes)
        clickCheckBox(pizzaCounts)

        clickButton(buttonConfirm, allParam)
        clickButtonShow(buttonShow);

    }

    // якщо натиснути на один з чекбоксів, помітки на інших чекбоксах знімаються
    private fun clickCheckBox(checkBoxes: List<CheckBox>){
        for (checkBox in checkBoxes) {
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                checkBoxes.forEach { it.isChecked = false }

                if (isChecked) {
                    buttonView.isChecked = true
                }
            }
        }
    }

    private fun clickButton(button: Button, allCheckBoxes: AllCheckBoxes){
        button.setOnClickListener {
            // шукаємо вибрані дані
            val selectPizzaType = allCheckBoxes.pizzaType.find { checkBox -> checkBox.isChecked }
            val selectPizzaSize = allCheckBoxes.pizzaSize.find { checkBox -> checkBox.isChecked }
            val selectPizzaCount = allCheckBoxes.pizzaCount.find { checkBox -> checkBox.isChecked }

            // якщо не всі параметри вибрані виводимо повідомлення попередження
            if (selectPizzaType == null || selectPizzaSize == null || selectPizzaCount == null){
                (activity as? MainActivity)?.showMessage(listOf("Not all parameters checked"), "alert")
            } else {
                val orderDetails = listOf(selectPizzaType.text.toString(), selectPizzaSize.text.toString(), selectPizzaCount.text.toString())

                (activity as? MainActivity)?.showMessage(orderDetails, "success")
            }
        }
    }

    private fun clickButtonShow(button: Button){
        button.setOnClickListener{
            val intent = Intent(requireContext(), ShowOrdersActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

            startActivity(intent)
        }
    }

    fun clearParameter(){
        allParam.pizzaType.forEach { it.isChecked = false }
        allParam.pizzaSize.forEach { it.isChecked = false }
        allParam.pizzaCount.forEach { it.isChecked = false }
    }

    private data class AllCheckBoxes(
        val pizzaType: List<CheckBox>,
        val pizzaSize: List<CheckBox>,
        val pizzaCount: List<CheckBox>
    )

}
