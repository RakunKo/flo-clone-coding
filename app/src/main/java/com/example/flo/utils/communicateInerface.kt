package com.example.flo.utils

import com.example.flo.data.entities.Album

interface communicateInerface {
    fun sendData(album: Album)
}