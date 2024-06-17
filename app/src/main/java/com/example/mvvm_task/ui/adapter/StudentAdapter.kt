package com.example.mvvm_task.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_task.R
import com.example.mvvm_task.data.model.StudentModel
import com.example.mvvm_task.ui.activity.UpdateStudentActivity

class StudentAdapter (var context: Context, var data : ArrayList<StudentModel>) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>(){
    class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var studentName : TextView = view.findViewById(R.id.lblName)
        var productPrice : TextView = view.findViewById(R.id.lblPrice)
        var studentDesc : TextView = view.findViewById(R.id.lblDescription)
        var btnEdit: TextView = view.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        var view = LayoutInflater.from(parent.context).
        inflate(R.layout.product_sample,
            parent,
            false)
        return StudentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.studentName.text = data[position].name
        holder.productPrice.text = data[position].price.toString()
        holder.studentDesc.text = data[position].description

        holder.btnEdit.setOnClickListener {
            var intent = Intent(context, UpdateStudentActivity::class.java)
            intent.putExtra("product",data[position])
            context.startActivity(intent)

        }
    }

    fun getProductID(position: Int) : String {
        return data[position].id
    }

    fun getImageName(position: Int) : String {
        return data[position].imageName
    }

    fun updateData(products: List<StudentModel>) {
        data.clear()
        data.addAll(products)
        notifyDataSetChanged()
    }

}