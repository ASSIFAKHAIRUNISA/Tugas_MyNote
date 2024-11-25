package com.example.tugasmynote

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tugasmynote.R
import com.example.tugasmynote.data.AppDatabase
import com.example.tugasmynote.data.VoterEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditVoterActivity : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etNIK: EditText
    private lateinit var rgGender: RadioGroup
    private lateinit var etAlamat: EditText
    private lateinit var btnSimpan: Button
    private var voterId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_voter)

        // Ambil voter_id dari Intent
        voterId = intent.getLongExtra("voter_id", -1L)
        if (voterId == -1L) {
            Toast.makeText(this, "Invalid Voter ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Inisialisasi UI
        etNama = findViewById(R.id.etNama)
        etNIK = findViewById(R.id.etNIK)
        rgGender = findViewById(R.id.rgGender)
        etAlamat = findViewById(R.id.etAlamat)
        btnSimpan = findViewById(R.id.btnSimpan)

        // Setup Database
        val database = AppDatabase.getDatabase(this)
        val voterDao = database.voterDao()

        // Load data voter berdasarkan ID
        lifecycleScope.launch {
            val voter = voterDao.getVoterById(voterId)
            runOnUiThread {
                voter?.let {
                    etNama.setText(it.name)
                    etNIK.setText(it.nik)
                    etAlamat.setText(it.address)
                    if (it.gender == "Laki Laki") {
                        rgGender.check(R.id.rbLakiLaki)
                    } else {
                        rgGender.check(R.id.rbPerempuan)
                    }
                }
            }
        }

        // Tombol simpan untuk edit data
        btnSimpan.setOnClickListener {
            val name = etNama.text.toString()
            val nik = etNIK.text.toString()
            val gender = if (rgGender.checkedRadioButtonId == R.id.rbLakiLaki) "Laki Laki" else "Perempuan"
            val address = etAlamat.text.toString()

            // Validasi inputan
            if (name.isEmpty() || nik.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val updatedVoter = VoterEntity(
                        id = voterId, // Gunakan ID yang sudah ada untuk update
                        name = name,
                        nik = nik,
                        gender = gender,
                        address = address
                    )
                    voterDao.update(updatedVoter)
                    runOnUiThread {
                        Toast.makeText(this@EditVoterActivity, "Data berhasil diubah", Toast.LENGTH_SHORT).show()
                        finish() // Kembali ke halaman utama
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@EditVoterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
