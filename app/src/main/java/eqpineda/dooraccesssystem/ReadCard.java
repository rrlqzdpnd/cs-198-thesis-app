package eqpineda.dooraccesssystem;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class ReadCard extends ActionBarActivity {
    private IntentFilter[] filters;
    private String[][] techs;
    private PendingIntent pendingIntent;
    private NfcAdapter adapter;
    private Tag tag;

    private static final int[][] keys = {
            { 0x50, 0x49, 0x4e, 0x41, 0x44, 0x4f },
            { 0x31, 0x32, 0x31, 0x32, 0x31, 0x32 },
            { 0xff, 0xff, 0xff, 0xff, 0xff, 0xff },
            { 0xd3, 0xf7, 0xd3, 0xf7, 0xd3, 0xf7 },
            { 0xa0, 0xa1, 0xa2, 0xa3, 0xa4, 0xa5 },
            { 0xb0, 0xb1, 0xb2, 0xb3, 0xb4, 0xb5 },
            { 0x4d, 0x3a, 0x99, 0xc3, 0x51, 0xdd },
            { 0x1a, 0x98, 0x2c, 0x7e, 0x45, 0x9a },
            { 0xaa, 0xbb, 0xcc, 0xdd, 0xee, 0xff },
            { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 },
            { 0xa0, 0xb0, 0xc0, 0xd0, 0xe0, 0xf0 }
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
        this.adapter.enableForegroundDispatch(this, pendingIntent, filters, techs);
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

    public void getAuthString(View view) {
        if(this.tag == null)
            Toast.makeText(getApplicationContext(), "No RFID initialized.", Toast.LENGTH_SHORT)
                    .show();
        else {
            MifareClassic card = MifareClassic.get(this.tag);
            if(authA(card) && authB(card))
                Log.i("AUTH", "Successfully authenticated sector");
        }
    }

    private boolean authA(MifareClassic card) {
        return true;
    }

    private boolean authB(MifareClassic card) {
        return true;
    }
}
