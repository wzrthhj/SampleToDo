package com.example.harvey.simpletodo;

import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class SimpleTodo extends AppCompatActivity {
    private ListView mListView;
    private EditText mEditText;
    private Button mAdd;
    private Realm myRealm;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_todo);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfig);
        myRealm = Realm.getDefaultInstance();

        mListView = (ListView) findViewById(R.id.listView);
        mEditText = (EditText) findViewById(R.id.editText);
        mAdd = (Button) findViewById(R.id.add);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        mItems = new ArrayList<String>();
        readItem();
        // Create an ArrayAdapter from List
        mAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, mItems);
        // DataBind ListView with items from ArrayAdapter
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                removeItem(position);
                MyLog.print("position:" + position);
                String name = mItems.get(position);
                gotoEdit(name);
            }
        });
    }

    private void gotoEdit(String name) {
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra(Constant.Edit_Name, name);
        int pos = mItems.indexOf(name);

        intent.putExtra(Constant.Name_Position, pos);
//        this.startActivity(intent);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2) {
            // fetch the message String
            if(data != null) {
                String newName = data.getStringExtra(Constant.Edit_Name);
                int position = data.getIntExtra(Constant.Name_Position, 0);
                String old = mItems.get(position);
                mItems.add(position, newName);
                mItems.remove(old);
                mAdapter.notifyDataSetChanged();
                MyLog.print("pos = " + position + ", new name = " + newName);
                updateName(old, newName);
            }
        }
    }

    private void updateName(String oldName, String newName) {
        ItemData item = myRealm.where(ItemData.class).equalTo("name", oldName).findFirst();
        if(item != null) {
            myRealm.beginTransaction();
            item.setName(newName);
            myRealm.copyToRealmOrUpdate(item);
            myRealm.commitTransaction();
        }
    }

    private void removeItem(int pos) {
        String name = mItems.remove(pos);
        mAdapter.notifyDataSetChanged();

        // delete item in the database
        myRealm.beginTransaction();
        myRealm.where(ItemData.class).equalTo("name", name).findFirst().removeFromRealm();
        myRealm.commitTransaction();
    }

    private void readItem() {
        RealmQuery<ItemData> query = myRealm.where(ItemData.class);
        RealmResults<ItemData> result1 = query.findAll();
        for(ItemData item : result1) {
            mItems.add(item.getName());
        }
    }

    public void addItem() {
        MyLog.print("addItem clicked");
        String name = mEditText.getText().toString();
        name.trim();
        if(!name.equals("")) {
            myRealm.beginTransaction();
            ItemData item = new ItemData();
            item.setName(name);
            myRealm.copyToRealmOrUpdate(item);
            myRealm.commitTransaction();
            mEditText.setText("");
            mItems.add(name);
            mAdapter.notifyDataSetChanged();
        }
    }
}
