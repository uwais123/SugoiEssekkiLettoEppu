package com.masuwes.notesapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.masuwes.notesapp.db.DatabaseContract.AUTHORITY
import com.masuwes.notesapp.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.masuwes.notesapp.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.masuwes.notesapp.db.NoteHelper

class NoteProvider : ContentProvider() {

    companion object {
        private const val NOTE = 1
        private const val NOTE_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var noteHelper: NoteHelper

        init {
            // content://com.masuwes.notesapp/note
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, NOTE)
            // content://com.masuwes.notesapp/note/id
            sUriMatcher.addURI(AUTHORITY,
                "$TABLE_NAME/#",
                NOTE_ID)
        }
    }

    // set create as boolean
    override fun onCreate(): Boolean {
        noteHelper = NoteHelper.getInstance(context as Context)
        noteHelper.open()
        return true
    }

    // add query with checking noteHelper
    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            NOTE -> cursor = noteHelper.queryAll()
            NOTE_ID -> cursor = noteHelper.queryById(uri.lastPathSegment.toString())
            else -> cursor = null
        }
        return null
     }


    override fun getType(uri: Uri): String? {
        return null
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added: Long = when (NOTE) {
            sUriMatcher.match(uri) -> noteHelper.insert(values)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val updated: Int = when (NOTE_ID) {
            sUriMatcher.match(uri) -> noteHelper.update(uri.lastPathSegment.toString(),values)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated

    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {

        val deleted: Int = when (NOTE_ID) {
            sUriMatcher.match(uri) -> noteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted

    }

}



























// end
