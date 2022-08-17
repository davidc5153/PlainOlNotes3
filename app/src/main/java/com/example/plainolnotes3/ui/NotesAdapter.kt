package com.example.plainolnotes3.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.plainolnotes3.EditorActivity
import com.example.plainolnotes3.R
import com.example.plainolnotes3.databinding.NoteListItemBinding
import com.example.plainolnotes3.database.NoteEntity
import com.example.plainolnotes3.utilities.Constants.NOTE_ID_KEY
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesAdapter(private val mNotes: List<NoteEntity>, private val mContext: Context) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.note_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = mNotes[position]
        holder.mTextView.text = mContext.getString(R.string.note_display_text, note.date, note.text)
        holder.mFab.setOnClickListener( View.OnClickListener {
            val intent = Intent(mContext, EditorActivity::class.java)
            intent.putExtra(NOTE_ID_KEY, note.id)
            mContext.startActivity(intent)
        })
    }

    override fun getItemCount(): Int {
        return mNotes.size
    }

    // The purpose of the ViewHolder Class is to manage the View itself
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mTextView: TextView = NoteListItemBinding.bind(itemView).noteText
        val mFab: FloatingActionButton = NoteListItemBinding.bind(itemView).floatingActionButton
    }
}