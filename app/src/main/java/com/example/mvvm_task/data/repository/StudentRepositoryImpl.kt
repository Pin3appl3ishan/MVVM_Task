package com.example.mvvm_task.data.repository

import android.net.Uri
import android.util.Log
import com.example.mvvm_task.data.model.StudentModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class StudentRepositoryImpl : StudentRepository {

    var firebaseDatabase = FirebaseDatabase.getInstance()
    var ref = firebaseDatabase.reference.child("students")

    var firebaseStorage = FirebaseStorage.getInstance()
    var storageRef : StorageReference = firebaseStorage.reference.child("students")

    override fun uploadImage(imageName: String, imageUrl: Uri, callback: (Boolean, String?) -> Unit){
        var imageReference = storageRef.child(imageName)

        imageUrl?.let { url->
            imageReference.putFile(url).addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener {downloadUrl->
                    var imagesUrl = downloadUrl.toString()
                    callback(true, imagesUrl)
                }
            }.addOnFailureListener {
                callback(false, "")
            }
        }
    }

    override fun addStudent(productModel: StudentModel, callback: (Boolean, String?,) -> Unit){
        var id = ref.push().key.toString()

        productModel.id = id

        ref.child(id).setValue(productModel).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Data uploaded successfully")
            } else {
                callback(false, "Unable to upload data")
            }
        }
    }

    override fun getAllStudent(callback: (List<StudentModel>?, Boolean, String?) -> Unit){
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var productList = mutableListOf<StudentModel>()

                for(eachData in snapshot.children){
                    var student = eachData.getValue(StudentModel::class.java)
                    if(student!=null){
                        Log.d("data from firebase",student.name)
                        Log.d("data from firebase",student.description)
                        Log.d("data from firebase",student.price.toString())

                        productList.add(student)
                    }
                }
                callback(productList,true,"DATA FETCHED")
            }
            override fun onCancelled(error: DatabaseError) {
                callback(null, false, "Unable to fetch ${error.message}")
            }
        })
    }

    override fun updateStudent(id:String, data: MutableMap<String, Any>?, callback: (Boolean, String?) -> Unit){
        data?.let {
            ref.child(id).updateChildren(it).addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true,"Your data has been update")

                } else {
                    callback(false,"Unable to update data")

                }
            }
        }
    }

    override fun deleteData(id:String, callback: (Boolean, String?) -> Unit){
        ref.child(id).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                callback(true,"Data has been deleted")
            }else{
                callback(false,"Unable to delete data")
            }
        }
    }

    override fun deleteImage(imageName:String, callback: (Boolean, String?) -> Unit){
        storageRef.child("products").child(imageName).delete().addOnCompleteListener{
            if (it.isSuccessful){
                callback(true,"Image deleted")
            }else{
                callback(false,"Unable to delete image")
            }
        }
    }

}