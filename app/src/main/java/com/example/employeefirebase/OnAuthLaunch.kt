package com.example.employeefirebase

import android.content.Intent

interface OnAuthLaunch {
    fun launch(intent: Intent)
    fun showListFragment()
}