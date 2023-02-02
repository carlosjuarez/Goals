package com.juvcarl.goalsapp.data.local.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Goal::class,
            parentColumns = ["uid"],
            childColumns = ["goalId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Step(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val name: String,
    @ColumnInfo(index = true) val goalId: Int,
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Step::class,
            parentColumns = ["uid"],
            childColumns = ["stepId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Frequency::class,
            parentColumns = ["id"],
            childColumns = ["frequencyId"],
            onDelete = ForeignKey.CASCADE
        )

    ]
)
data class StepFrequency(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(index = true) val stepId: Int,
    @ColumnInfo(index = true) val frequencyId: Int,
    val date: LocalDate?,
    val time: LocalTime?,
    val startDate: Date?,
    val endDate: Date?,
    val duration: Int?
)

data class StepWithFrequencies(
    @Embedded val step: Step,
    @Relation(
        parentColumn = "uid",
        entityColumn = "stepId"
    )
    val frequencies: List<StepFrequency>
)


@Dao
interface StepDao {

    @Query("SELECT * FROM step where goalid = :goalId ORDER BY uid DESC")
    fun getStepsFromGoal(goalId: Int): Flow<List<Step>>

    @Delete
    suspend fun deleteStep(item: Step)

    @Upsert
    suspend fun upsertStep(item: Step)

    @Upsert
    suspend fun upsertStepFrequency(items: List<StepFrequency>)

    @Transaction
    @Query("SELECT * FROM step where goalId = :goalId ORDER BY uid DESC")
    fun getStepsWithFrequencies(goalId: Int): List<StepWithFrequencies>

}