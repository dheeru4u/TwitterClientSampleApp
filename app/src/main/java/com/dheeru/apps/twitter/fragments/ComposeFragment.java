package com.dheeru.apps.twitter.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dheeru.apps.twitter.R;
import com.dheeru.apps.twitter.models.Tweet;
import com.dheeru.apps.twitter.utility.DeviceDimensionsHelper;
import com.makeramen.roundedimageview.RoundedImageView;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dkthaku on 6/3/16.
 */
public class ComposeFragment extends DialogFragment {

    public static final int MAX_CHAR_COUNT = 140;

    @Bind(R.id.ivIconClose)
    ImageView ivIconClose;

    @Bind(R.id.ivUserProfileImage)
    RoundedImageView ivUserProfileImage;

    @Bind(R.id.etTweet)
    EditText etTweet;

    @Bind(R.id.btnTweet)
    Button btnTweet;

    @Bind(R.id.tvCharacterCount)
    TextView tvCharacterCount;

    @Bind(R.id.ivIconReply)
    ImageView ivIconReply;

    @Bind(R.id.tvReplyTo)
    TextView tvReplyTo;

    Tweet mTweet;

    private OnFragmentInteractionListener mActivity;

    public ComposeFragment() {
        // Required empty public constructor
    }

    public static ComposeFragment newInstance() {
        ComposeFragment fragment = new ComposeFragment();
        return fragment;
    }

    public static ComposeFragment newInstance(Tweet tweet) {
        ComposeFragment fragment = new ComposeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("tweet", Parcels.wrap(tweet));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTweet = Parcels.unwrap(getArguments().getParcelable("tweet"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.compose_fragment, container, false);
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

        tvCharacterCount.setText(String.valueOf(MAX_CHAR_COUNT));

        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int textLength = text.length();
                int diff = MAX_CHAR_COUNT - textLength;
                tvCharacterCount.setText(String.valueOf(diff));

                if (diff < 0) {
                    tvCharacterCount.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    btnTweet.setEnabled(false);
                    btnTweet.setBackground(getResources().getDrawable(R.drawable.ic_twitter_bird));
                    btnTweet.setOnClickListener(null);
                } else {
                    tvCharacterCount.setTextColor(getResources().getColor(R.color.colorGrey));
                    btnTweet.setEnabled(true);
                    btnTweet.setBackground(getResources().getDrawable(R.drawable.ic_twitter_bird));
                    btnTweet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(mTweet == null) {
                                mActivity.postMessage(-1, etTweet.getText().toString());
                            } else {
                                mActivity.postMessage(mTweet.getTweetId(), etTweet.getText().toString());
                            }
                            dismiss();
                        }
                    });
                }
            }
        });

        etTweet.requestFocus();

        if(mTweet != null){
            ivIconReply.setVisibility(View.VISIBLE);
            tvReplyTo.setVisibility(View.VISIBLE);
            tvReplyTo.setText("In reply to " + mTweet.getUser().getName());
            etTweet.setText("@" + mTweet.getUser().getScreenName() + " ");
            etTweet.requestFocus();
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mActivity = (OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {

        void postMessage(long id, String message);
    }
}

