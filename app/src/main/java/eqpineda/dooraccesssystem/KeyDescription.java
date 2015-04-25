package eqpineda.dooraccesssystem;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import eqpineda.dooraccesssystem.helper.DatabaseHelper;
import eqpineda.dooraccesssystem.model.Keys;


public class KeyDescription extends ActionBarActivity {
    private Keys key = null;
    private DatabaseHelper db;
    private IntentFilter[] filters;
    private String[][] techs;
    private PendingIntent pendingIntent;
    private NfcAdapter adapter;
    private Tag tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_description);

        this.pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter mifare = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        this.filters = new IntentFilter[] { mifare };
        this.techs = new String[][] { new String[] {  NfcA.class.getName() } };
        this.adapter = NfcAdapter.getDefaultAdapter(this);

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
            case R.id.action_delete_key:
                AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
                alertBox.setTitle("Delete Key");
                alertBox.setMessage("Are you sure you want to delete this key?");
                alertBox.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        KeyDescription.this.db.deleteKey(KeyDescription.this.key.getKeyid());
                        Intent refresh = new Intent(KeyDescription.this, AllKeys.class);
                        startActivity(refresh);
                        finish();
                        Toast.makeText(getApplicationContext(), "Key successfully deleted!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                alertBox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
                    }
                });

                AlertDialog dialog = alertBox.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        this.adapter.disableForegroundDispatch(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.adapter.enableForegroundDispatch(this, this.pendingIntent, this.filters, this.techs);
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        byte[] id = this.tag.getId();
        Toast.makeText(getApplicationContext(), "Penis",
                Toast.LENGTH_SHORT).show();
    }

    public void authenticate(View view) {
        if (!this.adapter.isEnabled()) {
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        } else if (!this.adapter.isNdefPushEnabled()) {
            startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
        }


    }
}
