package dev.gerlot.securewebview.sample

import android.content.ContentProvider
import android.content.ContentValues
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.net.Uri
import android.os.CancellationSignal
import android.util.Log
import java.io.FileNotFoundException
import java.io.IOException


class AssetsProvider : ContentProvider() {

    @Throws(FileNotFoundException::class)
    override fun openAssetFile(uri: Uri, mode: String): AssetFileDescriptor? {
        Log.v(TAG, "AssetsGetter: Open asset file $uri")
        val fileName = uri.lastPathSegment ?: throw FileNotFoundException()

        return try {
            context?.assets?.openFd(fileName)
        } catch (e: IOException) {
            Log.e(TAG, Log.getStackTraceString(e))
            null
        }
    }

    override fun getType(p1: Uri): String? {
        // NOT IMPLEMENTED
        return null
    }

    override fun delete(p1: Uri, p2: String?, p3: Array<String>?): Int {
        // NOT IMPLEMENTED
        return 0
    }

    override fun query(
        p1: Uri,
        p2: Array<String>?,
        p3: String?,
        p4: Array<String>?,
        p5: String?
    ): Cursor? {
        // NOT IMPLEMENTED
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
        cancellationSignal: CancellationSignal?
    ): Cursor? {
        return super.query(uri, projection, selection, selectionArgs, sortOrder, cancellationSignal)
    }

    override fun insert(p1: Uri, p2: ContentValues?): Uri? {
        // NOT IMPLEMENTED
        return null
    }

    override fun onCreate(): Boolean {
        // NOT IMPLEMENTED
        return false
    }

    override fun update(p1: Uri, p2: ContentValues?, p3: String?, p4: Array<String>?): Int {
        // NOT IMPLEMENTED
        return 0
    }

    companion object {
        const val TAG = "AssetsProvider"
    }
}
