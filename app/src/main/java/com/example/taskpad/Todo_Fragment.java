package com.example.taskpad;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

public class Todo_Fragment extends Fragment implements RecyclerViewAdapter.OnTaskClickListener{

    RecyclerView recyclerView;
    RecyclerViewAdapter2 adapter;
    List<String> todoTitles;
    DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo_, container, false);

        // Initialize DBHelper
        dbHelper = new DBHelper(getContext());



        // Retrieve todo titles from the database
        todoTitles = dbHelper.getAllTodoTitles();

        recyclerView = view.findViewById(R.id.recyclerView2);
        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewAdapter2(getContext(), todoTitles);
        recyclerView.setAdapter(adapter);

        // Set the adapter's task click listener
//        adapter.setOnTaskClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the todo titles list when the fragment resumes
        List<String> updatedTodoTitles = dbHelper.getAllTodoTitles();
        if (updatedTodoTitles != null) {
            todoTitles.clear();
            todoTitles.addAll(updatedTodoTitles);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "Failed to retrieve todos from the database", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTaskClick(String title) {
        // Handle task click, e.g., show task details or edit task
        Toast.makeText(getContext(), "Clicked: " + title, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(String title) {
        // Find the ID of the todo with the given title
        List<String> allTodoTitles = dbHelper.getAllTodoTitles();
        long todoId = -1;
        for (int i = 0; i < allTodoTitles.size(); i++) {
            if (allTodoTitles.get(i).equals(title)) {
                todoId = i + 1; // Assuming IDs are 1-based index
                break;
            }
        }

        if (todoId != -1) {
            // Delete the todo from the database
            dbHelper.deleteTodo(todoId);

            // Remove the todo from the list and notify the adapter
            todoTitles.remove(title);
            adapter.notifyDataSetChanged();

            // Show a toast confirming the deletion
            Toast.makeText(getContext(), "Todo deleted: " + title, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Todo not found: " + title, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTaskClick(Task task) {

    }

    @Override
    public void onDeleteClick(Task task) {

    }

    public void refreshFragment() {
        List<String> updatedTodoTitles = dbHelper.getAllTodoTitles();
        if (updatedTodoTitles != null) {
            todoTitles.clear();
            todoTitles.addAll(updatedTodoTitles);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "Failed to retrieve todos from the database", Toast.LENGTH_SHORT).show();
        }
    }
}
