package com.example.geotaskapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

    private final List<Task> tasks;
    private final OnTaskActionListener listener;

    public interface OnTaskActionListener {
        void onEdit(Task task);
        void onDelete(Task task);
    }

    public TaskAdapter(@NonNull Context context, @NonNull List<Task> objects, OnTaskActionListener listener) {
        super(context, R.layout.item_task, objects);
        this.tasks = objects;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        }

        Task task = tasks.get(position);

        TextView tvTitle = convertView.findViewById(R.id.tvTaskTitle);
        ImageButton btnEdit = convertView.findViewById(R.id.btnEdit);
        ImageButton btnDelete = convertView.findViewById(R.id.btnDelete);

        tvTitle.setText(task.titulo);

        btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(task);
            }
        });

        btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(task);
            }
        });

        return convertView;
    }
}
