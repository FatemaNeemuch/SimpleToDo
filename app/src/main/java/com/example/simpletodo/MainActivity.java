package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //instance variables
    List<String> items;
    Button buttonAdd;
    EditText editItem;
    RecyclerView RVItems;
    ItemsAdapter itemsAdapter;

    //informs us that the main activity has been created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sets the layout as the main activity
        setContentView(R.layout.activity_main);

        //reference to each view
        buttonAdd = findViewById(R.id.buttonAdd);
        editItem = findViewById(R.id.editItem);
        RVItems = findViewById(R.id.RVItems);

        //method to load list
        loadItems();

        //when an item is long clicked
        ItemsAdapter.OnLongClickListener OnLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //delete the item from the list
                items.remove(position);
                //notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                //let user know that item was removed successfully
                Toast.makeText(getApplicationContext(),"Item was removed!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        itemsAdapter = new ItemsAdapter(items, OnLongClickListener);
        RVItems.setAdapter(itemsAdapter);
        RVItems.setLayoutManager(new LinearLayoutManager(this));

        //when the add button is clicked
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = editItem.getText().toString();
                //Add item to the model
                items.add(todoItem);
                //Notify the adapter that we added an item
                itemsAdapter.notifyItemInserted(items.size() -1 );
                //clear text box
                editItem.setText("");
                //let user know that item was added successfully
                Toast.makeText(getApplicationContext(),"Item was added!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    //return the file in which we store our list of items
    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    //This function will load items by reading every line of the data file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items");
            items = new ArrayList<>();
        }
    }

    //This function saves items by writing them into the data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items");
        }

    }

}