package com.application.bit_time.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.math.BigDecimal;

public class TaskItem {


    private int IDpk;
    private String Name;
    private int Duration;
    //immagine boh
    private Uri imageUri;


    public TaskItem()
    {
        this.IDpk = -1;
        this.Name = "emptyTask";
        this.Duration = 0;
        this.imageUri = null;

        //Log.i("TASKITEMCONST",this.toString());
    }
    public TaskItem(int id,String name, int duration,Uri imageUri)
    {
        this.IDpk = id;
        this.Name = name;
        this.Duration = duration;
        this.imageUri = imageUri;
    }
    public TaskItem(int id,String name, int duration,String imageUri)
    {
        this.IDpk = id;
        this.Name = name;
        this.Duration = duration;
        this.imageUri = Uri.parse(imageUri);
    }

    public TaskItem(int id,String name,String duration,String uriStr)
    {
        this.IDpk = id;
        this.Name = name;
        this.imageUri=Uri.parse(uriStr);

        try {
            this.Duration = Integer.parseInt(duration);
        }catch(NumberFormatException ex)
        {
            this.Duration = -1 ;
            Log.e("ERROR","exception thrown when converting time for TaskItem obj");
        }

    }
    public TaskItem(TaskItem original)
    {
        this.IDpk = original.IDpk;
        this.Name = new String(original.getName());
        this.Duration = original.getDurationInt();
        this.imageUri = original.getImageUri();
    }
    public String getName()
    {
        return Name;
    }
    public String getDuration()
    {
        return Integer.toString(Duration);
    }
    public int getDurationInt()
    {
        return Duration;
    }
    public void setName(String name)
    {
        this.Name = name;
    }
    public void setDuration(String duration)
    {
        this.Duration= Integer.parseInt(duration);
    }
    public int getID()
    {
        return this.IDpk;
    }
    public String getIdStr()
    {
        return Integer.toString(this.IDpk);
    }
    @Override
    public boolean equals(@Nullable Object obj) {

        if(obj instanceof TaskItem)
        {

            TaskItem objData = (TaskItem) obj;

            if(this.IDpk == objData.IDpk)
                return true;
        }

        return false;


    }
    @NonNull
    @Override
    public String toString() {

        return getIdStr()+" "+getName() + " " + getDuration();


    }
    public boolean isEqualToEmpty()
    {
        TaskItem emptyTask = new TaskItem();

        if(
                this.IDpk == emptyTask.IDpk &&
                this.Name.equals(emptyTask.getName()) &&
                this.Duration == emptyTask.Duration
        )
            return true;
        else return false;
    }
    public String getFormattedDuration()
    {

         String res =getTimeHelper().toStringShrt();
         Log.i("reslog",res);
         return res;

    }

    public BitmapDrawable getBitmapDrawableImage(Context context)
    {
        return new BitmapDrawable(context.getResources(),this.imageUri.getPath());
    }

    public Drawable getDrawableThumbnail(Context context) throws IOException {

        return new BitmapDrawable(context.getResources(),context.getContentResolver().loadThumbnail(this.imageUri, new Size(300, 300), null));
    }
    public Uri getImageUri()
    {
        return this.imageUri;
    }
    public TimeHelper getTimeHelper()
    {
        return new TimeHelper(this.Duration);
    }
    public String toStringShrt()
    {
        return getIdStr();
    }
}
