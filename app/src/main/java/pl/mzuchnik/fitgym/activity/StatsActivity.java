package pl.mzuchnik.fitgym.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import pl.mzuchnik.fitgym.R;
import pl.mzuchnik.fitgym.entity.Exercise;
import pl.mzuchnik.fitgym.util.AnimatedGifEncoder;
import pl.mzuchnik.fitgym.viewmodel.ExerciseViewModel;

public class StatsActivity extends AppCompatActivity {

    private long userId;
    private ExerciseViewModel exerciseViewModel;
    private List<Exercise> userExercise;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        this.userId = getIntent().getLongExtra("userId", 0);

        this.exerciseViewModel = new ViewModelProvider(this,
                ViewModelProvider
                        .AndroidViewModelFactory
                        .getInstance(this.getApplication())).get(ExerciseViewModel.class);
        this.imageView = findViewById(R.id.user_stat_image_view);

        exerciseViewModel.getAllExerciseByUserId(userId).observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(final List<Exercise> exercises) {
                byte[] gifbytes = generateGIF(getBitmapArrayFromExercisePaths(exercises));
                Glide.with(getApplicationContext())
                        .load(gifbytes)
                        .into(imageView);
            }
        });

    }


    private ArrayList<Bitmap> getBitmapArrayFromExercisePaths(List<Exercise> exercises){
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        for (Exercise exercise : exercises) {
            Bitmap bitmap = BitmapFactory.decodeFile(exercise.getPhotosPath());
            bitmaps.add(bitmap);
        }
        return bitmaps;
    }

    public byte[] generateGIF(List<Bitmap> bitmaps) {
        //ArrayList<Bitmap> bitmaps = getBitmapArrayFromExercisePaths();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(bos);
        encoder.setDelay(800);
        encoder.setQuality(20);
        for (Bitmap bitmap : bitmaps) {
            encoder.addFrame(bitmap);
        }
        encoder.finish();
        return bos.toByteArray();
    }

}