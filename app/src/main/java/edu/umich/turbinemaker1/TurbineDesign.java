package edu.umich.turbinemaker1;

import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import java.lang.reflect.Array;
import java.util.Arrays;

public class TurbineDesign extends AppCompatActivity {

    ImageView structureImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turbine_design);

        View v = findViewById(android.R.id.content);


        // Force landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setUpStructure();

        setUpBlades();

    }

    protected void onStart() {
        super.onStart();

        // Set starting values for sliders

    }

    void setUpStructure() {
        structureImageView = (ImageView) findViewById(R.id.turbine_design_structure);

        setUpStructureWidthSlider(structureImageView);
        setUpStructureHeightSlider(structureImageView);


    }

    void setUpStructureHeightSlider(final ImageView structure) {
        final int max_height_weight = 50;
        final int min_height_weight = 10;


        final int structure_coords[] = {0, 0};

        final int slider_max = 100;
        SeekBar height_slider = (SeekBar) findViewById(R.id.turbine_height_seekbar);
        height_slider.setMax(slider_max);

        final FrameLayout height_param_layout = (FrameLayout) findViewById(R.id.turbine_height_parameter);

        // Configure listener
        height_slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                // Calculate height scaling
                float variable_height = (max_height_weight - min_height_weight) * ( (float) progress /  (float) slider_max);
                int height_width = min_height_weight + (int) variable_height;
                height_param_layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, height_width));

                // Get structure coords
                structure.getLocationOnScreen(structure_coords);
                System.out.println(Arrays.toString(structure_coords));


                // Change blade view coords
                ImageView blades_placement = (ImageView) findViewById(R.id.blades_placement);

                int structure_h = blades_placement.getHeight();

                int structure_top_margin = structure_coords[1] - structure_h / 2;

                RelativeLayout.LayoutParams blades_params = (RelativeLayout.LayoutParams) blades_placement.getLayoutParams();
                blades_params.setMargins(0, structure_top_margin, 0, 0);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        height_slider.setProgress(0);
    }

    void setUpStructureWidthSlider(ImageView structure) {
        final int max_width_weight = 50;
        final int min_width_weight = 5;

        final int slider_max = 100;
        SeekBar width_slider = (SeekBar) findViewById(R.id.turbine_width_seekbar);
        width_slider.setMax(slider_max);

        final LinearLayout height_param_layout = (LinearLayout) findViewById(R.id.turbine_width_parameter);

        // Configure listener
        width_slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                // Calculate width scaling
                float variable_width = (max_width_weight - min_width_weight) * ( (float) progress /  (float) slider_max);
                int width_weight = min_width_weight + (int) variable_width;
                height_param_layout.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, width_weight));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    void setUpBlades() {
        // Create blade handle



    }
}
