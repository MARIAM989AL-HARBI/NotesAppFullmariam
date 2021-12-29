package com.example.notesappfull_mariam

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var db: DatabaseHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = DatabaseHandler(this)
        if(getItemsList().isNotEmpty()){
            RV_update()
        }else{
            cardView.isVisible=false
        }

        note_btn.setOnClickListener { addNote() }
    }
    private fun RV_update(){
        cardView.isVisible=true
        note_recyclerView.adapter = NoteAdapter(this, getItemsList())
        note_recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun getItemsList(): ArrayList<Note>{
        return db.viewNotes()
    }
    private fun addNote(){
        if(note_et.text.isEmpty()){
            Toast.makeText(this, "Error note is empty!!", Toast.LENGTH_LONG).show()
        }else{
            db.addNote(Note(0, note_et.text.toString()))
            note_et.text.clear()
            Toast.makeText(this, "Note Added successfully!!", Toast.LENGTH_LONG).show()
            RV_update()
        }

    }
    fun deleteNote(note: Note){
        db.deleteNotes(Note(note.noteId, note.noteText))
        RV_update()
    }
    fun updateNotes(note: Note){
        db.updateNotes(Note(note.noteId, note.noteText))
        RV_update()
    }
    fun raiseDialog(id: Int){
        val dialogBuilder = AlertDialog.Builder(this)
        val updatedNote = EditText(this)
        updatedNote.hint = "Enter new note"
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("Save", DialogInterface.OnClickListener {
                    _, _ -> updateNotes(Note(id, updatedNote.text.toString()))
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, _ -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Update Note")
        alert.setView(updatedNote)
        alert.show()
    }
}