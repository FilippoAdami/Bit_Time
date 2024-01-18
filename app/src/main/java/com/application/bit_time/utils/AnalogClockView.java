package com.application.bit_time.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.application.bit_time.R;

import java.util.Calendar;

public class AnalogClockView extends View {

    // Paint objects for drawing various elements
    private Paint clockPaint;          // Paint for the outer clock circle
    private Paint hourHandPaint;       // Paint for the hour hand
    private Paint minuteHandPaint;     // Paint for the minute hand
    private Paint textPaint;           // Paint for drawing text (numbers)
    private Paint minuteMarkPaint;     // Paint for drawing minute marks

    private Paint RedPaint;      // Paint for the pastel red color
    private Paint YellowPaint;   // Paint for the pastel yellow color
    private Paint GreenPaint;    // Paint for the pastel green color
    private Paint BluePaint;     // Paint for the pastel blue color
    private Paint primaryPaint;        // Paint for the primary color
    private Paint whitePaint;          // Paint for the white color
    private int centerX;               // X coordinate of the clock center
    private int centerY;               // Y coordinate of the clock center
    private int radius;                // Radius of the clock

    // Constructor for creating the view programmatically
    public AnalogClockView(Context context) {
        super(context);
        init();
    }

    // Constructor for creating the view from XML layout
    public AnalogClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // Initialize paint objects and other properties
    private void init() {
        Context context = getContext();
        int earthRedColor = ContextCompat.getColor(context, R.color.earth_red);
        int earthYellowColor = ContextCompat.getColor(context, R.color.earth_yellow);
        int earthGreenColor = ContextCompat.getColor(context, R.color.earth_green);
        int earthBlueColor = ContextCompat.getColor(context, R.color.earth_purple);

        int pastelRedColor = ContextCompat.getColor(context, R.color.pastel_red);
        int pastelYellowColor = ContextCompat.getColor(context, R.color.pastel_yellow);
        int pastelGreenColor = ContextCompat.getColor(context, R.color.pastel_green);
        int pastelBlueColor = ContextCompat.getColor(context, R.color.pastel_purple);

        int vividRedColor = ContextCompat.getColor(context, R.color.red);
        int vividYellowColor = ContextCompat.getColor(context, R.color.yellow);
        int vividGreenColor = ContextCompat.getColor(context, R.color.green);
        int vividBlueColor = ContextCompat.getColor(context, R.color.purple);

        int primaryColor = ContextCompat.getColor(context, R.color.primary);
        int whiteColor = ContextCompat.getColor(context, R.color.white);
        primaryPaint = new Paint();
        primaryPaint.setColor(primaryColor);
        primaryPaint.setStyle(Paint.Style.STROKE);
        whitePaint = new Paint();
        whitePaint.setColor(whiteColor);
        whitePaint.setStyle(Paint.Style.FILL);
        clockPaint = new Paint();
        clockPaint.setColor(primaryColor);
        clockPaint.setAlpha(128);
        clockPaint.setStyle(Paint.Style.STROKE);
        clockPaint.setStrokeWidth(3);

        //get the theme of the activity
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String theme = sharedPreferences.getString("theme", "pastel");

        hourHandPaint = new Paint();
        hourHandPaint.setStyle(Paint.Style.STROKE);
        hourHandPaint.setStrokeWidth(20);

        minuteHandPaint = new Paint();
        minuteHandPaint.setStyle(Paint.Style.STROKE);
        minuteHandPaint.setStrokeWidth(12);

        BluePaint = new Paint();
        BluePaint.setStyle(Paint.Style.STROKE);
        RedPaint = new Paint();
        RedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        YellowPaint = new Paint();
        YellowPaint.setStyle(Paint.Style.FILL);
        GreenPaint = new Paint();
        GreenPaint.setStyle(Paint.Style.FILL);

        if(theme.equals("PastelTheme") || theme.equals("BWTheme")){
            BluePaint.setColor(pastelBlueColor);
            RedPaint.setColor(pastelRedColor);
            YellowPaint.setColor(pastelYellowColor);
            GreenPaint.setColor(pastelGreenColor);
        }
        else if(theme.equals("EarthTheme")){
            BluePaint.setColor(earthBlueColor);
            RedPaint.setColor(earthRedColor);
            YellowPaint.setColor(earthYellowColor);
            GreenPaint.setColor(earthGreenColor);
        }
        else{
            BluePaint.setColor(vividBlueColor);
            RedPaint.setColor(vividRedColor);
            YellowPaint.setColor(vividYellowColor);
            GreenPaint.setColor(vividGreenColor);
        }

        hourHandPaint.setColor(BluePaint.getColor());
        minuteHandPaint.setColor(RedPaint.getColor());


        textPaint = new Paint();
        textPaint.setColor(primaryColor);
        textPaint.setTextSize(60);
        textPaint.setTextAlign(Paint.Align.CENTER);

        minuteMarkPaint = new Paint();
        minuteMarkPaint.setColor(primaryColor);
        minuteMarkPaint.setStrokeWidth(5);
    }

