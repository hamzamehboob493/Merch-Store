package merchstore.com.classifieds.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import merchstore.com.classifieds.Product;
import merchstore.com.classifieds.R;
import merchstore.com.classifieds.Review;
import merchstore.com.classifieds.adapters.ReviewsAdapter;


public class ProductReviewsFragment extends Fragment {

    public ProductReviewsFragment() {
        // Required empty public constructor
    }

    FirebaseDatabase database;
    ArrayList<Review> reviews;
    ReviewsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        database = FirebaseDatabase.getInstance();
        reviews = new ArrayList<>();
        adapter = new ReviewsAdapter(getContext(),reviews);
        Bundle bundle = getArguments();
        final String product_id = bundle.getString("product_id");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_reviews, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        final EditText text = view.findViewById(R.id.commentBox);
        ImageButton sendBtn = view.findViewById(R.id.sendBtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("Reviews").child(product_id).push().setValue(new Review(product_id,FirebaseAuth.getInstance().getUid(),text.getText().toString(),getDateTime()));
                text.setEnabled(false);
                text.setText("Publishing comment ...");
            }
        });


        database.getReference().child("Reviews").child(product_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                text.setText("");
                text.setEnabled(true);
                reviews.clear();
                for (DataSnapshot sampleSnapshot: dataSnapshot.getChildren()) {

                    Review review = new Review(sampleSnapshot.child("product_id").getValue(String.class),sampleSnapshot.child("author_id").getValue(String.class),
                            sampleSnapshot.child("comment").getValue(String.class),sampleSnapshot.child("date").getValue(String.class));


                    reviews.add(review);


                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });
        return view;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}
