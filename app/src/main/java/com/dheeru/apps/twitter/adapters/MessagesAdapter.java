package com.dheeru.apps.twitter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dheeru.apps.twitter.R;
import com.dheeru.apps.twitter.models.Message;
import com.dheeru.apps.twitter.utility.CommonUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dkthaku on 6/8/16.
 */
public class MessagesAdapter extends ArrayAdapter<Message> {

    Context mContext;

    public MessagesAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
        mContext = context;
    }

    public static class ViewHolder {

        @Bind(R.id.ivUserProfileImage)
        ImageView ivUserProfileImage;

        @Bind(R.id.tvUserName)
        TextView tvUserName;

        @Bind(R.id.tvScreenName)
        TextView tvScreenName;

        @Bind(R.id.tvMessage)
        TextView tvMessage;

        @Bind(R.id.tvRelativeTimestamp)
        TextView tvRelativeTimestamp;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Message message = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ivUserProfileImage.setImageResource(0);
        Glide.with(mContext).load(message.getSender().getProfileImageUrl()).error(R.drawable.photo_placeholder).placeholder(R.drawable.photo_placeholder).dontAnimate().into(viewHolder.ivUserProfileImage);

        viewHolder.tvUserName.setText(message.getSender().getName());
        viewHolder.tvScreenName.setText("@" + message.getSender().getScreenName());

        viewHolder.tvMessage.setText(message.getText());

        viewHolder.tvRelativeTimestamp.setText(CommonUtils.getFormattedRelativeTimestamp(message.getCreatedAt().toString()));

        return convertView;
    }
}

