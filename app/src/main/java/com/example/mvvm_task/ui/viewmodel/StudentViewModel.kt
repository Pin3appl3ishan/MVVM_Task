package com.example.mvvm_task.ui.viewmodel


import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm_task.data.model.StudentModel
import com.example.mvvm_task.data.repository.StudentRepository

class StudentViewModel(val repository: StudentRepository) : ViewModel() {

    fun deleteData(id:String,callback: (Boolean, String?) -> Unit){
        repository.deleteData(id, callback)
    }

    fun deleteImage(imageName:String,callback: (Boolean, String?) -> Unit){
        repository.deleteImage(imageName,callback)
    }


    fun updateProduct(id:String,data:MutableMap<String,Any>?,callback: (Boolean, String?) -> Unit){
        repository.updateStudent(id, data,callback)
    }


    fun addProduct(studentModel: StudentModel, callback: (Boolean, String?) -> Unit) {
        repository.addStudent(studentModel, callback)
    }

    fun uploadImage(imageName:String, imageurl: Uri, callback: (Boolean, String?) -> Unit) {
        repository.uploadImage(imageName ,imageurl) { success, imageUrl ->
            callback(success, imageUrl)
        }
    }

    private var _studentList = MutableLiveData<List<StudentModel>?>()

    var studentList = MutableLiveData<List<StudentModel>?>()
        get() = _studentList

    var _loadingState = MutableLiveData<Boolean>()

    var loadingState = MutableLiveData<Boolean>()
        get() = _loadingState

    fun fetchProduct(context: Context) {
        _loadingState.value = true
        repository.getAllStudent() {products, success, message ->

            if (success && products != null) {
                _loadingState.value = false
                _studentList.value = products
            }else{
                message.let {
                    Toast.makeText(context,it.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
