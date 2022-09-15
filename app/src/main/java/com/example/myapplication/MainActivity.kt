package com.example.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var nameView:EditText
    private lateinit var mailView:EditText
    private lateinit var phoneView:EditText
    private  lateinit var femaleView:RadioButton
    private lateinit var maleView:RadioButton
    private lateinit var classView:EditText
    private lateinit var majorView:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        nameView = findViewById(R.id.name_input)
        mailView = findViewById(R.id.mail_input)
        phoneView = findViewById(R.id.phone_input)
        femaleView = findViewById(R.id.female_radio)
        maleView = findViewById(R.id.male_radio)
        classView = findViewById(R.id.class_input)
        majorView = findViewById(R.id.major_input)
        nameView.setText(sharedPref.getString("name", null))
        mailView.setText(sharedPref.getString("mail", null))
        phoneView.setText(sharedPref.getString("phone", null))
        classView.setText(sharedPref.getString("class", null))
        majorView.setText(sharedPref.getString("major", null))
        femaleView.isChecked = sharedPref.getBoolean("female", false)
        maleView.isChecked = sharedPref.getBoolean("male", false)
    }

    fun onSave(view: View){
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        sharedPref.edit().putString("name", nameView.text.toString()).apply()
        sharedPref.edit().putString("mail", mailView.text.toString()).apply()
        sharedPref.edit().putString("phone", phoneView.text.toString()).apply()
        sharedPref.edit().putString("class", classView.text.toString()).apply()
        sharedPref.edit().putString("major", majorView.text.toString()).apply()
        sharedPref.edit().putBoolean("male", maleView.isChecked).apply()
        sharedPref.edit().putBoolean("female", femaleView.isChecked).apply()
        Toast.makeText(
            this,
            "saved",
            Toast.LENGTH_SHORT
        ).show()
        finish()
    }

    fun onCancel(view: View){
        finish()
    }
}