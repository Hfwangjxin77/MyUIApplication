package com.example.myuiapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mytestlibrary.MyTestLibraryActivity;

public class MainListActivity extends Activity {

    private String[] data = { "Calender", "CustomItemView", "MyHorizontalScrollView", "Mainlist",
            "CustomDrawView", "TestModuleActivity", "Pineapple", "Strawberry", "Cherry", "Mango" };
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainListActivity.this, android.R.layout.simple_list_item_1, data);
        listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (data[position].equals("Calender")) {
                    Intent intent = new Intent(MainListActivity.this,CalenderActivity.class);
                    startActivity(intent);
                }else if (data[position].equals("CustomItemView")) {
//                    Intent intent = new Intent(MainListActivity.this,ItemViewActivity.class);
//                    startActivity(intent);

                    String textMessage = "test by nick";
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);


// Start the activity
                    startActivity(sendIntent);
                }else if (data[position].equals("MyHorizontalScrollView")) {
                    Intent intent = new Intent(MainListActivity.this,MyHorizontalScrollViewActivity.class);
                    startActivity(intent);
                }else if (data[position].equals("Mainlist")) {
                    Intent intent = new Intent(MainListActivity.this,MainListActivity.class);
                    startActivity(intent);
                }else if (data[position].equals("CustomDrawView")) {
                    Intent intent = new Intent(MainListActivity.this,CustomDrawViewActivity.class);
                    startActivity(intent);
                }else if (data[position].equals("TestModuleActivity")) {
                    Intent intent = new Intent(MainListActivity.this, MyTestLibraryActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println("onNewIntent()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onRestart()");
    }
}
