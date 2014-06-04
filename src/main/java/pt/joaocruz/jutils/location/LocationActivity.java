package pt.joaocruz.jutils.location;

import android.content.*;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import pt.joaocruz.jutils.JAlert;
import pt.joaocruz.jutils.JLog;
import pt.joaocruz.jutils.R;

/**
 * Created by BEWARE S.A. on 12/03/14.
 */
public class LocationActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

    private static String TAG = "LocActivity";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;
    private boolean tracking = false;
    private GPSStatusBroadcastReceiver receiver;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST:
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "Google Play Services must be installed.", Toast.LENGTH_SHORT).show();
                    //   finish();
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new GPSStatusBroadcastReceiver();
        mLocationClient = new LocationClient(this, this, this);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        setTracking(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerGPSStatusReceiver();
        if (servicesConnected(false))
            mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        unregisterGPSStatusReceiver();
        if (mLocationClient.isConnected())
            mLocationClient.disconnect();
        super.onStop();
    }

    private void registerGPSStatusReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        registerReceiver(receiver, filter);
    }

    private void unregisterGPSStatusReceiver() {
        unregisterReceiver(receiver);
    }

    private boolean servicesConnected(boolean withAlert) {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                if (withAlert)
                    showErrorDialog(resultCode);
            } else {
                Toast.makeText(this, "This device is not supported.", Toast.LENGTH_LONG).show();
                //finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        JLog.print(TAG, "Connected");
        if (isTracking())
            mLocationClient.requestLocationUpdates(mLocationRequest, this);

    }

    @Override
    public void onDisconnected() {
        JLog.print(TAG, "Disconnected");
        Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    void showErrorDialog(int code) {
        GooglePlayServicesUtil.getErrorDialog(code, this, CONNECTION_FAILURE_RESOLUTION_REQUEST).show();
    }

    public Location getLastLocation(){
        return mLocationClient.getLastLocation();
    }

    @Override
    public void onLocationChanged(Location location) {

    }


    public boolean isTracking() {
        return tracking;
    }

    public void setTracking(boolean tracking) {
        this.tracking = tracking;
    }


    public void startTracking() {
        startTracking(null);
    }

    public void startTracking(TrackingOptions opt) {

        LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        setTracking(true);

        if (servicesConnected(true)) {
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                JAlert.showGPSDialog(this, getString(R.string.gps_missing_title), getString(R.string.gps_missing_msg), getString(R.string.gps_missing_activate), getString(R.string.gps_missing_cancel), requestLocationUpdates);
            else {
                if (mLocationClient.isConnected()) mLocationClient.requestLocationUpdates(mLocationRequest, LocationActivity.this);
                else mLocationClient.connect();
            }
        }

    }

    public void stopTracking() {
        if (mLocationClient.isConnected() && tracking) {
            mLocationClient.removeLocationUpdates(this);
            setTracking(false);
        }
    }

    public class GPSStatusBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            JLog.print("Received: " + arg1.getAction());
            if (arg1.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    JAlert.showGPSDialog(LocationActivity.this, getString(R.string.gps_missing_title), getString(R.string.gps_missing_msg), getString(R.string.gps_missing_activate), getString(R.string.gps_missing_cancel), requestLocationUpdates);
            }

        }
    }

    private Runnable requestLocationUpdates = new Runnable() {
        @Override
        public void run() {
            if (mLocationClient.isConnected())
                mLocationClient.requestLocationUpdates(mLocationRequest, LocationActivity.this);
            else
                mLocationClient.connect();
        }
    };



}
