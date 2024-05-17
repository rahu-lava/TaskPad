package com.example.taskpad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final OnTaskClickListener listener;
    private final Context context;
    private List<Task> tasks;

    public interface OnTaskClickListener {
        void onTaskClick(String title);

        void onDeleteClick(String title);

        void onTaskClick(Task task);

        void onDeleteClick(Task task);
    }

    RecyclerViewAdapter(Context context, List<Task> tasks, OnTaskClickListener listener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.tasks = tasks;
        this.listener = listener;
    }

    public void addTask(Task task) {
        tasks.add(task);
        notifyDataSetChanged();
    }

    public void deleteTask(int position) {
        tasks.remove(position);
        notifyDataSetChanged();
    }

    public void updateTask(int position, Task updatedTask) {
        tasks.set(position, updatedTask);
        notifyDataSetChanged();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.task_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.textTitle.setText(task.getTitle());
        holder.textDesc.setText(task.getDescription());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle, textDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.Title);
            textDesc = itemView.findViewById(R.id.Desc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Task clickedTask = tasks.get(position);
                        Toast.makeText(context, "Clicked task ID: " + clickedTask.getId(), Toast.LENGTH_SHORT).show();
                        listener.onTaskClick(clickedTask);
                    }
                }
            });
        }
    }
}
