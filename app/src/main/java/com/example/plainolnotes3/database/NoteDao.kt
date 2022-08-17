package com.example.plainolnotes3.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(noteEntity: NoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notesEntityList: List<NoteEntity>)

    @Delete
    suspend fun deleteNote(noteEntity: NoteEntity)

    @Query("SELECT * from notes WHERE id = :id")
    fun getNoteById(id: Int): Flow<NoteEntity>
    // Refer: https://medium.com/androiddevelopers/room-flow-273acffe5b57
    fun getNoteByIdDistinctUtilChanged(id: Int) = getNoteById(id).distinctUntilChanged()

    @Query("SELECT * FROM notes ORDER BY date DESC")
    fun getAll(): Flow<List<NoteEntity>> // DB Data is returned wrapped in a Kotlin Flow object

    @Query("DELETE FROM notes")
    suspend fun deleteAll(): Int

    @Query("SELECT COUNT(*) FROM notes")
    fun getCount(): Flow<Int>

}