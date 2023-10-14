package com.application.bit_time.Utils.Db;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.application.bit_time.R;

public class DbActivity extends AppCompatActivity {

    Button insertButton ;
    Button readButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);



        insertButton = findViewById(R.id.insertButton);
        readButton = findViewById(R.id.readButton);



        try {
           DbManager dbManager = new DbManager(getApplicationContext());

            insertButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //dbManager.insertActivityRecord("ginnastica","55");
                    //dbManager.insertTaskRecord("vestirsi","80");
                    Log.i("DB_INFO","record added");
                }
            });



            readButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   Cursor cursor = dbManager.selectAllActivities();

                    while(cursor.moveToNext())
                        Log.i("DB_RES",""+cursor.getString(1));

                }
            });
        }catch(SQLiteException sqLiteException)
        {
            Log.i("DB_ERROR","sqlLiteException thrown");
        }










    }
}