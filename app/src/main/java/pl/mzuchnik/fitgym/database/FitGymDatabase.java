package pl.mzuchnik.fitgym.database;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;

import pl.mzuchnik.fitgym.dao.ExerciseDao;
import pl.mzuchnik.fitgym.dao.UserDao;
import pl.mzuchnik.fitgym.entity.Exercise;
import pl.mzuchnik.fitgym.entity.ExerciseType;
import pl.mzuchnik.fitgym.entity.User;

@Database(entities = {User.class, Exercise.class}, version = 1)
public abstract class FitGymDatabase extends RoomDatabase {

    private static FitGymDatabase instance;

    public abstract UserDao userDao();

    public abstract ExerciseDao exerciseDao();

    public static synchronized FitGymDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    FitGymDatabase.class, "fit_gym_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    //.setJournalMode(JournalMode.TRUNCATE)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private UserDao userDao;
        private ExerciseDao exerciseDao;

        private PopulateDbAsyncTask(FitGymDatabase fitGymDatabase) {
            this.userDao = fitGymDatabase.userDao();
            this.exerciseDao = fitGymDatabase.exerciseDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.insert(new User("admin","admin"));
            userDao.insert(new User("user","user"));

            for (int i = 0; i < 10; i++) {
                Exercise exercise = new Exercise(1, ExerciseType.values()[new Random().nextInt(4)], LocalDateTime.now(ZoneId.of("Europe/Warsaw")),"Short description " + i);
                exerciseDao.insert(exercise);
            }
            for (int i = 0; i < 3; i++) {
                Exercise exercise = new Exercise(2, ExerciseType.values()[new Random().nextInt(4)], LocalDateTime.now(ZoneId.of("Europe/Warsaw")),"Short description " + i);
                exerciseDao.insert(exercise);
            }
            return null;
        }
    }

}
