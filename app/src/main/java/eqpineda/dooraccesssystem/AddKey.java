package eqpineda.dooraccesssystem;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import eqpineda.dooraccesssystem.helper.DatabaseHelper;
import eqpineda.dooraccesssystem.model.Keys;


public class AddKey extends ActionBarActivity {
    private DatabaseHelper db;
    private String keyTmp = "", keyInput = "", descTmp = "", descInput = "";

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
                if(s.toString().length() < AddKey.this.keyInput.length())
                    AddKey.this.keyInput = s.toString();
            }
        });
        keyText.setFilters(new InputFilter[] {
            new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                           int dstart, int dend) {
                    return validateKey(source.toString());
                }
            }
        });

        keyText = (EditText)findViewById(R.id.key_desc_id);
        keyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() < AddKey.this.descInput.length())
                    AddKey.this.descInput = s.toString();
            }
        });
        keyText.setFilters(new InputFilter[] {
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                               int dstart, int dend) {
                        return validateDescription(source.toString());
                    }
                }
        });
    }

    public void addKey(View view) {
        EditText eText = (EditText)findViewById(R.id.new_key_id);
        String authKey = eText.getText().toString();

        eText = (EditText)findViewById(R.id.key_desc_id);
        String desc = eText.getText().toString().toUpperCase();

        eText = (EditText)findViewById(R.id.key_notes);
        String notes = eText.getText().toString();

        if(authKey.equalsIgnoreCase("") || desc.trim().equalsIgnoreCase(""))
            Toast.makeText(getApplicationContext(), "Please enter key and description for the key.",
                    Toast.LENGTH_SHORT).show();
        else if(authKey.length() != 16)
            Toast.makeText(getApplicationContext(), "Authentication Key needs to be 16 characters" +
                    " long.", Toast.LENGTH_SHORT).show();
        else if(desc.length() > 32)
            Toast.makeText(getApplicationContext(), "Key Description must of maximum 32 characters",
                    Toast.LENGTH_SHORT).show();
        else {
            Keys key = new Keys(authKey, desc);
            if(!notes.trim().equalsIgnoreCase(""))
                key.setNotes(notes);

            this.db = new DatabaseHelper(getApplicationContext());
            this.db.insertKey(key);

            setResult(RESULT_OK, null);
            this.finish();
        }
    }

    private CharSequence validateKey(String s) {
        this.keyTmp = this.keyInput + s;
        if(this.keyTmp.length() > 16) // max length of 16 characters
            return "";
        else if(!this.keyTmp.matches("[a-zA-Z0-9]*")) // uses only alphanumeric characters as key
            return "";

        this.keyInput = this.keyTmp;
        return s;
    }

    private CharSequence validateDescription(String s) {
        this.descTmp = this.descInput + s;
        if(this.descTmp.length() > 32)
            return "";

        this.descInput = this.descTmp;
        return s.toUpperCase();
    }
}
