package merchstore.com.classifieds.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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

import merchstore.com.classifieds.Product;
import merchstore.com.classifieds.R;
import merchstore.com.classifieds.RecyclerItemClickListener;
import merchstore.com.classifieds.adapters.TimelineAdapter;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putString("detail",products.get(position).getDescription());
                        bundle.putString("title",products.get(position).getTitle());
                        bundle.putString("product_id",products.get(position).getId());
                        bundle.putString("image",products.get(position).getImage());
                        bundle.putString("author",products.get(position).getAuthor_name());
                        bundle.putString("authorid",products.get(position).getAuthor());
                        bundle.putString("price",products.get(position).getPrice() + "");
                        ProductDetailFragment productDetailFragment = new ProductDetailFragment();
                        productDetailFragment.setArguments(bundle);
                        transaction.replace(R.id.content,productDetailFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    @Override
                    public void onLongItemClick(View view, final int position) {
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(getContext());
                        }
                        builder.setTitle("Delete Product")
                                .setMessage("Are you sure you want to delete this product?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        database.getReference().child("Products").child(products.get(position).getId()).removeValue();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }));

        database.getReference().child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                products.clear();
                for (DataSnapshot sampleSnapshot: dataSnapshot.getChildren()) {
                    if(FirebaseAuth.getInstance().getUid().equals(sampleSnapshot.child("author").getValue(String.class)))
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


        return view;
    }

    

    @Override
    public void onResume() {
        super.onResume();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView name = toolbar.findViewById(R.id.name);
        name.setText("My Products");
        TextView price_tag = toolbar.findViewById(R.id.price_tag);
        price_tag.setVisibility(View.GONE);

    }


}
