package pl.edu.wat.fitapp.View.Main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import pl.edu.wat.fitapp.View.Main.Fragment.AboutFragment;
import pl.edu.wat.fitapp.Database.Entity.User;
import pl.edu.wat.fitapp.View.Main.Fragment.ExportFragment;
import pl.edu.wat.fitapp.View.Main.Fragment.GoalsFragment;
import pl.edu.wat.fitapp.View.Main.Fragment.HomeFragment;
import pl.edu.wat.fitapp.View.Main.Fragment.JournalFragment;
import pl.edu.wat.fitapp.View.Main.Fragment.Profile.ProfileFragment;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.View.Main.Fragment.SettingsFragment;
import pl.edu.wat.fitapp.Utils.ToastUtils;
import pl.edu.wat.fitapp.View.Welcome.WelcomeActivity;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mainNavigation;
    private FrameLayout mainFrame;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView tvUserName, tvEmail;

    private User user;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = getIntent();
        if (intent != null) {
            user = (User) intent.getSerializableExtra("user");
            action = (String) intent.getSerializableExtra("action");
        }

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        mainNavigation = findViewById(R.id.mainNavigation);
        mainFrame = findViewById(R.id.mainFrame);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (action != null && action.equals("openMeFragment")) {
            setFragment(new ProfileFragment());
            mainNavigation.setSelectedItemId(R.id.navMe);
        } else {
            setFragment(new HomeFragment());
        }

        View headerView = navigationView.getHeaderView(0);
        tvUserName = headerView.findViewById(R.id.tvUserName);
        tvEmail = headerView.findViewById(R.id.tvEmail);
        tvUserName.setText(user.getUserName());
        tvEmail.setText(user.getEmail());

        mainNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return changeBottomNavigationItem(menuItem);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return changeDrawerNavigationItem(menuItem);
            }
        });
    }


    private boolean changeDrawerNavigationItem(MenuItem menuItem) {
        Fragment selectedFragment = null;
        switch (menuItem.getItemId()) {
            case R.id.drawer_goals:
                selectedFragment = new GoalsFragment();
                mainNavigation.getMenu().setGroupCheckable(0, false, true);
                break;
            case R.id.drawer_settings:
                selectedFragment = new SettingsFragment();
                mainNavigation.getMenu().setGroupCheckable(0, false, true);
                break;
            case R.id.drawer_export:
                selectedFragment = new ExportFragment();
                mainNavigation.getMenu().setGroupCheckable(0, false, true);
                break;
            case R.id.drawer_about:
                selectedFragment = new AboutFragment();
                mainNavigation.getMenu().setGroupCheckable(0, false, true);
                break;
            case R.id.drawer_logout:
                Intent openWelcomeScreen = new Intent(MainActivity.this, WelcomeActivity.class);
                openWelcomeScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(openWelcomeScreen);
                ToastUtils.shortToast(MainActivity.this, "Wylogowano pomyślnie");
                MainActivity.this.finish();
                break;
            default:
                return false;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        setFragment(selectedFragment);
        return true;
    }

    private boolean changeBottomNavigationItem(MenuItem menuItem) {
        Fragment selectedFragment;
        switch (menuItem.getItemId()) {
            case R.id.navHome:
                mainNavigation.getMenu().setGroupCheckable(0, true, true);
                selectedFragment = new HomeFragment();
                break;
            case R.id.navJournal:
                mainNavigation.getMenu().setGroupCheckable(0, true, true);
                selectedFragment = new JournalFragment();
                break;
            case R.id.navMe:
                mainNavigation.getMenu().setGroupCheckable(0, true, true);
                selectedFragment = new ProfileFragment();
                break;
            default:
                return false;
        }
        setFragment(selectedFragment);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else {
            super.onBackPressed();
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        fragmentTransaction.commit();
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

}
