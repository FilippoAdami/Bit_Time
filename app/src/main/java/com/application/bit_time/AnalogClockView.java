package com.application.bit_time;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import java.util.Calendar;

public class AnalogClockView extends View {

    // Paint objects for drawing various elements
    private Paint clockPaint;          // Paint for the outer clock circle
    private Paint hourHandPaint;       // Paint for the hour hand
    private Paint minuteHandPaint;     // Paint for the minute hand
    private Paint textPaint;           // Paint for drawing text (numbers)
    private Paint minuteMarkPaint;     // Paint for drawing minute marks

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
        clockPaint = new Paint();
        clockPaint.setColor(Color.BLACK);
        clockPaint.setStyle(Paint.Style.STROKE);
        clockPaint.setStrokeWidth(5);

        hourHandPaint = new Paint();
        hourHandPaint.setColor(Color.BLUE);
        hourHandPaint.setStyle(Paint.Style.STROKE);
        hourHandPaint.setStrokeWidth(20);

        minuteHandPaint = new Paint();
        minuteHandPaint.setColor(Color.RED);
        minuteHandPaint.setStyle(Paint.Style.STROKE);
        minuteHandPaint.setStrokeWidth(10);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(60);
        textPaint.setTextAlign(Paint.Align.CENTER);

        minuteMarkPaint = new Paint();
        minuteMarkPaint.setColor(Color.BLACK);
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

        // Draw outer clock circle
        canvas.drawCircle(centerX, centerY, radius, clockPaint);

        // Draw minute markers and numbers
        for (int minute = 0; minute < 60; minute++) {
            float angle = ((minute * 360) / 60) - 90; // Calculate the angle for the current minute
            int startX = (int) (centerX + (radius - 20) * Math.cos(Math.toRadians(angle)));
            int startY = (int) (centerY + (radius - 20) * Math.sin(Math.toRadians(angle)));
            int endX = (int) (centerX + (radius - 40) * Math.cos(Math.toRadians(angle)));
            int endY = (int) (centerY + (radius - 40) * Math.sin(Math.toRadians(angle)));

            if (minute % 5 == 0) {
                // Draw hour numbers at every 5-minute interval
                String number = String.valueOf(minute / 5 == 0 ? 12 : minute / 5);
                canvas.drawText(number, endX, endY + 20, textPaint);
            }
            // Draw minute marks for minutes
            startX = (int) (centerX + (radius - 120) * Math.cos(Math.toRadians(angle)));
            startY = (int) (centerY + (radius - 120) * Math.sin(Math.toRadians(angle)));
            endX = (int) (centerX + (radius - 140) * Math.cos(Math.toRadians(angle)));
            endY = (int) (centerY + (radius - 140) * Math.sin(Math.toRadians(angle)));
            if (minute % 15 == 0) {
                // Draw hour numbers at every 5-minute interval
                String number = String.valueOf(minute == 0 ? 0 : minute);
                canvas.drawText(number, endX, endY + 20, textPaint);
            }
            else{
            canvas.drawLine(startX, startY, endX, endY, minuteMarkPaint);
            }

        }

        // Draw inner clock circle
        canvas.drawCircle(centerX, centerY, radius - 100, clockPaint);

        // Get current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        // Calculate angles for hour and minute hands
        float hourAngle = (hour % 12 + minute / 60.0f - 3) * 360 / 12 + 90;
        float minuteAngle = (minute) * 360 / 60;

        // Draw clock hands
        drawClockHand(canvas, centerX, centerY, hourAngle, radius * 0.5f, hourHandPaint, hour);
        drawClockHand(canvas, centerX, centerY, minuteAngle, radius * 0.7f, minuteHandPaint, minute);

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
        float textX = x - textBounds.width() / 2;
        float textY = y + textBounds.height() / 2;
        canvas.drawText(numberText, textX, textY, textPaint);
    }
}
