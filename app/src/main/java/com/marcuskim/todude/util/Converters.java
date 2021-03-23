package com.marcuskim.todude.util;

import androidx.room.TypeConverter;

import com.marcuskim.todude.model.Priority;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date convertFromTimeStampToDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long convertFromDateToTimeStamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String convertFromPriorityToStringName(Priority priority) {
        return priority == null ? null : priority.name();
    }

    @TypeConverter
    public static Priority convertFromStringNameToPriority(String priority) {
        return priority == null ? null : Priority.valueOf(priority);
    }
}
