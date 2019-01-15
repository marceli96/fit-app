package pl.edu.wat.fitapp.Main;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import pl.edu.wat.fitapp.Main.Fragment.AboutFragment;
import pl.edu.wat.fitapp.Database.User;
import pl.edu.wat.fitapp.Main.Fragment.ExportFragment;
import pl.edu.wat.fitapp.Main.Fragment.GoalsFragment;
import pl.edu.wat.fitapp.Main.Fragment.HomeFragment;
import pl.edu.wat.fitapp.Main.Fragment.JournalFragment;
import pl.edu.wat.fitapp.Main.Fragment.Profile.ProfileFragment;
import pl.edu.wat.fitapp.R;
import pl.edu.wat.fitapp.Main.Fragment.SettingsFragment;
import pl.edu.wat.fitapp.Welcome.WelcomeActivity;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mainNavigation;
    private FrameLayout mainFrame;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AboutFragment aboutFragment;
    private ExportFragment exportFragment;
    private GoalsFragment goalsFragment;
    private HomeFragment homeFragment;
    private JournalFragment journalFragment;
    private ProfileFragment profileFragment;
    private SettingsFragment settingsFragment;
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

        aboutFragment = new AboutFragment();
        exportFragment = new ExportFragment();
        goalsFragment = new GoalsFragment();
        homeFragment = new HomeFragment();
        journalFragment = new JournalFragment();
        profileFragment = new ProfileFragment();
        settingsFragment = new SettingsFragment();

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
            setFragment(profileFragment);
            navigationView.setCheckedItem(R.id.drawer_me);
            mainNavigation.setSelectedItemId(R.id.navMe);
        } else {
            setFragment(homeFragment);
            navigationView.setCheckedItem(R.id.drawer_home);
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

        switch (menuItem.getItemId()) {
            case R.id.drawer_home:
                setFragment(homeFragment);
                mainNavigation.setSelectedItemId(R.id.navHome);
                break;
            case R.id.drawer_journal:
                setFragment(journalFragment);
                mainNavigation.setSelectedItemId(R.id.navJournal);
                break;
            case R.id.drawer_me:
                setFragment(profileFragment);
                mainNavigation.setSelectedItemId(R.id.navMe);
                break;
            case R.id.drawer_goals:
                setFragment(goalsFragment);
                mainNavigation.getMenu().setGroupCheckable(0, false, true);
                break;
            case R.id.drawer_settings:
                setFragment(settingsFragment);
                mainNavigation.getMenu().setGroupCheckable(0, false, true);
                break;
            case R.id.drawer_export:
                setFragment(exportFragment);
                mainNavigation.getMenu().setGroupCheckable(0, false, true);
                break;
            case R.id.drawer_about:
                setFragment(aboutFragment);
                mainNavigation.getMenu().setGroupCheckable(0, false, true);
                break;
            case R.id.drawer_logout:
                Intent openWelcomeScreen = new Intent(MainActivity.this, WelcomeActivity.class);
                openWelcomeScreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(openWelcomeScreen);
                Toast.makeText(MainActivity.this, "Wylogowano pomyślnie", Toast.LENGTH_SHORT).show();
                MainActivity.this.finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean changeBottomNavigationItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navHome:
                mainNavigation.getMenu().setGroupCheckable(0, true, true);
                setFragment(homeFragment);
                navigationView.setCheckedItem(R.id.drawer_home);
                return true;
            case R.id.navJournal:
                mainNavigation.getMenu().setGroupCheckable(0, true, true);
                setFragment(journalFragment);
                navigationView.setCheckedItem(R.id.drawer_journal);
                return true;
            case R.id.navMe:
                mainNavigation.getMenu().setGroupCheckable(0, true, true);
                setFragment(profileFragment);
                navigationView.setCheckedItem(R.id.drawer_me);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
        {
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
