package edu.umich.turbinemaker1;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.Image;
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

        // Stops the spinner from spreading its horrible non-fullscreen layout
        ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


//        Toast.makeText(getContext(), "onCreateView called", Toast.LENGTH_SHORT).show();

        SharedPreferences userData = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor userDataEditor = userData.edit();


        if (mItem != null) {
            // Show corresponding details text
            ((TextView) rootView.findViewById(R.id.part_detail)).setText(mItem.details);

            switch (mItem.id) {
                case "Blades": setUpBlades(rootView);
                    break;
                case "Structure":   // TODO
                    break;
                case "Output":  // TODO
                    break;
                case "Location": userDataEditor.clear().apply();    // TODO make this a real section
            }
        }


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getContext(), "onResume called", Toast.LENGTH_SHORT).show();
    }

    public void onPause() {
        super.onPause();
//        Toast.makeText(getContext(), "onPause called", Toast.LENGTH_SHORT).show();
    }

    public void setUpBlades(View view) {
        ImageView[] blades = getBladeImageViewHandles(view);
        setUpBladeNumSlider(view, blades);
        setUpBladeSizeSlider(view, blades);
        setUpBladeTypeSpinner(view, blades);
    }

    public int setUpBladeNumSlider(View view, final ImageView[] blades) {
        // Load saved blade number, or default 3
        final SharedPreferences userData = getActivity().getPreferences(Context.MODE_PRIVATE);
        int savedNumBlades = userData.getInt(getString(R.string.num_blades_key), 3);

        // Set up seekBar
        final SeekBar num_seekBar = (SeekBar) view.findViewById(R.id.blade_num_seekBar);
        num_seekBar.setMax(9);
        num_seekBar.setProgress(savedNumBlades - 1);

        // Initialize blades
        for (int i = 0; i < blades.length; ++i) {
            // Set correct num blades visible
            if (i < savedNumBlades) {
                blades[i].setVisibility(ImageView.VISIBLE);
            } else {
                blades[i].setVisibility(ImageView.INVISIBLE);
            }

            // Animate
            ObjectAnimator anim = ObjectAnimator.ofFloat(blades[i], "rotation",
                    (360 / savedNumBlades) * i, (360 / savedNumBlades) * (i + 1));
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration((3600 + savedNumBlades * 100) / savedNumBlades); // scales to go slightly slower with more blades
            anim.setRepeatCount(ObjectAnimator.INFINITE);
            anim.start();
        }

        // Initialize text
        final TextView num_textView = (TextView) view.findViewById(R.id.blade_num_textView);
        num_textView.setText("Number of blades: " + (savedNumBlades));

        num_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final int numBlades = progress + 1;     // for convenience

                // Save num blades data
                userData.edit().putInt(getString(R.string.num_blades_key), numBlades).apply();

                for (int i = 0; i < blades.length; ++i) {
                    // Set correct num blades visible
                    if (i < numBlades) {
                        blades[i].setVisibility(ImageView.VISIBLE);
                    } else {
                        blades[i].setVisibility(ImageView.INVISIBLE);
                    }

                    // Have to reset animation (rotation position is dependent on animation)
                    ObjectAnimator anim = ObjectAnimator.ofFloat(blades[i], "rotation",
                            (360 / numBlades) * i, (360 / numBlades) * (i + 1));
                    anim.setInterpolator(new LinearInterpolator());
                    anim.setDuration((3600 + numBlades * 100) / numBlades); // scales to go slightly slower with more blades
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

    public void setUpBladeSizeSlider(View view, final ImageView[] blades) {
        String name = mItem.id;

        // Get saved size or default 397 (50 progress)
        final SharedPreferences userData = getActivity().getPreferences(Context.MODE_PRIVATE);
        int savedSize = userData.getInt(getString(R.string.blade_size_key), 397);

        for (ImageView blade : blades) {
            // Initialize size
            blade.getLayoutParams().width = savedSize;
            blade.getLayoutParams().height = savedSize;
        }

        // Set up the seek bar
        final SeekBar size_seekBar = (SeekBar) view.findViewById(R.id.part_size_seekBar);
        size_seekBar.setMax(99);
        size_seekBar.setProgress((savedSize - 250) / 3);

        // Declare these final so they can be passed into the set listener function
        final TextView size_textView = (TextView) view.findViewById(R.id.part_size_textView);
        final String part_name = name;

        // Set the viewed text to the following:
        size_textView.setText(part_name + " size: " + (size_seekBar.getProgress() + 1));
        size_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // When progress changes, change size of blades
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int bladeSize = 250 + (3 * progress);
                for (ImageView blade : blades) {
                    blade.getLayoutParams().height = bladeSize;
                    blade.getLayoutParams().width = bladeSize;
                }
                size_textView.setText(part_name + " size : " + (progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                userData.edit().putInt(getString(R.string.blade_size_key),
                        250 + (3 * size_seekBar.getProgress())).apply();
            }
        });

    }

    public void setUpBladeTypeSpinner(View view, final ImageView[] blades) {
        final String[] bladeTypeArray = getResources().getStringArray(R.array.blade_type_array);

        // Populate spinner with array in strings rsc
        Spinner bladeTypeMenu = (Spinner) view.findViewById(R.id.part_spinner);
        ArrayAdapter<CharSequence> bladeTypeAdapter =
                ArrayAdapter.createFromResource(getContext(), R.array.blade_type_array,
                        android.R.layout.simple_spinner_item);
        bladeTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bladeTypeMenu.setAdapter(bladeTypeAdapter);

        // Load saved blade type or default: paddle blades
        final SharedPreferences userData = getActivity().getPreferences(Context.MODE_PRIVATE);
        String savedBladeType = userData.getString(getString(R.string.blade_type_key), bladeTypeArray[0]);
        bladeTypeMenu.setSelection(bladeTypeAdapter.getPosition(savedBladeType));

        bladeTypeMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int bladeResource;

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Select correct blade type
                int select = 0;
                if (parent.getItemAtPosition(position).equals(bladeTypeArray[0])) {
                    select = 0;
                    bladeResource = R.drawable.paddle_blade;
                } else if (parent.getItemAtPosition(position).equals(bladeTypeArray[1])) {
                    select = 1;
                    bladeResource = R.drawable.airfoil_blade;
                }
                userData.edit().putString(getString(R.string.blade_type_key), bladeTypeArray[select]).apply();

                for (ImageView blade : blades) {
                    blade.setImageResource(bladeResource);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public ImageView[] getBladeImageViewHandles(View view) {
         ImageView blades[] = {(ImageView) view.findViewById(R.id.blade0),
                                (ImageView) view.findViewById(R.id.blade1),
                                (ImageView) view.findViewById(R.id.blade2),
                                (ImageView) view.findViewById(R.id.blade3),
                                (ImageView) view.findViewById(R.id.blade4),
                                (ImageView) view.findViewById(R.id.blade5),
                                (ImageView) view.findViewById(R.id.blade6),
                                (ImageView) view.findViewById(R.id.blade7),
                                (ImageView) view.findViewById(R.id.blade8),
                                (ImageView) view.findViewById(R.id.blade9)};
        return blades;
    }

}