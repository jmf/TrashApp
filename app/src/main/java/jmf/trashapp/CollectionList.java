package jmf.trashapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class CollectionList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_list);

        Intent intent = getIntent();
        String message = intent.getStringExtra(BePatientActivity.BEST_MATCH);
        TextView output = (TextView)findViewById(R.id.textView10);
        output.setText(message);


    }
}
