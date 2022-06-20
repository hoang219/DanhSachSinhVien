package com.hoangnn.danhsachsinhvien.controller

import com.hoangnn.danhsachsinhvien.model.Student

interface IClickItemStudent {
    fun onClickEdit(student: Student)
    fun onClickDelete(student: Student)
}