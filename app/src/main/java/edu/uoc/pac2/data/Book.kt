package edu.uoc.pac2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A book Model representing a piece of content.
 * isActive is true, because we created book when load form firestore
 */

@Entity (tableName = "books_table")
data class Book(
        @ColumnInfo (name = "author") val author: String? = null,
        @ColumnInfo (name = "description") val description: String? = null,
        @ColumnInfo (name = "publicationDate") val publicationDate: String? = null,
        @ColumnInfo (name = "title") val title: String? = null,
        @ColumnInfo (name = "uid") @PrimaryKey val uid: Int = 0,
        @ColumnInfo (name = "urlImage") val urlImage: String? = null,
        @ColumnInfo (name = "isActive") val isActive: Boolean = true,
)