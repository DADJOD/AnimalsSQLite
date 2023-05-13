package com.example.animalssqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cursoradapter.widget.SimpleCursorAdapter


@Suppress("NAME_SHADOWING")
class MainActivity : AppCompatActivity() {
    var orderBy: String? = null
    private var selectionArgs: Array<String>? = null
    private var selection: String? = null
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
            selection,
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
                handleSearch(item)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private var likeQuery = ""

    private fun handleSearch(item: MenuItem) {
        val searchView = item.actionView as SearchView

        item.expandActionView()
        searchView.setQuery(likeQuery, false)

        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (!TextUtils.isEmpty(newText)) {
                    selection = AnimalsTable.COLUMN_ANIMAL + "  like  ?"
                    selectionArgs = arrayOf("%$newText%")
                } else {
                    selection = null
                    selectionArgs = null
                }
                likeQuery = newText
                updateCursor()
                return true
            }
        })

    }

//    private fun handleSearch(item: MenuItem) {
//        val searchView = item.actionView as SearchView?
//        item.expandActionView()
//        searchView!!.setQuery(likeQuery, false)
//        searchView.setOnQueryTextListener(object : OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                if (!TextUtils.isEmpty(newText)) {
//                    selection = AnimalsTable.COLUMN_ANIMAL + " like ? "
//                    selectionArgs = arrayOf("%$newText%")
//                } else {
//                    selection = null
//                    selectionArgs = null
//                }
//                likeQuery = newText
//                updateCursor()
//                return true
//            }
//        })
//    }

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
                updateAnimalDialog(info.position)
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    @SuppressLint("Range")
    private fun updateAnimalDialog(position: Int) {
        val cursor = adapter.cursor
        cursor.moveToPosition(position)

        val dbId = cursor.getString(
            cursor.getColumnIndex(AnimalsTable.COLUMN_ID)
        )

        val animal = cursor.getString(
            cursor.getColumnIndex(AnimalsTable.COLUMN_ANIMAL)
        )

        val edit = LayoutInflater
            .from(this)
            .inflate(R.layout.dialog, null)
                as EditText

        edit.setText(animal)

        val b = AlertDialog.Builder(this)

        b
            .setTitle("Update Animal")
            .setView(edit)
            .setPositiveButton("Update ", DialogInterface.OnClickListener { dialog, which ->
                val animal = edit.text.toString()
                updateAnAnimal(dbId, animal)
                dialog.dismiss()
            })

        b.create().show()
    }

    private fun updateAnAnimal(dbId: String, animal: String) {
        val contentValues = ContentValues()

        contentValues.put(AnimalsTable.COLUMN_ANIMAL, animal)

        helper.writableDatabase.update(
            AnimalsTable.TABLE_ANIMALS,
            contentValues,
            AnimalsTable.COLUMN_ID + " = ?",
            arrayOf( dbId )
        )

        updateCursor()
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
            arrayOf(dbId)
        )

        updateCursor()
    }
}