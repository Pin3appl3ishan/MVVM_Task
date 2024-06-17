package com.example.mvvm_task.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mvvm_task.R
import com.example.mvvm_task.data.model.StudentModel
import com.example.mvvm_task.data.repository.StudentRepositoryImpl
import com.example.mvvm_task.databinding.ActivityUpdateStudentBinding
import com.example.mvvm_task.ui.viewmodel.StudentViewModel
import com.example.mvvm_task.utils.ImageUtils
import com.example.mvvm_task.utils.LoadingUtils
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class UpdateStudentActivity : AppCompatActivity() {
    lateinit var updateStudentBinding: ActivityUpdateStudentBinding
    lateinit var loadigUtils: LoadingUtils
    var id = ""
    var imageName = ""
    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref = firebaseDatabase.reference.child("students")

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null
    lateinit var studentViewModel: StudentViewModel

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }

    lateinit var imageUtils: ImageUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_student)

        loadigUtils = LoadingUtils(this)

        var repo = StudentRepositoryImpl()
        studentViewModel = StudentViewModel(repo)
        imageUtils = ImageUtils(this)
        imageUtils.registerActivity {
            imageUri = it
            Picasso.get().load(it).into(updateStudentBinding.updateImageBrowse)
        }

        var students: StudentModel? = intent.getParcelableExtra("product")
        id = students?.id.toString()
        imageName = students?.imageName.toString()
        updateStudentBinding.updateName.setText(students?.name)
        updateStudentBinding.updateDesc.setText(students?.description)
        updateStudentBinding.updatePrice.setText(students?.price.toString())

        Picasso.get().load(students?.url).into(updateStudentBinding.updateImageBrowse)

        updateStudentBinding.btnPostUpdate.setOnClickListener {
            if(imageUri == null){
                updateProduct(students?.url.toString())
            }else{
                uploadImage()
            }
        }
        updateStudentBinding.updateImageBrowse.setOnClickListener {
            imageUtils.launchGallery(this@UpdateStudentActivity)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun uploadImage() {
        loadigUtils.showLoading()
        imageUri?.let {
            studentViewModel.uploadImage(imageName, it) { success, imageUrl ->
                if (success) {
                    updateProduct(imageUrl.toString())
                }
            }

        }
    }


    fun updateProduct(url: String) {
        loadigUtils.showLoading()
        var name: String = updateStudentBinding.updateName.text.toString()
        var price: Int = updateStudentBinding.updatePrice.text.toString().toInt()
        var description: String = updateStudentBinding.updateDesc.text.toString()

        var data = mutableMapOf<String, Any>()
        data["name"] = name
        data["price"] = price
        data["description"] = description
        data["url"] = url

        studentViewModel.updateProduct(id, data) { success, message ->
            if (success) {
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                finish()
                loadigUtils.dismiss()
            } else {
                loadigUtils.dismiss()
                Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()

            }
        }
    }
}