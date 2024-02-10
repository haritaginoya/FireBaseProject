package com.note.firebase2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query


class HomePage : AppCompatActivity() {

    lateinit var recycle : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)
        val query: Query = FirebaseDatabase.getInstance()
            .reference
            .child("chats")
            .limitToLast(50)

        recycle = findViewById(R.id.recycle)

        val options: FirebaseRecyclerOptions<MyData> = FirebaseRecyclerOptions.Builder<MyData>()
            .setQuery(query, MyData::class.java)
            .build()

        var adapter =
            object : FirebaseRecyclerAdapter<MyData,MyClass>(options) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyClass {
                    // Create a new instance of the ViewHolder, in this case we are using a custom
                    // layout called R.layout.message for each item
                    val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.message, parent, false)
                    return MyClass(view)
                }

                override fun onBindViewHolder(holder: MyClass, position: Int, model: MyData) {

                }


            }


    }

    class MyClass(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {

    }
}