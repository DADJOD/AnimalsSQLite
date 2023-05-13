package com.example.animalssqlite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
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

        registerForContextMenu(list)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_up -> {
                orderBy = AnimalsTable.COLUMN_ANIMAL + "  asc  "
                updateCursor()
                true
            }

            R.id.main_down -> {
                // select * from animals order by animal desc
                orderBy = AnimalsTable.COLUMN_ANIMAL + "  desc  "
                updateCursor()
                true
            }

            R.id.main_add -> {

                true
            }

            R.id.main_search -> {

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.context, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo

        return when (item.itemId) {
            R.id.context_delete -> {
                deleteAnimal(info.position)
                true
            }

            R.id.context_update -> {

                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    @SuppressLint("Range")
    private fun deleteAnimal(position: Int) {
        val cursor = adapter.cursor
        cursor.moveToPosition(position)

        val dbId = cursor.getString(
            cursor.getColumnIndex(AnimalsTable.COLUMN_ID)
        )

        helper.writableDatabase.delete(
            AnimalsTable.TABLE_ANIMALS,
            AnimalsTable.COLUMN_ID + " = ?",
            arrayOf( dbId )
        )

        updateCursor()
    }


//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.main_up -> {
//                orderBy = AnimalsTable.COLUMN_ANIMAL + "  asc  "
//                updateCursor()
//                return true
//            }
//
//            R.id.main_down -> {
//                // select * from animals order by animal desc
//                orderBy = AnimalsTable.COLUMN_ANIMAL + "  desc  "
//                updateCursor()
//                return true
//            }
//
//            R.id.main_search -> {
//                handleSearch(item)
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
}