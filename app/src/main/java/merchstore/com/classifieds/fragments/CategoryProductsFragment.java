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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import merchstore.com.classifieds.Product;
import merchstore.com.classifieds.R;
import merchstore.com.classifieds.RecyclerItemClickListener;
import merchstore.com.classifieds.adapters.TimelineAdapter;


public class CategoryProductsFragment extends Fragment {


    FirebaseDatabase database;
    ArrayList<Product> products;
    TimelineAdapter adapter;
    ProgressBar progressBar;
    String category = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        database = FirebaseDatabase.getInstance();
        products = new ArrayList<>();
        adapter = new TimelineAdapter(getContext(),products);

        category = getArguments().getString("category");

        View view = inflater.inflate(R.layout.fragment_category_products, container, false);
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
                    public void onLongItemClick(View view, int position) {

                    }
                }));

        database.getReference().child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                products.clear();
                for (DataSnapshot sampleSnapshot: dataSnapshot.getChildren()) {
                    if(category.equals(sampleSnapshot.child("category").getValue(String.class)))
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
        name.setText(category);
        TextView price_tag = toolbar.findViewById(R.id.price_tag);
        price_tag.setVisibility(View.GONE);

    }

}
