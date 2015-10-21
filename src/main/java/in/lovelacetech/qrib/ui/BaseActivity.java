package in.lovelacetech.qrib.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import in.lovelacetech.qrib.R;

/**
 * Created by tioammar
 * on 10/18/15.
 */
public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    /**
     * this is will be the place we setup our main view
     * including all its actions
     */
    private DrawerLayout mMainLayout;

    private static final String PERSON_NAME_KEY = "person_name";
    private static final String PERSON_USER_KEY = "person_user";
    private static final String PERSON_AVATAR_KEY = "person_avatar";

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        // setting up navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(0); // set default checked item

        mMainLayout = (DrawerLayout) findViewById(R.id.main_layout);
        Toolbar toolbar = getSupportToolbar();
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(this);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.action_new_event);
        fab.setOnClickListener(this);

        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setDisplayShowTitleEnabled(false);
    }

    private Toolbar getSupportToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        if (toolbar != null){
            // setting up action bar for ab
            setSupportActionBar(toolbar);
            return toolbar;
        }
        else return null;
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        if (viewId == R.id.action_new_event){
            // starting new event activity
        }
        // run navigation click event if viewId is not defined
        else mMainLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        item.setChecked(true);
        mMainLayout.closeDrawers();
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setUpNavigationHeader();
    }

    /**
     * setting up navigation header
     * showing user avatar, full name, and user name
     * opening user profile on click event
     */
    private void setUpNavigationHeader() {
        // getting user info
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        final String name = sp.getString(PERSON_NAME_KEY, null);
        final String user = sp.getString(PERSON_USER_KEY, null);
        final String avatar = sp.getString(PERSON_AVATAR_KEY, null);

        // TODO:
        // bind user info to view
    }
}
