package com.quarterlife.detectuserinactivitytest1.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.quarterlife.detectuserinactivitytest1.R;

public class SettingFragment extends BaseFragment {

    //========= onCreateView START =========//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }
    //========= onCreateView END =========//

    //========= onFragmentFirstVisible START =========//
    @Override
    protected void onFragmentFirstVisible() {

    }
    //========= onFragmentFirstVisible END =========//

    //========= onFragmentVisibleChange START =========//
    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible == true) {    /** fragment invisible --> visible  */

        } else {    /** fragment visible --> invisible  */

        }
    }
    //========= onFragmentVisibleChange END =========//

    //========= onViewCreated START =========//
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    //========= onViewCreated END =========//
}