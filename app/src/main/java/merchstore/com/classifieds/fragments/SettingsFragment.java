package merchstore.com.classifieds.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import merchstore.com.classifieds.Categories;
import merchstore.com.classifieds.R;
import merchstore.com.classifieds.User;

import static android.app.Activity.RESULT_OK;


public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }


    EditText nameBox, phoneBox, passBox, cityBox;
    ChipCloud chipCloud;
    Button submitBtn;
    FirebaseDatabase database;
    ArrayList<String> selectCategory;
    String[] categories;
    CircleImageView image;
    String images = "";
    String oldPass = "";
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;

    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://merchstore-67498.appspot.com/");    //change the url according to your firebase app
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        database = FirebaseDatabase.getInstance();
        categories = getResources().getStringArray(R.array.products);
        pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading....");
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_settings, container, false);
        nameBox = view.findViewById(R.id.nameBox);
        phoneBox = view.findViewById(R.id.phoneBox);
        passBox = view.findViewById(R.id.password);
        cityBox = view.findViewById(R.id.cityBox);
        chipCloud = view.findViewById(R.id.chip_cloud);
        submitBtn = view.findViewById(R.id.button);
        image = view.findViewById(R.id.profile_image);
        selectCategory = new ArrayList<>();
        final List<String> wordList = Arrays.asList(categories);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference().child("User").child(FirebaseAuth.getInstance().getUid()).setValue(new User(nameBox.getText().toString(),phoneBox.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getEmail(),passBox.getText().toString(),cityBox.getText().toString(),images,selectCategory)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        AuthCredential credential = EmailAuthProvider
                                .getCredential(user.getEmail(), oldPass);


                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.updatePassword(passBox.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getContext(), "Password Updated!", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getContext(), "Cannot update password!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {

                                        }
                                    }
                                });
                    }
                });


            }
        });

        new ChipCloud.Configure()
                .chipCloud(chipCloud)
                .selectedColor(R.color.colorPrimaryDark)
                .selectedFontColor(Color.parseColor("#ffffff"))
                .deselectedColor(Color.parseColor("#e1e1e1"))
                .deselectedFontColor(Color.parseColor("#333333"))
                .selectTransitionMS(500)
                .deselectTransitionMS(250)
                .labels(categories)
                .mode(ChipCloud.Mode.MULTI)
                .allCaps(false)
                .gravity(ChipCloud.Gravity.CENTER)
                .textSize(getResources().getDimensionPixelSize(R.dimen.default_textsize))
                .verticalSpacing(getResources().getDimensionPixelSize(R.dimen.vertical_spacing))
                .minHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.min_horizontal_spacing))
                .chipListener(new ChipListener() {
                    @Override
                    public void chipSelected(int index) {
                        selectCategory.add(categories[index]);
                    }
                    @Override
                    public void chipDeselected(int index) {
                        selectCategory.remove(categories[index]);
                    }
                })
                .build();

        database.getReference().child("User").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameBox.setText(dataSnapshot.child("name").getValue(String.class));
                phoneBox.setText(dataSnapshot.child("phone").getValue(String.class));
                passBox.setText(dataSnapshot.child("password").getValue(String.class));
                oldPass = dataSnapshot.child("password").getValue(String.class);
                cityBox.setText(dataSnapshot.child("city").getValue(String.class));

                for(DataSnapshot snapshot : dataSnapshot.child("interests").getChildren()){

                    int index = getIndex(snapshot.getValue(String.class));
                        if(index > -1)
                            chipCloud.setSelectedChip(index);

                }

                Picasso.get().load(dataSnapshot.child("image").getValue(String.class)).placeholder(R.drawable.placeholder).into(image);
                images = dataSnapshot.child("image").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });

        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);

                //Setting image to ImageView


                if(filePath != null) {
                    pd.show();

                    final StorageReference childRef = storageRef.child(System.currentTimeMillis() + "profile.jpg");

                    //uploading the image
                    UploadTask uploadTask = childRef.putFile(filePath);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Picasso.get().load(uri).into(image);
                                    database.getReference().child("User").child(FirebaseAuth.getInstance().getUid()).child("image").setValue(uri.toString());
                                }
                            });
                            pd.dismiss();
                            //image.setImageBitmap(bitmap);
                            Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        TextView name = toolbar.findViewById(R.id.name);
        name.setText("Settings");
        TextView price_tag = toolbar.findViewById(R.id.price_tag);
        price_tag.setVisibility(View.GONE);

    }

    int getIndex(String name) {
        int index = -1;
        for (int i=0;i<categories.length;i++) {
            if (categories[i].equals(name)) {
                index = i;
                break;
            }
        }
        return index;
    }


}
