package merchstore.com.classifieds.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import merchstore.com.classifieds.Product;
import merchstore.com.classifieds.R;
import merchstore.com.classifieds.RecyclerItemClickListener;
import merchstore.com.classifieds.adapters.TimelineAdapter;


public class TimelineFragment extends Fragment {

    public TimelineFragment() {
        // Required empty public constructor
    }

    FirebaseDatabase database;
    ArrayList<Product> products;
    TimelineAdapter adapter;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        database = FirebaseDatabase.getInstance();
        products = new ArrayList<>();
        adapter = new TimelineAdapter(getContext(),products);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);



        database.getReference().child("User").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> types = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.child("interests").getChildren()) {
                    types.add(snapshot.getValue(String.class));
                }

                database.getReference().child("Products").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        products.clear();
                        for (DataSnapshot sampleSnapshot: dataSnapshot.getChildren()) {
                            if(types.contains(sampleSnapshot.child("category").getValue(String.class)))
                            products.add(new Product(sampleSnapshot.child("title").getValue(String.class),sampleSnapshot.child("description").getValue(String.class)
                                    ,sampleSnapshot.child("author").getValue(String.class),sampleSnapshot.child("category").getValue(String.class),
                                    sampleSnapshot.child("image").getValue(String.class),sampleSnapshot.child("price").getValue(Integer.class),
                                    sampleSnapshot.child("author_name").getValue(String.class),sampleSnapshot.getKey()));
                        }


                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView name = toolbar.findViewById(R.id.name);
        name.setText("Timeline");
        TextView price_tag = toolbar.findViewById(R.id.price_tag);
        price_tag.setVisibility(View.GONE);

    }

}
