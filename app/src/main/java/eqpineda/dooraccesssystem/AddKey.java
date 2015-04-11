package eqpineda.dooraccesssystem;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import eqpineda.dooraccesssystem.helper.DatabaseHelper;
import eqpineda.dooraccesssystem.model.Keys;


public class AddKey extends ActionBarActivity {
    DatabaseHelper db;
    String tmp = "", input = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_key);
        EditText keyText = (EditText)findViewById(R.id.new_key_id);

        keyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() < input.length())
                    input = s.toString();
            }
        });
        keyText.setFilters(new InputFilter[] {
            new InputFilter() {
                @Override
                public CharSequence filter(CharSequence input, int arg1, int arg2, Spanned arg3,
                                           int arg4, int arg5) {
                    return validate(input.toString());
                }
            }
        });
    }

    public void addKey(View view) {
        EditText eText = (EditText)findViewById(R.id.new_key_id);
        String authKey = eText.getText().toString();
        eText = (EditText)findViewById(R.id.key_desc_id);
        String desc = eText.getText().toString();
        Keys key = new Keys(authKey, desc);
        db = new DatabaseHelper(getApplicationContext());
        db.insertKey(key);

        setResult(RESULT_OK, null);
        this.finish();
        return;
    }

    private CharSequence validate(String s) {
        this.tmp = this.input + s;
        if(this.tmp.length() > 16) // max length of 16 characters
            return "";
        else if(!this.tmp.matches("[a-zA-Z0-9]*")) // uses only alphanumeric characters as key
            return "";
        else {
            this.input = this.tmp;
            return s;
        }
    }
}
