package com.example.tugasmynote.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface VoterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(voter: VoterEntity)

    @Update
    suspend fun update(voter: VoterEntity) // Menambahkan update query

    @Query("SELECT * FROM voter_table")
    fun getAllVoters(): LiveData<List<VoterEntity>>

    @Query("SELECT * FROM voter_table WHERE id = :voterId")

    suspend fun getVoterById(voterId: kotlin.Long): VoterEntity?

    @Delete
    suspend fun delete(voter: VoterEntity)  // Fungsi untuk menghapus data
}

