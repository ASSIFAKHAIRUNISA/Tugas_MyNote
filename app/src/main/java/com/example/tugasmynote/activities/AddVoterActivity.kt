package com.example.tugasmynote.activities

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

class AddVoterActivity : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etNIK: EditText
    private lateinit var rgGender: RadioGroup
    private lateinit var etAlamat: EditText
    private lateinit var btnSimpan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_voter)

        // Inisialisasi UI
        etNama = findViewById(R.id.etNama)
        etNIK = findViewById(R.id.etNIK)
        rgGender = findViewById(R.id.rgGender)
        etAlamat = findViewById(R.id.etAlamat)
        btnSimpan = findViewById(R.id.btnSimpan)

        // Setup Database
        val database = AppDatabase.getDatabase(this)
        val voterDao = database.voterDao()

        // Tombol simpan untuk tambah data
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

            // Menambahkan data baru tanpa menyetel ID manual
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val newVoter = VoterEntity(
                        id = 0, // Room akan otomatis meng-generate ID
                        name = name,
                        nik = nik,
                        gender = gender,
                        address = address
                    )
                    voterDao.insert(newVoter)
                    runOnUiThread {
                        Toast.makeText(this@AddVoterActivity, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                        // Kembali ke halaman utama setelah data berhasil disimpan
                        setResult(RESULT_OK) // Memberitahukan bahwa data berhasil ditambahkan
                        finish()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@AddVoterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
