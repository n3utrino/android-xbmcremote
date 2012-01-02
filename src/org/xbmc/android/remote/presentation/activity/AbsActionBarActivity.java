package org.xbmc.android.remote.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by IntelliJ IDEA.
 * User: n3utrino
 * Date: 30.12.11
 * Time: 16:47
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbsActionBarActivity extends Activity {

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
}
