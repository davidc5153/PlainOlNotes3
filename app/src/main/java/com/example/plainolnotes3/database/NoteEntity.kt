package com.example.plainolnotes3.database

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes")
data class NoteEntity(@PrimaryKey(autoGenerate = true) var id: Int, var date: Date, var text: String) {

    @Ignore
    constructor(date: Date, text: String = ""): this(0, date, text) // ID is generated by the DB

    @Ignore
    constructor(text: String = ""): this(0, Date(), text)

    override fun toString(): String {
        return "NoteEntity(id=$id, date=$date, text='$text')"
    }

}
