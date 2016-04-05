package edu.umich.turbinemaker1;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;

import edu.umich.turbinemaker1.parts.PartsContent;


/**
 * A fragment representing a single Part detail screen.
 * This fragment is either contained in a {@link PartListActivity}
 * in two-pane mode (on tablets) or a {@link PartDetailActivity}
 * on handsets.
 */
public class PartDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private PartsContent.Part mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PartDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force landscape mode, remove action bar
        ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = PartsContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.content);
            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.part_detail, container, false);

        // Stops the spinner from spreading its goddamn cancer ebola
        ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if (mItem != null) {
            // Show corresponding details text
            ((TextView) rootView.findViewById(R.id.part_detail)).setText(mItem.details);
            if(mItem.id.equals("Blades") || mItem.id.equals("Structure")){
                setUpSlider(rootView, mItem.id);
            }
            // Set up blades page
            if (mItem.id.equals("Blades")) {
                setBladesImage(rootView);
            }
        }


        return rootView;
    }

    public void setBladesImage(View view) {
        // Set up spinner
        Spinner bladeTypeMenu = (Spinner) view.findViewById(R.id.part_spinner);
        ArrayAdapter<CharSequence> bladeTypeAdapter =
                ArrayAdapter.createFromResource(getContext(), R.array.blade_type_array,
                        android.R.layout.simple_spinner_item);
        bladeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bladeTypeMenu.setAdapter(bladeTypeAdapter);

        final ImageView blades = (ImageView) view.findViewById(R.id.part_imageView);
        blades.getLayoutParams().height = 400;
        blades.getLayoutParams().width = 400;
        blades.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_center));

        // Respond to dropdown
        bladeTypeMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Paddle")) {
                    // Set paddle image
                    blades.setImageResource(R.drawable.paddles_test);
                } else if (parent.getItemAtPosition(position).equals("Airfoil")) {
                    // Set airfoils image
                    blades.setImageResource(R.drawable.airfoil_blades);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

    }

    public void setUpSlider(View view, String name) {
        // Set up the seek bar
        final SeekBar seekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        seekBar.setMax(99);
        seekBar.setProgress(50);

        // Declare these final so they can be passed into the set listener function
        final TextView seekBar_text = (TextView) view.findViewById(R.id.seek_bar_percent);
        final String part_name = name;
        final View view_final = view;

        // Set the viewed text to the following:
        seekBar_text.setText("Bar Progress : " + seekBar.getProgress());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // ImageView handle
            final ImageView part_imageView = (ImageView) view_final.findViewById(R.id.part_imageView);

            // When progress changes, change size of blades
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                part_imageView.getLayoutParams().height = 300 + (2 * progress);
                part_imageView.getLayoutParams().width = 300 + (2 * progress);
                part_imageView.getAnimation().reset();


                seekBar_text.setText(part_name + " size : " + (progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    public void onNothingSelected(AdapterView<?> parent) {
        // Nothing yet
    }

}