package merchstore.com.classifieds.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import br.com.felix.imagezoom.ImageZoom;
import merchstore.com.classifieds.MainActivity;
import merchstore.com.classifieds.R;
import merchstore.com.classifieds.Review;


public class ProductDetailFragment extends Fragment {

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    ChipCloud chipCloud;
    LinearLayout buttonsLayout;
    Button shareBtn, phoneBtn;
    FirebaseDatabase database;
    String author_phone = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        buttonsLayout = view.findViewById(R.id.buttonsLayout);
        shareBtn = view.findViewById(R.id.shareBtn);
        phoneBtn = view.findViewById(R.id.callBtn);
        database = FirebaseDatabase.getInstance();

        final Bundle product_bundle = getArguments();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView name = toolbar.findViewById(R.id.name);
        name.setText(product_bundle.getString("title"));
        TextView price_tag = toolbar.findViewById(R.id.price_tag);
        price_tag.setVisibility(View.VISIBLE);
        price_tag.setText("RS." + product_bundle.getString("price") +"/-");

        //((MainActivity)getActivity()).getSupportActionBar().setTitle(product_bundle.getString("title"));

        ImageZoom image = view.findViewById(R.id.image);
        Picasso.get().load(product_bundle.getString("image")).into(image);
        chipCloud = (ChipCloud) view.findViewById(R.id.chip_cloud);
        new ChipCloud.Configure()
                .chipCloud(chipCloud)
                .selectedColor(Color.parseColor("#ff6969"))
                .selectedFontColor(Color.parseColor("#ffffff"))
                .deselectedColor(Color.parseColor("#e1e1e1"))
                .deselectedFontColor(Color.parseColor("#333333"))
                .selectTransitionMS(500)
                .deselectTransitionMS(250)
                .labels(new String[] {"Detail", "Reviews"})
                .mode(ChipCloud.Mode.SINGLE)
                .allCaps(false)
                .gravity(ChipCloud.Gravity.CENTER)
                .textSize(getResources().getDimensionPixelSize(R.dimen.default_textsize))
                .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.vertical_spacing))
                .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.min_horizontal_spacing))
                .chipListener(new ChipListener() {
                    @Override
                    public void chipSelected(int index) {
                        if(index==0) {
                            buttonsLayout.setVisibility(View.VISIBLE);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString("detail",product_bundle.getString("detail"));
                            ProductDetailTextFragment textFragment = new ProductDetailTextFragment();
                            textFragment.setArguments(bundle);
                            transaction.replace(R.id.sub_frame,textFragment);
                            transaction.commit();
                        } else {
                            buttonsLayout.setVisibility(View.GONE);
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            Bundle bundle = new Bundle();
                            bundle.putString("product_id",product_bundle.getString("product_id"));
                            ProductReviewsFragment reviewsFragment = new ProductReviewsFragment();
                            reviewsFragment.setArguments(bundle);
                            transaction.replace(R.id.sub_frame,reviewsFragment);
                            transaction.commit();
                        }
                    }
                    @Override
                    public void chipDeselected(int index) {

                    }
                })
                .build();

        chipCloud.setSelectedChip(0);

        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(author_phone.equals("") || author_phone == "")
                    Toast.makeText(getContext(), "Fetching author phone...", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + author_phone));
                    startActivity(intent);
                }
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out " + product_bundle.getString("title") + " in only Rs." + product_bundle.getString("price") + " by " + product_bundle.getString("author") + "\n Contact: " + product_bundle.getString("phone") + " \n\n ~ MerchStore");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        database.getReference().child("User").child(product_bundle.getString("authorid")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                author_phone = dataSnapshot.child("phone").getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });

        return view;
    }



}
