package com.marcuskim.todude.adapter;

import com.marcuskim.todude.model.Task;

public interface OnTodoClickListener {
    void onTodoClick(Task task);
    void onTodoRadioButtonClicked(Task task);
}
