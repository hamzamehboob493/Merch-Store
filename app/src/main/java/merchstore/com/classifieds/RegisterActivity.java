package merchstore.com.classifieds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    Spinner spinner;
    EditText fullnameBox, phoneBox, emailBox, passBox, confirmPassBox;
    Button signupBtn, loginBtn;
    String city;
    ChipCloud chipCloud;
    String[] cities;
    String[] categories;
    ArrayList<String> selectCategory;
    private FirebaseAuth auth;
    ProgressDialog progressDialog;
    NestedScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            Drawable background = this.getResources().getDrawable(R.drawable.purplebluegradient);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're setting up your account. Please Wait!");

        scrollView = findViewById(R.id.scrollview);


        selectCategory = new ArrayList<>();
        fullnameBox = findViewById(R.id.nameBox);
        phoneBox = findViewById(R.id.phoneBox);
        emailBox = findViewById(R.id.emailBox);
        passBox = findViewById(R.id.passwordBox);
        confirmPassBox = findViewById(R.id.confirmPasswordBox);
        signupBtn = findViewById(R.id.createBtn);
        loginBtn = findViewById(R.id.alreadyBtn);

        spinner = findViewById(R.id.citySpinner);
        cities = getResources().getStringArray(R.array.cities);
        categories = getResources().getStringArray(R.array.products);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.cities, R.layout.spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = cities[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        chipCloud = (ChipCloud) findViewById(R.id.chip_cloud);
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



        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                );
                if(passBox.getText().toString().equals(confirmPassBox.getText().toString())) {
                    progressDialog.show();
                    auth.createUserWithEmailAndPassword(emailBox.getText().toString(), passBox.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                progressDialog.cancel();
                                Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.cancel();
                                FirebaseUser firebaseUser = task.getResult().getUser();
                                User user = new User(fullnameBox.getText().toString(), phoneBox.getText().toString(), emailBox.getText().toString(), passBox.getText().toString(), city, "https://firebasestorage.googleapis.com/v0/b/merchstore-67498.appspot.com/o/avatar-1577909__340.png?alt=media&token=83e47375-17e3-411f-855f-a3e0a5cc962a", selectCategory);
                                database.getReference().child("User").child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            finish();
                                            Toast.makeText(RegisterActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            progressDialog.cancel();
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Confirm password does not match.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

}
