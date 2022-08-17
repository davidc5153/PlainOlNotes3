package com.example.plainolnotes3.database

import android.content.Context
import android.util.Log
import com.example.plainolnotes3.utilities.SampleData
import com.example.plainolnotes3.utilities.SingletonHolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AppRepository private constructor(mContext: Context) {

    companion object : SingletonHolder<AppRepository, Context>(::AppRepository)

    private val mDB = AppDatabase.getInstance(mContext)
    val mNotes: Flow<List<NoteEntity>> = getAllNotes()

    fun addSampleData() {
       runBlocking {
           launch {
               mDB.noteDao().insertAll(SampleData.getNotes())
           }
       }
    }

    private fun getAllNotes(): Flow<List<NoteEntity>> {
        Log.i("AppRepository", "Fetching all notes form the DB")
        return mDB.noteDao().getAll()
    }

    fun deleteAllNotes() {
        runBlocking {
            launch {
                mDB.noteDao().deleteAll()
            }
        }
    }

    fun getNote(id: Int): Flow<NoteEntity> {
        return mDB.noteDao().getNoteById(id)
    }

    fun saveNote(noteEntity: NoteEntity) {
        runBlocking {
            launch {
                mDB.noteDao().insertNote(noteEntity)
            }
        }
    }

    fun deleteNote(mCurrentNote: NoteEntity) {
        runBlocking {
            launch {
                mDB.noteDao().deleteNote(mCurrentNote)
            }
        }
    }
}