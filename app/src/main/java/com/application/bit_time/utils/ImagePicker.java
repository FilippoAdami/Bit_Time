package com.application.bit_time.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ImagePicker extends ActivityResultContract<Void, Uri> {

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Void input) {
        // Create an intent to pick an image from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        return intent;
    }

    @Override
    public Uri parseResult(int resultCode, @Nullable Intent intent) {
        // Parse the result and return the selected image URI
        if (resultCode == android.app.Activity.RESULT_OK && intent != null) {
            return intent.getData();
        }
        return null;
    }
}

