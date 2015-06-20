package com.deltainductions.rb.deltatask3;

import android.content.Context;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;



public class MainActivity extends AppCompatActivity implements LocationListener{
    private static String TAG = "TAG";
    LocationTask task;
    EditText editText;
    Double latitude,longitude;
    TextView textView,textView2,textView3;
    Button button;
    static String location=null;
    String text = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RelativeLayout relativelayout = (RelativeLayout)findViewById(R.id.relativelayout);
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria,false);
        Log.d(TAG, provider);
        if(provider!=null) {
            Location location1 = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider,2000,1,this);
            if (location1 != null) {
                Log.d(TAG, location1.toString());
                onLocationChanged(location1);
            } else {
                Toast.makeText(getApplicationContext(), "location not found", Toast.LENGTH_LONG).show();
            }
        }
         else
        {
        Toast.makeText(getApplicationContext(),"Provider is null",Toast.LENGTH_LONG).show();
        }
        Geocoder geocoder= new Geocoder(this, Locale.getDefault());
       try {
            location=geocoder.getFromLocation(latitude,longitude,1).get(0).getLocality();
            location = location.toLowerCase();
            Log.d(TAG,location);
        } catch (IOException e) {

        }

        editText = (EditText)findViewById(R.id.edittext);
        textView = (TextView)findViewById(R.id.textview);
        textView2 = (TextView)findViewById(R.id.textview2);
        textView3 = (TextView)findViewById(R.id.textview3);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equals(""))
                location = editText.getText().toString();
                Log.d(TAG, location);
               text = location;
               textView.setText(text);
               task = new LocationTask();
                try {
                    ArrayList<HashMap<String,String>> mainarray = task.execute().get();
                    Float i = Float.parseFloat(mainarray.get(1).get("value"));
                    Float j = Float.parseFloat(mainarray.get(1).get("max"));
                    Float k = Float.parseFloat(mainarray.get(1).get("min"));
                    String S = mainarray.get(0).get("name");
                    i=i-Float.parseFloat("273.14");
                    Double roundOff = Math.round(i * 100.0) / 100.0;
                    String temp =roundOff.toString();
                    j=j-Float.parseFloat("273.14");
                    Double roundOff1 = Math.round(j * 100.0) / 100.0;
                    String temp1 =roundOff1.toString();
                    k=k-Float.parseFloat("273.14");
                    Double roundOff2 = Math.round(k * 100.0) / 100.0;
                    String temp2 =roundOff2.toString();
                    textView.setText(temp+"°");
                    textView3.setText(location+" - "+S);
                    if(!(temp1.equals(temp2))) {
                        textView2.setText(temp1 + "/" + temp2);
                        editText.setText(location+" - "+S);
                    }
                    if(S.equals("clear sky"))
                        relativelayout.setBackgroundResource(R.drawable.sunnyskymeadowim);
                    else
                        relativelayout.setBackgroundResource(R.drawable.clouds);
                } catch (InterruptedException e) {

                } catch (ExecutionException e) {

                }
            }
        });
    }
    public void onLocationChanged(Location location)
    {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public static class LocationTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>
    {

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
            String downloadurl = "http://api.openweathermap.org/data/2.5/weather?q=" + location + "&mode=xml";
            ArrayList<HashMap<String, String>> newarray = null;
            try {
                URL url = new URL(downloadurl);
                try {
                    HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
                    urlconnection.setRequestMethod("GET");
                    InputStream inputstream = urlconnection.getInputStream();
                    newarray = parseXML(inputstream);
                } catch (IOException e) {

                }

            } catch (MalformedURLException e) {
            Log.d(TAG,"URL ERROR");
            }
            return newarray;
        }
    }
    public static ArrayList<HashMap<String,String>> parseXML(InputStream inputstream)
    {
        DocumentBuilderFactory documententfactory = DocumentBuilderFactory.newInstance();
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<HashMap<String, String>>();
        try {
            DocumentBuilder documentbuilder = documententfactory.newDocumentBuilder();
            Document doc = documentbuilder.parse(inputstream);
            org.w3c.dom.Element root = doc.getDocumentElement();
            NodeList nodeList = root.getElementsByTagName("temperature");
            NodeList nodeList2 = root.getElementsByTagName("clouds");
            Node currentnode = null;
            Node currentnode2 = null;
            Node att = null;
            Node att2 = null;
            NamedNodeMap currentmap = null;
            NamedNodeMap currentmap2=null;
            HashMap<String, String> hashMap = null,hashMap2 = null;
            for(int j=0;j<nodeList2.getLength();j++)
            {
                currentnode2 = nodeList2.item(j);
                hashMap2 = new HashMap<>();
                currentmap2 = currentnode2.getAttributes();
                att2 = currentmap2.item(1);
                hashMap2.put("name",att2.getTextContent());
                Log.d(TAG, att2.getTextContent());
                arrayList.add(hashMap2);
            }
            for (int i = 0; i < nodeList.getLength(); i++) {
                currentnode = nodeList.item(i);
                hashMap = new HashMap<>();
                hashMap.put("temperature", currentnode.getNodeName());
                arrayList.add(hashMap);
                currentmap = currentnode.getAttributes();
                for (int k = 0; k < currentmap.getLength(); k++) {
                    att = currentmap.item(k);
                    switch (k) {
                        case 0:
                            hashMap.put("value", att.getTextContent());
                            break;
                        case 1:
                            hashMap.put("min", att.getTextContent());
                            break;
                        case 2:
                            hashMap.put("max", att.getTextContent());
                            break;
                        case 3:
                            hashMap.put("unit", att.getTextContent());
                            break;
                    }
                }
            }

        }catch (Exception e) {}
         return arrayList;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
