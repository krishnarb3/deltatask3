package com.deltainductions.rb.deltatask3;

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
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static android.widget.Toast.makeText;


public class MainActivity extends AppCompatActivity {
    private static String TAG = "TAG";
    LocationTask task;
    EditText editText;
    TextView textView,textView2;
    Button button;
    static String location;
    String text = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RelativeLayout relativelayout = (RelativeLayout)findViewById(R.id.relativelayout);
        editText = (EditText)findViewById(R.id.edittext);
        textView = (TextView)findViewById(R.id.textview);
        textView2 = (TextView)findViewById(R.id.textview2);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               location = editText.getText().toString();
               text = location;
               textView.setText(text);
               task = new LocationTask();
                try {
                    ArrayList<HashMap<String,String>> mainarray = task.execute().get();
                    Float i = Float.parseFloat(mainarray.get(0).get("value"));
                    Float j = Float.parseFloat(mainarray.get(0).get("max"));
                    Float k = Float.parseFloat(mainarray.get(0).get("min"));
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
                    textView.setText(temp+"°\n");
                    if(!(temp1.equals(temp2))) {
                        textView2.setText(temp1 + "/" + temp2);
                    }
                    relativelayout.setBackgroundResource(R.drawable.clouds);
                } catch (InterruptedException e) {

                } catch (ExecutionException e) {

                }
            }
        });
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
            //Log.d(TAG,newarray.toString());
            return newarray;
        }
    }
    public static ArrayList<HashMap<String,String>> parseXML(InputStream inputstream)
    {
        DocumentBuilderFactory documententfactory = DocumentBuilderFactory.newInstance();
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<HashMap<String, String>>();
        /*DocumentBuilderFactory documententfactory1 = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentbuilder1 = null;
        try {
            documentbuilder1 = documententfactory1.newDocumentBuilder();
            Document doc1 = null;
            doc1 = documentbuilder1.parse(inputstream);
            org.w3c.dom.Element root1 = doc1.getDocumentElement();
            NodeList nodeList1 = root1.getElementsByTagName("clouds");
            Node currentnode1 = null;
            NamedNodeMap newmap1 = null, currentmap1 = null;
            Node att1 = null;
            HashMap<String, String> hashMap1 = null;
            Log.d(TAG, (Integer.toString(nodeList1.getLength())));
            for (int j = 0; j < nodeList1.getLength(); j++) {
                currentnode1 = nodeList1.item(j);
                hashMap1 = new HashMap<>();
                Log.d(TAG, currentnode1.getNodeName());
                hashMap1.put("clouds", currentnode1.getNodeName());
                arrayList.add(hashMap1);
                currentmap1 = currentnode1.getAttributes();
                att1 = currentmap1.item(1);
                Log.d(TAG, att1.getNodeName() + " " + att1.getTextContent());
                hashMap1.put("name", att1.getTextContent());
            }
            arrayList.add((HashMap<String, String>) currentmap1);
        }catch (Exception  e){}*/
        try {
            DocumentBuilder documentbuilder = documententfactory.newDocumentBuilder();
            Document doc = documentbuilder.parse(inputstream);
            org.w3c.dom.Element root = doc.getDocumentElement();
            //Log.d(TAG,root.getTagName());
            NodeList nodeList = root.getElementsByTagName("temperature");
            NodeList nodeList2 = root.getElementsByTagName("clouds");
            Log.d(TAG, nodeList2.toString());
            Node currentnode = null;
            Node currentnode2 = null;
            Node att = null;
            Node att2 = null;
            NamedNodeMap newmap = null, currentmap = null;
            NamedNodeMap newmap2=null,currentmap2=null;
            Log.d(TAG, (Integer.toString(nodeList.getLength())));
            Log.d(TAG, (Integer.toString(nodeList2.getLength())));
            HashMap<String, String> hashMap = null,hashMap2 = null;
            for(int j=0;j<nodeList2.getLength();j++)
            {
                currentnode2 = nodeList2.item(j);
                hashMap2 = new HashMap<>();
                Log.d(TAG, currentnode2.getNodeName());
                /*currentmap2 = currentnode2.getAttributes();
                att2 = currentmap2.item(0);
                Log.d(TAG, att2.getTextContent());
                hashMap2.put("name",att2.getTextContent());
                arrayList.add((HashMap<String,String>) currentmap2);*/
            }
            for (int i = 0; i < nodeList.getLength(); i++) {
                currentnode = nodeList.item(i);
                hashMap = new HashMap<>();
                Log.d(TAG, currentnode.getNodeName());
                hashMap.put("temperature", currentnode.getNodeName());
                arrayList.add(hashMap);
                currentmap = currentnode.getAttributes();
                for (int k = 0; k < currentmap.getLength(); k++) {
                    att = currentmap.item(k);
                    //Log.d(TAG, att.getNodeName() + " " + att.getTextContent());
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
                    //arrayList.add(hashMap);
                }
                arrayList.add((HashMap<String, String>) currentmap);
                Log.d(TAG, "ERROR");
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