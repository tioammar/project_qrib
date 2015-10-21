/*
 * Copyright 2015 Aditya Amirullah. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.lovelacetech.qrib.ui;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.ArrayMap;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import in.lovelacetech.qrib.R;
import in.lovelacetech.qrib.model.MarkItem;
import in.lovelacetech.qrib.model.LocationHelper;
import in.lovelacetech.qrib.provider.Contract;
import in.lovelacetech.qrib.utility.LocationUtility;
import in.lovelacetech.qrib.utility.SessionUtility;

public class MainActivity extends BaseActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        LoaderManager.LoaderCallbacks<Cursor>,
        GoogleMap.OnMarkerClickListener {

    private static final String PERSON_ID_KEY = "person_id";

    private GoogleMap mMap;
    private GoogleApiClient mGoogleClient;
    private Location mUserLocation;
    private LocationRequest mLocationRequest;

    private ArrayMap<Marker, MarkItem> mMarkers;
    private Marker mMyMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check user status
        if (!SessionUtility.getLoginStatus(this)){
            // back to login activity
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // setting up client
        setUpGoogleClient();
    }

    private synchronized void setUpGoogleClient(){
        mGoogleClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mLocationRequest = LocationRequest.create()
                .setInterval(30 * 1000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // traffic is enable
        // we want to share information as much as we can btw
        mMap.setTrafficEnabled(true);
        mMap.setOnMarkerClickListener(this);
    }

    private void startLocationRequest() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient,
                mLocationRequest, this);
    }

    private void stopLocationUpdate() {
        LocationServices.FusedLocationApi
                .removeLocationUpdates(mGoogleClient, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleClient.connect();
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleClient.isConnected()){
            startLocationRequest();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleClient.isConnected()){
            stopLocationUpdate();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleClient.isConnected()){
            mGoogleClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // get last location
        mUserLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleClient);
        if (mUserLocation != null){
            if (LocationUtility.saveUserLocation(this, mUserLocation)){
                // run data service and update location on server
            }
        }
    }

    /**
     * clearing user marker
     * make it null
     */
    private void removeUserMarker(){
        if (mMyMarker != null){
            mMyMarker.remove();
            mMyMarker = null;
        }
    }

    /**
     * adding user marker
     */
    private Marker setUserMarker(double longitude, double latitude){
        MarkerOptions opt = new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("You're here");

        return mMap.addMarker(opt);
    }

    private void setUserLocation() {
        // if location null, using last saved location in preference
        if (mUserLocation == null) setLastLocation();

        removeUserMarker();

        double longitude = mUserLocation.getLongitude();
        double latitude = mUserLocation.getLongitude();
        mMyMarker = setUserMarker(longitude, latitude);
    }

    /**
     * getting saved location on shared preference
     */
    private void setLastLocation() {
        LocationHelper helper = LocationUtility.getLastLocation(this);
        // null means everything is zero
        // do nothing
        if (helper == null) return;

        removeUserMarker();
        mMyMarker = setUserMarker(helper.longitude, helper.latitude);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // retry connection
        mGoogleClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mUserLocation = location;
        if (LocationUtility.saveUserLocation(this, location)){
            // run data service and update location on server
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String user = Integer.toString(sp.getInt(PERSON_ID_KEY, 0));

        if (user.equals("0")) return null;

        // exclude my location from query
        String selection = Contract.MarkColumn.COLUMN_USER + " != ? ";

        return new CursorLoader(this, Contract.MarkColumn.CONTENT_URI,
                MarkItem.COLUMNS,
                selection,
                new String[]{user},
                null);
    }

    /**
     * we do everything after loader is finish loading
     * first we clear the map and adding user location
     *
     * we are using cursor loader advantages here
     * it loads again if provider is changing
     * that's why we only adding/removing marker on load finished
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // clear map
        mMap.clear();

        // set up again because the map is clear
        setUserLocation();

        // do nothing when cursor is empty
        if (data.getCount() == 0) return;

        if (mMarkers == null) {
            mMarkers = new ArrayMap<>(data.getCount());
        } else {
            // set to null first before setting new size
            mMarkers = null;
            mMarkers = new ArrayMap<>(data.getCount());
        }

        // setup event marker here
        for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()){
            MarkItem mark = MarkItem.getInstance(data);
            Marker marker = setUpMark(mark);
            mMarkers.put(marker, mark);
        }
    }

    private Marker setUpMark(MarkItem mark) {
        // setup all mark on map here
        final double longitude = mark.longitude;
        final double latitude = mark.latitude;

        MarkerOptions opt = new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(mark.content);

        return mMap.addMarker(opt);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // nothing to do here
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker == mMyMarker){
            // return false to use default behavior
            return false;
        }

        MarkItem mark = mMarkers.get(marker);

        // testing purpose
        Toast.makeText(this, mark.content, Toast.LENGTH_SHORT).show();

        return true;
    }
}
