package eqpineda.dooraccesssystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import eqpineda.dooraccesssystem.helper.DatabaseHelper;
import eqpineda.dooraccesssystem.model.Keys;


public class AllKeys extends ActionBarActivity {
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_keys);

        this.db = new DatabaseHelper(getApplicationContext());
        List<Keys> keys = this.db.getKeys();
        if(keys.size() > 0) {
            LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout);
            if(layout != null) {
                layout.removeAllViews();
                for(Keys key : keys) {
                    Button b = new Button(getApplicationContext());
                    b.setText(key.getDescription());
                    b.getBackground().setColorFilter(0xFFCACACA, PorterDuff.Mode.LIGHTEN);
                    b.setTextColor(0xFF222222);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 0, 4);
                    b.setLayoutParams(params);

                    b.setTag(key.getKeyid());
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AllKeys.this.keyDetails(v);
                        }
                    });

                    layout.addView(b);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_keys, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        switch(id) {
//            case R.id.action_settings:
//                return true;
            case R.id.action_add_key:
                intent = new Intent(this, AddKey.class);
                startActivityForResult(intent, 1);
                return true;
            case R.id.action_delete_all_keys:
                AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
                alertBox.setTitle("Delete All Keys");
                alertBox.setMessage("Are you sure you want to delete all existing keys?");
                alertBox.setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AllKeys.this.db.clearAll();
                        Intent refresh = new Intent(AllKeys.this, AllKeys.class);
                        startActivity(refresh);
                        finish();
                        Toast.makeText(getApplicationContext(), "Keys successfully deleted!",
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
            case R.id.action_read_card:
                intent = new Intent(this, ReadCard.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Intent refresh = new Intent(this, AllKeys.class);
            startActivity(refresh);
            this.finish();
        }
    }

    public void keyDetails(View v) {
        int keyID = (int)v.getTag();
//        Log.i("PRESSED", "Button for key " + keyID +  " was pressed");
        Intent intent = new Intent(this, KeyDescription.class);
        intent.putExtra("KEYID", keyID);
        startActivity(intent);
    }
}
