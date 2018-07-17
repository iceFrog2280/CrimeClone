package com.example.icefrog.crimeclone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by icefrog on 2017/8/14 0014.
 */

public class CrimeListFragment extends Fragment {
    private static final String TAG = "CrimeListFragment";
    private static String SAVE_SUBTITLE = "mSubtitleVisible";

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private int mCurrentIndex = 0; //之前选择的Crime
    private boolean mSubtitleVisible = false; // 标题栏显示数量状态

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu( true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate( R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById( R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));

        if( savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVE_SUBTITLE, false);//在Fragment中不能使用default?本来就是null 肯定报错
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView() { //更新RecyclerView
        List<Crime> list = CrimeLab.get( getContext()).getCrimeList();
        if( mAdapter == null){
            mAdapter = new CrimeAdapter( list);
            mCrimeRecyclerView.setAdapter( mAdapter);
        }else {
            mAdapter.setCrimes( list);
            mAdapter.notifyDataSetChanged();
        }
    }

    //处理RecyclerView列表
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;

        public  CrimeAdapter( List<Crime> crimes){
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from( getActivity());
            View view = layoutInflater.inflate( R.layout.list_item_crime, parent, false);
            return new CrimeHolder( view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get( position);
            holder.bindCrime( crime);// ?这里什么逻辑
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }
    }

    //处理子项
    private class CrimeHolder extends RecyclerView.ViewHolder {
        private Crime mCrime;

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        public CrimeHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById( R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById( R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById( R.id.list_item_crime_solved_checkbox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CrimePagerActivity.newIntent( getActivity(), mCrime.getId());
                    startActivity( intent);
                }
            });

            mSolvedCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCrime.setSolved( !mCrime.isSolved());
                }
            });
        }

        public void bindCrime( Crime crime){//更新对应Crime
            mCrime = crime;
            mTitleTextView.setText( mCrime.getTitle());
            mDateTextView.setText( StyleFormat.Date(mCrime.getDate()));
            mSolvedCheckBox.setChecked( mCrime.isSolved());
        }
    }

    //处理上方工具栏

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
       inflater.inflate( R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem( R.id.menu_item_show_subtitle);
        if( mSubtitleVisible){
            subtitleItem.setTitle( R.string.hide_subtitle);
        }else {
            subtitleItem.setTitle( R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: 2017/8/16 0016
        CrimeLab lab = CrimeLab.get( getActivity());
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime newCrime = new Crime();
                lab.addCrime( newCrime);
                Intent intent = CrimePagerActivity.newIntent( getActivity(), newCrime.getId());
                startActivity( intent);
                updateView();
                return true;
            case R.id.menu_item_delete_crime:

                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                int crimeCount = lab.getCrimeList().size();
                String subTitle = getResources().getQuantityString( R.plurals.subtitle_plural, crimeCount, crimeCount);
                if( !mSubtitleVisible) {
                    subTitle = null;
                    item.setTitle( R.string.show_subtitle);
                }else {
                    item.setTitle( R.string.hide_subtitle);
                }
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                activity.getSupportActionBar().setSubtitle( subTitle);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
