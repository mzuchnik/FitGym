package pl.mzuchnik.fitgym.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.mzuchnik.fitgym.entity.Exercise;

@Dao
public interface ExerciseDao {

    @Insert
    void insert(Exercise exercise);

    @Update
    void update(Exercise exercise);

    @Delete
    void delete(Exercise exercise);

    @Query("SELECT * FROM userExercises WHERE userId = :userId")
    LiveData<List<Exercise>> findAllExercisesForUserId(long userId);

    @Query("SELECT * FROM userExercises")
    LiveData<List<Exercise>> findAllExercises();

    @Query("DELETE FROM userExercises WHERE userId = :userId")
    void deleteAllForUserId(long userId);

    @Query("SELECT * FROM userExercises WHERE id = :id")
    Exercise findById(long id);
}
