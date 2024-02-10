package com.note.firebase2

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream


class HomePage : AppCompatActivity() {

    private lateinit var adapter: FirebaseRecyclerAdapter<MyData, MyClass>
    private var resultUri: Uri? = null
    lateinit var recycle : RecyclerView
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)
        database = Firebase.database.reference
        val query: Query = FirebaseDatabase.getInstance()
            .reference
            .child("hh")
            .limitToLast(50)

        recycle = findViewById(R.id.recycle)

        val options: FirebaseRecyclerOptions<MyData> = FirebaseRecyclerOptions.Builder<MyData>()
            .setQuery(query, MyData::class.java)
            .build()

         adapter =
            object : FirebaseRecyclerAdapter<MyData,MyClass>(options) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyClass {


                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.message, parent, false)
                    return MyClass(view)
                }

                override fun onBindViewHolder(holder: MyClass, position: Int, model: MyData) {

                    holder.selct_photo.setOnClickListener {
                        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                            .start(this@HomePage)
                        holder.selct_photo.setImageURI(resultUri)

                    }

                    holder.edit.setOnClickListener {
                        val bitmap = (holder.selct_photo.drawable as BitmapDrawable).bitmap
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()

                        var dataaa = MyData(model.key,"abc",holder.product_name.text.toString(),holder.product_price.text.toString(),holder.product_des.text.toString(),holder.dis.text.toString())
                        database.child("hh").setValue(dataaa)

                    }
                    holder.product_name.setText(model.product_name)
                }


            }
        recycle.adapter = adapter


    }
    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }
    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }
    class MyClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var selct_photo: ImageView
        lateinit var product_name: TextInputEditText
        lateinit var product_price: TextInputEditText
        lateinit var product_des: TextInputEditText
        lateinit var dis: TextInputEditText
        lateinit var edit: MaterialButton
        lateinit var progress: ProgressBar

        init {
            selct_photo = itemView.findViewById(R.id.eselct_photo)
            product_name = itemView.findViewById(R.id.eproduct_name)
            product_price = itemView.findViewById(R.id.eproduct_price)
            product_des = itemView.findViewById(R.id.eproduct_des)
            dis = itemView.findViewById(R.id.edis)
            edit = itemView.findViewById(R.id.eadd)
            progress = itemView.findViewById(R.id.eprogress)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                 resultUri = result.uri

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }
}