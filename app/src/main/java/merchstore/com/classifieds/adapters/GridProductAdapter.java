package merchstore.com.classifieds.adapters;

import android.content.Context;
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

import merchstore.com.classifieds.Product;
import merchstore.com.classifieds.R;

public class GridProductAdapter extends  RecyclerView.Adapter<GridProductAdapter.CategoriesViewHolder> {

    Context context;
    ArrayList<Product> products;

    public GridProductAdapter(Context context, ArrayList<Product> products){
        this.products = products;
        this.context = context;
    }


    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CategoriesViewHolder(LayoutInflater.from(context).inflate(R.layout.grid_product,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriesViewHolder categoriesViewHolder, int i) {
        Product product = products.get(i);

        categoriesViewHolder.title.setText(product.getTitle());
        categoriesViewHolder.price.setText("$"+product.getPrice());
        Picasso.get().load(product.getImage()).into(categoriesViewHolder.imageView, new Callback() {
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
        return products.size();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title, price;
        ProgressBar progressBar;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            imageView = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
        }
    }
}
