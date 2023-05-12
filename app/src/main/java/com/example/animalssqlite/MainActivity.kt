package com.example.animalssqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import androidx.cursoradapter.widget.SimpleCursorAdapter

class MainActivity : AppCompatActivity() {
    var orderBy: String? = null
    private var selectionArgs: Array<String>? = null
    private var selections: String? = null
    lateinit var list: ListView
    private lateinit var helper: AnimalsHelper
    lateinit var adapter: SimpleCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = findViewById(R.id.list)
        list.emptyView = findViewById(R.id.notFound)

        helper = AnimalsHelper(this)
        adapter = SimpleCursorAdapter(
            this,
            android.R.layout.simple_list_item_1,
            null,
            arrayOf(AnimalsTable.COLUMN_ANIMAL),
            intArrayOf(android.R.id.text1),
            0
        )

        list.adapter = adapter

        updateCursor()
    }

    private fun updateCursor() {
        val cursor = helper.readableDatabase.query(
            AnimalsTable.TABLE_ANIMALS,
            null,
            selections,
            selectionArgs,
            null,
            null,
            orderBy
        )

        adapter.swapCursor(cursor)
    }
}