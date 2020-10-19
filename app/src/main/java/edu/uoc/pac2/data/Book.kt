package edu.uoc.pac2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A book Model representing a piece of content.
 */
@Entity
data class Book(
        @ColumnInfo (name = "author") var author: String? = null,
        @ColumnInfo (name = "description") var description: String? = null,
        @ColumnInfo (name = "publicationDate") var publicationDate: String? = null,
        @ColumnInfo (name = "title") var title: String? = null,
        @PrimaryKey var uid: Int? = null,
        @ColumnInfo (name = "urlImage") var urlImage: String? = null,
)