package com.example.osmsample;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

	private MapView mapView;
	private ItemizedIconOverlay<OverlayItem> myLocationOverlay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.mapView = new MapView(this, 256);
        this.mapView.setClickable(true);
        this.mapView.setBuiltInZoomControls(true);
        this.mapView.getController().setZoom(17);
        this.mapView.getController().setCenter(new GeoPoint(35.64483, 139.40881));
        setContentView(mapView);
        ArrayList<OverlayItem> overlays = getOverlays();
        ResourceProxy resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());
		OnItemGestureListener<OverlayItem> gesture = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
			@Override
			public boolean onItemLongPress(int arg0, OverlayItem arg1) {
				// TODO Auto-generated method stub
				return true;
			}
			@Override
			public boolean onItemSingleTapUp(int arg0, OverlayItem arg1) {
				// TODO Auto-generated method stub
				return onSingleTapUpHelper(arg0, arg1);
			}        	
		};
        this.myLocationOverlay = new ItemizedIconOverlay<OverlayItem>(overlays, gesture, resourceProxy);
        this.mapView.getOverlays().add(this.myLocationOverlay);
        this.mapView.invalidate();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@SuppressLint("ShowToast")
	public boolean onSingleTapUpHelper(int i, OverlayItem item) {
		StringBuffer bf = new StringBuffer();
		bf.append("Tap: ").append(item.mTitle).append(":").append(item.mDescription);
		Toast.makeText(getApplicationContext(), bf.toString(), Toast.LENGTH_LONG).show();
		return true;
	}

	private ArrayList<OverlayItem> getOverlays() {
        ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
        HttpClient httpClient = new DefaultHttpClient();
        
        StringBuilder uri = new StringBuilder("http://10.0.2.2:3000/maps.json");
        HttpGet request = new HttpGet(uri.toString());
        HttpResponse httpResponse = null;
         
        try {
            httpResponse = httpClient.execute(request);
        } catch (Exception e) {
            Log.d("MainActivity", "Error HTTP Execute");
            return overlays;
        }
         
        int status = httpResponse.getStatusLine().getStatusCode();
         
        JSONArray rootObject = null;
        if (HttpStatus.SC_OK == status) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                httpResponse.getEntity().writeTo(outputStream);
                String data = outputStream.toString();
                rootObject = new JSONArray(data);
            } catch (Exception e) {
            	Log.e("MainActivity", "rootObject", e);
            }
        } else {
            Log.d("MainActivity", "Status" + status);
            return overlays;
        }
        if (rootObject == null) {
        	Log.d("MainActivity", "rootObject is null");
        	return overlays;
        }
        try {
        	for (int i = 0; i < rootObject.length(); i++) {
        		JSONObject obj = rootObject.getJSONObject(i);
        		double latitude = obj.getDouble("latitude");
        		double longitude = obj.getDouble("longitude");
                GeoPoint overlayPoint = new GeoPoint(latitude, longitude);
                String title = obj.getString("title");
                overlays.add(new OverlayItem(title, title, overlayPoint));        		
        	}
        } catch (Exception e) {
        	Log.d("MainActivity", "jsonError");
        }
        return overlays;
	}
	
}
