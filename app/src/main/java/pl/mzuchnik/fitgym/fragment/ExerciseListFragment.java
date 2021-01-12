package pl.mzuchnik.fitgym.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.mzuchnik.fitgym.R;
import pl.mzuchnik.fitgym.activity.AddExerciseActivity;
import pl.mzuchnik.fitgym.activity.EditExerciseActivity;
import pl.mzuchnik.fitgym.activity.MainActivity;
import pl.mzuchnik.fitgym.adapter.ExerciseAdapter;
import pl.mzuchnik.fitgym.entity.Exercise;
import pl.mzuchnik.fitgym.entity.ExerciseType;
import pl.mzuchnik.fitgym.viewmodel.ExerciseViewModel;

import static android.app.Activity.RESULT_OK;


public class ExerciseListFragment extends Fragment implements ExerciseAdapter.OnExerciseClickListener {

    public static final int ADD_EXERCISE_REQUEST = 1;
    private ExerciseViewModel exerciseViewModel;
    private ExerciseAdapter exerciseAdapter;
    private long userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.userId = getArguments().getLong("userId");

        return inflater.inflate(R.layout.fragment_exercise_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.exercise_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        this.exerciseAdapter = new ExerciseAdapter(this);
        recyclerView.setAdapter(this.exerciseAdapter);

        this.exerciseViewModel = new ViewModelProvider((MainActivity) requireActivity()).get(ExerciseViewModel.class);

        exerciseViewModel.getAllExerciseByUserId(userId).observe(getViewLifecycleOwner(), new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                Collections.sort(exercises, sortByDate());
                exerciseAdapter.setExercises(exercises);
            }
        });

        FloatingActionButton floatingActionButton = view.findViewById(R.id.button_add_exercise);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddExerciseActivity.class);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, ADD_EXERCISE_REQUEST);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                 int direction) {
                exerciseViewModel.delete(
                        exerciseAdapter.getExerciseAtPosition(
                                viewHolder.getAdapterPosition()));
                Toast.makeText(getActivity(), "Exercise deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_EXERCISE_REQUEST && resultCode == RESULT_OK) {
            Exercise newExercise = (Exercise) data.getSerializableExtra(AddExerciseActivity.EXTRA_EXERCISE_OBJECT);
            newExercise.setDate(LocalDateTime.now(ZoneId.of("Europe/Warsaw")));
            exerciseViewModel.insert(newExercise);
            Toast.makeText(getActivity(), "Exercise saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Exercise not saved", Toast.LENGTH_SHORT).show();
        }
    }

    private Comparator<Exercise> sortByDate() {
        return new Comparator<Exercise>() {
            @Override
            public int compare(Exercise o1, Exercise o2) {
                if (o1.getDate().isBefore(o2.getDate()))
                    return 1;
                else if (o1.getDate().equals(o2.getDate()))
                    return 0;
                else
                    return -1;
            }
        };
    }

    @Override
    public void onExerciseClickListener(int position) {
        Log.d("OnExerciseCLickListener: ", "" + position);

        Exercise exerciseAtPosition = this.exerciseAdapter.getExerciseAtPosition(position);
        Intent editExerciseIntent = new Intent(getContext(), EditExerciseActivity.class);
        editExerciseIntent.putExtra("exerciseId", exerciseAtPosition.getId());

        startActivity(editExerciseIntent);
    }
}