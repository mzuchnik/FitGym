package pl.mzuchnik.fitgym.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import pl.mzuchnik.fitgym.R;
import pl.mzuchnik.fitgym.entity.Exercise;
import pl.mzuchnik.fitgym.entity.ExerciseType;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseHolder> {

    private List<Exercise> exercises = new ArrayList<>();
    private OnExerciseClickListener onExerciseClickListener;

    public ExerciseAdapter(OnExerciseClickListener exerciseClickListener) {
        this.onExerciseClickListener = exerciseClickListener;
    }

    @NonNull
    @Override
    public ExerciseHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_item, parent, false);
        return new ExerciseHolder(itemView, onExerciseClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseHolder holder, int position) {
        Exercise currentExercise = exercises.get(position);
        holder.textViewExerciseType.setText(currentExercise.getExerciseType().name());
        holder.imageViewExerciseType.setImageResource(getDrawableForExerciseType(currentExercise.getExerciseType()));
        holder.textViewExerciseDate.setText(currentExercise.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        holder.textViewExerciseDescription.setText(currentExercise.getDescription());
        holder.textViewExerciseTime.setText(currentExercise.getDate().format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    class ExerciseHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textViewExerciseType;
        private TextView textViewExerciseDate;
        private TextView textViewExerciseDescription;
        private TextView textViewExerciseTime;
        private ImageView imageViewExerciseType;

        private OnExerciseClickListener onExerciseClickListener;

        public ExerciseHolder(@NonNull View itemView, OnExerciseClickListener onExerciseClickListener) {
            super(itemView);
            textViewExerciseType = itemView.findViewById(R.id.text_view_exercise_type);
            textViewExerciseDate = itemView.findViewById(R.id.text_view_exercise_date);
            textViewExerciseDescription = itemView.findViewById(R.id.text_view_exercise_description);
            textViewExerciseTime = itemView.findViewById(R.id.text_view_exercise_time);
            imageViewExerciseType = itemView.findViewById(R.id.image_view_exercise_type);

            this.onExerciseClickListener = onExerciseClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("ExerciseHolder -> onClick(): ", ""+v);
            onExerciseClickListener.onExerciseClickListener(getAdapterPosition());
        }
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    public Exercise getExerciseAtPosition(int position){
        return exercises.get(position);
    }

    private int getDrawableForExerciseType(ExerciseType exerciseType){
        int drawable = 0;
        switch (exerciseType){
            case GYM: drawable = R.drawable.ic_baseline_accessibility_new_24; break;
            case CYCLING: drawable = R.drawable.ic_baseline_directions_bike_24; break;
            case RUNNING: drawable = R.drawable.ic_baseline_directions_run_24; break;
            case SWIMMING: drawable = R.drawable.ic_baseline_pool_24; break;
        }
        return drawable;
    }

    public interface OnExerciseClickListener {
        void onExerciseClickListener(int position);
    }



}
