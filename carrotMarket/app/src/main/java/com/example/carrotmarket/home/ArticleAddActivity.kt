package com.example.carrotmarket.home

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.carrotmarket.ArticleModel
import com.example.carrotmarket.R
import com.example.carrotmarket.databinding.ActivityArticleAddBinding
import com.example.carrotmarket.utils.Companion.DB_ARTICLES
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ArticleAddActivity : AppCompatActivity() {

    private val binding by lazy{ ActivityArticleAddBinding.inflate(layoutInflater)}
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result: ActivityResult ->
            binding.photoImageView.setImageURI(result.data?.data)
        }
    private val auth: FirebaseAuth by lazy{ Firebase.auth }
    private val storage: FirebaseStorage by lazy{ Firebase.storage }
    private val articleDB: DatabaseReference by lazy{ Firebase.database.reference.child(DB_ARTICLES)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.imageAddButton.setOnClickListener {
            when{
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED ->{
                    startContentProvider()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)->{
                    showPermissionContextPopup()
                }
                else->{

                }
            }
        }
        binding.submitButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val price = binding.priceEditText.text.toString()
            val sellerId = auth.currentUser?.uid.orEmpty()

            val model = ArticleModel(sellerId, title, System.currentTimeMillis(), "$price 원", "")
            articleDB.push().setValue(model)

            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1010 ->
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startContentProvider()
                } else{
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun startContentProvider(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type ="image/*"
        getContent.launch(intent)
    }

    private fun showPermissionContextPopup(){
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 가져오기위해 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1010)
            }
            .create()
            .show()

    }
}