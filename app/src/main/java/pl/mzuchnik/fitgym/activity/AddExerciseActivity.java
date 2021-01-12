package pl.mzuchnik.fitgym.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import pl.mzuchnik.fitgym.R;
import pl.mzuchnik.fitgym.entity.Exercise;
import pl.mzuchnik.fitgym.entity.ExerciseType;

public class AddExerciseActivity extends AppCompatActivity {

    public static final int CAMERA_DATA_REQUEST_CODE = 22;

    public static final String EXTRA_EXERCISE_OBJECT
            = "pl.mzuchnik.fitgym.activity.EXTRA_EXERCISE_OBJECT";

    private File photoFile;

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

    private Button takePhotoButton;


    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        this.userId = getIntent().getLongExtra("userId", 0);

        bindRIdWithViews();

        ArrayAdapter<ExerciseType> adapter =
                new ArrayAdapter<ExerciseType>(this, android.R.layout.simple_spinner_dropdown_item, ExerciseType.values());
        spinnerExerciseType.setAdapter(adapter);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
        setTitle("Add exercise");

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

        Intent data = new Intent();

        Exercise newExercise = new Exercise();

        newExercise.setUserId(userId);
        newExercise.setExerciseType(exerciseType);
        newExercise.setDescription(description);
        newExercise.setShoulder(shoulder);
        newExercise.setChest(chest);
        newExercise.setBack(back);
        newExercise.setBiceps(biceps);
        newExercise.setBiceps(triceps);
        newExercise.setForearm(forearm);
        newExercise.setStomach(stomach);
        newExercise.setThigh(thigh);
        newExercise.setBum(bum);
        newExercise.setCalf(calf);
        newExercise.setPhotosPath(photoFile.getAbsolutePath());

        data.putExtra(EXTRA_EXERCISE_OBJECT, newExercise);

        setResult(RESULT_OK, data);
        finish();

    }

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

    private void bindRIdWithViews() {
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
        takePhotoButton = findViewById(R.id.button_take_photo);
        imageView = findViewById(R.id.image_view_user_photo);

        showMeasureButton.setOnClickListener(showMeasureOnClickListener);

        takePhotoButton.setOnClickListener(openCameraOnClickListener);
    }

    private int getIntFromEditText(EditText editText) throws NumberFormatException {
        return Integer.parseInt(editText.getText().toString());
    }

    private View.OnClickListener showMeasureOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), MeasureActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener openCameraOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                photoFile = getPhotoFile(UUID.randomUUID().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri fileProvider = FileProvider.getUriForFile(getApplication(), "pl.mzuchnik.fitgym.fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

            startActivityForResult(intent, CAMERA_DATA_REQUEST_CODE);
        }
    };

    private File getPhotoFile(String fileName) throws IOException {
        File externalFilesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(fileName,".jpg", externalFilesDir);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_DATA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}