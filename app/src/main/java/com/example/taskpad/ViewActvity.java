package com.example.taskpad;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class ViewActvity extends AppCompatActivity {

    ImageView editImg, delImg, backImg;
    EditText title, desc;
    String origin ;
    boolean isTitleChanged, isDescChanged, isUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_actvity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editImg = findViewById(R.id.EditImage);
        delImg = findViewById(R.id.DeleteImage);
        backImg = findViewById(R.id.BackImage);
        title = findViewById(R.id.TitleText);
        desc = findViewById(R.id.DescText);

        origin = getIntent().getStringExtra("origin");

        assert origin != null;
        if (origin.equals("Add")) {

            editImg.setImageResource(R.drawable.baseline_check_24);

            title.setEnabled(true);
            desc.setEnabled(true);

        } else if(origin.equals("Show")){

            String TitleI = getIntent().getStringExtra("title");
            String DescI = getIntent().getStringExtra("description");

            title.setText(TitleI);
            desc.setText(DescI);

        }

        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();

        backImg.setOnClickListener(v -> {
            ShowSaveDailog();
        });

// Inside the onClickListener for DelImg
        delImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the task ID of the task being viewed
                int taskId = getIntent().getIntExtra("id", -1); // Replace with the actual task ID
                DBHelper db = new DBHelper(ViewActvity.this);
                db.deleteTask(taskId);
                finish();

            }
        });
        editImg.setOnClickListener(view -> {
            if (origin.equals("Show") && !isUpdated ) {
                editImg.setImageResource(R.drawable.baseline_check_24);
                title.setEnabled(true);
                desc.setEnabled(true);
                desc.requestFocus();
                isUpdated = true;
            }else if(origin.equals("Add") || isUpdated){

                if (validateInput()) {
                    editImg.setImageResource(R.drawable.baseline_edit_square_24);
                    title.setEnabled(false);
                    desc.setEnabled(false);
                    isUpdated = false;
                    SaveTask();
                    origin = "Show";
                }
            }
        });

        dispatcher.addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ShowSaveDailog();
            }
        });

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this implementation
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isTitleChanged = true;  // Set flag on any text change
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Not needed for this implementation
            }
        });

        desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this implementation
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isDescChanged = true;  // Set flag on any text change
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Not needed for this implementation
            }
        });

    }
    private boolean validateInput() {
        boolean isValid = true;

        if (title.getText().toString().trim().isEmpty()) {
            title.setError("Title is required");  // Set error message
            isValid = false;
        }

        if (desc.getText().toString().trim().isEmpty()) {
            desc.setError("Description is required");  // Set error message
            isValid = false;
        }

        return isValid;
    }
    private void SaveTask() {

        if (isDescChanged||isTitleChanged) {
            DBHelper db = new DBHelper(this);

            String taskTitle = title.getText().toString().trim();
            String taskDescription = desc.getText().toString().trim();

            if (origin.equals("Add")) {
                // Create a new task and add it to the database
                Toast.makeText(this, "Data Added", Toast.LENGTH_SHORT).show();
                Task newTask = new Task(taskTitle, taskDescription);
                db.addTask(newTask);
                isTitleChanged = isDescChanged = false;
            } else if (origin.equals("Show")) {
                Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show();
                // Update the existing task in the database
                int taskId = getIntent().getIntExtra("id", -1);
                if (taskId != -1) {
                    Task updatedTask = new Task(taskId, taskTitle, taskDescription);
                    db.updateTask(updatedTask);
                    isTitleChanged = isDescChanged = false;
                }
            }
        }
    }

    private void ShowSaveDailog() {
        Log.d("ShowSaveDialog", "Method called");
        if (isTitleChanged || isDescChanged) {
            Log.d("ShowSaveDialog", "Title or Description changed");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Save Task")
                    .setMessage("Do you want to save the changes before exiting?")
                    .setPositiveButton("Save", (dialog, which) -> {
                        SaveTask();
                        finish();
                    })
                    .setNegativeButton("Discard", (dialog, which) -> finish())
                    .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
            Log.d("ShowSaveDialog", "Dialog should be shown");
        } else {
            Log.d("ShowSaveDialog", "No changes detected, finishing");
                finish();
        }
    }
}