package org.awesomeapp.messenger.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactViewHolder extends RecyclerView.ViewHolder
{
    public ContactViewHolder(ContactListItem view) {
        super(view);
        mView = view;
    }

    public ContactListItem mView;
    public String mAddress;
    public String mNickname;
    public long mProviderId;
    public long mAccountId;
    public long mContactId;

    public View mContainer;
    public TextView mLine1;
    public TextView mLine2;
    public ImageView mAvatar;
    public ImageView mMediaThumb;

    public View mSubBox;
    public Button mButtonSubApprove;
    public Button mButtonSubDecline;
}
