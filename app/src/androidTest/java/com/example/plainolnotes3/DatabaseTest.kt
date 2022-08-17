package com.example.plainolnotes3

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.plainolnotes3.database.AppDatabase
import com.example.plainolnotes3.database.NoteDao
import com.example.plainolnotes3.database.NoteEntity
import com.example.plainolnotes3.utilities.SampleData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @see [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    companion object {
        private const val TAG: String = "Junit"
    }

    private lateinit var context: Context
    private lateinit var mDb: AppDatabase
    private lateinit var mDao: NoteDao

    @Before
    fun createDB() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        mDao = mDb.noteDao()
        Log.i(TAG, "* Created Database *")
    }

    @After
    fun closeDB() {
        mDb.close()
        Log.i(TAG, "* Closed Database *")
    }

    @Test
    fun checkContext() {
        assertEquals("com.example.plainolnotes3", context.packageName)
        Log.i(TAG, "checkContext: Context Correct")
    }

    @Test
    fun createAndRetrieveNotes() {
        Log.i(TAG, "createAndRetrieveNotes: Before runBlocking")
        runBlocking {
            Log.i(TAG, "createAndRetrieveNotes: Before insertAll")
            mDao.insertAll(SampleData.getNotes())
            Log.i(TAG, "createAndRetrieveNotes: After insertAll")
            Log.i(TAG, "createAndRetrieveNotes: Before launch")
            launch {
                val count = mDao.getCount().first()
                Log.i(TAG, "createAndRetrieveNotes: count = $count")
                assertEquals(SampleData.getNotes().size, count)
            }
            Log.i(TAG, "createAndRetrieveNotes: After launch")
        }
        Log.i(TAG, "createAndRetrieveNotes: After runBlocking")
    }

    @Test
    fun compareStrings() {
        runBlocking {
            Log.i(TAG, "compareStrings: Before insertAll")
            mDao.insertAll(SampleData.getNotes())
            Log.i(TAG, "compareStrings: After insertAll")
            assertEquals(SampleData.getNotes()[0].text, mDao.getNoteById(1).first().text)
        }
    }

    @Test
    fun insertNoteIntoEmptyDb () {
        runBlocking {
            mDao.insertNote(NoteEntity("A new note"))
            assertEquals(mDao.getNoteById(1).first().text, "A new note")
        }
    }

    @Test
    fun insertNoteIntoPopulatedDb () {
        runBlocking {
            mDao.insertAll(SampleData.getNotes())
            mDao.insertNote(NoteEntity( "A new note"))
            assertEquals(mDao.getNoteById(SampleData.getNotes().size + 1).first().text, "A new note")
        }
    }

    @Test
    fun deleteAllNotes () {
        runBlocking {
            mDao.insertAll(SampleData.getNotes())
            assertEquals(SampleData.getNotes().size, mDao.getCount().first())
            mDao.deleteAll()
            assertEquals(0, mDao.getCount().first())
        }
    }

    @Test
    fun deleteSingleNote () {
        runBlocking {
            mDao.insertAll(SampleData.getNotes())
            val note = mDao.getNoteById(1).first()
            assertEquals(SampleData.getNotes()[0].text, note.text)
            mDao.deleteNote(note)
            val count = mDao.getCount().first()
            Log.i(TAG, "deleteSingleNote: count = $count")
            assertEquals(SampleData.getNotes().size - 1, count)
            assertNull(mDao.getNoteById(1).first())
            assertNotNull(mDao.getNoteById(2).first())
        }
    }
}