package eqpineda.dooraccesssystem;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import eqpineda.dooraccesssystem.helper.DatabaseHelper;
import eqpineda.dooraccesssystem.model.Keys;


public class KeyDescription extends ActionBarActivity {
    private Keys key = null;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_description);

        Intent intent = getIntent();
        int keyId = intent.getIntExtra("KEYID", -1);
        if(keyId != -1) {
//            Log.i("MESSAGE", "Got intent extra with keyID:\t" + keyId);
            this.db = new DatabaseHelper(getApplicationContext());
            this.key = this.db.getKeyDetails(keyId);
            TextView tv = (TextView)findViewById(R.id.key_desc);
            tv.setText(this.key.getDescription());
            tv = (TextView)findViewById(R.id.key_notes);
            tv.setText(this.key.getNotes());
        }
        else {
            Toast.makeText(getApplicationContext(), "Key doesn't exist, please try again.",
                    Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_key_description, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
//            case R.id.action_settings:
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
