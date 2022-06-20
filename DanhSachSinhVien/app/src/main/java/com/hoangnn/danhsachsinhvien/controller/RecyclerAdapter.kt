package com.hoangnn.danhsachsinhvien.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hoangnn.danhsachsinhvien.R
import com.hoangnn.danhsachsinhvien.model.Student
import java.text.SimpleDateFormat

class RecyclerAdapter(private val listStudent: ArrayList<Student>, private val listener: IClickItemStudent): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val format = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.studen_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val student = listStudent[position]
        holder.tv_name.text = student.name
        holder.tv_birthday.text = format.format(student.birthday)
        holder.tv_phonenumber.text = student.phonenumber
        holder.tv_specialized.text = student.specialized
        holder.btn_edit.setOnClickListener { listener.onClickEdit(student) }
        holder.btn_delete.setOnClickListener { listener.onClickDelete(student) }
    }

    override fun getItemCount(): Int {
        return listStudent.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tv_name: TextView
        var tv_birthday: TextView
        var tv_phonenumber: TextView
        var tv_specialized: TextView
        var btn_edit: Button
        var btn_delete: Button
        init {
            tv_name = itemView.findViewById(R.id.tv_name)
            tv_birthday = itemView.findViewById(R.id.tv_birthday)
            tv_phonenumber = itemView.findViewById(R.id.tv_phonenumber)
            tv_specialized = itemView.findViewById(R.id.tv_specialized)
            btn_edit = itemView.findViewById(R.id.btn_edit)
            btn_delete = itemView.findViewById(R.id.btn_delete)
        }
    }
}