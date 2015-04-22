package com.yalantis.fragment.dialog;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yalantis.R;
import com.yalantis.adapter.ShareDialogAdapter;
import com.yalantis.interfaces.IShareDialog;
import com.yalantis.util.SharingUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Aleksandr on 27.11.2014.
 */
public class ShareDialogFragment extends BaseDialogFragment implements AdapterView.OnItemClickListener {

    public static final String TAG = ShareDialogFragment.class.getSimpleName();

    private static final String TWITTER = "twitter";
    private static final String FACEBOOK = "facebook";

    private IShareDialog iShareDialog;
    private ShareDialogAdapter adapter;

    public static ShareDialogFragment newInstance() {
        return new ShareDialogFragment();
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/jpeg");
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(sharingIntent, 0);
        // make Fb & Tw first in the list
        Collections.sort(activityList, new Comparator<ResolveInfo>() {

            @Override
            public int compare(@NonNull ResolveInfo lhs, @NonNull ResolveInfo rhs) {
                if (lhs.activityInfo.packageName.contains(TWITTER) || lhs.activityInfo.packageName.contains(FACEBOOK)) {
                    return -1;
                }
                if (rhs.activityInfo.packageName.contains(TWITTER) || rhs.activityInfo.packageName.contains(FACEBOOK)) {
                    return 1;
                }
                return 0;
            }
        });
        // remove multiply list items of one app (and whole Google+)
        List<ResolveInfo> resultList = new ArrayList<>();
        for (ResolveInfo info : activityList) {
            if (info.activityInfo.packageName.equals(SharingUtils.GOOGLE_PLUS)) {
                continue;
            }
            boolean isItem = false;
            for (ResolveInfo curInfo : resultList) {
                if (curInfo.activityInfo.packageName.equals(info.activityInfo.packageName)) {
                    isItem = true;
                }
            }
            if (!isItem) {
                resultList.add(info);
            }
        }
        adapter = new ShareDialogAdapter(getActivity(), resultList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_share, container, false);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(@NonNull AdapterView<?> parent, @NonNull View view, int position, long id) {
        ResolveInfo info = adapter.getItem(position);
        if (iShareDialog != null) {
            dismiss();
            iShareDialog.onShare(info);
        }
    }

    public void setShareDialog(IShareDialog iShareDialog) {
        this.iShareDialog = iShareDialog;
    }

}
