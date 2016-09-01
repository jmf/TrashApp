package jmf.trashapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.GeomagneticField;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BePatientActivity extends AppCompatActivity {

    public final static String BEST_MATCH = "This will never be read either ;)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_be_patient);

        //Stop thread error message from happening: (May freeze the app)
        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        LocationManager locman = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //Set up GPS

        //Choose best location provider:
        Criteria crit = new Criteria();
        String provider = locman.getBestProvider(crit, false);

        Location location = locman.getLastKnownLocation(provider);

        //No location present.
        if(location==null) {
            Toast.makeText(BePatientActivity.this, "Could not get location. Please retry.", Toast.LENGTH_LONG).show();
        }
        else {
            //Get location fromm GPS
            double lon = (double) location.getLongitude();
            double lat = (double) location.getLatitude();

            //Here a position can be set for testing purposes:
            //double lon = 0;
            //double lat = 0;

            double factor = Math.PI / 180; //Degrees to radian
            //Calculate 2 km radius:
            //1 deg latitude = 111km
            double latmin = lat - (1 / 111.0);
            double latmax = lat + (1 / 111.0);
            //1 deg longitude = 111 km on equator times factor cos(longitude)
            double lonmin = lon - (1 / 111.0) * Math.cos(lon * factor);
            double lonmax = lon + (1 / 111.0) * Math.cos(lon * factor);

            Intent intent = getIntent();
            String op = intent.getStringExtra(MainActivity.OVERPASS_MESSAGE);

            //Add coordinates and ending to the request string:
            op = op.concat("("+ lonmin + "," + latmin + "," + lonmax + "," + latmax + ")");
            op = op.concat(";out body;");

            System.out.println("Query: "+op);
            String result="";
            String connectionURL="";

            //Create an URL for the quuery:
            try {
                connectionURL = "http://overpass-api.de/api/interpreter?data=" + java.net.URLEncoder.encode(op, "UTF8");
            }
            catch(java.io.UnsupportedEncodingException e){
                Toast.makeText(BePatientActivity.this, "Something, somewhere went terribly wrong...", Toast.LENGTH_SHORT).show();
            }

            System.out.println("Connecting to: "+connectionURL);

            double reallat=0; //Final latitude
            double reallon=0; //Final longitude
            double dist=0;    //Final distance

            try {
                //Sendd a query to Overpass
                URL url = new URL(connectionURL);
                InputStreamReader isr = new InputStreamReader(url.openStream());

                BufferedReader reader = new BufferedReader(isr);
                Intent newIntent = new Intent(this, MainActivity.class);
                startActivity(newIntent);
                String str;
                double distance=4;
                while((str=reader.readLine())!=null) {
                    if(str.contains("<node")){
                        double tmplon = Double.parseDouble(str.substring(str.indexOf("lon")+5,str.indexOf("lon")+15));
                        double tmplat = Double.parseDouble(str.substring(str.indexOf("lat")+5,str.indexOf("lat")+15));

                        dist = Math.sqrt((1/111.0)*Math.cos(lon*(Math.PI/180))*(1/111.0)*Math.cos(lon*(Math.PI/180))*(lon-tmplon)*(lon-tmplon) + (1/111.0)*(1/111.0)*(lat-tmplat)*(lat-tmplat));
                        if(dist<distance){
                            distance=dist;
                            reallat=tmplat;
                            reallon=tmplon;
                        }
                    }
                }
                reader.close();
            }
            catch(Exception e){
                Toast.makeText(BePatientActivity.this, "Error: "+e.getClass(), Toast.LENGTH_SHORT).show();
            }

            Intent newIntent = new Intent(this, CollectionList.class);

            String dir="";
            //Give directions that can be used with a compass or the sun as direction:
            if(lat<reallat){
                dir+="N";
            }
            else{
                dir+="S";
            }
            if(lon<reallon){
                dir+="E";
            }
            else{
                dir+="W";
            }

            result="Distance: "+dist*100+" meters\nDirection: "+ dir;

            newIntent.putExtra(BEST_MATCH, result);
            startActivity(newIntent);


        }
    }
}
