/*
 *      Copyright (C) 2005-2009 Team XBMC
 *      http://xbmc.org
 *
 *  This Program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2, or (at your option)
 *  any later version.
 *
 *  This Program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with XBMC Remote; see the file license.  If not, write to
 *  the Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139, USA.
 *  http://www.gnu.org/copyleft/gpl.html
 *
 */

package org.xbmc.android.remote.presentation.activity;

import org.xbmc.android.remote.R;
import org.xbmc.android.remote.business.ManagerFactory;
import org.xbmc.android.remote.presentation.controller.ListController;
import org.xbmc.android.remote.presentation.widget.JewelView;
import org.xbmc.android.util.KeyTracker;
import org.xbmc.android.util.KeyTracker.Stage;
import org.xbmc.android.util.OnLongPressBackKeyTracker;
import org.xbmc.api.business.IEventClientManager;
import org.xbmc.api.object.Movie;
import org.xbmc.eventclient.ButtonCodes;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MovieDetailsActivity extends AbsActionBarActivity {

    private ConfigurationManager mConfigurationManager;

	private KeyTracker mKeyTracker;
    

	
    public MovieDetailsActivity() {
    	if(Integer.parseInt(VERSION.SDK) < 5) {
	    	mKeyTracker = new KeyTracker(new OnLongPressBackKeyTracker() {
	
				@Override
				public void onLongPressBack(int keyCode, KeyEvent event,
						Stage stage, int duration) {
					onKeyLongPress(keyCode, event);
				}
	
				@Override
				public void onShortPressBack(int keyCode, KeyEvent event,
						Stage stage, int duration) {
					MovieDetailsActivity.super.onKeyDown(keyCode, event);
				}
				
			});
    	}
	}
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moviedetails);

        MovieDetailsFragment fragment = (MovieDetailsFragment)getFragmentManager().findFragmentByTag("movieDetails");

        final Movie movie = (Movie)getIntent().getSerializableExtra(ListController.EXTRA_MOVIE);
        fragment.updateContent(movie);

        // remove nasty top fading edge
        FrameLayout topFrame = (FrameLayout)findViewById(android.R.id.content);
        topFrame.setForeground(null);

		//mMovieDetailsController.setupPlayButton((Button)findViewById(R.id.moviedetails_playbutton));


		
		mConfigurationManager = ConfigurationManager.getInstance(this);
	}

    @Override
	protected void onResume() {
		super.onResume();
		//mMovieDetailsController.onActivityResume(this);
		mConfigurationManager.onActivityResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//mMovieDetailsController.onActivityPause();
		mConfigurationManager.onActivityPause();
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean handled = (mKeyTracker != null)?mKeyTracker.doKeyUp(keyCode, event):false;
		return handled || super.onKeyUp(keyCode, event);
	}
	
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		Intent intent = new Intent(MovieDetailsActivity.this, HomeActivity.class);
		intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		IEventClientManager client = ManagerFactory.getEventClientManager(mMovieDetailsController);
//		switch (keyCode) {
//			case KeyEvent.KEYCODE_VOLUME_UP:
//				client.sendButton("R1", ButtonCodes.REMOTE_VOLUME_PLUS, false, true, true, (short)0, (byte)0);
//				return true;
//			case KeyEvent.KEYCODE_VOLUME_DOWN:
//				client.sendButton("R1", ButtonCodes.REMOTE_VOLUME_MINUS, false, true, true, (short)0, (byte)0);
//				return true;
//		}
//		client.setController(null);
		boolean handled =  (mKeyTracker != null)?mKeyTracker.doKeyDown(keyCode, event):false;
		return handled || super.onKeyDown(keyCode, event);
	}
}