package com.ephraimhowardkunz.familymap.templetrip;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ephraimhowardkunz.familymap.templetrip.Model.DataManager;
import com.ephraimhowardkunz.familymap.templetrip.Model.RealmDictionaryObject;
import com.ephraimhowardkunz.familymap.templetrip.Model.Temple;

import io.realm.RealmList;

import static com.ephraimhowardkunz.familymap.templetrip.TempleDetailFragment.ARG_ITEM_ID;


/**
 * A simple {@link DialogFragment} subclass.
 */
public class ScheduleFragment extends DialogFragment {
    private Temple mItem;
    private Spinner timeSpinner;
    private Spinner daySpinner;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        mItem = DataManager.getTempleById(getActivity().getApplicationContext(), getArguments().getString(ARG_ITEM_ID));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_schedule, null);
        timeSpinner = (Spinner)view.findViewById(R.id.time_spinner);


        daySpinner = (Spinner)view.findViewById(R.id.day_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity().getApplicationContext(),
                R.array.day_array,
                R.layout.spinner_layout
        );
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        daySpinner.setAdapter(adapter);
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String timeArray[] = getTimeArrayForDay(
                        daySpinner.getItemAtPosition(position).toString(),
                        mItem.getEndowmentSchedule());
                ArrayAdapter<CharSequence> timeAdapter = new ArrayAdapter<CharSequence>(
                        getActivity().getApplicationContext(),
                        R.layout.spinner_layout,
                        timeArray);
                timeAdapter.setDropDownViewResource(R.layout.spinner_layout);
                timeSpinner.setAdapter(timeAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.create_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Create a calendar event and show a success toast
                    }
                })
                .setNegativeButton(R.string.cancel_button_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ScheduleFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private String[] getTimeArrayForDay(String day, RealmList<RealmDictionaryObject> dicts){
        for(int i = 0; i < dicts.size(); ++i){
            String key = dicts.get(i).getKey();
            String value = dicts.get(i).getValue();
            if(key.equals(day)){
                return value.split(",");
            }
        }
        return new String[0];
    }
}
