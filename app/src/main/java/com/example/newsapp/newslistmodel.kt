package com.example.newsapp

import android.net.Uri

class newslistmodel {

    lateinit var key : String
    lateinit var title : String
    lateinit var text : String
    lateinit var img : String

    constructor()
    {

    }
    constructor(key: String, title: String, text: String, img: String) {
        this.key = key
        this.title = title
        this.text = text
        this.img = img
    }
}