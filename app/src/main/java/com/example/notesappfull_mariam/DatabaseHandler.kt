package com.example.notesappfull_mariam

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler
    (context: Context): SQLiteOpenHelper(context,"noteDataBase",null,1){
    var sqLiteDatabase: SQLiteDatabase = writableDatabase
    override fun onCreate(db: SQLiteDatabase?) {
        if(db != null){
            db.execSQL("CREATE TABLE NotesTable (id INTEGER PRIMARY KEY, note TEXT)")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {}
    fun addNote(note: Note): Long{
        val contentValues = ContentValues()
        contentValues.put("note", note.noteText)

        val success = sqLiteDatabase.insert("NotesTable", null, contentValues)
        return success
    }


    @SuppressLint("Range")
    fun viewNotes(): ArrayList<Note>{
        val noteList: ArrayList<Note> = ArrayList()
        val selectQuery = "SELECT * FROM NotesTable"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var noteText: String

        if(cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                noteText = cursor.getString(cursor.getColumnIndex("note"))

                val note = Note(noteId = id, noteText = noteText)
                noteList.add(note)
            } while (cursor.moveToNext())
        }

        return noteList
    }
    fun updateNotes(note: Note): Int {
        val contentValues = ContentValues()

        contentValues.put("note", note.noteText)
        val success = sqLiteDatabase.update("NotesTable", contentValues, "id = ${note.noteId}", null)
        return success
    }
    fun deleteNotes(note: Note): Int{
        val contentValues = ContentValues()
        contentValues.put("id", note.noteId)
        val success = sqLiteDatabase.delete("NotesTable", "id = ${note.noteId}", null)
        return success
    }
}