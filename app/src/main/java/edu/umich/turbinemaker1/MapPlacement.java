package edu.umich.turbinemaker1;

import android.app.ActionBar;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MapPlacement extends AppCompatActivity {

    private static final String TURBINE_IMAGEVIEW_TAG = "draggable turbine";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_placement);

        // Force landscape mode, remove action bar
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // Turbine ImageView
        ImageView turbine = (ImageView) findViewById(R.id.dragDemo_imageView);
        turbine.setTag(TURBINE_IMAGEVIEW_TAG);
        turbine.setOnTouchListener(startDragOnTouch);



        // Map Layout
        FrameLayout map = (FrameLayout) findViewById(R.id.map_frameLayout);
        map.setOnDragListener(new MapDragEventListener());

    }



    // LISTENERS - -  - -  - -  - -  - -  - -  - -  - -  - -  - -  - -  - -  - -  - -  - -  - -
    // On Touch Listener to start drag
    private View.OnTouchListener startDragOnTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            // Create drag shadow
            View.DragShadowBuilder shadowBuilder = new TurbineDragShadowBuilder(v);

            // Start drag
            v.startDrag(null, shadowBuilder, v, 0);
            v.setVisibility(View.INVISIBLE);

            return false;
        }
    };

    // Turbine drag shadow builder (image that shows when you drag)
    private static class TurbineDragShadowBuilder extends View.DragShadowBuilder {
        // Drag shadow drawable
        private View shadowView;

        public TurbineDragShadowBuilder(View v) {
            super(v);
            shadowView = v;
        }

        // Callback that sends drag shadow dimensions and touch point back to system
        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            int width, height;
;
            width = shadowView.getWidth();
            height = shadowView.getHeight();

            shadowSize.set(width, height);
            shadowTouchPoint.set(width / 2, height / 2);

        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            shadowView.draw(canvas);
        }
    }

    protected class MapDragEventListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {

            // Drag event
            final int action = event.getAction();

            // Dragged object
            ImageView draggedImage = (ImageView) event.getLocalState();

            // Handle different drag events
            switch (action) {

                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setAlpha(0.7f);
                    v.invalidate();
                    return true;

                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;

                case DragEvent.ACTION_DRAG_EXITED:
                    v.setAlpha(1.0f);
                    v.invalidate();
                    return true;

                case DragEvent.ACTION_DROP:
                    int draggedW = draggedImage.getWidth();
                    int draggedH = draggedImage.getHeight();

                    // Create new object, set w and h
                    ImageView genImage = new ImageView(getApplicationContext());
                    genImage.setLayoutParams(new FrameLayout.LayoutParams(draggedImage.getWidth(),
                            draggedImage.getHeight()));

                    // Copy image and pos from draggedImage
                    genImage.setImageDrawable(draggedImage.getDrawable());
                    genImage.setX(event.getX() - draggedW / 2);
                    genImage.setY(event.getY() - draggedH / 2);

                    ((ViewGroup) v).addView(genImage);
                    genImage.setOnTouchListener(startDragOnTouch);


                    // Hide old object
                    draggedImage.setVisibility(View.GONE);


                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    // Reset highlighting
                    v.setAlpha(1f);
                    v.invalidate();

                    boolean inMap = event.getResult();
                    if (!inMap) {
                        // Restore visibility to dragged object
                        draggedImage.setVisibility(View.VISIBLE);
                    }

                    return true;

                default:
                    Toast.makeText(getApplicationContext(), "missed case", Toast.LENGTH_SHORT).show();
                    break;
            }

            return false;
        }
    }

}

