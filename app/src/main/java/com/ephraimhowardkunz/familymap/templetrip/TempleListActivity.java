package com.ephraimhowardkunz.familymap.templetrip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ephraimhowardkunz.familymap.templetrip.Model.DataManager;
import com.ephraimhowardkunz.familymap.templetrip.Model.Temple;
import com.ephraimhowardkunz.familymap.templetrip.View.SimpleDividerItemDecoration;

import java.util.List;

import io.realm.RealmChangeListener;

/**
 * An activity representing a list of Temples. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TempleDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class TempleListActivity extends AppCompatActivity {
    private final String TAG = "TempleListActivity";
    private RealmChangeListener realmListener;
    private RecyclerView recyclerView;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temple_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        DataManager.setUpParseAndRealm(getBaseContext());
        DataManager.importFromParseIntoRealm(getBaseContext());


        recyclerView = (RecyclerView)findViewById(R.id.temple_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.temple_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        realmListener = new RealmChangeListener() {
            @Override
            public void onChange() {
                setupRecyclerView(recyclerView);
            }
        };
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getBaseContext()));
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DataManager.getAllTemplesFromRealm(getBaseContext())));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Temple> mValues;

        public SimpleItemRecyclerViewAdapter(List<Temple> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.temple_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mNameView.setText(mValues.get(position).getName());
            holder.mDedicationView.setText(mValues.get(position).getDedication().toString());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(TempleDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        TempleDetailFragment fragment = new TempleDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.temple_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, TempleDetailActivity.class);
                        intent.putExtra(TempleDetailFragment.ARG_ITEM_ID, holder.mItem.getId());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mNameView;
            public final TextView mDedicationView;
            public Temple mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mNameView = (TextView) view.findViewById(R.id.temple_name);
                mDedicationView = (TextView) view.findViewById(R.id.temple_dedication_date);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }
        }
    }
}
