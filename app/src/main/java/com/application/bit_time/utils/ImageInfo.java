package com.application.bit_time.utils;

import android.net.Uri;

import androidx.annotation.NonNull;

public class ImageInfo {
    private final Uri uri;
    private final String name;

    public ImageInfo(Uri uri,String name)
    {
        this.uri = uri;
        this.name = name;
    }

    public Uri getUri()
    {
        return this.uri;
    }
    @NonNull
    @Override
    public String toString() {
        return this.uri.toString()+" "+this.name;
    }
}

