package com.application.bit_time.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.activity.result.contract.ActivityResultContract;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class SoundPicker extends ActivityResultContract<Void, Uri> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Void input) {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION); // or RingtoneManager.TYPE_RINGTONE for ringtone
        return intent;
    }

    @Override
    public Uri parseResult(int resultCode, @Nullable Intent intent) {
        if (resultCode == Activity.RESULT_OK && intent != null) {
            return intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        }
        return null;
    }
}

