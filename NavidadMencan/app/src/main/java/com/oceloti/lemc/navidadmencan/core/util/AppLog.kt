package com.oceloti.lemc.navidadmencan.core.util

import android.util.Log

object AppLog {
    private const val TAG = "NavidadMencan"
    private var isDebug = true // This will be set by build variant
    
    fun d(tag: String = TAG, message: String) {
        if (isDebug) {
            Log.d(tag, message)
        }
    }
    
    fun i(tag: String = TAG, message: String) {
        if (isDebug) {
            Log.i(tag, message)
        }
    }
    
    fun w(tag: String = TAG, message: String) {
        if (isDebug) {
            Log.w(tag, message)
        }
    }
    
    fun e(tag: String = TAG, message: String, throwable: Throwable? = null) {
        if (isDebug) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }
    }
    
    fun v(tag: String = TAG, message: String) {
        if (isDebug) {
            Log.v(tag, message)
        }
    }
    
    fun wtf(tag: String = TAG, message: String) {
        if (isDebug) {
            Log.wtf(tag, message)
        }
    }
    
    // Extension functions for convenience
    fun logD(tag: String = TAG, message: String) = d(tag, message)
    fun logE(tag: String = TAG, message: String, throwable: Throwable? = null) = e(tag, message, throwable)
    
    fun setDebugMode(debug: Boolean) {
        isDebug = debug
    }
}