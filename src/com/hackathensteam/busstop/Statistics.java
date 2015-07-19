package com.hackathensteam.busstop;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Statistics extends Activity implements View.OnClickListener {

	private static final String SPACE = "  ";
	private static final String EURO = "\u20ac";
	private TextView distance;
	private TextView moneySaved;
	private TextView totalEarnedMoney;
	private TextView estimatedTime;
	private TextView co2;
	private TextView points;
	public static double latStart;
	public static double longStart;
	public static double latDestination;
	public static double longDestination;
	private double estimatedTimeMinutes = 10.1;
	private ImageButton stop, map, statistics, ticket;

	// on create function
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistics);

		// menu
		stop = (ImageButton) findViewById(R.id.imageButtonStop);
		stop.setOnClickListener(this);
		map = (ImageButton) findViewById(R.id.ImageButtonMap);
		map.setOnClickListener(this);
		statistics = (ImageButton) findViewById(R.id.ImageButtonStatistics);
		statistics.setOnClickListener(this);
		ticket = (ImageButton) findViewById(R.id.ImageButtonTicket);
		ticket.setOnClickListener(this);

		estimatedTime = (TextView) findViewById(R.id.estimatedTime);
		//estimatedTime.setText(SPACE + estimatedTimeMinutes + " m");

		moneySaved = (TextView) findViewById(R.id.moneySaved);
		//moneySaved.setText(SPACE + "2.3 " + EURO);

		co2 = (TextView) findViewById(R.id.co2);
		
		
		points = (TextView) findViewById(R.id.points);
		//points.setText(SPACE + "85");
		float distanceInMeters = Map.overalldistance;

		 // int carDistance;
		  // +parking distance
		  float carLitres = (12*distanceInMeters)/1000;
		  float money = (float) ((carLitres*1.60) + 1.30);
		  
		  moneySaved.setText(SPACE + money + EURO);
		  
		  float co=(125*distanceInMeters)/100000;
		  
		  co2.setText(SPACE +co +" g");
		  
		  int point2=(int) (money*102);
		  points.setText(SPACE + point2);
		  
		  int est_time=(int) distanceInMeters*2;
		  estimatedTime.setText(SPACE + est_time + " m");
		// distance.setText(Float.toString(distanceInMeters));
		//distance.setText(Double.toString(latStart));

		// 9 lit/100km
		// 125 grams of CO2 per kilometre

		// 100.000 9lit
		// carDistance carLitres

		// +parking distance
		// carLitres = (12*carDistance)/100.000
		// moneyEarned = (carLitres*1.60) - 0.70

		// totalMoneyEarned = totalMoneyEarned + moneyEarned; //write to file
		// pointSystem

	}

	// function upeuthuni gia to handling to go button
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ImageButtonMap:
			try {
				Class mapclass = Class.forName("com.hackathensteam.busstop.Map");
				Intent mapintent = new Intent(this, mapclass);
				startActivity(mapintent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.imageButtonStop:
			try {
				Class mapclass = Class.forName("com.hackathensteam.busstop.MainActivity");
				Intent mapintent = new Intent(this, mapclass);
				startActivity(mapintent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case R.id.ImageButtonTicket:
        	try {
        		Class ticketclass=Class.forName("com.hackathensteam.busstop.Ticket");
				Intent ticketintent=new Intent(this,ticketclass);
				startActivity(ticketintent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	break;
		}
	}
}
