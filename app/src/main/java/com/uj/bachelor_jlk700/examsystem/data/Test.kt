package com.uj.bachelor_jlk700.examsystem.data

data class Test (val id: Int,
                 val timeInMinutes: Long,
                 val questions: List<Question>)

// val topic
// val uid