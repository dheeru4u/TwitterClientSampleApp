package com.dheeru.apps.twitter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dheeru.apps.twitter.R;
import com.dheeru.apps.twitter.utility.DeviceDimensionsHelper;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dkthaku on 6/8/16.
 */
public class DmFragment extends DialogFragment {

    @Bind(R.id.ivIconClose)
    ImageView ivIconClose;

    @Bind(R.id.ivUserProfileImage)
    RoundedImageView ivUserProfileImage;

    @Bind(R.id.etTo)
    EditText etTo;

    @Bind(R.id.etMessage)
    EditText etMessage;

    @Bind(R.id.btnSend)
    Button btnSend;

    private OnDmFragmentInteractionListener mActivity;

    public DmFragment() {
        // Required empty public constructor
    }

    public static DmFragment newInstance() {
        DmFragment fragment = new DmFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_dm, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivIconClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.postMessage(etTo.getText().toString(), etMessage.getText().toString());
                dismiss();
            }
        });

        etTo.requestFocus();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDmFragmentInteractionListener) {
            mActivity = (OnDmFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onResume(){
        super.onResume();
        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = (int) DeviceDimensionsHelper.convertDpToPixel(400, getContext());
        getDialog().getWindow().setLayout(width, height);
    }

    public interface OnDmFragmentInteractionListener {

        void postMessage(String to, String message);
    }
}

