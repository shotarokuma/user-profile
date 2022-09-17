package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var nameView:EditText
    private lateinit var mailView:EditText
    private lateinit var phoneView:EditText
    private  lateinit var femaleView:RadioButton
    private lateinit var maleView:RadioButton
    private lateinit var classView:EditText
    private lateinit var majorView:EditText
    private lateinit var tempImgFileName: String
    private lateinit var tempImgUri: Uri
    private lateinit var cameraResult: ActivityResultLauncher<Intent>
    private lateinit var viewModel:MyViewModel;
    private lateinit var line: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        imageView = findViewById(R.id.photo_image)
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

        Util.checkPermissions(this);

        tempImgFileName= "xd_img.jpg"
        val tempImgFile = File(getExternalFilesDir(null), tempImgFileName)
        tempImgUri = FileProvider.getUriForFile(this, "com.example.myapplication", tempImgFile)
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        cameraResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = Util.getBitmap(this, tempImgUri)
                viewModel.userImage.value = bitmap;

                line = tempImgUri.path.toString();

            }
        }
        viewModel.userImage.observe(this, Observer { it ->
            imageView.setImageBitmap(it)
        })

        if(savedInstanceState != null){
            line = savedInstanceState.getString(KEY).toString();
        }
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

    fun onTakePhoto(view:View){
        val intent:Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempImgUri)
        cameraResult.launch(intent)
    }

    fun onCancel(view: View){
        finish()
    }

    companion object{
        val KEY = "my_mey"
    }
}