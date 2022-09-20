package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.io.ByteArrayOutputStream
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
    private lateinit var viewModel:MyViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.photo_image)
        nameView = findViewById(R.id.name_input)
        mailView = findViewById(R.id.mail_input)
        phoneView = findViewById(R.id.phone_input)
        femaleView = findViewById(R.id.female_radio)
        maleView = findViewById(R.id.male_radio)
        classView = findViewById(R.id.class_input)
        majorView = findViewById(R.id.major_input)

        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        loadProfile()

        Util.checkPermissions(this)

        tempImgFileName= "xd_img.jpg"
        val tempImgFile = File(getExternalFilesDir(null), tempImgFileName)
        tempImgUri = FileProvider.getUriForFile(this, "com.example.myapplication", tempImgFile)
        cameraResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = Util.getBitmap(this, tempImgUri)
                viewModel.userImage.value = bitmap
            }
        }
        viewModel.userImage.observe(this, Observer { it ->
            imageView.setImageBitmap(it)
        })
    }


    private  fun loadProfile(){
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        nameView.setText(sharedPref.getString(NAME, null))
        mailView.setText(sharedPref.getString(MAIL, null))
        phoneView.setText(sharedPref.getString(PHONE, null))
        classView.setText(sharedPref.getString(CLASS, null))
        majorView.setText(sharedPref.getString(MAJOR, null))
        femaleView.isChecked = sharedPref.getBoolean(FEMALE, false)
        maleView.isChecked = sharedPref.getBoolean(MALE, false)
        val imgData: String? = sharedPref.getString(IMG_DATA, null)
        if(imgData !== null){
            val b: ByteArray = Base64.decode(imgData, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
            viewModel.userImage.value = bitmap
        }
    }


   private fun saveProfile(){
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        sharedPref.edit().putString(NAME, nameView.text.toString()).apply()
        sharedPref.edit().putString(MAIL, mailView.text.toString()).apply()
        sharedPref.edit().putString(PHONE, phoneView.text.toString()).apply()
        sharedPref.edit().putString(CLASS, classView.text.toString()).apply()
        sharedPref.edit().putString(MAJOR, majorView.text.toString()).apply()
        sharedPref.edit().putBoolean(MALE, maleView.isChecked).apply()
        sharedPref.edit().putBoolean(FEMALE, femaleView.isChecked).apply()

       val bas:ByteArrayOutputStream = ByteArrayOutputStream()
       viewModel.userImage.value?.compress(Bitmap.CompressFormat.JPEG, 100, bas)
       val b: ByteArray = bas.toByteArray()
       val encodedImg: String = Base64.encodeToString(b, Base64.DEFAULT)
       sharedPref.edit().putString(IMG_DATA, encodedImg).apply()
    }


    fun onSave(view: View){
        saveProfile()
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
        const val NAME = "name"
        const val MAIL = "mail"
        const val PHONE = "phone"
        const val CLASS = "class"
        const val MAJOR = "major"
        const val MALE = "male"
        const val FEMALE = "female"
        const val IMG_DATA = "img_data"
    }
}