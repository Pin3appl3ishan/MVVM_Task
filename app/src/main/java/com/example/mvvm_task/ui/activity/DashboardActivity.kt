package com.example.mvvm_task.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm_task.R
import com.example.mvvm_task.data.repository.StudentRepositoryImpl
import com.example.mvvm_task.databinding.ActivityDashboardBinding
import com.example.mvvm_task.ui.adapter.StudentAdapter
import com.example.mvvm_task.ui.viewmodel.StudentViewModel

class DashboardActivity : AppCompatActivity() {
    lateinit var studentAdapter: StudentAdapter

    lateinit var studentViewModel : StudentViewModel

    lateinit var dashBoardBinding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        val repo = StudentRepositoryImpl()
        studentViewModel = StudentViewModel(repo)
        studentViewModel.fetchProduct(this@DashboardActivity)

        studentAdapter = StudentAdapter(this@DashboardActivity, ArrayList())

        dashBoardBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            adapter = studentAdapter
        }

        studentViewModel.loadingState.observe(this) { loading->
            if (loading) {
                dashBoardBinding.progressBar3.visibility = View.VISIBLE
            } else {
                dashBoardBinding.progressBar3.visibility = View.GONE
            }
        }

        studentViewModel.studentList.observe(this) { products ->
            products?.let {
                studentAdapter.updateData(it)
            }

        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var id = studentAdapter.getProductID(viewHolder.adapterPosition)
                var imageName = studentAdapter.getImageName(viewHolder.adapterPosition)

                studentViewModel.deleteData(id) {
                        success, message ->
                    if(success) {
                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                        studentViewModel.deleteImage(imageName) {
                                success, message ->
                        }
                    } else{
                        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                    }

                }
            }
        }).attachToRecyclerView(dashBoardBinding.recyclerView)

        dashBoardBinding.floatingActionButton.setOnClickListener {
            var intent = Intent(this@DashboardActivity, AddStudentActivity::class.java)
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}