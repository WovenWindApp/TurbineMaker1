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
        //set up the seek bar
        final SeekBar seek_bar = (SeekBar) view.findViewById(R.id.seek_bar);
        seek_bar.setMax(6);
        seek_bar.setProgress(3);
        //declare these final so they can be passed into the set listener function
        final TextView text_view = (TextView) view.findViewById(R.id.seek_bar_percent);
        final String part_name = name;
        final View view_final = view;

        //set the viewed text to the following:
        text_view.setText("Bar Progress : " + seek_bar.getProgress());
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            //when the progress changes, we change the size of the object
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(part_name.equals("Blades")) {
                    final ImageView blades = (ImageView) view_final.findViewById(R.id.part_imageView);
                    if(progress > 3) {
                        blades.getLayoutParams().height = 400 + 25*progress;
                        blades.getLayoutParams().width = 400 + 25*progress;
                    }
                    else if(progress == 3){
                        blades.getLayoutParams().height = 400;
                        blades.getLayoutParams().width = 400;
                    }
                    else if(progress < 3) {
                        blades.getLayoutParams().height = 400 - 25*(3 - progress);
                        blades.getLayoutParams().width = 400 - 25*(3 - progress);
                    }
                    blades.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_center));
                }
                text_view.setText(part_name + " size : " + (progress + 1));
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