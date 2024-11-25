package com.example.tugasmynote.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "voter_table")
data class VoterEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val nik: String,
    val gender: String,
    val address: String
)
