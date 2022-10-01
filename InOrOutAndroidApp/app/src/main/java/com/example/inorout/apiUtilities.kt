package com.example.inorout

import okhttp3.RequestBody

interface apiUtilities {
    fun toPartMap(): HashMap<String?, RequestBody?>
}