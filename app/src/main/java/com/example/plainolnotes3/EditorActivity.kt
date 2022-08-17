package com.example.plainolnotes3

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.plainolnotes3.database.NoteEntity
import com.example.plainolnotes3.databinding.ActivityEditorBinding
import com.example.plainolnotes3.utilities.Constants.EDITING_KEY
import com.example.plainolnotes3.utilities.Constants.NOTE_ID_KEY
import com.example.plainolnotes3.viewmodel.EditorViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EditorActivity : AppCompatActivity() {

    private var mCurrentNote: NoteEntity? = null
    private lateinit var binding: ActivityEditorBinding
    private lateinit var mViewModel: EditorViewModel
    private var mEditing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_check)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState != null && savedInstanceState.containsKey(EDITING_KEY)) {
            mEditing = savedInstanceState.getBoolean(EDITING_KEY)
        }
        initViewModel()
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this)[EditorViewModel::class.java]

        val extras = intent.extras
        if (extras != null && extras.containsKey(NOTE_ID_KEY)) {
            binding.toolbarLayout.title = getString(R.string.edit_note)
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    val noteData = mViewModel.getNote(extras.getInt(NOTE_ID_KEY)) as Flow<NoteEntity>
                    noteData.collect {
                        mCurrentNote = it
                        if (it != null && !mEditing) {
                            binding.contentScrolling.noteText?.setText(it.text)
                        }
                    }
                }
            }
        } else {
            mCurrentNote = null
            binding.toolbarLayout.title = getString(R.string.new_note)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (mCurrentNote != null) {
            menuInflater.inflate(R.menu.menu_editor, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                saveAndReturn()
                true
            }
            R.id.action_delete_note -> {
                deleteNote()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        saveAndReturn()
        super.onBackPressed()
    }

    private fun saveAndReturn() {
        val text = binding.contentScrolling.noteText?.text.toString().trim()
        if (mCurrentNote != null) {
            mCurrentNote!!.text = text
        } else {
            if (TextUtils.isEmpty(text)) {
                return
            }
            mCurrentNote = NoteEntity(text = text)
        }
        mViewModel.saveNote(mCurrentNote!!)
        finish() // Close the current Activity and return to the previous Activity (Note List)
    }

    private fun deleteNote() {
        mViewModel.deleteNote(mCurrentNote)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(EDITING_KEY, true)
        super.onSaveInstanceState(outState)
    }
}