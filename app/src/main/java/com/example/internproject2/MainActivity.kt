@file:Suppress("DEPRECATION")

package com.example.internproject2

import androidx.appcompat.app.AppCompatActivity
import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
//import android.view.View
import com.example.internproject2.databinding.ActivityMainBinding
import java.io.IOException
import java.io.InputStream
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainHandler = Handler()
    private lateinit var progressDialog: ProgressDialog
    private fun isValidUrl(url: String): Boolean {
        return try {
            URL(url).toURI()
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.clearbtn.setOnClickListener {
            binding.etURL.text.clear()
            binding.imageView.setImageBitmap(null)
        }

        binding.entrbtn.setOnClickListener {
            val url = binding.etURL.text.toString()

            if (url.isBlank() || !isValidUrl(url)) {
                // Проверка на пустой URL или неверный URL
                Toast.makeText(this@MainActivity, "Enter correct URL", Toast.LENGTH_SHORT).show()
            } else {
                FetchImage(url).start()
            }
        }
    }

    inner class FetchImage(private val URL: String) : Thread() {
        private var bitmap: Bitmap? = null

        override fun run() {
            mainHandler.post {
                progressDialog = ProgressDialog(this@MainActivity)
                progressDialog.setMessage("Getting your pic....")
                progressDialog.setCancelable(false)
                progressDialog.show()
            }

            val inputStream: InputStream?
            try {
                inputStream = URL(URL).openStream()
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            mainHandler.post {
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                }
                binding.imageView.setImageBitmap(bitmap)
            }
        }
    }
}