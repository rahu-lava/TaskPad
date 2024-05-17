package com.example.taskpad;
// RecyclerViewAdapter2.java

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder> {
    private final LayoutInflater layoutInflater;
    private List<String> todos;
    private DBHelper dbHelper;

    public RecyclerViewAdapter2(Context context, List<String> todos) {
        this.layoutInflater = LayoutInflater.from(context);
        this.todos = todos;
        this.dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.todo_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String todoTitle = todos.get(position);
        if (holder.textTitle != null) {
            holder.textTitle.setText(todoTitle);
        }

        if (holder.editButton != null) {
            holder.editButton.setOnClickListener(v -> {
                showEditDialog(holder.itemView.getContext(), holder.getAdapterPosition());
            });
        }

        if (holder.deleteButton != null) {
            holder.deleteButton.setOnClickListener(v -> {
                int todoId = position + 1; // Assuming position matches ID
                dbHelper.deleteTodo(todoId);
                todos.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(holder.itemView.getContext(), "Todo deleted", Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    private void showEditDialog(Context context, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = layoutInflater.inflate(R.layout.dialog_edit_todo, null);
        EditText editTextTodoTitle = dialogView.findViewById(R.id.editTextTodoTitle);
        editTextTodoTitle.setText(todos.get(position));

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.buttonSave).setOnClickListener(v -> {
            String newTitle = editTextTodoTitle.getText().toString().trim();
            if (!newTitle.isEmpty()) {
                int todoId = position + 1; // Assuming position matches ID
                dbHelper.updateTodoTitle(todoId, newTitle);
                todos.set(position, newTitle);
                notifyItemChanged(position);
                dialog.dismiss();
                Toast.makeText(context, "Todo updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    public interface OnTaskClickListener {
        void onTaskClick(String title);

        void onDeleteClick(String title);

        void onTaskClick(Task task);

        void onDeleteClick(Task task);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        ImageView editButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.Title2);
            editButton = itemView.findViewById(R.id.imageView2);
            deleteButton = itemView.findViewById(R.id.imageView);
        }
    }
}
