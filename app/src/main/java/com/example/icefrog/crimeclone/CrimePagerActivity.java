package com.example.icefrog.crimeclone;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private static String EXTRA_CRIME_ID = "extra_crime_id";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mViewPager = (ViewPager) findViewById( R.id.activity_crime_pager_view_page);
        mCrimes = CrimeLab.get( this).getCrimeList();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter( new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public int getCount() {
                return mCrimes.size();
            }

            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get( position);
                return CrimeFragment.newInstance( crime.getId());
            }
        });

        for (int i = 0; i < mCrimes.size(); i++) {
            if( mCrimes.get(i).getId() == uuid){
                mViewPager.setCurrentItem( i);
                break;
            }
        }
    }

    public static Intent newIntent(Context packageContext, UUID crime_uuid){
        Intent intent = new Intent( packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crime_uuid);//不能直接 crime因为其不支持java.io.Serializable
        return intent;
    }
}
