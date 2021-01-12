package pl.mzuchnik.fitgym.database.type_converter;

import androidx.room.TypeConverter;

import pl.mzuchnik.fitgym.entity.ExerciseType;

public class ExerciseTypeConverter {

    @TypeConverter
    public static String fromExerciseTypeToString(ExerciseType value) {
        return value.name();
    }

    @TypeConverter
    public static ExerciseType fromStringToExerciseType(String value) {
        return ExerciseType.valueOf(value);
    }
}
