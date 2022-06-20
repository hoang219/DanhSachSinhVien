package com.hoangnn.danhsachsinhvien.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hoangnn.danhsachsinhvien.controller.IClickItemStudent
import com.hoangnn.danhsachsinhvien.controller.RecyclerAdapter
import com.hoangnn.danhsachsinhvien.controller.StudentCtr
import com.hoangnn.danhsachsinhvien.databinding.ActivityMainBinding
import com.hoangnn.danhsachsinhvien.model.Student

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null
    private val studentCtr = StudentCtr(this)
    private var listStudent = ArrayList<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        setItemSpiner()
        binding.cbUniversity.isChecked = true
        binding.cbCollege.isChecked = true

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            intent.putExtra("action", "add")
            startActivity(intent)
        }

        binding.btnSearch.setOnClickListener {
            val data = binding.edtSearch.text.toString()
            var specialized = ""

            if (binding.cbUniversity.isChecked && !binding.cbCollege.isChecked) specialized = "Đại học"
            else if (!binding.cbUniversity.isChecked && binding.cbCollege.isChecked) specialized = "Cao đẳng"
            else if (!binding.cbUniversity.isChecked && !binding.cbCollege.isChecked) specialized = "Không tồn tại"

            listStudent = studentCtr.searchStudent(data, specialized)
            binding.spnSort.setSelection(0)
            studentCtr.sortStudent(listStudent, 0)
            showListStudent()
        }
    }

    private fun setItemSpiner(){
        val listSort = arrayOf("Tên tăng dần", "Tên giảm dần", "SDT tăng dần", "SDT giảm dần")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listSort)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnSort.adapter = arrayAdapter
        binding.spnSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                listStudent = studentCtr.sortStudent(listStudent, p2)
                showListStudent()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun showListStudent(){
        layoutManager = LinearLayoutManager(this)
        binding.rcvStudent.layoutManager = layoutManager
        val listener = object: IClickItemStudent{
            override fun onClickEdit(student: Student) {
                val intent = Intent(this@MainActivity, AddStudentActivity::class.java)
                intent.putExtra("action", "edit")
                intent.putExtra("student", student)
                startActivity(intent)
            }

            override fun onClickDelete(student: Student) {
                showDialog(student)
            }
        }
        adapter = RecyclerAdapter(listStudent, listener)
        binding.rcvStudent.adapter = adapter
    }

    private fun showDialog(student: Student){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Bạn có chắc chắn muốn xóa")
        alertDialog.setMessage("Sinh viên: ${student.name}")

        alertDialog.setPositiveButton("No") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
        }

        alertDialog.setNegativeButton("Yes") { _: DialogInterface, _: Int ->
            val result = studentCtr.deleteStudent(student)
            if (result == 1) {
                showListStudent()
                Toast.makeText(this, "Đã xóa thông tin sinh viên", Toast.LENGTH_LONG).show()
            } else Toast.makeText(this, "Xóa thông tin sinh viên thất bại", Toast.LENGTH_LONG).show()
        }

        alertDialog.show()
    }
}