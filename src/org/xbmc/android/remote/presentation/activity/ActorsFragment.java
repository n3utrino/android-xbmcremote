package org.xbmc.android.remote.presentation.activity;

import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import org.xbmc.android.remote.presentation.controller.ActorListController;
import org.xbmc.android.remote.presentation.controller.MovieListController;

/**
 * Created by IntelliJ IDEA.
 * User: n3utrino
 * Date: 31.12.11
 * Time: 13:26
 * To change this template use File | Settings | File Templates.
 */
public class ActorsFragment extends ListFragment {

    ActorListController controller;
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
        if (controller == null) {
            controller = new ActorListController(ActorListController.TYPE_MOVIE);

        }
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
