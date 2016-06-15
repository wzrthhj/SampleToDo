package com.example.harvey.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    private EditText mEditText;
    private Button mSave;
    private int mPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        mEditText = (EditText) findViewById(R.id.editText);
        mSave = (Button) findViewById(R.id.button);
        final Intent intent = getIntent();
        String name = intent.getStringExtra(Constant.Edit_Name);
        mPosition = intent.getIntExtra(Constant.Name_Position, 0);

        mEditText.setText(name);

        int len = name.length();
//        Editable etext = mEditText.getText();
//        mEditText.setSelection(, len);
        mEditText.setSelection(len);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mEditText.getText().toString();
                Intent intentMessage = new Intent();
                // put the message to return as result in Intent
                intentMessage.putExtra(Constant.Name_Position, mPosition);
                intentMessage.putExtra(Constant.Edit_Name, name);
                // Set The Result in Intent
                setResult(2, intentMessage);
                finish();
            }
        });
    }
}
