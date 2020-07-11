package merchstore.com.classifieds.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import merchstore.com.classifieds.Categories;
import merchstore.com.classifieds.Product;
import merchstore.com.classifieds.R;
import merchstore.com.classifieds.RecyclerItemClickListener;
import merchstore.com.classifieds.Review;
import merchstore.com.classifieds.adapters.CategoriesAdapter;
import merchstore.com.classifieds.adapters.GridProductAdapter;
import merchstore.com.classifieds.adapters.GridSpacingItemDecoration;
import merchstore.com.classifieds.adapters.LatestAdapter;


public class CategoriesFragment extends Fragment {


    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    SliderLayout sliderLayout;
    ArrayList<Categories> categories;
    ArrayList<String> latest;
    ArrayList<Product> products;
    FirebaseDatabase database;
    CategoriesAdapter adapter;
    LatestAdapter latestAdapter;
    GridProductAdapter productAdapter;
    TextView latestTag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_categories, container, false);

        database = FirebaseDatabase.getInstance();
        categories = new ArrayList<>();
        products = new ArrayList<>();
        latest = new ArrayList<>();
        latestTag = view.findViewById(R.id.latestTag);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(),"font/robotobold.ttf");
        latestTag.setTypeface(typeface);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView latestRecyclerView = view.findViewById(R.id.latestRecyclerView);
        RecyclerView productsRecyclerView = view.findViewById(R.id.productsRecyclerView);
        productsRecyclerView.addOnItemTouchListener(
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

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putString("category",categories.get(position).getCategory());
                        CategoryProductsFragment categoryProductsFragment = new CategoryProductsFragment();
                        categoryProductsFragment.setArguments(bundle);
                        transaction.replace(R.id.content,categoryProductsFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));


                        recyclerView.setNestedScrollingEnabled(false);
        latestRecyclerView.setNestedScrollingEnabled(false);
        productsRecyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext());
        horizontalLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        latestRecyclerView.setLayoutManager(horizontalLayoutManager);

        productsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 20, false));
        productsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));



        productAdapter = new GridProductAdapter(getContext(),products);
        latestAdapter = new LatestAdapter(getContext(),latest);
        adapter = new CategoriesAdapter(getContext(),categories);
        latestRecyclerView.setAdapter(latestAdapter);
        recyclerView.setAdapter(adapter);
        productsRecyclerView.setAdapter(productAdapter);

        database.getReference().child("Categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categories.clear();
                for (DataSnapshot sampleSnapshot: dataSnapshot.getChildren()) {
                    categories.add(new Categories(sampleSnapshot.getKey(),sampleSnapshot.getValue(String.class)));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        database.getReference().child("Latest").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                latest.clear();
                for (DataSnapshot sampleSnapshot: dataSnapshot.getChildren()) {
                    latest.add(sampleSnapshot.getValue(String.class));
                }
                latestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        database.getReference().child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                products.clear();
                for (DataSnapshot sampleSnapshot: dataSnapshot.getChildren()) {
                    products.add(new Product(sampleSnapshot.child("title").getValue(String.class),sampleSnapshot.child("description").getValue(String.class)
                    ,sampleSnapshot.child("author").getValue(String.class),sampleSnapshot.child("category").getValue(String.class),
                            sampleSnapshot.child("image").getValue(String.class),sampleSnapshot.child("price").getValue(Integer.class),
                            sampleSnapshot.child("author_name").getValue(String.class),sampleSnapshot.getKey()));
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        /*
        database.getReference().child("Products").push().setValue(new Product(
                "Q Infinity E - 5.5\" - 2GB RAM",
                "Product details of Q Infinity E - 5.5\" - 2GB RAM - 16GB ROM - Black",
                "CoLrkxSr75O4gJOG52nVyHhU4NK2",
                "Electronics",
                "https://firebasestorage.googleapis.com/v0/b/merchstore-67498.appspot.com/o/9938266b4ea525c3ca2eb705a136875e.jpg?alt=media&token=d7b2a198-3647-46d5-a56e-ff547641b2cf",
                550,
                "Mian Asad Ali"));

        */

        //database.getReference().child("Reviews").child("-LVepEzXCK1RcW3GYfpb").push().setValue(new Review("-LVepEzXCK1RcW3GYfpb","Mian Asad Ali","Awesome Product","9 January 2019 6:34PM"));



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView name = toolbar.findViewById(R.id.name);
        name.setText("Categories");
        TextView price_tag = toolbar.findViewById(R.id.price_tag);
        price_tag.setVisibility(View.GONE);

    }
}
