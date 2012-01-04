package org.xbmc.android.remote.presentation.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import org.xbmc.android.remote.presentation.controller.ActorListController;
import org.xbmc.android.remote.presentation.controller.MovieListController;

public class ActorsFragment extends ListFragment {

    ActorListController controller = new ActorListController(ActorListController.TYPE_MOVIE);
    Handler mHandler = new Handler();

    public ActorsFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        controller.onCreate(getActivity(), mHandler, this);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        controller.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        controller.onCreateOptionsMenu(menu);
    }

}
