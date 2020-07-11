package merchstore.com.classifieds.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import merchstore.com.classifieds.MainActivity;
import merchstore.com.classifieds.Product;
import merchstore.com.classifieds.R;
import merchstore.com.classifieds.Review;
import merchstore.com.classifieds.fragments.ProductDetailFragment;

public class TimelineAdapter extends  RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder> {

    Context context;
    ArrayList<Product> products;
    FirebaseDatabase firebaseDatabase;
    public TimelineAdapter(Context context, ArrayList<Product> products){
        this.products = products;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
    }


    @NonNull
    @Override
    public TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TimelineViewHolder(LayoutInflater.from(context).inflate(R.layout.row_product,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final TimelineViewHolder timelineViewHolder, final int position) {
        final Product product = products.get(position);

        firebaseDatabase.getReference().child("User").child(product.getAuthor()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.get().load(dataSnapshot.child("image").getValue(String.class)).placeholder(R.drawable.placeholder_big).into(timelineViewHolder.profile_image);
                timelineViewHolder.city.setText(dataSnapshot.child("city").getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });

        firebaseDatabase.getReference().child("Products").child(product.getId()).child("Likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                timelineViewHolder.likeTxt.setText(dataSnapshot.getChildrenCount() + " likes");
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot.getKey().equals(FirebaseAuth.getInstance().getUid())) {
                        product.setStatus(1);
                        timelineViewHolder.likeImage.setImageResource(R.drawable.like_enabled);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });




        timelineViewHolder.likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product.getStatus() == 1) {
                    FirebaseDatabase.getInstance().getReference().child("Products").child(product.getId()).child("Likes").child(FirebaseAuth.getInstance().getUid()).setValue(null);
                    timelineViewHolder.likeImage.setImageResource(R.drawable.like);
                    product.setStatus(0);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Products").child(product.getId()).child("Likes").child(FirebaseAuth.getInstance().getUid()).setValue(1);
                    timelineViewHolder.likeImage.setImageResource(R.drawable.like_enabled);
                }



            }
        });
        timelineViewHolder.title.setText(product.getTitle());
        timelineViewHolder.price.setText("Rs."+product.getPrice());
        timelineViewHolder.category.setText(product.getCategory());
        Picasso.get().load(product.getImage()).placeholder(R.drawable.placeholder_big).into(timelineViewHolder.product_image);

        timelineViewHolder.product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = ((MainActivity)context).getSupportFragmentManager().beginTransaction();
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
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }



    @Override
    public int getItemCount() {
        return products.size();
    }

    public class TimelineViewHolder extends RecyclerView.ViewHolder{

        TextView author,title,price,city,category,likeTxt;
        CircleImageView profile_image;
        RoundedImageView product_image;
        ImageView likeImage;

        public TimelineViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            city = itemView.findViewById(R.id.city);
            category = itemView.findViewById(R.id.category);
            profile_image = itemView.findViewById(R.id.profile_image);
            product_image = itemView.findViewById(R.id.image);
            likeImage = itemView.findViewById(R.id.likeBtn);
            likeTxt = itemView.findViewById(R.id.liketxt);
        }
    }
}
