package merchstore.com.classifieds.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import merchstore.com.classifieds.R;
import merchstore.com.classifieds.Review;
import merchstore.com.classifieds.User;

public class ReviewsAdapter extends  RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    Context context;
    ArrayList<Review> reviews;
    FirebaseDatabase firebaseDatabase;
    public ReviewsAdapter(Context context, ArrayList<Review> reviews){
        this.reviews = reviews;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
    }


    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ReviewsViewHolder(LayoutInflater.from(context).inflate(R.layout.row_review,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ReviewsViewHolder reviewsViewHolder, int i) {
        Review review = reviews.get(i);

        firebaseDatabase.getReference().child("User").child(review.getAuthor_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewsViewHolder.author.setText(dataSnapshot.child("name").getValue(String.class));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {            }
        });


        reviewsViewHolder.date.setText(review.getDate());
        reviewsViewHolder.comment.setText(review.getComment());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder{

        TextView author,date,comment;

        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author);
            date = itemView.findViewById(R.id.date);
            comment = itemView.findViewById(R.id.comment);
        }
    }
}
