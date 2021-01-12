package pl.mzuchnik.fitgym.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import pl.mzuchnik.fitgym.dao.ExerciseDao;
import pl.mzuchnik.fitgym.database.FitGymDatabase;
import pl.mzuchnik.fitgym.entity.Exercise;

public class ExerciseRepository {

    private ExerciseDao exerciseDao;

    public ExerciseRepository(Application application) {
        FitGymDatabase fitGymDatabase = FitGymDatabase.getInstance(application);
        exerciseDao = fitGymDatabase.exerciseDao();
    }

    public Exercise findById(long id) throws ExecutionException, InterruptedException {
        return new FindByExerciseIdAsyncTask(exerciseDao).execute(id).get();
    }
    public void insert(Exercise exercise){
        new InsertExerciseAsyncTask(exerciseDao).execute(exercise);
    }

    public void update(Exercise exercise){
        new UpdateExerciseAsyncTask(exerciseDao).execute(exercise);
    }

    public void delete(Exercise exercise){
        new DeleteExerciseAsyncTask(exerciseDao).execute(exercise);
    }

    public void deleteAllForUserId(long userId){
        new DeleteAllForUserExerciseAsyncTask(exerciseDao).execute(userId);
    }

    public LiveData<List<Exercise>> getAllUserExerciseForUserId(long userId) {
        return exerciseDao.findAllExercisesForUserId(userId);
    }

    public LiveData<List<Exercise>> getAllUserExercise(){
        return exerciseDao.findAllExercises();
    }

    private static class InsertExerciseAsyncTask extends AsyncTask<Exercise, Void, Void>{
        private ExerciseDao exerciseDao;

        public InsertExerciseAsyncTask(ExerciseDao exerciseDao) {
            this.exerciseDao = exerciseDao;
        }

        @Override
        protected Void doInBackground(Exercise... exercises) {
            exerciseDao.insert(exercises[0]);
            return null;
        }
    }
    private static class UpdateExerciseAsyncTask extends AsyncTask<Exercise, Void, Void>{
        private ExerciseDao exerciseDao;

        public UpdateExerciseAsyncTask(ExerciseDao exerciseDao) {
            this.exerciseDao = exerciseDao;
        }

        @Override
        protected Void doInBackground(Exercise... exercises) {
            exerciseDao.update(exercises[0]);
            return null;
        }
    }
    private static class DeleteExerciseAsyncTask extends AsyncTask<Exercise, Void, Void>{
        private ExerciseDao exerciseDao;

        public DeleteExerciseAsyncTask(ExerciseDao exerciseDao) {
            this.exerciseDao = exerciseDao;
        }

        @Override
        protected Void doInBackground(Exercise... exercises) {
            exerciseDao.delete(exercises[0]);
            return null;
        }
    }
    private static class DeleteAllForUserExerciseAsyncTask extends AsyncTask<Long, Void, Void>{
        private ExerciseDao exerciseDao;

        public DeleteAllForUserExerciseAsyncTask(ExerciseDao exerciseDao) {
            this.exerciseDao = exerciseDao;
        }

        @Override
        protected Void doInBackground(Long... longs) {
            exerciseDao.deleteAllForUserId(longs[0]);
            return null;
        }
    }
    private static class FindByExerciseIdAsyncTask extends AsyncTask<Long, Exercise, Exercise>{
        private ExerciseDao exerciseDao;

        public FindByExerciseIdAsyncTask(ExerciseDao exerciseDao) {
            this.exerciseDao = exerciseDao;
        }

        @Override
        protected Exercise doInBackground(Long... id) {
            return exerciseDao.findById(id[0]);
        }
    }
}
