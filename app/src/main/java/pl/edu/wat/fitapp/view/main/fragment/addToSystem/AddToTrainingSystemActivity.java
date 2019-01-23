package pl.edu.wat.fitapp.view.main.fragment.addToSystem;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import pl.edu.wat.fitapp.R;

public class AddToTrainingSystemActivity extends AppCompatActivity {

    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private AddExerciseToTrainingSystemFragment addExerciseToTrainingSystemFragment;
    private AddTrainingToTrainingSystemFragment addTrainingToTrainingSystemFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_training_system);

        addExerciseToTrainingSystemFragment = new AddExerciseToTrainingSystemFragment();
        addTrainingToTrainingSystemFragment = new AddTrainingToTrainingSystemFragment();

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.container);
        sectionsPageAdapter.addFragment(addExerciseToTrainingSystemFragment, "Ćwiczenia");
        sectionsPageAdapter.addFragment(addTrainingToTrainingSystemFragment, "Treningi");
        viewPager.setAdapter(sectionsPageAdapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public class SectionsPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitleList = new ArrayList<>();

        public SectionsPageAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}
