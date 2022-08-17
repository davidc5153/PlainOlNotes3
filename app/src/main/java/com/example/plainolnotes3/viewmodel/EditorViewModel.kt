package com.example.plainolnotes3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.plainolnotes3.database.AppRepository
import com.example.plainolnotes3.database.NoteEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class EditorViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: AppRepository = AppRepository.getInstance(application.applicationContext)

    fun getNote(id: Int): StateFlow<NoteEntity> {
        return mRepository.getNote(id).stateIn(
            initialValue = NoteEntity(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )
    }

    fun saveNote(noteEntity: NoteEntity) {
        mRepository.saveNote(noteEntity)
    }

    fun deleteNote(mCurrentNote: NoteEntity?) {
        if (mCurrentNote != null) {
            mRepository.deleteNote(mCurrentNote)
        }
    }
}
