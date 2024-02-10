package com.note.firebase2

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream
import kotlin.random.Random


class AddProduct : AppCompatActivity() {

    private lateinit var storage: FirebaseStorage
    lateinit var selct_photo: ImageView
    lateinit var product_name: TextInputEditText
    lateinit var product_price: TextInputEditText
    lateinit var product_des: TextInputEditText
    lateinit var dis: TextInputEditText
    lateinit var add: MaterialButton
    lateinit var progress: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addproduct)
        storage = Firebase.storage

        selct_photo = findViewById(R.id.selct_photo)
        product_name = findViewById(R.id.product_name)
        product_price = findViewById(R.id.product_price)
        product_des = findViewById(R.id.product_des)
        dis = findViewById(R.id.dis)
        add = findViewById(R.id.add)
        progress = findViewById(R.id.progress)

        selct_photo.setOnClickListener {
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
        }
        val storageRef = storage.reference

        val mountainImagesRef = storageRef.child("Product/${product_name.text}${Random.nextInt(10000)}.jpg")

        mountainImagesRef.name == mountainImagesRef.name // true
        mountainImagesRef.path == mountainImagesRef.path // false
        add.setOnClickListener {
progress.visibility = View.VISIBLE
            selct_photo.isDrawingCacheEnabled = true
            selct_photo.buildDrawingCache()
            val bitmap = (selct_photo.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            var uploadTask = mountainImagesRef.putBytes(data)
            uploadTask.addOnFailureListener {


            }.addOnSuccessListener { taskSnapshot ->
                val urlTask = uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    mountainImagesRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        val database = Firebase.database
                        val myRef = database.getReference(product_name.text.toString())
                        var data = MyData(myRef.key!!,downloadUri.toString(),product_name.text.toString(),product_price.text.toString(),product_des.text.toString(),dis.text.toString())

                        myRef.setValue(data)
                        Log.d("=-=-==", "onCreate: $data")
                        progress.visibility = View.GONE
                    } else {
                        // Handle failures
                        // ...
                    }
                }


            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                selct_photo.setImageURI(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
}