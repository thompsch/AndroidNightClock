package com.ca13b.nightclock;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    private MainActivity mainActivity;
    private Float alpha;

    public TouchListener(Context context, MainActivity main) {
        gestureDetector = new GestureDetector(context, new GestureListener());
        this.mainActivity = main;
        this.alpha = main.findViewById(R.id.time).getAlpha();
    }

    public void onSwipeUp() {
        this.alpha = Math.min(this.alpha + 0.2f, 1.0f);
        Log.d("up", this.alpha.toString());
        changeAlpha();
    }

    public void onSwipeDown() {
        this.alpha = Math.max(this.alpha - 0.2f, 0.1f);
        Log.d("down", this.alpha.toString());
        changeAlpha();
    }

    private void changeAlpha(){
        mainActivity.findViewById(R.id.time).setAlpha(this.alpha);
        mainActivity.findViewById(R.id.btnAlarm).setAlpha(this.alpha);
    }

    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        //private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceY) > Math.abs(distanceX) && Math.abs(distanceY) > SWIPE_DISTANCE_THRESHOLD) {
                if (distanceY < 0)
                    onSwipeUp();
                else
                    onSwipeDown();
                return true;
            }
            return false;
        }
    }
}
