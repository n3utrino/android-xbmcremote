package org.xbmc.android.remote.presentation.activity;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.widget.ListView;
import org.xbmc.android.remote.R;
import org.xbmc.android.remote.presentation.controller.MovieListController;

/**
 * Created by IntelliJ IDEA.
 * User: n3utrino
 * Date: 31.12.11
 * Time: 13:26
 * To change this template use File | Settings | File Templates.
 */
public class MoviesFragment extends ListFragment {

    public static final String TAG = "movies_fragment";

    private MovieListController controller = new MovieListController();
    private boolean mDualPane = false;
    private View fragmentView;
    private Handler mHandler = new Handler();

    public MoviesFragment() {
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
            if(details == null){

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(fragmentView == null){
            fragmentView = inflater.inflate(R.layout.movielibrary_fragment,container,false);
        }
        return  fragmentView;
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
