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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import merchstore.com.classifieds.Categories;
import merchstore.com.classifieds.R;

public class CategoriesAdapter extends  RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    Context context;
    ArrayList<Categories> categories;
    Typeface typeface;

    public CategoriesAdapter(Context context, ArrayList<Categories> categories){
        this.categories = categories;
        this.context = context;
        this.typeface = Typeface.createFromAsset(context.getAssets(),"font/roboto.ttf");
    }


    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CategoriesViewHolder(LayoutInflater.from(context).inflate(R.layout.grid_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriesViewHolder categoriesViewHolder, int i) {
        Categories category = categories.get(i);



        /************** Setting Values **************/
        Picasso.get().load(category.getImage()).into(categoriesViewHolder.imageView, new Callback() {
            @Override
            public void onSuccess() {
                categoriesViewHolder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        categoriesViewHolder.title.setText(category.getCategory());



        /************** Setting TypeFace **************/
        categoriesViewHolder.title.setTypeface(typeface);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title;
        ProgressBar progressBar;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            imageView = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.txt);

        }
    }
}
