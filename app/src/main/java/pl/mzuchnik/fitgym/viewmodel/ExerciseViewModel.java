package pl.mzuchnik.fitgym.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import pl.mzuchnik.fitgym.entity.Exercise;
import pl.mzuchnik.fitgym.repository.ExerciseRepository;

public class ExerciseViewModel extends AndroidViewModel {

    private ExerciseRepository exerciseRepository;
    //private LiveData<List<Exercise>> allExerciseByUserId;
    //private LiveData<List<Exercise>> allExercises;

    public ExerciseViewModel(@NonNull Application application) {
        super(application);

        exerciseRepository = new ExerciseRepository(application);
        //allExercises = exerciseRepository.getAllUserExercise();
    }

    public Exercise findById(long id) throws ExecutionException, InterruptedException {
        return exerciseRepository.findById(id);
    }

    public void insert(Exercise exercise){
        exerciseRepository.insert(exercise);
    }

    public void update(Exercise exercise){
        exerciseRepository.update(exercise);
    }

    public void delete(Exercise exercise){
        exerciseRepository.delete(exercise);
    }

    public void deleteAllForUserId(long userId){
        exerciseRepository.deleteAllForUserId(userId);
    }


    public LiveData<List<Exercise>> getAllExerciseByUserId(long userId) {
        return exerciseRepository.getAllUserExerciseForUserId(userId);
    }
    public LiveData<List<Exercise>> getAllExercise(){
        return exerciseRepository.getAllUserExercise();
    }
}
