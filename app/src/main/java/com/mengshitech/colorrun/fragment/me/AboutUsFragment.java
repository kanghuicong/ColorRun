package com.mengshitech.colorrun.fragment.me;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.utils.MainBackUtility;
import com.mengshitech.colorrun.utils.Utility;

/**
 * atenklsy
 */
public class AboutUsFragment extends Fragment implements View.OnClickListener{
    View me_version_view;
    TextView aboutus_version,aboutus_agreement;
    LinearLayout aboutus_update,aboutus_feedback,aboutus_connection;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.rgMainBottom.setVisibility(View.GONE);
        me_version_view = View.inflate(getActivity(), R.layout.me_aboutus, null);
        MainBackUtility.MainBack(me_version_view,"关于我们",getFragmentManager());
        FindId();
        return me_version_view;
    }

    private void FindId() {
        aboutus_version = (TextView)me_version_view.findViewById(R.id.me_aboutus_version);
        aboutus_update = (LinearLayout) me_version_view.findViewById(R.id.me_aboutus_update);
        aboutus_update.setOnClickListener(this);
        aboutus_feedback = (LinearLayout)me_version_view.findViewById(R.id.me_aboutus_feedback);
        aboutus_feedback.setOnClickListener(this);
        aboutus_connection = (LinearLayout)me_version_view.findViewById(R.id.me_aboutus_connection);
        aboutus_connection.setOnClickListener(this);
        aboutus_agreement = (TextView)me_version_view.findViewById(R.id.me_aboutus_agreement);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_aboutus_update:
                break;
            case R.id.me_aboutus_feedback:
                Utility.replace2DetailFragment(getFragmentManager(), new AboutUsFeedBack());
               break;
            case R.id.me_aboutus_connection:
                Utility.replace2DetailFragment(getFragmentManager(), new AboutUsConnection());
                break;
            case R.id.me_aboutus_agreement:
                break;
        }
    }
}
