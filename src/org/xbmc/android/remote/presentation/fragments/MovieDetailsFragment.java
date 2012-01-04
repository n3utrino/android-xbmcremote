package org.xbmc.android.remote.presentation.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.xbmc.android.remote.R;
import org.xbmc.android.remote.business.ManagerFactory;
import org.xbmc.android.remote.presentation.activity.ListActivity;
import org.xbmc.android.remote.presentation.activity.NowPlayingActivity;
import org.xbmc.android.remote.presentation.controller.AbstractController;
import org.xbmc.android.remote.presentation.controller.IController;
import org.xbmc.android.remote.presentation.controller.ListController;
import org.xbmc.android.remote.presentation.controller.MovieListController;
import org.xbmc.android.remote.presentation.widget.JewelView;
import org.xbmc.api.business.DataResponse;
import org.xbmc.api.business.IControlManager;
import org.xbmc.api.business.IVideoManager;
import org.xbmc.api.object.Actor;
import org.xbmc.api.object.Movie;
import org.xbmc.api.presentation.INotifiableController;
import org.xbmc.api.type.ThumbSize;

/**
* Created by IntelliJ IDEA.
* User: n3utrino
* Date: 31.12.11
* Time: 13:26
* To change this template use File | Settings | File Templates.
*/
public class MovieDetailsFragment extends Fragment {

    private View mFragmentView;
    private MovieDetailsController mMovieDetailsController;

    private static final String NO_DATA = "-";
    private static final int[] sStarImages = { R.drawable.stars_0, R.drawable.stars_1, R.drawable.stars_2, R.drawable.stars_3, R.drawable.stars_4, R.drawable.stars_5, R.drawable.stars_6, R.drawable.stars_7, R.drawable.stars_8, R.drawable.stars_9, R.drawable.stars_10 };

