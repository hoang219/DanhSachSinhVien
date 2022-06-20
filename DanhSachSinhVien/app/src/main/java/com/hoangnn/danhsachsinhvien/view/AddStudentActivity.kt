package com.hoangnn.danhsachsinhvien.view

import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hoangnn.danhsachsinhvien.controller.DateInputMask
import com.hoangnn.danhsachsinhvien.controller.StudentCtr
import com.hoangnn.danhsachsinhvien.databinding.ActivityAddStudentBinding
import com.hoangnn.danhsachsinhvien.model.Student
import java.text.SimpleDateFormat

class AddStudentActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAddStudentBinding
    private val format = SimpleDateFormat("dd/MM/yyyy")
    private lateinit var action: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        listener()
        DateInputMask(binding.edtBirthday).listen()

        binding.btnAdd.setOnClickListener {
            if (action == "add") addStudent()
            else if (action == "edit") updateStudent()
        }
    }

    private fun listener(){
        action = intent.getStringExtra("action").toString()
        if (action == "add"){
            binding.btnAdd.text = "Add"
        }else if (action == "edit"){
            binding.btnAdd.text = "Update"
            val student = intent.getSerializableExtra("student") as Student
            binding.edtName.setText(student.name)
            binding.edtBirthday.setText(format.format(student.birthday))
            binding.edtPhoneNumber.setText(student.phonenumber)
            if(student.specialized == "Đại học")
                binding.btnrUniversity.isChecked = true
            else if (student.specialized == "Cao đẳng")
                binding.btnrCollege.isChecked = true
        }
    }

    private fun addStudent(){
        val id = 0
        val name = binding.edtName.text.toString()
        val birthday = format.parse(binding.edtBirthday.text.toString())
        val phonenumber = binding.edtPhoneNumber.text.toString()
        val specialized = if(binding.btnrUniversity.isChecked){
            "Đại học"
        }else if (binding.btnrCollege.isChecked){
            "Cao đẳng"
        }else{
            "Không xác định"
        }
        val student = Student(0, name, birthday, phonenumber, specialized)
        val studentCtr = StudentCtr(this)
        val result = studentCtr.addStudent(student)
        when (result) {
            0 -> {
                Toast.makeText(this, "Thêm sinh viên thành công", Toast.LENGTH_LONG).show()
            }
            1 -> {
                Toast.makeText(this, "Số điện thoại đã tồn tại vui lòng thay một số khác", Toast.LENGTH_LONG).show()
            }
            else -> Toast.makeText(this, "Thêm sinh viên thất bại", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateStudent(){
        val student = intent.getSerializableExtra("student") as Student
        val name = binding.edtName.text.toString()
        val birthday = format.parse(binding.edtBirthday.text.toString())
        val phonenumber = binding.edtPhoneNumber.text.toString()

        val specialized = if(binding.btnrUniversity.isChecked){
            "Đại học"
        }else if (binding.btnrCollege.isChecked){
            "Cao đẳng"
        }else{
            "Không xác định"
        }

        student.name = name
        student.birthday = birthday
        student.phonenumber = phonenumber
        student.specialized = specialized

        val studentCtr = StudentCtr(this)
        val result = studentCtr.updateStudent(student)
        if (result == 0){
            Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_LONG).show()
        }else if (result == 1){
            Toast.makeText(this, "Số điện thoại đã tồn tại vui lòng thay một số khác", Toast.LENGTH_LONG).show()
        }else Toast.makeText(this, "Cập nhật thông tin thất bại", Toast.LENGTH_LONG).show()
    }
}