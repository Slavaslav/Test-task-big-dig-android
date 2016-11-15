package com.myapplication.testtaskbigdig1;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HistoryFragment.OnFragmentInteractionListener, TestFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        /*//write data to database
        ContentValues cv = new ContentValues();
        for (int i = 1; i <= 3; i++) {
            cv.put(BigDigProvider.IMAGE_COLUMN_URI, "http://goole.com.uaq/" + i);
            cv.put(BigDigProvider.IMAGE_COLUMN_STATUS, i);
            cv.put(BigDigProvider.IMAGE_COLUMN_DATE, "14.11.201" + i);
            getContentResolver().insert(BigDigProvider.IMAGE_CONTENT_URI, cv);
        }

        //read data froma database
        Cursor c = getContentResolver().query(BigDigProvider.IMAGE_CONTENT_URI, null, null, null, null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex(BigDigProvider.IMAGE_COLUMN_ID);
            int uriColIndex = c.getColumnIndex(BigDigProvider.IMAGE_COLUMN_URI);
            int statusColIndex = c.getColumnIndex(BigDigProvider.IMAGE_COLUMN_STATUS);
            int dateColIndex = c.getColumnIndex(BigDigProvider.IMAGE_COLUMN_DATE);
            do {
                System.out.println(String.format("id = %d, uri = %s, status = %d, date = %s", c.getInt(idColIndex), c.getString(uriColIndex), c.getInt(statusColIndex), c.getString(dateColIndex)));
            } while (c.moveToNext());
        }*/

        // clear table
        /*BigDigProvider.DBHelper dbHelper = new BigDigProvider.DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + BigDigProvider.IMAGE_TABLE_NAME);
        db.close();
        dbHelper.close();*/
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addPage(TestFragment.newInstance(), "Test");
        adapter.addPage(HistoryFragment.newInstance(), "History");
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> pageNames = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addPage(Fragment fragment, String pageName) {
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
