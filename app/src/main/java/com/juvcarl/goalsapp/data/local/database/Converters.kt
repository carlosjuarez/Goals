package com.juvcarl.goalsapp.data.local.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class Converters{
    @TypeConverter
    fun fromTimeStamp(value: Long?): Date? {
        return value?.let { Date(value) }
    }

    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromLocalTime(value: LocalTime?): String?{
        return value?.let { it.toString() }
    }

    @TypeConverter
    fun toLocalTime(value: String?): LocalTime?{
        return value?.let { LocalTime.parse(it) }
    }

    @TypeConverter
    fun fromLocalDate(value: LocalDate?): String?{
        return value?.let { it.toString() }
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate?{
        return value?.let { LocalDate.parse(it) }
    }
}