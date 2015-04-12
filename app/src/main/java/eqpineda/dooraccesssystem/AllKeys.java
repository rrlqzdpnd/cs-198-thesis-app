package eqpineda.dooraccesssystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

        db = new DatabaseHelper(getApplicationContext());
        List<Keys> keys = db.getKeys();
        if(keys.size() > 0) {
            LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout);
            if(layout != null) {
                layout.removeAllViews();
                for(Keys key : keys) {
                    Button b = new Button(getApplicationContext());
                    b.setText(key.getDescription());
                    layout.addView(b);
                }
//                Button b = new Button(getApplicationContext());
//                b.setText("Test");
//                layout.addView(b);
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

        switch(id) {
            case R.id.action_settings:
                return true;
            case R.id.action_add_key:
                Intent intent = new Intent(this, AddKey.class);
                startActivityForResult(intent, 1);
                return true;
            case R.id.action_delete_all_keys:
                AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
                alertBox.setTitle("Delete All Keys");
                alertBox.setMessage("Are you sure you want to delete all existing keys?");
                alertBox.setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.clearAll();
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

    public class DeleteAllDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());
            alertbox.setMessage("Delete all existing keys?")
                    .setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Keys successfully deleted!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            return alertbox.create();
        }
    }
}
