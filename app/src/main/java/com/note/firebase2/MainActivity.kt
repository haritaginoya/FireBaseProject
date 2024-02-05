package com.note.firebase2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var storage: FirebaseStorage
    lateinit var pr: ProgressBar
    lateinit var imageView: ImageView
    lateinit var btn: Button
    var list = HashMap<String,MyData>()

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pr = findViewById(R.id.pr)
        imageView = findViewById(R.id.image)
        btn = findViewById(R.id.btn)
        imageView.setOnClickListener {
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
        }
        for(i in 0..3)
        {
            var dattta = MyData("name${Random.nextInt(1000)}","pass${Random.nextInt(1000)}")
            list.put("key$i",dattta)
        }
        btn.setOnClickListener {

            pr.visibility = View.VISIBLE
            // Write a message to the database
            val database = Firebase.database
            val myRef = database.getReference("new${Random.nextInt(1000)}")

            myRef.setValue(list)
            pr.visibility = View.INVISIBLE
//            storage = Firebase.storage
//            val storageRef = storage.reference
//            val mountainImagesRef = storageRef.child("images/mountains${Random.nextInt(10000)}.jpg")
//
//            mountainImagesRef.name == mountainImagesRef.name // true
//            mountainImagesRef.path == mountainImagesRef.path // false
//
//            // Get the data from an ImageView as bytes
//            imageView.isDrawingCacheEnabled = true
//            imageView.buildDrawingCache()
//            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
//            val baos = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//            val data = baos.toByteArray()
//
//            var uploadTask = mountainImagesRef.putBytes(data)
//            uploadTask.addOnFailureListener {
//                // Handle unsuccessful uploads
//            }.addOnSuccessListener { taskSnapshot ->
//
//                val urlTask = uploadTask.continueWithTask { task ->
//                    if (!task.isSuccessful) {
//                        task.exception?.let {
//                            throw it
//                        }
//                    }
//                    mountainImagesRef.downloadUrl
//                }.addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        val downloadUri = task.result
//
//                        pr.visibility = View.INVISIBLE
//                    } else {
//                        // Handle failures
//                        // ...
//                    }
//                }
//            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                var resultUri = result.uri
                imageView.setImageURI(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
}