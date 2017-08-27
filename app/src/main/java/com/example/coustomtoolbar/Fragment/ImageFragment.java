package com.example.coustomtoolbar.Fragment;

import android.support.v4.app.Fragment;

/**
 * Created by SEELE on 2017/8/27.
 */

public abstract class ImageFragment extends Fragment {

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            load();
        }
    }

    protected abstract void load();

}