    // Called when the view's size changes (such as during layout changes)
    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        // Calculate the center and radius of the clock based on the view's size
        centerX = w / 2;
        centerY = h / 2;
        radius = Math.min(centerX, centerY) - 10;
    }

    // The main drawing function
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Get current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // Calculate angles for hour and minute hands
        float hourAngle = (hour % 12 + minute / 60.0f - 3) * 360 / 12 + 90;
        float minuteAngle = (minute + second / 60.0f)*6;
        // Draw outer clock circle
        canvas.drawCircle(centerX, centerY, radius, clockPaint);
        Paint whitePait = new Paint();

        //Get TimesString from SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String TimesString = sharedPreferences.getString("TimesString", "000000,000000,000000,000000,000000,000000,00000");

        //Get current task from SharedPreferences
        int currentTaskID = sharedPreferences.getInt("currentTask", 000000);
        // Get currentTaskStartingTime from SharedPreferences
        float taskStartingTime = sharedPreferences.getFloat("taskStartingTime", 0.0f);
        // Calculate the starting angle for the current task
        float taskStartingAngle = taskStartingTime*6;
        //Get IDsString from SharedPreferences
        String IDsString = sharedPreferences.getString("IDsString", "000000,000000,000000,000000,000000");

        //Parse the IDsString into an array of ints
        String[] IDsArray = IDsString.split(",");
        int[] IDsIntArray = new int[5];
        for (int i = 0; i < 5; i++) {
            IDsIntArray[i] = Integer.parseInt(IDsArray[i]);
        }

        //Get the index of the current task in the IDsIntArray
        int currentTaskIndex = 0;
        for (int i = 0; i < 5; i++) {
            if(IDsIntArray[i] == currentTaskID){
                currentTaskIndex = i;
            }
        }

        // Get the parsed arrays from the input string
        int[][][] parsedArrays = parseTimeString(TimesString);

        // Calculate the total times for each task
        float[] total_times = new float[7];
        for (int i = 0; i < 7; i++) {
            float hours = parsedArrays[i][0][0] * 10 + parsedArrays[i][0][1];
            float minutes = parsedArrays[i][1][0] * 10 + parsedArrays[i][1][1];
            float seconds = parsedArrays[i][2][0] * 10 + parsedArrays[i][2][1];

            float time = hours*60 + minutes + seconds/60;
            total_times[i] = time;
        }
        // Calculate the starting angles for each task
        float[] taskSliceStart = new float[6];

        taskSliceStart[0] = (total_times[6] + total_times[1])*6;
        taskSliceStart[1] = (taskSliceStart[0] + total_times[2]*6);
        taskSliceStart[2] = (taskSliceStart[1] + total_times[3]*6);
        taskSliceStart[3] = (taskSliceStart[2] + total_times[4]*6);
        taskSliceStart[4] = (taskSliceStart[3] + total_times[5]*6);
        taskSliceStart[5] = total_times[6]*6;

        if(currentTaskIndex == 0){
            taskStartingAngle = taskSliceStart[5];
        }

        boolean[] isTaskDone = new boolean[5];
        for (int i = 0; i < 5; i++) {
            isTaskDone[i] = false;
        }
        if (currentTaskIndex > 0){
            isTaskDone[currentTaskIndex-1] = true;
        }

        Paint[] taskSlicePaint = new Paint[5];

        int j = -1;
        for (int i = 0; i < 5; i++) {
            taskSlicePaint[i] = new Paint();
            if(isTaskDone[i]){
                j=i;
            }
        }
        RectF rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        float endCurrentAngle = 0f;
        for (int i = 0; i < 5; i++) {
            taskSlicePaint[i].setStyle(Paint.Style.FILL);
            // Set the done tasks to white (do not show them)
            if(i <= j){
                taskSlicePaint[i].setColor(Color.argb(0, 255, 255, 255));
            }
            // Set the current task to green-yellow-red
            else if (i == j+1){
                taskSlicePaint[i].setColor(Color.argb(0, 255, 255, 255));
                /*float startAngle = taskSliceStart[5];
                if(i>0){
                    for(int z=0; z<i; z++){
                        startAngle = startAngle + total_times[z+1]*6;
                    }
                }*/
                endCurrentAngle = taskStartingAngle + total_times[i+1]*6;

                float yellowSliceStart = (float) (taskStartingAngle + (0.65*total_times[i+1])*6);
                float redSliceStart = (float) (taskStartingAngle + (0.85*total_times[i+1])*6);

                // Draw the green slice starting at minuteAngle
                canvas.drawArc(rectF, taskStartingAngle - 90, yellowSliceStart - taskStartingAngle, true, GreenPaint);
                canvas.drawArc(rectF, yellowSliceStart - 90, redSliceStart - yellowSliceStart, true, YellowPaint);
                canvas.drawArc(rectF, redSliceStart - 90, endCurrentAngle - redSliceStart, true, RedPaint);
            }
            // Set the future tasks to shades of gray
            else{
                int k = j+2;
                int x = 210-((i-k)*70);
                taskSlicePaint[i].setColor(Color.argb(128, x, x, x));
                canvas.drawArc(rectF, endCurrentAngle -90, total_times[i+1]*6, true, taskSlicePaint[i]);
                endCurrentAngle = endCurrentAngle + total_times[i+1]*6;
            }
        }

        //canvas.drawArc(rectF, taskSliceStart[5] - 90, total_times[1]*6, true, taskSlicePaint[0]);
        //canvas.drawArc(rectF, taskSliceStart[0] - 90, total_times[2]*6, true, taskSlicePaint[1]);
        //canvas.drawArc(rectF, taskSliceStart[1] - 90, total_times[3]*6, true, taskSlicePaint[2]);
        //canvas.drawArc(rectF, taskSliceStart[2] - 90, total_times[4]*6, true, taskSlicePaint[3]);
        //canvas.drawArc(rectF, taskSliceStart[3] - 90, total_times[5]*6, true, taskSlicePaint[4]);

        // Draw an inner white circle
        canvas.drawCircle(centerX, centerY, radius - 120, whitePaint);


        int startX = 0;
        int startY = 0;
        // Draw minute markers and numbers
        for (int min = 0; min < 60; min++) {
            float angle = ((min * 360) / 60) - 90; // Calculate the angle for the current minute
            startX = (int) (centerX + (radius - 150) * Math.cos(Math.toRadians(angle)));
            startY = (int) (centerY + (radius - 150) * Math.sin(Math.toRadians(angle)));
            int endX = (int) (centerX + (radius - 170) * Math.cos(Math.toRadians(angle)));
            int endY = (int) (centerY + (radius - 170) * Math.sin(Math.toRadians(angle)));

            if (min % 5 == 0) {
                // Draw hour numbers at every 5-minute interval
                String number = String.valueOf(min / 5 == 0 ? 12 : min / 5);
                int Hour = min / 5;
                    if(Hour == calendar.get(Calendar.HOUR)){
                        textPaint.setColor(hourHandPaint.getColor());
                        textPaint.setAlpha(200);
                        //draw a little empty circle to circle the number
                        canvas.drawCircle(endX, endY, 50, textPaint);
                        textPaint.setColor(primaryPaint.getColor());
                        textPaint.setAlpha(255);
                        minuteMarkPaint.setStrokeWidth(15);
                    }
                    else{
                        minuteMarkPaint.setStrokeWidth(5);
                    }
                canvas.drawText(number, endX, endY + 20, textPaint);
            }
            // Draw minute marks for minutes
            startX = (int) (centerX + (radius - 20) * Math.cos(Math.toRadians(angle)));
            startY = (int) (centerY + (radius - 20) * Math.sin(Math.toRadians(angle)));
            endX = (int) (centerX + (radius - 50) * Math.cos(Math.toRadians(angle)));
            endY = (int) (centerY + (radius - 50) * Math.sin(Math.toRadians(angle)));
            if (min % 15 != 0 && min % 5 == 0) {
                endX = (int) (centerX + (radius - 80) * Math.cos(Math.toRadians(angle)));
                endY = (int) (centerY + (radius - 80) * Math.sin(Math.toRadians(angle)));
            }
            if (min == calendar.get(Calendar.MINUTE)) {
                // Highlight the actual minute's tick
                minuteMarkPaint.setColor(minuteHandPaint.getColor());
                textPaint.setColor(minuteMarkPaint.getColor());
                minuteMarkPaint.setStrokeWidth(15);
            }
            else{
                minuteMarkPaint.setColor(primaryPaint.getColor());
                minuteMarkPaint.setStrokeWidth(3);
                textPaint.setColor(primaryPaint.getColor());
            }
            if (min % 15 == 0) {
                // Draw minute numbers at every 15-minute interval
                String number = String.valueOf(min == 0 ? 0 : min);
                canvas.drawText(number, endX, endY + 20, textPaint);
                textPaint.setColor(Color.BLACK);
            }
            else{
                canvas.drawLine(startX, startY, endX, endY, minuteMarkPaint);
                textPaint.setColor(Color.BLACK);
            }
        }

        // Draw inner clock circle
        canvas.drawCircle(centerX, centerY, radius - 120, clockPaint);
        canvas.drawCircle(centerX, centerY, radius - 220, clockPaint);
        //String h = String.valueOf(calendar.get(Calendar.HOUR));
        //canvas.drawText( String.valueOf(taskStartingTime), centerX, centerY, textPaint);

        // Draw clock hands
        drawClockHand(canvas, centerX, centerY, hourAngle, radius -170, hourHandPaint, hour);
        drawClockHand(canvas, centerX, centerY, minuteAngle, radius -70, minuteHandPaint, minute);

        // Request a redraw after a delay
        postInvalidateDelayed(1000);
    }

    // Helper function to draw clock hands with numbers
    private void drawClockHand(Canvas canvas, int centerX, int centerY, float angle, float length, Paint paint, int number) {
        float x = (float) (centerX + (length - 40) * Math.sin(Math.toRadians(angle)));
        float y = (float) (centerY - (length - 40) * Math.cos(Math.toRadians(angle)));

        // Draw clock hand line
        canvas.drawLine(centerX, centerY, x, y, paint);

        // Draw number inside the clock hand
        Rect textBounds = new Rect();
        String numberText = String.valueOf(number);
        textPaint.getTextBounds(numberText, 0, numberText.length(), textBounds);
    }

    // New method to parse the input string into an array of arrays of int
    public int[][][] parseTimeString(String inputString) {
        int[][][] parsedArrays = new int[7][3][2]; // 7 arrays of 3 arrays of 2 int each

        if (!TextUtils.isEmpty(inputString)) {
            String[] timeArray = inputString.split(",");

            for (int i = 0; i < Math.min(7, timeArray.length); i++) {
                String timeChunk = timeArray[i];
                for (int j = 0; j < Math.min(3, timeChunk.length() / 2); j++) {
                    String digits = timeChunk.substring(j * 2, (j + 1) * 2);
                    parsedArrays[i][j][0] = Integer.parseInt(digits.substring(0, 1));
                    parsedArrays[i][j][1] = Integer.parseInt(digits.substring(1, 2));
                }
            }
        }

        return parsedArrays;
    }
}
