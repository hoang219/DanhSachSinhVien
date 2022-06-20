package com.hoangnn.danhsachsinhvien.model

import java.io.Serializable
import java.util.*

data class Student (val id: Int, var name: String, var birthday: Date, var phonenumber: String, var specialized: String): Serializable