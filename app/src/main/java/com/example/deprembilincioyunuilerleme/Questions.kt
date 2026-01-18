package com.example.deprembilincioyunuilerleme

// Firebase'in verileri eşleştirebilmesi için parametrelerin varsayılan değerleri (="") olmalı
data class Question(
    val text: String = "",
    val a: String = "",
    val b: String = "",
    val c: String = "",
    val d: String = "",
    val correct: String = ""
)