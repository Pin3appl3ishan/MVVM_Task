package com.example.mvvm_task.data.repository

import android.net.Uri
import com.example.mvvm_task.data.model.StudentModel

interface StudentRepository {
    fun uploadImage(imageName: String, imageUrl: Uri, callback: (Boolean, String?) -> Unit)

    fun addStudent(productModel: StudentModel, callback: (Boolean, String?,) -> Unit)

    fun getAllStudent(callback: (List<StudentModel>?, Boolean, String?) -> Unit)

    fun updateStudent(id:String, data: MutableMap<String, Any>?,callback: (Boolean, String?) -> Unit)

    fun deleteData(id:String, callback: (Boolean, String?) -> Unit)

    fun deleteImage(imageName:String, callback: (Boolean, String?) -> Unit)
}