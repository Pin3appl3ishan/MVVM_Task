package com.example.mvvm_task.ui.activity

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mvvm_task.R
import com.example.mvvm_task.data.model.StudentModel
import com.example.mvvm_task.data.repository.StudentRepositoryImpl
import com.example.mvvm_task.databinding.ActivityAddStudentBinding
import com.example.mvvm_task.ui.viewmodel.StudentViewModel
import com.example.mvvm_task.utils.ImageUtils
import com.example.mvvm_task.utils.LoadingUtils
import com.squareup.picasso.Picasso
import java.util.UUID

class AddStudentActivity : AppCompatActivity() {
    lateinit var addProductBinding: ActivityAddStudentBinding

    var imageUri : Uri? = null

    lateinit var imageUtils: ImageUtils
    lateinit var productViewModel: StudentViewModel

    lateinit var loadingUtils: LoadingUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_student)

        loadingUtils = LoadingUtils(this)

        imageUtils = ImageUtils(this)
        imageUtils.registerActivity{url ->
            url.let{
                imageUri = it
                Picasso.get().load(it).into(addProductBinding.imageBrowse)
            }
        }

        // there is repository in parameter of ProductViewModel
        var repo = StudentRepositoryImpl()
        productViewModel = StudentViewModel(repo)

        addProductBinding.imageBrowse.setOnClickListener{
            imageUtils.launchGallery(this@AddStudentActivity)
        }

        addProductBinding.btnPost.setOnClickListener {
            uploadImage()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun uploadImage() {
        loadingUtils.showLoading()
        val imageName = UUID.randomUUID().toString()
        imageUri?.let {
            productViewModel.uploadImage(imageName,it) {
                    success, imageUrl ->
                if(success) {
                    addStudent(imageUrl.toString(), imageName.toString())
                }
            }
        }
    }

    fun addStudent(url:String, imageName: String) {
        var name: String =addProductBinding.editTextProductName.text.toString()
        var price: Int =addProductBinding.editTextProductPrice.text.toString().toInt()
        var desc: String =addProductBinding.editTextProductDesc.text.toString()

        var data = StudentModel("",name,price,desc,url,imageName)
        productViewModel.addProduct(data) {
                success,message->
            if (success) {
                loadingUtils.dismiss()
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                finish()
            } else {
                loadingUtils.dismiss()
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}