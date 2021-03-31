package com.marcuskim.todude;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.chip.Chip;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.marcuskim.todude.model.Priority;
import com.marcuskim.todude.model.SharedViewModel;
import com.marcuskim.todude.model.Task;
import com.marcuskim.todude.model.TaskViewModel;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private EditText enterTaskEditText;
    private ImageButton calendarButton;
    private ImageButton priorityButton;
    private RadioGroup priorityRadioGroup;
    private RadioButton selectedRadioButton;
    private int selectedButtonId;
    private ImageButton saveButton;
    private CalendarView calendarView;
    private Group calendarGroup;
    private Date dueDate = null;
    Calendar calendar = Calendar.getInstance();
    private SharedViewModel sharedViewModel;
    private boolean isEdit;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.bottom_sheet, container, false);
        calendarGroup = view.findViewById(R.id.calendar_group);
        calendarView = view.findViewById(R.id.calendar_view);
        calendarButton = view.findViewById(R.id.today_calendar_button);
        enterTaskEditText = view.findViewById(R.id.enter_todo_et);
        saveButton = view.findViewById(R.id.save_todo_button);
        priorityButton = view.findViewById(R.id.priority_todo_button);
        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);

        Chip todayChip = view.findViewById(R.id.today_chip);
        todayChip.setOnClickListener(this);
        Chip tomorrowChip = view.findViewById(R.id.tomorrow_chip);
        tomorrowChip.setOnClickListener(this);
        Chip nextWeekChip = view.findViewById(R.id.next_week_chip);
        nextWeekChip.setOnClickListener(this);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        
       clearEnterTaskEditText();
        
        if (sharedViewModel.getSelectedItem().getValue() != null) {
            isEdit = sharedViewModel.getIsEdit();
            Task task = sharedViewModel.getSelectedItem().getValue();
            enterTaskEditText.setText(task.getTask());
        }

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clearEnterTaskEditText();
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        calendarButton.setOnClickListener(view12 -> {
            calendarGroup.setVisibility(calendarGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
//            Log.d("Cal", "onViewCreated: ===> month: " + (month + 1) + ", day of month: " + dayOfMonth);

            calendar.clear();
            calendar.set(year, month, dayOfMonth);
            dueDate = calendar.getTime();

        });


        saveButton.setOnClickListener(view1 -> {
            String task = enterTaskEditText.getText().toString().trim();
            if(!TextUtils.isEmpty(task)) {
                Task newTask = new Task(task, Priority.HIGH, dueDate, Calendar.getInstance().getTime(), false);
                if (isEdit) {
                    Task taskToUpdate = sharedViewModel.getSelectedItem().getValue();
                    Log.d("update", "onViewCreated: " + taskToUpdate.task);
                    taskToUpdate.setTask(task);
                    taskToUpdate.setDateCreated(Calendar.getInstance().getTime());
                    taskToUpdate.setPriority(Priority.HIGH);
                    taskToUpdate.setDueDate(dueDate);
                    TaskViewModel.update(taskToUpdate);

                    sharedViewModel.setIsEdit(false);
                } else {
                    TaskViewModel.insert(newTask);
                }

                dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.today_chip) {
            calendar.add(Calendar.DAY_OF_YEAR, 0);
            dueDate = calendar.getTime();
            Log.d("TIME", "onClick: " + dueDate.toString());

        } else if (id == R.id.tomorrow_chip) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            dueDate = calendar.getTime();
            Log.d("TIME", "onClick: " + dueDate.toString());

        } else if (id == R.id.next_week_chip) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            dueDate = calendar.getTime();
            Log.d("TIME", "onClick: " + dueDate.toString());

        }
    }
    
    public void clearEnterTaskEditText() {
        if(!isEdit) {
            enterTaskEditText.setText("");
        }
        
    }
    
}