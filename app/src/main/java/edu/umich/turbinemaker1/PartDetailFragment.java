package edu.umich.turbinemaker1;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

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

        // Set up blades
        final ImageView blades[] = {(ImageView) view.findViewById(R.id.blade0),
                (ImageView) view.findViewById(R.id.blade1),
                (ImageView) view.findViewById(R.id.blade2),
                (ImageView) view.findViewById(R.id.blade3),
                (ImageView) view.findViewById(R.id.blade4),
                (ImageView) view.findViewById(R.id.blade5),
                (ImageView) view.findViewById(R.id.blade6),
                (ImageView) view.findViewById(R.id.blade7),
                (ImageView) view.findViewById(R.id.blade8),
                (ImageView) view.findViewById(R.id.blade9),};
        final int numBlades = setUpBladeNumSlider(view, blades);

        // Set size control
        setUpBladeSizeSlider(view, "Blades", numBlades, blades);

        for (int i = 0; i < blades.length; ++i) {
            // Initialize size
            blades[i].getLayoutParams().width = 400;
            blades[i].getLayoutParams().height = 400;
        }

        // Respond to dropdown
        bladeTypeMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int bladeResource = R.drawable.airfoil_blade;

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Paddle")) {
                    // Set paddle image
                    bladeResource = R.drawable.paddles_test;
                } else if (parent.getItemAtPosition(position).equals("Airfoil")) {
                    // Set airfoils image
                    bladeResource = R.drawable.airfoil_blade;   // set blade type
                }

                for (int i = 0; i < blades.length; ++i) {
                    // Rotate blades correctly
                    blades[i].setImageResource(bladeResource);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

    }

    public int setUpBladeNumSlider(View view, final ImageView[] blades) {
        // Set up seekBar
        final SeekBar num_seekBar = (SeekBar) view.findViewById(R.id.blade_num_seekBar);
        num_seekBar.setMax(9);
        num_seekBar.setProgress(2);

        // Initialize rotation
        for (int i = 0; i < blades.length; ++i) {
            ObjectAnimator anim = ObjectAnimator.ofFloat(blades[i], "rotation",
                    (360/3) * i, (360/3) * (i + 1));
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(3600 / 3);
            anim.setRepeatCount(ObjectAnimator.INFINITE);
            anim.start();
        }

        // Initialize text
        final TextView num_textView = (TextView) view.findViewById(R.id.blade_num_textView);
        num_textView.setText("Number of blades: " + (num_seekBar.getProgress() + 1));

        num_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final int numBlades = progress + 1;     // for convenience
                for (int i = 0; i < blades.length; ++i) {
                    // Set blades visible
                    if (i <= progress) {
                        blades[i].setVisibility(ImageView.VISIBLE);
                    }
                    else {
                        blades[i].setVisibility(ImageView.INVISIBLE);
                    }

                    // Have to reset animation (rotation position is dependent on animation)
                    ObjectAnimator anim = ObjectAnimator.ofFloat(blades[i], "rotation",
                            (360/numBlades) * i, (360/numBlades) * (i + 1));
                    anim.setInterpolator(new LinearInterpolator());
                    anim.setDuration(3600 / numBlades);
                    anim.setRepeatCount(ObjectAnimator.INFINITE);
                    anim.start();
                }
                // Set text
                num_textView.setText("Number of blades: " + (num_seekBar.getProgress() + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        return num_seekBar.getProgress() + 1;
    }

    public void setUpBladeSizeSlider(View view, String name, final int numBlades, final ImageView[] blades) {
        // Set up the seek bar
        final SeekBar size_seekBar = (SeekBar) view.findViewById(R.id.part_size_seekBar);
        size_seekBar.setMax(99);
        size_seekBar.setProgress(50);

        // Declare these final so they can be passed into the set listener function
        final TextView size_textView = (TextView) view.findViewById(R.id.part_size_textView);
        final String part_name = name;

        // Set the viewed text to the following:
        size_textView.setText(part_name + " size: " + (size_seekBar.getProgress() + 1));
        size_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // When progress changes, change size of blades
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                for (int i = 0; i < blades.length; ++i) {
                    blades[i].getLayoutParams().height = 300 + (2 * progress);
                    blades[i].getLayoutParams().width = 300 + (2 * progress);
                }

                size_textView.setText(part_name + " size : " + (progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

}