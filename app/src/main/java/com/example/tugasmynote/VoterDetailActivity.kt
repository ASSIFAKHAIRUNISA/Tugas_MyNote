package com.example.tugasmynote

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tugasmynote.R
import com.example.tugasmynote.data.AppDatabase
import kotlinx.coroutines.launch

class VoterDetailActivity : AppCompatActivity() {

    private lateinit var tvDetailName: TextView
    private lateinit var tvDetailNik: TextView
    private lateinit var tvDetailGender: TextView
    private lateinit var tvDetailAddress: TextView
    private lateinit var btnClose: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voter_detail)

        tvDetailName = findViewById(R.id.tvDetailName)
        tvDetailNik = findViewById(R.id.tvDetailNik)
        tvDetailGender = findViewById(R.id.tvDetailGender)
        tvDetailAddress = findViewById(R.id.tvDetailAddress)
        btnClose = findViewById(R.id.btnClose)

        val voterId = intent.getLongExtra("voter_id", -1)

        // Pastikan untuk menjalankan query di dalam coroutine
        val database = AppDatabase.getDatabase(this)
        val voterDao = database.voterDao()

        // Menjalankan query dengan coroutine
        lifecycleScope.launch {
            val voter = voterDao.getVoterById(voterId) // Memanggil suspend function
            if (voter != null) {
                tvDetailName.text = voter.name
                tvDetailNik.text = voter.nik
                tvDetailGender.text = voter.gender
                tvDetailAddress.text = voter.address
            } else {
                // Tampilkan pesan jika data tidak ditemukan
                tvDetailName.text = "Voter not found"
            }
        }

        // Tombol untuk menutup tampilan detail
        btnClose.setOnClickListener {
            finish()  // Kembali ke tampilan sebelumnya
        }
    }
}
