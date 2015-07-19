package com.hackathensteam.busstop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

//edw orizoume ti leitourgikotita tou menu
public class MainActivity extends ActionBarActivity implements OnItemSelectedListener,View.OnClickListener {

	//Initialize variables
	Spinner spinner_Select_starting_bus_stop;
	static Spinner spinner_Select_bus;
	static Spinner spinner_Select_ending_bus_stop;
	TextView textview_Select_starting_bus_stop,textview_Select_bus,textview_Select_ending_bus_stop;
	ImageButton stop,map,statistics,ticket;
	Button go;
	String[] starting_bus_stop = { "Syntagma", "Plateia Omonoias", "Ampelokipoi", "Eyaggelismos",
			   "Monastiraki" };
	String[] bus_lines = { "206", "237" };
	public String destination;
	ArrayList<String> ending_bus_stop = new ArrayList<String>();
	//String[] ending_bus_stop =new String[10];
	
	String csvname="";
	//on create function
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		ending_bus_stop[0]="theo";
		//epilogi arxikis stasis
		
		
		//epilogi tou leoforeiou
		spinner_Select_bus = (Spinner) findViewById(R.id.select_bus);
		textview_Select_bus = (TextView) findViewById(R.id.textView_select_bus);
		textview_Select_bus.setText("Select Bus Route");
		textview_Select_bus.setTextSize(16);
		ArrayAdapter<String> adapter_state1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, bus_lines);
		adapter_state1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_Select_bus.setAdapter(adapter_state1);
		spinner_Select_bus.setOnItemSelectedListener(this);
		csvname=(String)spinner_Select_bus.getSelectedItem();
		loadcsv(csvname);

		//epilogi telikis stasis
		spinner_Select_ending_bus_stop = (Spinner) findViewById(R.id.select_ending_bus_stop);
		textview_Select_ending_bus_stop = (TextView) findViewById(R.id.textView_select_ending_bus_stop);
		textview_Select_ending_bus_stop.setText("Select Final Destination");
		textview_Select_ending_bus_stop.setTextSize(16);
		ArrayAdapter<String> adapter_state2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ending_bus_stop);
		adapter_state2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_Select_ending_bus_stop.setAdapter(adapter_state2);
		
		//button go 
		go = (Button) findViewById(R.id.gobutton);
		go.setText("Go");
		go.setOnClickListener(this);
		

		
		//menu
		stop=(ImageButton)findViewById(R.id.imageButtonStop);
		stop.setOnClickListener(this);
		map=(ImageButton)findViewById(R.id.ImageButtonMap);
		map.setOnClickListener(this);
		statistics=(ImageButton)findViewById(R.id.ImageButtonStatistics);
		statistics.setOnClickListener(this);
		ticket=(ImageButton)findViewById(R.id.ImageButtonTicket);
		ticket.setOnClickListener(this);
		
	}
	
	
	private void loadcsv(String csvname){
		ending_bus_stop.clear();
		InputStreamReader is =null ;
		String busstop_name;
		try {
			 is = new InputStreamReader(getAssets().open(csvname+".csv"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(is);
		
		try {
	        String line;
	        
	        int i=0;

	        while ((line = reader.readLine()) != null) {
	             String[] RowData = line.split(",");
	            if (i>0){
	             busstop_name = RowData[1];
	             ending_bus_stop.add(busstop_name);
	            }
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
	
	//spinner list handling (pros to paron den to xrisimopoioume)
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			   long id) {
		spinner_Select_bus.setSelection(position);
		
			   destination = (String) spinner_Select_bus.getSelectedItem();
//			  selVersion.setText("Selected Android OS:" + selState);
			  csvname=(String)spinner_Select_bus.getSelectedItem();
				loadcsv(csvname);
			 }

			  public void onNothingSelected(AdapterView<?> arg0) {
			  // TODO Auto-generated method stub

			  }
			  
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}



	//function upeuthuni gia to handling to go button
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId())
		{
        case R.id.gobutton:
        	try {
				Class mapclass=Class.forName("com.hackathensteam.busstop.Map");
				Intent mapintent=new Intent(MainActivity.this,mapclass);
				startActivity(mapintent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	break;
        	
      	
        case R.id.ImageButtonMap:
        	try {
        		Class mapclass=Class.forName("com.hackathensteam.busstop.Map");
				Intent mapintent=new Intent(MainActivity.this,mapclass);
				startActivity(mapintent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	break;
        	
        case R.id.ImageButtonStatistics:
        	try {
        		Class statisticsclass=Class.forName("com.hackathensteam.busstop.Statistics");
				Intent statisticsintent=new Intent(MainActivity.this,statisticsclass);
				startActivity(statisticsintent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	break;
        	
        	
        	
        	
        case R.id.ImageButtonTicket:
        	try {
        		Class ticketclass=Class.forName("com.hackathensteam.busstop.Ticket");
				Intent ticketintent=new Intent(MainActivity.this,ticketclass);
				startActivity(ticketintent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	break;
        	
        	
       
          
		}
	}
}
