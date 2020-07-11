package merchstore.com.classifieds.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import merchstore.com.classifieds.R;


public class ProductDetailTextFragment extends Fragment {

    public ProductDetailTextFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_product_detail_text, container, false);
        TextView textView = view.findViewById(R.id.textView);
        Bundle bundle = getArguments();
        textView.setText(bundle.getString("detail"));
        return view;
    }

}
