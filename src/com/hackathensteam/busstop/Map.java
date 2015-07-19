package com.hackathensteam.busstop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Map extends Activity implements View.OnClickListener  {
 
 private MapView myOpenMapView;
 private IMapController myMapController;
 private TextView NextBusStop;
 private Location lastLocation;
 public static float overalldistance;
 
 LocationManager locationManager;
 ImageButton stop,map,statistics,ticket,stopbutton;
 ArrayList<OverlayItem> overlayItemArray,overlayItemArrayBusStops,gpsTruckBus;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        myOpenMapView = (MapView)findViewById(R.id.mapview);
        NextBusStop = (TextView)findViewById(R.id.textViewNextBusStop);
        
  //menu
  		stop=(ImageButton)findViewById(R.id.imageButtonStop);
  		stop.setOnClickListener(this);
  		map=(ImageButton)findViewById(R.id.ImageButtonMap);
  		map.setOnClickListener(this);
  		statistics=(ImageButton)findViewById(R.id.ImageButtonStatistics);
  		statistics.setOnClickListener(this);
  		ticket=(ImageButton)findViewById(R.id.ImageButtonTicket);
  		ticket.setOnClickListener(this);
  		stopbutton=(ImageButton)findViewById(R.id.imageButtonbusStop);
        myOpenMapView.setMultiTouchControls(true);
//      myOpenMapView.setBuiltInZoomControls(true);
        myMapController = myOpenMapView.getController();
        myOpenMapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
        myMapController.setZoom(16);
        

        //--- Create Overlay
        overlayItemArray = new ArrayList<OverlayItem>();
        overlayItemArrayBusStops= new ArrayList<OverlayItem>();
        gpsTruckBus= new ArrayList<OverlayItem>();
        
        DefaultResourceProxyImpl defaultResourceProxyImpl = new DefaultResourceProxyImpl(this);
        MyItemizedIconOverlay myItemizedIconOverlay = new MyItemizedIconOverlay(overlayItemArray, null, defaultResourceProxyImpl);
        myOpenMapView.getOverlays().add(myItemizedIconOverlay);
        
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        
        if(lastLocation != null)
        {
         updateLoc(lastLocation);
        }
        
        //Add Scale Bar
//        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(this);
//        myOpenMapView.getOverlays().add(myScaleBarOverlay);
        
        LoadBusStops();
//      GeoPoint myPoint1 = new GeoPoint(51.510357, -0.116773);
//      OverlayItem newMyLocationItem1 = new OverlayItem(
//    	       "My Location2", "My Location2", myPoint1);
//   	     overlayItemArrayBusStops.add(newMyLocationItem1);
        
       // LoadBeggingBusStop();
 //LoadOverallDistance();
        LoadOverallDistanceDemo();
    }
    
    
 private void LoadOverallDistanceDemo(){overalldistance=overlayItemArrayBusStops.size();}
 private void LoadOverallDistance(){
	 
	 GeoPoint begin=overlayItemArrayBusStops.get(0).getPoint();
	 float beg_lat= (float) begin.getLatitude();
	 float beg_lon= (float) begin.getLongitude();
	 
	 GeoPoint final_dest=overlayItemArrayBusStops.get(overlayItemArrayBusStops.size()).getPoint();
	 float final_lat= (float) final_dest.getLatitude();
	 float final_lon= (float) final_dest.getLongitude();
	 
	  Location loc1 = new Location("");
	  loc1.setLatitude(beg_lat);
	  loc1.setLongitude(beg_lon);

	  Location loc = new Location("");
	  loc.setLatitude(final_lat);
	  loc.setLongitude(final_lon);
	  overalldistance = loc1.distanceTo(loc);
	 
 }

    
