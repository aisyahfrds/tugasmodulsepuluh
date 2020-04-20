package com.icha.tugasmodulsembilan

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.icha.tugasmodulsembilan.db.NoteRoomDatabase
import com.icha.tugasmodulsembilan.model.Note
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getNotesData()

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this, EditActivity::class.java))
        }

    }

    private fun getNotesData(){
        val database = NoteRoomDatabase.getDatabase(applicationContext)
        val dao = database.getNoteDao()
        val listItems = arrayListOf<Note>()
        listItems.addAll(dao.getAll())
        setupRecyclerView(listItems)
        if (listItems.isNotEmpty()){
            text_view_note_empty.visibility = View.GONE
        }
        else{
            text_view_note_empty.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView(listItems: ArrayList<Note>){
        recycler_view_main.apply {
            adapter = NoteAdapter(listItems, object : NoteAdapter.NoteListener{
                override fun OnItemClicked(note: Note) {
                    val intent = Intent(this@MainActivity, EditActivity::class.java)
                    intent.putExtra(EditActivity().EDIT_NOTE_EXTRA, note)
                    startActivity(intent)
                }
            })

            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        getNotesData()
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setMessage("Do you want to Exit?")
        builder.setPositiveButton("Yes") { dialog, which ->
            //if user pressed "yes", then he is allowed to exit from application
            finish()
        }
        builder.setNegativeButton("No") { dialog, which ->
            //if user select "No", just cancel this dialog and continue with app
            dialog.cancel()
        }
        val alert = builder.create()
        alert.show()
    }
}
