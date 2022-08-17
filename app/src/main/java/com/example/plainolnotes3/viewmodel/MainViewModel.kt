package com.example.plainolnotes3.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.plainolnotes3.database.AppRepository
import com.example.plainolnotes3.database.NoteEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository: AppRepository = AppRepository.getInstance(application.applicationContext)
    // val mNotes = mRepository.mNotes
    val mNotes: StateFlow<List<NoteEntity>> = mRepository.mNotes.stateIn(
        initialValue = ArrayList<NoteEntity>(),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun addSampleData() {
        mRepository.addSampleData()
    }

    fun deleteAllNotes() {
        mRepository.deleteAllNotes()
    }
}
