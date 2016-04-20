package com.ephraimhowardkunz.familymap.templetrip;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ephraimhowardkunz.familymap.templetrip.Model.DataManager;
import com.ephraimhowardkunz.familymap.templetrip.Model.Temple;

/**
 * A fragment representing a single Temple detail screen.
 * This fragment is either contained in a {@link TempleListActivity}
 * in two-pane mode (on tablets) or a {@link TempleDetailActivity}
 * on handsets.
 */
public class TempleDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Temple mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TempleDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DataManager.getTempleById(getContext(), getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            activity.setTitle(mItem.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.temple_detail, container, false);

        setupViewElements(rootView);

        return rootView;
    }

    public void setupViewElements(View rootView){
        ImageView imageView = (ImageView)rootView.findViewById(R.id.temple_detail_image);
        DataManager.getTempleImage(getContext(), imageView, mItem);

        Button mapButton = (Button)rootView.findViewById(R.id.temple_detail_map_button);
        mapButton.setText(mItem.getAddress());
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("google.navigation:q=" + mItem.getAddress());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        Button webButton = (Button)rootView.findViewById(R.id.temple_detail_web_button);
        webButton.setText(mItem.getWebViewUrl());
        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Uri uri = Uri.parse(mItem.getWebViewUrl());
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(i);
                }
                catch (Exception ex){
                    assert false;
                    ex.printStackTrace();
                }
            }
        });
    }
}
