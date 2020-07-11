package merchstore.com.classifieds;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

import merchstore.com.classifieds.fragments.AddNewFragment;
import merchstore.com.classifieds.fragments.CategoriesFragment;
import merchstore.com.classifieds.fragments.ProfileFragment;
import merchstore.com.classifieds.fragments.SettingsFragment;
import merchstore.com.classifieds.fragments.TimelineFragment;
import merchstore.com.classifieds.menu.DrawerAdapter;
import merchstore.com.classifieds.menu.DrawerItem;
import merchstore.com.classifieds.menu.SimpleItem;

public class MainActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    private static final int HOME = 0;
    private static final int CATEGORY = 1;
    private static final int ADD_PRODUCT = 2;
    private static final int PROFILE = 3;
    private static final int SETTINGS = 4;
    private static final int LOGOUT = 5;

    private String[] screenTitles;
    private Drawable[] screenIcons;
    private FirebaseAuth auth;
    private SlidingRootNav slidingRootNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();


        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE},
                1);


        TimelineFragment categoriesFragment = new TimelineFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content,categoriesFragment);
        transaction.commit();


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter menuadapter = new DrawerAdapter(Arrays.asList(
                createItemFor(HOME).setChecked(true),
                createItemFor(CATEGORY),
                createItemFor(ADD_PRODUCT),
                createItemFor(PROFILE),
                createItemFor(SETTINGS),
                createItemFor(LOGOUT)));
        menuadapter .setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(menuadapter);

        menuadapter.setSelected(HOME);


        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);// show back button
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
                } else {
                    //show hamburger

                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                    slidingRootNav = new SlidingRootNavBuilder(MainActivity.this)
                            .withToolbarMenuToggle(toolbar)
                            .withMenuOpened(false)
                            .withContentClickableWhenMenuOpened(false)
                            .withMenuLayout(R.layout.menu_left_drawer)
                            .inject();

                    screenIcons = loadScreenIcons();
                    screenTitles = loadScreenTitles();

                    DrawerAdapter menuadapter = new DrawerAdapter(Arrays.asList(
                            createItemFor(HOME).setChecked(true),
                            createItemFor(CATEGORY),
                            createItemFor(ADD_PRODUCT),
                            createItemFor(PROFILE),
                            createItemFor(SETTINGS),
                            createItemFor(LOGOUT)));
                    menuadapter .setListener(MainActivity.this);

                    RecyclerView list = findViewById(R.id.list);
                    list.setNestedScrollingEnabled(false);
                    list.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    list.setAdapter(menuadapter);

                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            slidingRootNav.openMenu();

                        }
                    });
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainActivity.this, "Application cannot work properly without permissions.", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE},
                            1);
                }
                return;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                AddNewFragment addNewFragment = new AddNewFragment();
                FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                transaction3.replace(R.id.content,addNewFragment);
                transaction3.addToBackStack(null);
                transaction3.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(int position) {

        switch (position) {
            case HOME:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                TimelineFragment timelineFragment = new TimelineFragment();
                FragmentTransaction transaction0 = getSupportFragmentManager().beginTransaction();
                transaction0.replace(R.id.content,timelineFragment);
                transaction0.commit();
                break;
            case CATEGORY:
                CategoriesFragment categoriesFragment = new CategoriesFragment();
                FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                transaction2.replace(R.id.content,categoriesFragment);
                transaction2.addToBackStack(null);
                transaction2.commit();
                break;
            case ADD_PRODUCT:
                AddNewFragment addNewFragment = new AddNewFragment();
                FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                transaction3.replace(R.id.content,addNewFragment);
                transaction3.addToBackStack(null);
                transaction3.commit();
                break;
            case PROFILE:
                ProfileFragment profileFragment = new ProfileFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content,profileFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case SETTINGS:
                SettingsFragment settingsFragment = new SettingsFragment();
                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                transaction1.replace(R.id.content,settingsFragment);
                transaction1.addToBackStack(null);
                transaction1.commit();
                break;
            case LOGOUT:
                auth.signOut();
                Intent intent = new Intent(MainActivity.this,WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
        slidingRootNav.closeMenu();
        //Fragment selectedScreen = CenteredTextFragment.createFor(screenTitles[position]);
        //showFragment(selectedScreen);
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.black))
                .withTextTint(color(R.color.black))
                .withSelectedIconTint(color(R.color.purpleColor))
                .withSelectedTextTint(color(R.color.purpleColor));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

}
