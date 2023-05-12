package com.example.animalssqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AnimalsHelper(
    context: Context,
) : SQLiteOpenHelper(context, DATA_BASE, null, VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        AnimalsTable.onCreate(db)
//        UsersTable...
//        FilesTable...
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        AnimalsTable.onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        const val DATA_BASE = "animals.db"
        const val VERSION = 1
    }
}