private void LoadBusStops() {
	InputStreamReader is =null ;
	String stop_id,busstop_name,stop_lat,stop_lon;
	try {
		 is = new InputStreamReader(getAssets().open(MainActivity.spinner_Select_bus.getSelectedItem()+".csv"));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	BufferedReader reader = new BufferedReader(is);
	
	try {
        String line;
        int i=0;
//        GeoPoint myPoint2 = new GeoPoint(38036508, 23802559);
//        OverlayItem newMyLocationItem2 = new OverlayItem("", "", myPoint2);
//        overlayItemArrayBusStops.add(newMyLocationItem2);
        while ((line = reader.readLine()) != null) {
             String[] RowData = line.split(",");
             stop_id = RowData[0];
             busstop_name = RowData[1];
             stop_lat=RowData[2];
             stop_lon=RowData[3];
//             System.out.println("'"+MainActivity.spinner_Select_ending_bus_stop.getSelectedItem()+"'");
            
             if(i>=1) {
             float lat=Float.parseFloat(stop_lat);
             float lon=Float.parseFloat(stop_lon);
             
             GeoPoint myPoint1 = new GeoPoint(lat, lon);
             OverlayItem newMyLocationItem1 = new OverlayItem(stop_id, busstop_name, myPoint1);
             overlayItemArrayBusStops.add(newMyLocationItem1);
             }
             if(busstop_name.equals(MainActivity.spinner_Select_ending_bus_stop.getSelectedItem())){break;}
             i+=1;
        }
    }
    catch (IOException ex) {
       
    }
    finally {
        try {
            is.close();
        }
        catch (IOException e) {
       
        }
    }
	
	}

private void LoadBeggingBusStop() {
	if(overlayItemArrayBusStops.size()>0){
	for(int i=0;i<overlayItemArrayBusStops.size();i++){
		GeoPoint point=overlayItemArrayBusStops.get(0).getPoint();
		Location loc5 = new Location("");
//	     GeoPoint tempbusstop=overlayItemArrayBusStops.get(0).getPoint();
	     loc5.setLatitude(point.getLatitude());
	     loc5.setLongitude(point.getLongitude());
        Float distanceInMeters = loc5.distanceTo(lastLocation);
        if(distanceInMeters<20){break;}
        else{overlayItemArrayBusStops.remove(0);}
	}
	}
}

    
    @Override
 protected void onResume() {
  // TODO Auto-generated method stub
  super.onResume();
  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
  locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);
 }

 @Override
 protected void onPause() {
  // TODO Auto-generated method stub
  super.onPause();
  locationManager.removeUpdates(myLocationListener);
 }

 private void updateLoc(Location loc){
	 
     GeoPoint locGeoPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
     
     //save gps location
     OverlayItem GpsPosition = new OverlayItem("", "", locGeoPoint);
     gpsTruckBus.add(GpsPosition);
     
     myMapController.setCenter(locGeoPoint); 
     setOverlayLoc(loc);
     myOpenMapView.invalidate();
     
     //find next bus stop
     if(overlayItemArrayBusStops.size()>0){
	     Location loc1 = new Location("");
	     GeoPoint tempbusstop=overlayItemArrayBusStops.get(0).getPoint();
	     loc1.setLatitude(tempbusstop.getLatitude());
	     loc1.setLongitude(tempbusstop.getLongitude());
	     Float distanceInMeters = loc1.distanceTo(loc);
	     NextBusStop.setTextSize(18);
	     if(overlayItemArrayBusStops.size()>1)
	     {
	    	 NextBusStop.setBackgroundColor(Color.GRAY);
	    	 NextBusStop.setTextColor(Color.WHITE);
	    	 NextBusStop.setText("Reaching next station in "+Float.toString(distanceInMeters.intValue())+" meters.         "+'\n'+
	    			 "Pending Bus Stops: "+overlayItemArrayBusStops.size());
	     }
     
	     if(overlayItemArrayBusStops.size()==1)
	     {
	    	 NextBusStop.setBackgroundColor(Color.GRAY);
	    	 NextBusStop.setTextColor(Color.WHITE);
	    	 NextBusStop.setText("Reaching destination in "+Float.toString(distanceInMeters.intValue())+" meters. Press Bus Stop Button!");
	     }
     
	     //check if next busstop is close
	     if(distanceInMeters<=15&&overlayItemArrayBusStops.size()>1){
	    	 overlayItemArrayBusStops.remove(0);
	    	 }
	     
	     //check if next busstop is the last
	     if(distanceInMeters<=15&&overlayItemArrayBusStops.size()==1){
	//    	 overlayItemArrayBusStops.remove(0);
	    	 //donisi
	    	 stopbutton.setVisibility(0);
	    	 }
     }
    }

	//function upeuthuni gia to handling to go button
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId())
		{
   
     	
   	
	     case R.id.imageButtonStop:
	     	try {
	     			Class stopclass=Class.forName("com.hackathensteam.busstop.MainActivity");
					Intent stopintent=new Intent(this,stopclass);
					startActivity(stopintent);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	     	break;
	       
	     case R.id.ImageButtonStatistics:
	        	try {
	        		Class statisticsclass=Class.forName("com.hackathensteam.busstop.Statistics");
					Intent statisticsintent=new Intent(Map.this,statisticsclass);
					startActivity(statisticsintent);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	break;
	     	
	        	
	     case R.id.ImageButtonTicket:
	        	try {
	        		Class ticketclass=Class.forName("com.hackathensteam.busstop.Ticket");
					Intent ticketintent=new Intent(Map.this,ticketclass);
					startActivity(ticketintent);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	break;
		}	
	}
 
 private void setOverlayLoc(Location overlayloc){
  GeoPoint overlocGeoPoint = new GeoPoint(overlayloc);
  //---
     overlayItemArray.clear();
     OverlayItem newMyLocationItem = new OverlayItem(
       "My Location", "My Location", overlocGeoPoint);
     overlayItemArray.add(newMyLocationItem);
     //---
 }
    
    private LocationListener myLocationListener
    = new LocationListener(){

  @Override
  public void onLocationChanged(Location location) {
   // TODO Auto-generated method stub
   updateLoc(location);
  }

  @Override
  public void onProviderDisabled(String provider) {
   // TODO Auto-generated method stub
   
  }

  @Override
  public void onProviderEnabled(String provider) {
   // TODO Auto-generated method stub
   
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
   // TODO Auto-generated method stub
   
  }
     
    };

    
private class MyItemizedIconOverlay extends ItemizedIconOverlay<OverlayItem>{

  public MyItemizedIconOverlay(List<OverlayItem> pList,org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener<OverlayItem> pOnItemGestureListener,
		  ResourceProxy pResourceProxy)
  {
   
	  super(pList, pOnItemGestureListener, pResourceProxy);
   // TODO Auto-generated constructor stub
  }

  @Override
  public void draw(Canvas canvas, MapView mapview, boolean arg2) {
   // TODO Auto-generated method stub
   super.draw(canvas, mapview, arg2);
  
	   if(!gpsTruckBus.isEmpty())
	   {
		    for(int i=0;i<gpsTruckBus.size();i++)
		    {
		    //overlayItemArray have only ONE element only, so I hard code to get(0)
			    GeoPoint in = gpsTruckBus.get(i).getPoint();
			    
			    Point out = new Point();
			    mapview.getProjection().toPixels(in, out);
			    Bitmap bm = BitmapFactory.decodeResource(getResources(), 
			    		R.drawable.gpscircle);
			    
			    canvas.drawBitmap(bm, 
			      out.x - bm.getWidth()/2,  //shift the bitmap center
			      out.y - bm.getHeight()/2,  //shift the bitmap center
			      null);
		    }
	   }
   
	   if(!overlayItemArray.isEmpty())
	   {
	    
	    //overlayItemArray have only ONE element only, so I hard code to get(0)
	    GeoPoint in = overlayItemArray.get(0).getPoint();
	    
	    Point out = new Point();
	    mapview.getProjection().toPixels(in, out);
	    
	    Bitmap bm = BitmapFactory.decodeResource(getResources(), 
	    		R.drawable.bus_icon);
	  
	    canvas.drawBitmap(bm, 
	      out.x - bm.getWidth()/2,  //shift the bitmap center
	      out.y - bm.getHeight()/2,  //shift the bitmap center
	      null);
	   }
	   
	   if(!overlayItemArrayBusStops.isEmpty())
	   {
		    for(int i=0;i<overlayItemArrayBusStops.size();i++)
		    {
		    //overlayItemArray have only ONE element only, so I hard code to get(0)
			    GeoPoint in = overlayItemArrayBusStops.get(i).getPoint();
			    
			    Point out = new Point();
			    mapview.getProjection().toPixels(in, out);
			    
			    Bitmap bm = BitmapFactory.decodeResource(getResources(), 
			    		R.drawable.bus_stop_icon);
			    canvas.drawBitmap(bm, 
			      out.x - bm.getWidth()/2,  //shift the bitmap center
			      out.y - bm.getHeight(),  //shift the bitmap center
			      null);
		    }
	   }
	   
	
  }

  @Override
public boolean onSingleTapConfirmed(MotionEvent event, MapView mapView) {
	// TODO Auto-generated method stub
	  return true;
}

@Override
  public boolean onSingleTapUp(MotionEvent event, MapView mapView) {
   // TODO Auto-generated method stub
   //return super.onSingleTapUp(event, mapView);
   return true;
  }
    }
}
