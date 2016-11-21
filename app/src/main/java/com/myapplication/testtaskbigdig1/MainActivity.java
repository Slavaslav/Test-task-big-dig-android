package com.myapplication.testtaskbigdig1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HistoryFragment.OnFragmentInteractionListener, TestFragment.OnFragmentInteractionListener {

    static final String OPERATION_TYPE = "operation_type";
    static final String OPERATION_CREATE = "operation_create";
    static final String OPERATION_UPDATE = "operation_update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addPage(TestFragment.newInstance(), getString(R.string.test));
        adapter.addPage(HistoryFragment.newInstance(), getString(R.string.history));
        viewPager.setAdapter(adapter);
    }

    public void startTaskBigDig2(Bundle bundle) {
        Intent i = getPackageManager().getLaunchIntentForPackage(getString(R.string.name_package_application_b));
        if (i == null) {
            Toast toast = Toast.makeText(this, R.string.application_b_not_found, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            i.setAction(Intent.ACTION_SEND);
            i.putExtras(bundle);
            startActivity(i);
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        final ArrayList<Fragment> fragments = new ArrayList<>();
        final ArrayList<String> pageNames = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        void addPage(Fragment fragment, String pageName) {
            fragments.add(fragment);
            pageNames.add(pageName);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageNames.get(position);
        }
    }
}
