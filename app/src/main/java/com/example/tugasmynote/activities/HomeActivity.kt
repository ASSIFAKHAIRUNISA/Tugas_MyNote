package com.example.tugasmynote.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasmynote.EditVoterActivity
import com.example.tugasmynote.R
import com.example.tugasmynote.VoterAdapter
import com.example.tugasmynote.VoterDetailActivity
import com.example.tugasmynote.data.AppDatabase
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // RecyclerView untuk daftar voter
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Database dan DAO
        val database = AppDatabase.getDatabase(this)
        val voterDao = database.voterDao()

        // Observer untuk perubahan data voter
        val voterListLiveData = voterDao.getAllVoters() // Pastikan ini adalah LiveData
        voterListLiveData.observe(this, Observer { voters ->
            val adapter = VoterAdapter(this, voters, { voter ->
                // Intent untuk membuka EditVoterActivity
                val intent = Intent(this, EditVoterActivity::class.java)
                intent.putExtra("voter_id", voter.id)
                startActivity(intent)
            }, { voter ->
                // Hapus voter
                lifecycleScope.launch {
                    try {
                        voterDao.delete(voter)
                        Toast.makeText(this@HomeActivity, "Data voter berhasil dihapus", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@HomeActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }, { voter ->
                // Intent untuk membuka VoterDetailActivity
                val intent = Intent(this, VoterDetailActivity::class.java)
                intent.putExtra("voter_id", voter.id)
                startActivity(intent)
            })
            recyclerView.adapter = adapter
        })

        // Tombol tambah data
        findViewById<Button>(R.id.btnTambahData).setOnClickListener {
            val intent = Intent(this, AddVoterActivity::class.java)
            startActivityForResult(intent, 1) // Menunggu hasil dari AddVoterActivity
        }

        // Tombol logout
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    // Menangani hasil dari AddVoterActivity setelah data ditambahkan
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Data berhasil ditambahkan, tidak perlu melakukan apa-apa di sini karena LiveData sudah menangani update
        }
    }
}
