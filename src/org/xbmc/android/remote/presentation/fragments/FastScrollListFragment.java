package org.xbmc.android.remote.presentation.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.xbmc.android.remote.R;

/**
 * Using the FastscrollList
 */
public class FastScrollListFragment extends ListFragment {
    protected View fragmentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(fragmentView == null){
            fragmentView = inflater.inflate(R.layout.movielibrary_fragment,container,false);
        }
        return  fragmentView;
    }
}
