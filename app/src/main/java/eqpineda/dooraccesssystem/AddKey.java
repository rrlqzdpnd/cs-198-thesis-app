package eqpineda.dooraccesssystem;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import eqpineda.dooraccesssystem.helper.DatabaseHelper;
import eqpineda.dooraccesssystem.model.Keys;


public class AddKey extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_key);
    }

    public void addKey(View view) {
        EditText eText = (EditText)findViewById(R.id.new_key_id);
        String authKey = eText.getText().toString();
        Keys key = new Keys(authKey);
        return;
    }
}
