package com.marcuskim.todude.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.marcuskim.todude.R;
import com.marcuskim.todude.model.Task;
import com.marcuskim.todude.util.Utils;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static OnTodoClickListener onTodoClickListener = null;
    private final List<Task> taskList;


    public RecyclerViewAdapter(List<Task> taskList, OnTodoClickListener onTodoClickListener) {
        this.taskList = taskList;
        this.onTodoClickListener = onTodoClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        if (task.getDueDate() != null) {
            String formattedDate = Utils.formatDate(task.getDueDate());

            ColorStateList colorStateList = new ColorStateList(new int[][]{
                    new int[] { -android.R.attr.state_enabled},
                    new int[] {android.R.attr.state_enabled}
            }, new int[]{
                    Color.LTGRAY,
                    Utils.priorityColor(task)
            });

            holder.task.setText(task.getTask());
            holder.dueDateChip.setText(formattedDate);
            holder.dueDateChip.setTextColor(Utils.priorityColor(task));
            holder.dueDateChip.setChipIconTint(colorStateList);
            holder.radioButton.setButtonTintList(colorStateList);
        } else {
            holder.dueDateChip.setText("No due date");
        }


        holder.task.setText(task.getTask());


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public AppCompatRadioButton radioButton;
        public AppCompatTextView task;
        public Chip dueDateChip;

        OnTodoClickListener onTodoClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            radioButton = itemView.findViewById(R.id.todo_radio_button);
            task = itemView.findViewById(R.id.todo_row_todo);
            dueDateChip = itemView.findViewById(R.id.todo_row_chip);
            this.onTodoClickListener = RecyclerViewAdapter.onTodoClickListener;

            itemView.setOnClickListener(this);
            radioButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Task currentTask = taskList.get(getAdapterPosition());
            int id = v.getId();
            if (id == R.id.todo_row_layout) {
                currentTask = taskList.get(getAdapterPosition());
                onTodoClickListener.onTodoClick(currentTask);
            } else if (id == R.id.todo_radio_button){
                onTodoClickListener.onTodoRadioButtonClicked(currentTask);
            }
        }
    }
}
