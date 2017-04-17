package com.codemobiles.myauthen;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class SuccessActivity extends AppCompatActivity
{

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
    private UserBean mUserbean;
    private FeedJSONFragment mFeedFragment;

    private FloatingActionMenu mFab_menu;
    private FloatingActionButton mFab_foods;
    private FloatingActionButton mFab_superhero;
    private FloatingActionButton mFab_songs;
    private FloatingActionButton mFab_training;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        //Get input inten
        mUserbean = (UserBean) getIntent().getParcelableExtra(UserBean.TABLE_NAME);

        //Toast.makeText(this, "Login Complete: " + mUserbean.username + " and " + mUserbean.password, Toast.LENGTH_SHORT).show();

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

        bindWidget();
        setEvents();
    }


    private void setEvents()
    {
        mFab_foods.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mFeedFragment.feed(FeedJSONFragment.kFeed_foods);
                Toast.makeText(SuccessActivity.this, "food click", Toast.LENGTH_SHORT).show();
                mFab_menu.close(true);
            }
        });

        mFab_superhero.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mFeedFragment.feed(FeedJSONFragment.kFeed_superheros);
                Toast.makeText(SuccessActivity.this, "Hero click", Toast.LENGTH_SHORT).show();
                mFab_menu.close(true);
            }
        });

        mFab_songs.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mFeedFragment.feed(FeedJSONFragment.kFeed_songs);
                Toast.makeText(SuccessActivity.this, "Song click", Toast.LENGTH_SHORT).show();
                mFab_menu.close(true);
            }
        });

        mFab_training.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mFeedFragment.feed(FeedJSONFragment.kFeed_trainings);
                Toast.makeText(SuccessActivity.this, "Training click", Toast.LENGTH_SHORT).show();
                mFab_menu.close(true);
            }
        });
    }

    private void bindWidget()
    {
        mFab_menu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        mFab_menu.setClosedOnTouchOutside(true);

        mFab_foods = (FloatingActionButton) findViewById(R.id.fab_foods);
        mFab_superhero = (FloatingActionButton) findViewById(R.id.fab_superhero);
        mFab_songs = (FloatingActionButton) findViewById(R.id.fab_songs);
        mFab_training = (FloatingActionButton) findViewById(R.id.fab_training);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_success, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        } else if (id == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position)
            {
                case 0:
                    if (mFeedFragment == null)
                    {
                        mFeedFragment = new FeedJSONFragment();
                    }
                    Bundle _userbeanBundle = new Bundle();
                    _userbeanBundle.putParcelable(UserBean.TABLE_NAME, mUserbean);
                    mFeedFragment.setArguments(_userbeanBundle);

                    return mFeedFragment;

                case 1:

                    return new FeedRecycleFragment();

                case 2:

                    return new FeedSOAPFragment();
            }
            return null;
        }

        @Override
        public int getCount()
        {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case 0:
                    return "JSON VIEW";
                case 1:
                    return "RECYCLE VIEW";
                case 2:
                    return "SOAP VIEW";
            }
            return null;
        }
    }
}
