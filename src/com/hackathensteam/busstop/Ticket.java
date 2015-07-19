package com.hackathensteam.busstop;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Ticket extends Activity implements  View.OnClickListener,OnItemSelectedListener{
	private static final String BASE_URI = "snf-665777.vm.okeanos.grnet.gr:5000/show_form";
	private static final String REQUEST_PATH = "5000";
	private final String USER_AGENT = "Mozilla/5.0";
	ImageButton stop,map,statistics,ticket;
	TextView name,afm,validation;
	EditText textname,textafm;
	Button getfreeticket,check;
	
	static Spinner spinner1;

	String[] spin = { "Karta anergias", "Karta sitisis" };

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ticket);
		
		name=(TextView)findViewById(R.id.textViewName);
		afm=(TextView)findViewById(R.id.textViewAFM);
		validation=(TextView)findViewById(R.id.textViewValidation);
		spinner1=(Spinner)findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spin);
		adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter_state);
		spinner1.setOnItemSelectedListener(this);
		
		name.setText("Name:");
		name.setTextSize(16);
		
		afm.setText("AFM:");
		afm.setTextSize(16);
		
		textname=(EditText)findViewById(R.id.editTextName);
		textafm=(EditText)findViewById(R.id.editTextAFM);
		getfreeticket=(Button)findViewById(R.id.buttonGetFreeTicket);
		getfreeticket.setOnClickListener(this);
		
		check=(Button)findViewById(R.id.buttonCheck);
		check.setOnClickListener(this);
		
		stop=(ImageButton)findViewById(R.id.imageButtonStop);
  		stop.setOnClickListener(this);
  		map=(ImageButton)findViewById(R.id.ImageButtonMap);
  		map.setOnClickListener(this);
  		statistics=(ImageButton)findViewById(R.id.ImageButtonStatistics);
  		statistics.setOnClickListener(this);
  		ticket=(ImageButton)findViewById(R.id.ImageButtonTicket);
  		ticket.setOnClickListener(this);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		//HttpURLConnectionExample http = new HttpURLConnectionExample();
		 
		System.out.println("Testing 1 - Send Http GET request");
//		try {
//			sendGet();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
  
//		System.out.println("\nTesting 2 - Send Http POST request");
//		try {
//			sendPost();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
		
	}

	private void sendGet() throws Exception {

//		Uri.Builder builder = Uri.parse(BASE_URI).buildUpon();
//		builder.appendPath(REQUEST_PATH);
//		
//		builder.appendQueryParameter("name", "alexis_tsipras3");
//		builder.appendQueryParameter("afm", "000222xxxx");
//		Uri builtUri = builder.build();
//		String url = builtUri.toString();
//		System.out.println("lala");
//		System.out.println(url);
		String url = "http://snf-665777.vm.okeanos.grnet.gr:5000/get_entries";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("GET Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
			//System.out.println()
		}
		in.close();

		// print result
		String json=response.toString();
		System.out.println("This "+json);
//		{'eligibility': True, 'afm': u'110110551', 'name': u'John Doe'}
		if(json.contains("{'eligibility': True, 'afm': u'"+textafm.getText()+"', 'name': u'"+textname.getText()+"'}"))
		{
			validation.setTextColor(Color.GREEN);
			validation.setText("Free Ticket Granted!!!");
			validation.setTextSize(16);
			try {
				Class freeclass=Class.forName("com.hackathensteam.busstop.FreeTicket");
				Intent freeintent=new Intent(Ticket.this,freeclass);
				startActivity(freeintent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
//			afm.setText("true");
			
		}
		else{
			//afm.setText("false");
			if(!textname.getText().equals("")){
				
			
			validation.setTextColor(Color.RED);
			validation.setText("Validation Pending!");
			validation.setTextSize(16);
			}
		}
		

	}

	// HTTP POST request
	private void sendPost() throws Exception {
 
		String url = "http://snf-665777.vm.okeanos.grnet.gr:5000/add_to_form";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		//con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 
		//"Name: "++" AFM: "+textafm.getText());
		String urlParameters = "name="+textname.getText()+"&afm=" + textafm.getText();
		//'name':'Giannasdis Papadaki43s82232', 'afm':'000200002421122334ssdd
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(response.toString());
 
	}
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
					Intent statisticsintent=new Intent(Ticket.this,statisticsclass);
					startActivity(statisticsintent);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	break;
	        	
	     case R.id.ImageButtonMap:
	        	try {
	        		Class mapclass=Class.forName("com.hackathensteam.busstop.Map");
					Intent mapintent=new Intent(Ticket.this,mapclass);
					startActivity(mapintent);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	break;
	        	
	     case R.id.buttonGetFreeTicket:
			try {
//				System.out.println("Name: "+textname.getText()+" AFM: "+textafm.getText());
				sendPost();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        	break;
	        	
	     case R.id.buttonCheck:
				try {
//					System.out.println("Name: "+textname.getText()+" AFM: "+textafm.getText());
					sendGet();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        	break;
	     	
		}	
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
