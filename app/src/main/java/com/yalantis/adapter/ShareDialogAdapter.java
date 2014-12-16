package com.yalantis.adapter;

import android.app.Activity;
import android.content.pm.ResolveInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yalantis.R;

import java.util.List;

/**
 * Created by ED on 26.11.2014.
 */
public class ShareDialogAdapter extends BaseListAdapter<ResolveInfo> {

    public ShareDialogAdapter(Activity context, List<ResolveInfo> list) {
        super(context, list, R.layout.item_share_dialog);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View rowView = getInflater().inflate(R.layout.item_share_dialog, null, true);
        // set share name
        TextView shareName = (TextView) rowView.findViewById(R.id.shareName);
        // Set share image
        ImageView imageShare = (ImageView) rowView.findViewById(R.id.shareImage);

        // set native name of App to share
        shareName.setText((getItem(position)).activityInfo.applicationInfo.loadLabel(getContext().getPackageManager()).toString());

        // share native image of the App to share
        imageShare.setImageDrawable((getItem(position)).activityInfo.applicationInfo.loadIcon(getContext().getPackageManager()));

        return rowView;
    }// end getView

}