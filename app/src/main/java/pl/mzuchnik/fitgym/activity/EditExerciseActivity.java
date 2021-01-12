package pl.mzuchnik.fitgym.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

import pl.mzuchnik.fitgym.R;
import pl.mzuchnik.fitgym.entity.Exercise;
import pl.mzuchnik.fitgym.entity.ExerciseType;
import pl.mzuchnik.fitgym.viewmodel.ExerciseViewModel;
import pl.mzuchnik.fitgym.viewmodel.UserViewModel;

public class EditExerciseActivity extends AppCompatActivity {

    private ExerciseViewModel exerciseViewModel;
    private Exercise exercise;
    long exerciseId;

    private EditText editTextDescription;
    private Spinner spinnerExerciseType;
    private EditText shoulderEditText;
    private EditText chestEditText;
    private EditText backEditText;
    private EditText bicepsEditText;
    private EditText tricepsEditText;
    private EditText forearmEditText;
    private EditText stomachEditText;
    private EditText thighEditText;
    private EditText bumEditText;
    private EditText calfEditText;

    private ImageView imageView;

    private Button showMeasureButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exercise);
        try {
            bindRIdWithViews();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        writeValuesToViews();
    }

    private void bindRIdWithViews() throws ExecutionException, InterruptedException {
        editTextDescription = findViewById(R.id.edit_text_exercise_description);
        spinnerExerciseType = findViewById(R.id.spinner_exercise_type);
        shoulderEditText = findViewById(R.id.edit_text_exercise_shoulder);
        chestEditText = findViewById(R.id.edit_text_exercise_chest);
        backEditText = findViewById(R.id.edit_text_exercise_back);
        bicepsEditText = findViewById(R.id.edit_text_exercise_biceps);
        tricepsEditText = findViewById(R.id.edit_text_exercise_triceps);
        forearmEditText = findViewById(R.id.edit_text_exercise_forearm);
        stomachEditText = findViewById(R.id.edit_text_exercise_stomach);
        thighEditText = findViewById(R.id.edit_text_exercise_thigh);
        bumEditText = findViewById(R.id.edit_text_exercise_bum);
        calfEditText = findViewById(R.id.edit_text_exercise_calf);
        showMeasureButton = findViewById(R.id.show_measure_button);
        imageView = findViewById(R.id.edit_image_view_user_photo);

        showMeasureButton.setOnClickListener(onClickListener);

        this.exerciseId = getIntent().getLongExtra("exerciseId", 0);

        this.exerciseViewModel = new ViewModelProvider(this,
                ViewModelProvider
                        .AndroidViewModelFactory
                        .getInstance(this.getApplication())).get(ExerciseViewModel.class);

        this.exercise = exerciseViewModel.findById(exerciseId);

    }

    private void writeValuesToViews(){
        editTextDescription.setText(exercise.getDescription());
        ArrayAdapter<ExerciseType> adapter =
                new ArrayAdapter<ExerciseType>(this, android.R.layout.simple_spinner_dropdown_item, Collections.singletonList(exercise
                .getExerciseType()));
        spinnerExerciseType.setEnabled(false);
        spinnerExerciseType.setAdapter(adapter);
        shoulderEditText.setText(String.valueOf(exercise.getShoulder()));
        chestEditText.setText(String.valueOf(exercise.getChest()));
        backEditText.setText(String.valueOf(exercise.getBack()));
        bicepsEditText.setText(String.valueOf(exercise.getBiceps()));
        tricepsEditText.setText(String.valueOf(exercise.getTriceps()));
        forearmEditText.setText(String.valueOf(exercise.getForearm()));
        stomachEditText.setText(String.valueOf(exercise.getStomach()));
        thighEditText.setText(String.valueOf(exercise.getThigh()));
        bumEditText.setText(String.valueOf(exercise.getBum()));
        calfEditText.setText(String.valueOf(exercise.getCalf()));
        if(exercise.getPhotosPath() != null && !exercise.getPhotosPath().isEmpty()) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(exercise.getPhotosPath()));
            imageView.setVisibility(View.VISIBLE);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(),MeasureActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_exercise_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_exercise:
                saveExercise();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void saveExercise() {
        String description = editTextDescription.getText().toString();
        ExerciseType exerciseType = (ExerciseType) spinnerExerciseType.getSelectedItem();

        int shoulder, chest, back, biceps, triceps, forearm, stomach, thigh, bum, calf;

        try {
            shoulder = getIntFromEditText(shoulderEditText);
            chest = getIntFromEditText(chestEditText);
            back = getIntFromEditText(backEditText);
            biceps = getIntFromEditText(bicepsEditText);
            triceps = getIntFromEditText(tricepsEditText);
            forearm = getIntFromEditText(forearmEditText);
            stomach = getIntFromEditText(stomachEditText);
            thigh = getIntFromEditText(thighEditText);
            bum = getIntFromEditText(bumEditText);
            calf = getIntFromEditText(calfEditText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Measure should be integer value", Toast.LENGTH_SHORT).show();
            return;
        }
        if (description.trim().isEmpty() || exerciseType == null) {
            Toast.makeText(this, "Please insert description and exercise type", Toast.LENGTH_SHORT).show();
            return;
        }

        exercise.setExerciseType(exerciseType);
        exercise.setDescription(description);
        exercise.setShoulder(shoulder);
        exercise.setChest(chest);
        exercise.setBack(back);
        exercise.setBiceps(biceps);
        exercise.setBiceps(triceps);
        exercise.setForearm(forearm);
        exercise.setStomach(stomach);
        exercise.setThigh(thigh);
        exercise.setBum(bum);
        exercise.setCalf(calf);

        exerciseViewModel.update(exercise);

        Toast.makeText(this, "Exercise updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    private int getIntFromEditText(EditText editText) throws NumberFormatException {
        return Integer.parseInt(editText.getText().toString());
    }

}