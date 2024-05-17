package com.example.taskpad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Task_Fragment extends Fragment implements RecyclerViewAdapter.OnTaskClickListener{

    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    List<Task> tasks;
    DBHelper dbHelper;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task_, container, false);


        // Create dummy data for RecyclerView
        tasks = new ArrayList<>();
        tasks.add(new Task("Task 1Task 1Task 1Task 1Task 1Task 1Task 1Task 1Task 1Task 1", "Description 1 hey"));
        tasks.add(new Task("Task 2Task 1Task 1Task 1Task 1Task 1Task 1Task 1Task 1Task 1Task 1", "Description 2 there"));
        // Add more tasks as needed
        dbHelper = new DBHelper(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewAdapter(getContext(), tasks, this);
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the tasks list when the fragment resumes
        List<Task> updatedTasks = dbHelper.getAllTasks();
        if (updatedTasks != null) {
            tasks.clear();
            tasks.addAll(updatedTasks);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "Failed to retrieve tasks from the database", Toast.LENGTH_SHORT).show();
        }

        // Fetch new data (if applicable)


    }
    // Define the ActivityResultLauncher
    ActivityResultLauncher<Intent> viewActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Toast.makeText(getContext(), " if pass hua", Toast.LENGTH_LONG).show();  // -1 is the default value if taskId is not found
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        int taskId = data.getIntExtra("taskId", -1);
                        if (taskId != -1) {
                            // Delete the task with the received task ID
                            dbHelper.deleteTask(taskId);
                            // Update the RecyclerView
                            List<Task> updatedTasks = dbHelper.getAllTasks();
                            tasks.clear();
                            tasks.addAll(updatedTasks);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            });

    @Override
    public void onTaskClick(String title) {

    }

    @Override
    public void onDeleteClick(String title) {

    }

    @Override
    public void onTaskClick(Task task) {
        // Handle click here, e.g., start a new activity with task title and description
        Intent intent = new Intent(getActivity(), ViewActvity.class);
        Toast.makeText(getContext(), task.getId()+"ye id pass ho raha hai", Toast.LENGTH_LONG).show();
        intent.putExtra("id", task.getId());
        intent.putExtra("title", task.getTitle());
        intent.putExtra("description", task.getDescription());
        intent.putExtra("origin", "Show"); // Or "ActivityB" depending on the source
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Task task) {

    }
}