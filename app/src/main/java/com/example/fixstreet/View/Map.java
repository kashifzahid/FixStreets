package com.example.fixstreet.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;

import com.example.fixstreet.R;
import com.example.fixstreet.Volley.VolleyPostCallBack;
import com.example.fixstreet.Volley.VolleyRequest;

import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class Map extends AppCompatActivity {

    private static final String TAG = "Map";
    MapView map = null;
    IMapController mapController;
    Geocoder geocoder;
    List<Address> addresses;

    TextView txt_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_map);
        map = (MapView) findViewById(R.id.map);
        txt_address=findViewById(R.id.txt_address);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        mapController = map.getController();
        mapController.setZoom(13);
        GeoPoint startPoint = new GeoPoint(20.5992, 72.9342);
        final Marker startMarker = new Marker(map);
        startMarker.setDraggable(true);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);

        startMarker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.e(TAG, "onDrag: "+marker.getPosition());
                displayLocation(marker.getPosition());
            }

            @Override
            public void onMarkerDragStart(Marker marker) {

            }
        });

    }

    private void displayLocation(GeoPoint position) {
        mapController.setCenter(position);
        final String requestString = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" +
                Double.toString(position.getLatitude()) + "&lon=" + Double.toString( position.getLongitude()) + "&zoom=18&addressdetails=1";

        VolleyRequest.GetRequest(this, requestString, new VolleyPostCallBack() {
            @Override
            public void OnSuccess(JSONObject jsonObject) {

                Log.e(TAG, "OnSuccess: "+jsonObject );
            }

            @Override
            public void OnFailure(String err) {

            }
        });
//        try {
//            @SuppressWarnings("unused")
//            Request request = builder.sendRequest(null, new RequestCallback() {
//                @Override
//                public void onResponseReceived(Request request, Response response) {
//                    if (response.getStatusCode() == 200) {
//                        String city = "";
//                        try {
//                            JSONValue json = JSONParser.parseStrict(response);
//                            JSONObject address = json.isObject().get("address").isObject();
//                            final String quotes = "^\"|\"$";
//
//                            if (address.get("city") != null) {
//                                city = address.get("city").toString().replaceAll(quotes, "");
//                            } else if (address.get("village") != null) {
//                                city = address.get("village").toString().replaceAll(quotes, "");
//                            }
//                        } catch (Exception e) {
//                        }
//                    }
//                }
//            });
//        } catch (Exception e) {
//        }
        //txt_address.setText(address+" "+city+" "+country);

    }

    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause(){
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
}
