package com.example.animalssqlite

import android.database.sqlite.SQLiteDatabase

class AnimalsTable {
    companion object {
        const val TABLE_ANIMALS = "animals"
        const val COLUMN_ID = "_id"
        const val COLUMN_ANIMAL = "animal"

        private const val CREATE =
                    "    create table animals ( _id integer primary key autoincrement ,   " +
                    "    animal text not null ) ;    "

        fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE)
            populate(db)
        }

        private fun populate(db: SQLiteDatabase) {
            db.execSQL("    insert into animals (animal) values ('crow')    ")
            db.execSQL("    insert into animals (animal) values ('dingo')    ")
            db.execSQL("    insert into animals (animal) values ('owl')    ")
            db.execSQL("    insert into animals (animal) values ('eagle')    ")
            db.execSQL("    insert into animals (animal) values ('doggy')    ")
            db.execSQL("    insert into animals (animal) values ('kitty')    ")
            db.execSQL("    insert into animals (animal) values ('elephant')    ")
            db.execSQL("    insert into animals (animal) values ('mouse')    ")
        }

        fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            TODO("Not yet implemented")
        }
    }

}

//internal object AnimalsTable {
//    const val TABLE_ANIMALS = "animals"
//    const val COLUMN_ID = "_id"
//    const val COLUMN_ANIMAL = "animal"
//    const val CREATE = "   create table animals  (   " +
//            "  _id   integer primary key autoincrement ,  animal text not null  ) ;   "
//
//    fun onCreate(sqLiteDatabase: SQLiteDatabase) {
//        sqLiteDatabase.execSQL(CREATE)
//        populate(sqLiteDatabase)
//    }
//
//    private fun populate(sqLiteDatabase: SQLiteDatabase) {
//        sqLiteDatabase.execSQL(" insert into animals (animal) values ('crow')    ")
//        sqLiteDatabase.execSQL(" insert into animals (animal) values ('dingo')    ")
//        sqLiteDatabase.execSQL(" insert into animals (animal) values ('owl')    ")
//        sqLiteDatabase.execSQL(" insert into animals (animal) values ('eagle')    ")
//        sqLiteDatabase.execSQL(" insert into animals (animal) values ('doggy')    ")
//        sqLiteDatabase.execSQL(" insert into animals (animal) values ('kitty')    ")
//        sqLiteDatabase.execSQL(" insert into animals (animal) values ('elephant')    ")
//    }
//
//    fun onUpgrade(sqLiteDatabase: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
//}

