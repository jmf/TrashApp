package jmf.trashapp;

import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    boolean aluminium = false;
    boolean cans = false;
    boolean cardboard = false;
    boolean clothes = false;
    boolean paper = false;
    boolean plastic = false;
    boolean greenwaste = false;
    boolean electronics = false;

    public final static String OVERPASS_MESSAGE = "This will never be read ;)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void findTrash(View view){
        //Check if GPS is available
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!service.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(MainActivity.this, "Please activate GPS!", Toast.LENGTH_LONG).show();
        }

        //If GPS is available, see if at least one collection type is chosen
        else if(aluminium||cans||cardboard||clothes||paper||plastic||greenwaste||electronics){
            Intent intent = new Intent(this, BePatientActivity.class);
            //Concatenating first half of the message for the overpass API
            String message = "node";
            if(aluminium){
                message = message.concat("[\"recycling:aluminium\"=\"yes\"]");
            }
            if(cans){
                message = message.concat("[\"recycling:cans\"=\"yes\"]");
            }
            if(cardboard){
                message = message.concat("[\"recycling:cardboard\"=\"yes\"]");
            }
            if(clothes){
                message = message.concat("[\"recycling:clothes\"=\"yes\"]");
            }
            if(paper){
                message = message.concat("[\"recycling:paper\"=\"yes\"]");
            }
            if(plastic){
                message = message.concat("[\"recycling:plastic\"=\"yes\"]");
            }
            if(greenwaste){
                message = message.concat("[\"recycling:green_waste\"=\"yes\"]");
            }
            if(electronics){
                message = message.concat("[\"recycling:electronics\"=\"yes\"]");
            }
            intent.putExtra(OVERPASS_MESSAGE, message);
            startActivity(intent);
        }
        //Else tell the user to choose a collection type
        else{
            Toast.makeText(MainActivity.this, "Please select at least one collection category!", Toast.LENGTH_LONG).show();
        }
    }

    //Toggle the booleans when the respective toggle button is clicked:

    public void toggleAluminium(View view){
        aluminium=!aluminium;
    }
    public void toggleCans(View view){
        cans=!cans;
    }
    public void toggleCardboard(View view){
        cardboard=!cardboard;
    }
    public void toggleClothes(View view){
        clothes=!clothes;
    }
    public void togglePaper(View view){
        paper=!paper;
    }
    public void togglePlastic(View view){
        plastic=!plastic;
    }
    public void toggleGreenWaste(View view){
        greenwaste=!greenwaste;
    }
    public void toggleElectronics(View view){
        electronics=!electronics;
    }
}