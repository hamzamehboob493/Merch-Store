package merchstore.com.classifieds.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import merchstore.com.classifieds.Categories;
import merchstore.com.classifieds.R;

public class LatestAdapter extends  RecyclerView.Adapter<LatestAdapter.CategoriesViewHolder> {

    Context context;
    ArrayList<String> images;

    public LatestAdapter(Context context, ArrayList<String> images){
        this.images = images;
        this.context = context;
    }


    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CategoriesViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriesViewHolder categoriesViewHolder, int i) {
        String latest = images.get(i);

        /************** Setting Values **************/
        Picasso.get().load(latest).into(categoriesViewHolder.imageView, new Callback() {
            @Override
            public void onSuccess() {
                categoriesViewHolder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder{

        RoundedImageView imageView;
        ProgressBar progressBar;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
