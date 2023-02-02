/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.juvcarl.goalsapp.data.local.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase.CONFLICT_NONE
import androidx.room.Database
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Callback
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Goal::class,Frequency::class,Step::class,StepFrequency::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao

    abstract fun stepDao(): StepDao

    abstract fun frequencyDao(): FrequencyDao
}

var databaseCallback: Callback = object : Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        if(db.isOpen){
            try{
                val values = listOf(
                    ContentValues().apply {
                        put("name" ,"unique")
                        put("description","Occurs only once")
                        put("category", "0")
                    },
                    ContentValues().apply {
                        put("name" ,"daily")
                        put("description","Occurs every day")
                        put("category", "0")
                    },
                    ContentValues().apply {
                        put("name" ,"weekly")
                        put("description","Occurs once a week")
                        put("category", 0)
                    },
                    ContentValues().apply {
                        put("name" ,"monthly")
                        put("description","Occurs once a month")
                        put("category", 0)
                    },
                    ContentValues().apply {
                        put("name" ,"other")
                        put("description","to select a day")
                        put("category", 1)
                    },
                    ContentValues().apply {
                        put("name" ,"monday")
                        put("description","Monday")
                        put("category", 2)
                    },
                    ContentValues().apply {
                        put("name" ,"tuesday")
                        put("description","Tuesday")
                        put("category", 2)
                    },
                    ContentValues().apply {
                        put("name" ,"wednesday")
                        put("description","Wednesday")
                        put("category", 2)
                    },
                    ContentValues().apply {
                        put("name" ,"thursday")
                        put("description","Thursday")
                        put("category", 2)
                    },
                    ContentValues().apply {
                        put("name" ,"friday")
                        put("description","Friday")
                        put("category", 2)
                    },
                    ContentValues().apply {
                        put("name" ,"saturday")
                        put("description","Saturday")
                        put("category", 2)
                    },
                    ContentValues().apply {
                        put("name" ,"sunday")
                        put("description","Sunday")
                        put("category", 2)
                    },
                )
                values.forEach{
                    db.insert("frequency",CONFLICT_NONE,it)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        // do something every time database is open
    }
}