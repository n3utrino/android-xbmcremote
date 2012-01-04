package org.xbmc.android.remote.presentation.fragments;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.widget.ListView;
import org.xbmc.android.remote.R;
import org.xbmc.android.remote.presentation.activity.FastScrollListFragment;
import org.xbmc.android.remote.presentation.activity.MovieDetailsFragment;
import org.xbmc.android.remote.presentation.controller.MovieListController;

/**
 * Movie list Fragment. Is aware of the details fragment and shows it if 
 * the movieDetailsFragment placeholder is present. The Controller Calls the
 * MovieDetailsFragment or an Activity with the MovieDetailsFragment.
 *
 * The movies fragment handles the actionBar menu for itself. So any action Loading the Fragment
 * will get the correct menus.
 */
public class MoviesFragment extends FastScrollListFragment {

    public static final String TAG = "movies_fragment";

    private MovieListController controller = new MovieListController();
    private boolean mDualPane = false;
    private Handler mHandler = new Handler();

    public MoviesFragment() {
    }

    public boolean isDualPane() {
        return mDualPane;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        controller.onCreate(getActivity(), mHandler, this);


        View detailsFrame = getActivity().findViewById(R.id.movieDetailsFragment);
        mDualPane = detailsFrame != null
                && detailsFrame.getVisibility() == View.VISIBLE;

        if(mDualPane){
            MovieDetailsFragment details = (MovieDetailsFragment)
                    getFragmentManager().findFragmentById(R.id.movieDetailsFragment);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if(details == null || !details.isVisible()){
                //todo: is this really necessary if its not visible?
                details = new MovieDetailsFragment();


                ft.replace(R.id.movieDetailsFragment,details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            } else{
               ft.show(details);
            }
            ft.commit();
        }
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

    @Override
    public void onPause() {
        controller.onActivityPause();
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        controller.onActivityResume(this.getActivity());
    }
}
