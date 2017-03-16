package com.scltrainer.uni_mainz.sclerchenbergtrainerassist;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Aktivity die das UebungDetailFragment hostet.
 */

public class UebungActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    //TODO: Kapseln!
    public int entryID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uebung);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        entryID = extras.getInt("_id");
        Log.i("UebungActivity", "ID: " + entryID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UebungActivity.this, LayoutEditActivity.class);
                startActivity(intent);
            }
        });

    }


    /**
     * Startet Intent zu dem ausgelesenen Link.
     * Muss in der XML definiert werden.
     * @param v
     */
    public void onClickOpenVideo(View v) {
        //TODO: Prüfen ob das mit beliebigen Youtubelinks klappt.
        TextView tv = (TextView) findViewById(R.id.uebungVideoURL);
        Log.i("UebungActivity", tv.getText().toString());
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(tv.getText().toString()));
        startActivity(i);
        };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_uebung, menu);

        /**
         * Nimmt den Pfeil oben links raus.
         */
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static int entryID;

        // wird für die Listenansicht benötigt
        private Cursor dbCursor;
        // bildet den Cursor auf die ListView ab
        private UebungDetailAdapter listAdapter;
        // Schnittstelle zur Datenbank
        private DBConnection dbConnection;


        public DetailFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DetailFragment newInstance(int sectionNumber) {
            DetailFragment fragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            entryID = sectionNumber;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_uebung_detail, container, false);
            dbConnection= DBHelper.getConnection(this.getContext());
            dbCursor = selectCursorUebungDetail(entryID);
            //Log.i(TAG, "Cursor wurde initiiert");
            getActivity().startManagingCursor(dbCursor);
            //Log.i(TAG, "startManagingCursor");
            listAdapter = new UebungDetailAdapter(this.getContext(), dbCursor);
            listAdapter.bindView(rootView, this.getContext(), dbCursor);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            Button uploadButton = (Button) rootView.findViewById(R.id.uploadButtonUebung);
            uploadButton.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "ÄTSCHIBÄTSCH", Toast.LENGTH_LONG).show();
                }
            });

            return rootView;
        }

        public Cursor selectCursorUebungDetail(int entryID){
            /*String[] sArr = {"_id", DBInfo.EXERCISE_COLUMN_NAME_NAME, DBInfo.EXERCISE_COLUMN_NAME_AUTORNAME, DBInfo.EXERCISE_COLUMN_NAME_DESCRIPTION, DBInfo.EXERCISE_COLUMN_NAME_TECHNIC, DBInfo.EXERCISE_COLUMN_NAME_TACTIC, DBInfo.EXERCISE_COLUMN_NAME_PHYSIS,DBInfo.EXERCISE_COLUMN_NAME_RATING ,DBInfo.EXERCISE_COLUMN_NAME_DURATION, DBInfo.EXERCISE_COLUMN_NAME_AGE, DBInfo.EXERCISE_COLUMN_NAME_KEYWORDS, DBInfo.EXERCISE_COLUMN_NAME_VIDEOLINK};
            return dbConnection.select(DBInfo.EXERCISE_TABLE_NAME, sArr, null, null);*/
            String s = DBInfo.EXERCISE_COLUMN_NAME_IDLOCAL + " = ? ";
            String[] sArgs = {"" + entryID};
            Log.i("DetailFragment", "entryID in cursor: " + entryID);
            String[] sArr = {"_id", DBInfo.EXERCISE_COLUMN_NAME_NAME, DBInfo.EXERCISE_COLUMN_NAME_AUTORNAME,
                    DBInfo.EXERCISE_COLUMN_NAME_DESCRIPTION, DBInfo.EXERCISE_COLUMN_NAME_RATING, DBInfo.EXERCISE_COLUMN_NAME_TECHNIC,
                    DBInfo.EXERCISE_COLUMN_NAME_TACTIC, DBInfo.EXERCISE_COLUMN_NAME_PHYSIS,
                    DBInfo.EXERCISE_COLUMN_NAME_DURATION, DBInfo.EXERCISE_COLUMN_NAME_AGE,
                    DBInfo.EXERCISE_COLUMN_NAME_KEYWORDS, DBInfo.EXERCISE_COLUMN_NAME_VIDEOLINK};
            return dbConnection.select(DBInfo.EXERCISE_TABLE_NAME, sArr, s, sArgs);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position) {
                case 0:
                    return new LayoutFragment();
                case 1:
                    return DetailFragment.newInstance(entryID);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.field);
                case 1:
                    return getResources().getString(R.string.details);
                case 2:
                    return "SECTION 3";
            }
            return null;
        }

    }
}
