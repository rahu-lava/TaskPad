package com.example.taskpad;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity   {


    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;
    ViewPagerAdapter adapter;
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ExtendedFloatingActionButton extendedFab = findViewById(R.id.extended_fab);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Notes");
                    break;
                case 1:
                    tab.setText("ToDo");
                    break;
            }
        }).attach();

        extendedFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentTabPosition = tabLayout.getSelectedTabPosition();
                if (currentTabPosition == 0) {
                    Intent intent = new Intent(HomeActivity.this, ViewActvity.class);
                    intent.putExtra("origin", "Add"); // Or "ActivityB" depending on the source
                    intent.putExtra("id", "");
                    intent.putExtra("title", " ");
                    intent.putExtra("description", " ");
                    startActivity(intent);
                } else if (currentTabPosition == 1) {
                    // Show the dialog for adding a new todo
                    showAddTodoDialog();
                }
            }
        });

    }

    private void showAddTodoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_todo, null);
        EditText editTextTodoTitle = dialogView.findViewById(R.id.editTextTodoTitle);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new DBHelper(HomeActivity.this);
                ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(HomeActivity.this);;

                String todoTitle = editTextTodoTitle.getText().toString().trim();
                if (!todoTitle.isEmpty()) {
                    // Add the todo to the database
                    long todoId = dbHelper.addTodo(todoTitle);
                    if (todoId != -1) {
                        // Successfully added todo to database
                        // You can also update the UI accordingly if needed
                        Toast.makeText(HomeActivity.this, "Todo added", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
// In your Activity
                        Objects.requireNonNull(tabLayout.getTabAt(0)).select();

                        //                        tabLayout.selectTab(tabLayout.getTabAt(1));
                    } else {
//                        // Failed to add todo to database
                        Toast.makeText(HomeActivity.this, "Failed to add todo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Title cannot be empty
                    Toast.makeText(HomeActivity.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }


}