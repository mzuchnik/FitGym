package pl.mzuchnik.fitgym.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pl.mzuchnik.fitgym.R;
import pl.mzuchnik.fitgym.activity.MainActivity;
import pl.mzuchnik.fitgym.activity.StatsActivity;
import pl.mzuchnik.fitgym.adapter.ExerciseAdapter;
import pl.mzuchnik.fitgym.entity.Exercise;
import pl.mzuchnik.fitgym.entity.User;
import pl.mzuchnik.fitgym.viewmodel.ExerciseViewModel;
import pl.mzuchnik.fitgym.viewmodel.UserViewModel;


public class HomeFragment extends Fragment implements ExerciseAdapter.OnExerciseClickListener {

    private ExerciseViewModel exerciseViewModel;
    private UserViewModel userViewModel;
    private ExerciseAdapter firstExerciseAdapter;
    private ExerciseAdapter lastExerciseAdapter;
    private long userId;
    private Button statsButton;


    private TextView helloTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.userId = getArguments().getLong("userId");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.exerciseViewModel = new ViewModelProvider((MainActivity) requireActivity()).get(ExerciseViewModel.class);
        this.userViewModel = new ViewModelProvider((MainActivity) requireActivity()).get(UserViewModel.class);
        this.statsButton = view.findViewById(R.id.generate_stats_button);

        helloTextView = view.findViewById(R.id.hello_message_text_view);
        User user = null;
        try {
            user = userViewModel.findUserById(userId);
            helloTextView.setText("Hello " + user.getName() + " " + user.getSurname());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


        RecyclerView firstExerciseRecyclerView = view.findViewById(R.id.first_exercise_fragment);
        firstExerciseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firstExerciseRecyclerView.setHasFixedSize(true);

        this.firstExerciseAdapter = new ExerciseAdapter(this);
        firstExerciseRecyclerView.setAdapter(this.firstExerciseAdapter);


        RecyclerView lastExerciseRecyclerView = view.findViewById(R.id.last_exercise_fragment);
        lastExerciseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        lastExerciseRecyclerView.setHasFixedSize(true);

        this.lastExerciseAdapter = new ExerciseAdapter(this);
        lastExerciseRecyclerView.setAdapter(this.lastExerciseAdapter);

        lastExerciseRecyclerView.setAdapter(lastExerciseAdapter);


        final ArrayList<Exercise> firstExercise = new ArrayList<Exercise>();
        final ArrayList<Exercise> lastExercise = new ArrayList<Exercise>();


        exerciseViewModel.getAllExerciseByUserId(userId).observe(getViewLifecycleOwner(), new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                if (!exercises.isEmpty() && exercises.size() > 1) {
                    Collections.sort(exercises, sortByDate());
                    firstExercise.add(exercises.get(exercises.size() - 1));
                    firstExerciseAdapter.setExercises(firstExercise);
                    lastExercise.add(exercises.get(0));
                    lastExerciseAdapter.setExercises(lastExercise);
                }
            }
        });

        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StatsActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onExerciseClickListener(int position) {
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
}