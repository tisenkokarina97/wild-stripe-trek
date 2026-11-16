package com.Qapfoxo9.Diknos.data.local

import android.content.Context
import android.content.SharedPreferences

class TrekPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    fun readInt(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun writeInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun readBoolean(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun writeBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun remove(keys: List<String>) {
        val editor = prefs.edit()
        for (key in keys) editor.remove(key)
        editor.apply()
    }

    companion object {
        private const val STORE_NAME = "wild_stripe_trek_store"
        const val KEY_UNLOCKED_LEVEL = "unlocked_level"
        const val KEY_STRIPES = "stripes_"
        const val KEY_JOURNAL = "journal_"
        const val KEY_TOTAL_PAWPRINTS = "total_pawprints"
        const val KEY_TOTAL_TREKS = "total_treks"
        const val KEY_TREKS_SAVED = "treks_saved"
        const val KEY_BEST_STREAK = "best_streak"
        const val KEY_ROUNDS_PLAYED = "rounds_played"
        const val KEY_ROUNDS_WON = "rounds_won"
        const val KEY_DEFAULT_TAP = "opt_default_tap"
        const val KEY_SAFE_FIRST = "opt_safe_first"
    }
}
