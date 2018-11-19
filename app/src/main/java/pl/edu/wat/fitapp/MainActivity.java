package pl.edu.wat.fitapp;

import android.app.Activity;
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
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;

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
    private MeFragment meFragment;
    private ProgressFragment progressFragment;
    private RecipesFragment recipesFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        mainNavigation = (BottomNavigationView)findViewById(R.id.mainNavigation);
        mainFrame = (FrameLayout)findViewById(R.id.mainFrame);

        aboutFragment = new AboutFragment();
        exportFragment = new ExportFragment();
        goalsFragment = new GoalsFragment();
        homeFragment = new HomeFragment();
        journalFragment = new JournalFragment();
        meFragment = new MeFragment();
        progressFragment = new ProgressFragment();
        recipesFragment = new RecipesFragment();
        settingsFragment = new SettingsFragment();

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        setFragment(homeFragment);
        navigationView.setCheckedItem(R.id.drawer_home);

        mainNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
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
                        setFragment(meFragment);
                        return true;
                        default:
                            return false;
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.drawer_home:
                        setFragment(homeFragment);
                        mainNavigation.setSelectedItemId(R.id.navHome);
                        break;
                    case R.id.drawer_journal:
                        setFragment(journalFragment);
                        mainNavigation.setSelectedItemId(R.id.navJournal);
                        break;
                    case R.id.drawer_progress:
                        setFragment(progressFragment);
                        mainNavigation.getMenu().setGroupCheckable(0, false, true);
                        break;
                    case R.id.drawer_goals:
                        setFragment(goalsFragment);
                        mainNavigation.getMenu().setGroupCheckable(0, false, true);
                        break;
                    case R.id.drawer_recipes:
                        setFragment(recipesFragment);
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
                        startActivity(openWelcomeScreen);
                        MainActivity.this.finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        fragmentTransaction.commit();
    }
}
