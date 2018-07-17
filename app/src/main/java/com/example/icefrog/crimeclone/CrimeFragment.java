package com.example.icefrog.crimeclone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.UUID;

/**
 * Created by icefrog on 2017/8/14 0014.
 */

public class CrimeFragment extends Fragment {
    private String TAG = "CrimeFragment";
    private static String ARG_CRIME_ID = "arg_crime_id";

    private static int REQUEST_PHTOT = 2;

    ImageView mCrimePhoto;
    ImageButton mCrimeCamera;
    EditText mTitleEditText;

    Button mCrimeDate;
    CheckBox mCrimeIsSolved;
    Button mCrimeSuspect;
    Button mCrimeReport;

    Crime mCrime;
    File mPhotoFile;
    CrimeLab mLab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {//初始化数据
        super.onCreate(savedInstanceState);
        UUID uuid = (UUID) getArguments().getSerializable( ARG_CRIME_ID);
        mLab = CrimeLab.get(getActivity());
        mCrime = mLab.getCrime( uuid);
        mPhotoFile = mLab.getPhotoFile( mCrime);
        Log.d(TAG, "onCreate: "+mCrime.getTitle()); //每次创建都会将上层fragment都构建一遍 故出现 wang good
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) { //初始化视图
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate( R.layout.fragment_crime, container, false);

        initTitle(view);
        initContext(view);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mLab.updataCrime( mCrime);
    }

    private void initTitle(View view) {
        // TODO: 2017/8/16 0016
        final PackageManager packageManager = getActivity().getPackageManager();

        mCrimePhoto = (ImageView) view.findViewById( R.id.crime_photo);
        updataPhoto();

        mCrimeCamera = (ImageButton) view.findViewById( R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakeImage = mPhotoFile != null
                && captureImage.resolveActivity( packageManager) != null;
        if( !canTakeImage){
            mCrimeCamera.setEnabled( false);
        }else {
            Context context = getActivity();
            Uri uri = FileProvider.getUriForFile( context,
                    context.getApplicationContext().getPackageName()+".provider", mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        mCrimeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage, REQUEST_PHTOT);
            }
        });

        mTitleEditText = (EditText) view.findViewById(R.id.crime_title);
        mTitleEditText.setText( mCrime.getTitle());
        mTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle( s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initContext(View view) {
        mCrimeDate = (Button) view.findViewById( R.id.crime_date);
        mCrimeDate.setText( StyleFormat.Date(mCrime.getDate()));

        mCrimeIsSolved = (CheckBox) view.findViewById( R.id.crime_solved);
        mCrimeIsSolved.setChecked( mCrime.isSolved());
        mCrimeIsSolved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCrime.setSolved( !mCrime.isSolved());
            }
        });

        mCrimeSuspect = (Button) view.findViewById( R.id.crime_suspect);
        mCrimeSuspect.setText( mCrime.getSuspect());
        mCrimeSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/8/16 0016
                StyleFormat.showNoWork(getActivity());
            }
        });

        mCrimeReport = (Button) view.findViewById( R.id.crime_report);
        mCrimeReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/8/16 0016
                StyleFormat.showNoWork(getActivity());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == REQUEST_PHTOT){//更新照片
            if( resultCode == Activity.RESULT_OK){
                updataPhoto();
            }
        }
    }

    static public Fragment newInstance(UUID crime_id){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crime_id);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments( args);
        return fragment;
    }

//更新函数
    private void updataPhoto() {
        if( mPhotoFile == null || !mPhotoFile.exists()){
            mCrimePhoto.setImageDrawable( null);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mCrimePhoto.setImageBitmap( bitmap);
        }
    }
}