    public MovieDetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mFragmentView!= null){
            return mFragmentView;
        }

        mFragmentView = inflater.inflate(R.layout.moviedetails_fragment,container,false);
        
        return mFragmentView;
        
    }

    public void updateContent(Movie movie) {

        View view = mFragmentView;

        clearView();

        mMovieDetailsController = new MovieDetailsController(this.getActivity(), movie);
        
        ((TextView)view.findViewById(R.id.movie_title)).setText(movie.getName());
//
//        Log.i("MovieDetailsActivity", "rating = " + movie.rating + ", index = " + ((int) Math.round(movie.rating % 10)) + ".");
        if (movie.rating > -1) {
            ((ImageView)view.findViewById(R.id.moviedetails_rating_stars)).setImageResource(sStarImages[(int)Math.round(movie.rating % 10)]);
        }
        ((TextView)view.findViewById(R.id.moviedetails_director)).setText(movie.director);
        ((TextView)view.findViewById(R.id.moviedetails_genre)).setText(movie.genres);
        ((TextView)view.findViewById(R.id.moviedetails_runtime)).setText(movie.runtime);
        ((TextView)view.findViewById(R.id.moviedetails_rating)).setText(String.valueOf(movie.rating));

        mMovieDetailsController.updateMovieDetails(new Handler(),
                (TextView)view.findViewById(R.id.moviedetails_rating_numvotes),
                (TextView)view.findViewById(R.id.moviedetails_studio),
                (TextView)view.findViewById(R.id.moviedetails_plot),
                (TextView)view.findViewById(R.id.moviedetails_parental),
                (Button)view.findViewById(R.id.moviedetails_trailerbutton),
                (LinearLayout)view.findViewById(R.id.moviedetails_datalayout));

        mMovieDetailsController.loadCover((JewelView)view.findViewById(R.id.moviedetails_jewelcase));
    }

    private void clearView(){

        LinearLayout actorsView = (LinearLayout)this.getActivity().findViewById(R.id.moviedetails_actor_container);
        if(actorsView != null){
            actorsView.removeAllViews();
        }
        ((JewelView)this.getActivity().findViewById(R.id.moviedetails_jewelcase)).setCover(R.drawable.default_jewel);



    }

    private static class MovieDetailsController extends AbstractController implements INotifiableController, IController {
        
        private IVideoManager mVideoManager;
        private IControlManager mControlManager;
        private final Movie mMovie;
        
        MovieDetailsController(Activity activity, Movie movie) {
            super.onCreate(activity, new Handler());
            mActivity = activity;
            mMovie = movie;
            mVideoManager = ManagerFactory.getVideoManager(this);
            mControlManager = ManagerFactory.getControlManager(this);
        }
        
        public void setupPlayButton(Button button) {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mControlManager.playFile(new DataResponse<Boolean>() {
                        public void run() {
                            if (value) {
                                mActivity.startActivity(new Intent(mActivity, NowPlayingActivity.class));
                            }
                        }
                    }, mMovie.getPath(), mActivity.getApplicationContext());
                }
            });
        }
        
        public void loadCover(final JewelView jewelView) {
            mVideoManager.getCover(new DataResponse<Bitmap>() {
                public void run() {
                    if (value != null) {
                        jewelView.setCover(value);
                    }
                }
            }, mMovie, ThumbSize.BIG, null, mActivity.getApplicationContext(), false);
        }
        
        public void updateMovieDetails(final Handler handler, final TextView numVotesView, final TextView studioView, final TextView plotView, final TextView parentalView, final Button trailerButton, final LinearLayout dataLayout) {
            mVideoManager.updateMovieDetails(new DataResponse<Movie>() {
                public void run() {
                    final Movie movie = value;
                    if (movie == null) {
                        Log.w(TAG, "updateMovieDetails: value is null.");
                        return;
                    }
                    numVotesView.setText(movie.numVotes > 0 ? " (" + movie.numVotes + " votes)" : "");
                    studioView.setText(movie.studio.equals("") ? MovieDetailsFragment.NO_DATA : movie.studio);
                    plotView.setText(movie.plot.equals("") ? MovieDetailsFragment.NO_DATA : movie.plot);
                    parentalView.setText(movie.rated.equals("") ? MovieDetailsFragment.NO_DATA : movie.rated);
                    if (movie.trailerUrl != null && !movie.trailerUrl.equals("")) {
                        trailerButton.setEnabled(true);
                        trailerButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                mControlManager.playFile(new DataResponse<Boolean>() {
                                    public void run() {
                                        if (value) {
                                            Toast toast = Toast.makeText(mActivity,  "Playing trailer for \"" + movie.getName() + "\"...", Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                    }
                                }, movie.trailerUrl, mActivity.getApplicationContext());
                            }
                        });
                    }

                    if (movie.actors != null) {
                        final LayoutInflater inflater = mActivity.getLayoutInflater();
                        
                        LinearLayout actorsView = (LinearLayout)mActivity.findViewById(R.id.moviedetails_actor_container);
                        actorsView.removeAllViews();
                        //int n = 0;
                        for (Actor actor : movie.actors) {
                            final View view = inflater.inflate(R.layout.actor_item, null);
                            
                            ((TextView)view.findViewById(R.id.actor_name)).setText(actor.name);
                            ((TextView)view.findViewById(R.id.actor_role)).setText("as " + actor.role);
                            ImageButton img = ((ImageButton)view.findViewById(R.id.actor_image));
                            mVideoManager.getCover(new DataResponse<Bitmap>() {
                                public void run() {
                                    if (value != null) {
                                        handler.post(new Runnable() {
                                            public void run() {
                                                ((ImageButton)view.findViewById(R.id.actor_image)).setImageBitmap(value);
                                            }
                                        });
                                    }
                                }
                            }, actor, ThumbSize.SMALL, null, mActivity.getApplicationContext(), false);
                            
                            img.setTag(actor);
                            img.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent nextActivity;
                                    Actor actor = (Actor)v.getTag();
                                    nextActivity = new Intent(view.getContext(), ListActivity.class);
                                    nextActivity.putExtra(ListController.EXTRA_LIST_CONTROLLER, new MovieListController());
                                    nextActivity.putExtra(ListController.EXTRA_ACTOR, actor);
                                    mActivity.startActivity(nextActivity);
                                }
                            });
                            actorsView.addView(view);
                            //n++;
                        }
                    }
                }
            }, mMovie, mActivity.getApplicationContext());
        }

        public void onActivityPause() {
            mVideoManager.setController(null);
            mVideoManager.postActivity();
            mControlManager.setController(null);
        }

        public void onActivityResume(Activity activity) {
            mVideoManager.setController(this);
            mControlManager.setController(this);
        }
    }
}
