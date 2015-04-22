package eqpineda.dooraccesssystem;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import eqpineda.dooraccesssystem.helper.DatabaseHelper;


public class ReadCard extends ActionBarActivity {
    private IntentFilter[] filters;
    private String[][] techs;
    private PendingIntent pendingIntent;
    private NfcAdapter adapter;
    private Tag tag;

    private final byte[][] keys = {
            { (byte)0x50, (byte)0x49, (byte)0x4e, (byte)0x41, (byte)0x44, (byte)0x4f },
            { (byte)0x31, (byte)0x32, (byte)0x31, (byte)0x32, (byte)0x31, (byte)0x32 },
            { (byte)0x31, (byte)0x31, (byte)0x31, (byte)0x31, (byte)0x31, (byte)0x32 },
            // Default keys
            { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff },
            { (byte)0xd3, (byte)0xf7, (byte)0xd3, (byte)0xf7, (byte)0xd3, (byte)0xf7 },
            { (byte)0xa0, (byte)0xa1, (byte)0xa2, (byte)0xa3, (byte)0xa4, (byte)0xa5 },
            { (byte)0xb0, (byte)0xb1, (byte)0xb2, (byte)0xb3, (byte)0xb4, (byte)0xb5 },
            { (byte)0x4d, (byte)0x3a, (byte)0x99, (byte)0xc3, (byte)0x51, (byte)0xdd },
            { (byte)0x1a, (byte)0x98, (byte)0x2c, (byte)0x7e, (byte)0x45, (byte)0x9a },
            { (byte)0xaa, (byte)0xbb, (byte)0xcc, (byte)0xdd, (byte)0xee, (byte)0xff },
            { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00 },
            { (byte)0xa0, (byte)0xb0, (byte)0xc0, (byte)0xd0, (byte)0xe0, (byte)0xf0 }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_card);

        this.pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter mifare = new IntentFilter((NfcAdapter.ACTION_TECH_DISCOVERED));
        this.filters = new IntentFilter[] { mifare };
        this.techs = new String[][] { new String[] {  NfcA.class.getName() } };
        this.adapter = NfcAdapter.getDefaultAdapter(this);
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
        ByteBuffer wrapped = ByteBuffer.wrap(id);
        wrapped.order(ByteOrder.LITTLE_ENDIAN);
        int signedInt = wrapped.getInt();
        long number = signedInt & 0xffffffffl;
        Toast.makeText(getApplicationContext(), "Tag detected: " + number, Toast.LENGTH_LONG)
                .show();
    }

    public void getAuthString(View view) throws IOException {
        if(this.tag == null)
            Toast.makeText(getApplicationContext(), "No RFID initialized.", Toast.LENGTH_SHORT)
                    .show();
        else {
            try {
                MifareClassic card = MifareClassic.get(this.tag);
                card.connect();
                if (authCard(card, true) && authCard(card, false)) {
                    Log.i("AUTH", "Successfully authenticated sector");

                    byte[] authString = card.readBlock(1);
                    String auth = convertToString(authString);

                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    final View v = inflater.inflate(R.layout.activity_read_card_description_alert,
                            null);
                    AlertDialog.Builder keyDescDialog = new AlertDialog.Builder(
                            this);
                    keyDescDialog.setTitle("Add New Key from Card");
                    keyDescDialog.setView(v);
                    keyDescDialog.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText editText = (EditText)v.findViewById(R.id.key_desc);
                            String desc = editText.getText().toString();

                            Toast.makeText(getApplicationContext(), desc, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                    keyDescDialog.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
                    final AlertDialog keyDesc = keyDescDialog.create();

                    AlertDialog.Builder addKeyDialog = new AlertDialog.Builder(this);
                    addKeyDialog.setTitle("Add Key");
                    addKeyDialog.setMessage("Do you want to add the key of this RFID to" +
                            " your list of keys?");
                    addKeyDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            keyDesc.show();
                        }
                    });
                    addKeyDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
                    AlertDialog addKey = addKeyDialog.create();
                    addKey.show();
                }
                else
                    Log.i("AUTH", "Access denied");

                card.close();
            }
            catch(IOException ex) {
                Toast.makeText(getApplication(), "Error reading card. Please try again.",
                        Toast.LENGTH_SHORT).show();
                Log.e("AUTH", "Something went wrong when authenticating card, please check if" +
                        " card is placed properly.");
            }
        }
    }

    private boolean authCard(MifareClassic card, boolean isSectorA) throws IOException {
        for(byte[] x: this.keys) {
            boolean b = (isSectorA) ? card.authenticateSectorWithKeyA(0, x)
                                    : card.authenticateSectorWithKeyB(0, x);

            if(b)
                return true;
        }

        return false;
    }

    private String convertToString(byte[] array) {
        if(array.length == 0)
            return "";
        return (char)array[0] + convertToString(Arrays.copyOfRange(array, 1, array.length));
    }
}
