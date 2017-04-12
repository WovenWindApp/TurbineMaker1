package edu.umich.turbinemaker1;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class TurbineDesign extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turbine_design);

        View v = findViewById(android.R.id.content);


        // Force landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setUpStructure();




    }


    void setUpStructure() {
        ImageView structure = (ImageView) findViewById(R.id.turbine_design_structure);

        setUpStructureWidthSlider(structure);


    }


    void setUpStructureWidthSlider(ImageView structure) {
        


    }
}
