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

package org.xbmc.android.widget.slidingtabs;

import android.app.*;
import android.view.MenuItem;
import org.xbmc.android.remote.presentation.activity.HomeActivity;
import org.xbmc.android.util.KeyTracker;
import org.xbmc.android.util.OnLongPressBackKeyTracker;
import org.xbmc.android.util.KeyTracker.Stage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.view.KeyEvent;

public class FragmentTabActivity extends Activity {
	
	private KeyTracker mKeyTracker = null;

	public FragmentTabActivity() {
		if (Integer.parseInt(VERSION.SDK) < 5) {
			mKeyTracker = new KeyTracker(new OnLongPressBackKeyTracker() {
	
				@Override
				public void onLongPressBack(int keyCode, KeyEvent event,
						Stage stage, int duration) {
					onKeyLongPress(keyCode, event);
				}
	
				@Override
				public void onShortPressBack(int keyCode, KeyEvent event,
						Stage stage, int duration) {
					callSuperOnKeyDown(keyCode, event);
				}
				
			});
		}
	}
	
	protected void callSuperOnKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);

	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar bar = getActionBar();
        bar.setDisplayShowTitleEnabled(false);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    }

    @Override
	protected void onPostCreate(Bundle icicle) {
		super.onPostCreate(icicle);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentTab", getActionBar().getSelectedNavigationIndex());
	}

	/**
	 * Updates the screen state (current list and other views) when the content
	 * changes.
	 * 
	 *@see Activity#onContentChanged()
	 */

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
	
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		Intent intent = new Intent(FragmentTabActivity.this, HomeActivity.class);
		intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean handled = (mKeyTracker != null) && mKeyTracker.doKeyDown(keyCode, event);
		return handled || super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean handled = (mKeyTracker != null) && mKeyTracker.doKeyUp(keyCode, event);
		return handled || super.onKeyUp(keyCode, event);
	}


    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private final Bundle mArgs;
        private Fragment mFragment;

        public TabListener(Activity activity, String tag, Class<T> clz) {
            this(activity, tag, clz, null);
        }

        public TabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
            mArgs = args;

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
            if (mFragment != null && !mFragment.isDetached()) {
                FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
                ft.detach(mFragment);
                ft.commit();
            }
        }

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment == null) {
                mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                ft.attach(mFragment);
            }
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            //Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT).show();
        }
    }
}