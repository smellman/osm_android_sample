package com.example.osmsample;

import java.util.ArrayList;

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
        GeoPoint overlayPoint = new GeoPoint(35.64483, 139.40881);
        ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
        overlays.add(new OverlayItem("明星大学", "OSCの会場だよ", overlayPoint));
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

	
}
