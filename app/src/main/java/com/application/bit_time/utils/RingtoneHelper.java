package com.application.bit_time.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RingtoneHelper {

    public static List<String> getRingtoneTitles(Context context, int type) {
        List<String> ringtoneTitles = new ArrayList<>();

        RingtoneManager ringtoneManager = new RingtoneManager(context);
        ringtoneManager.setType(type);

        Cursor cursor = ringtoneManager.getCursor();

        while (cursor.moveToNext()) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            ringtoneTitles.add(title);
        }

        cursor.close();
        return ringtoneTitles;
    }

    public static String getRingtoneUriByTitle(Context context, String title, int type) {
        RingtoneManager ringtoneManager = new RingtoneManager(context);
        ringtoneManager.setType(type);

        Cursor cursor = ringtoneManager.getCursor();

        while (cursor.moveToNext()) {
            String currentTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);

            if (currentTitle.equals(title)) {
                int position = cursor.getPosition();
                return ringtoneManager.getRingtoneUri(position).toString();
            }
        }

        cursor.close();
        return null;
    }

    public static void playRingtone(Context context, String uriString) {
        if (uriString != null) {
            Uri uri = Uri.parse(uriString);
            Ringtone ringtone = RingtoneManager.getRingtone(context, uri);

            if (ringtone != null) {
                ringtone.play();
            }
        }
    }
}
