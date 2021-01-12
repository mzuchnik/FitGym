package pl.mzuchnik.fitgym.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.time.LocalDateTime;

import pl.mzuchnik.fitgym.database.type_converter.DateConverter;
import pl.mzuchnik.fitgym.database.type_converter.ExerciseTypeConverter;

@Entity(tableName = "userExercises")
@TypeConverters({ExerciseTypeConverter.class, DateConverter.class})
public class Exercise implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "userId")
    @ForeignKey(entity = User.class,
            parentColumns = "id",
            childColumns = "userId")
    private long userId;

    /*Storage multiple path to image
    * Image's path are split with ',' character*/
    private String photosPath;

    private ExerciseType exerciseType;

    private LocalDateTime date;

    private String description;

    private int shoulder;

    private int chest;

    private int back;

    private int biceps;

    private int triceps;

    private int forearm;

    private int stomach;

    private int thigh;

    private int bum;

    private int calf;

    public Exercise() {
    }

    public Exercise(long userId, ExerciseType exerciseType, LocalDateTime date, String description) {
        this.userId = userId;
        this.exerciseType = exerciseType;
        this.date = date;
        this.description = description;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(ExerciseType exerciseType) {
        this.exerciseType = exerciseType;
    }

    public int getShoulder() {
        return shoulder;
    }

    public void setShoulder(int shoulder) {
        this.shoulder = shoulder;
    }

    public int getChest() {
        return chest;
    }

    public void setChest(int chest) {
        this.chest = chest;
    }

    public int getBack() {
        return back;
    }

    public void setBack(int back) {
        this.back = back;
    }

    public int getBiceps() {
        return biceps;
    }

    public void setBiceps(int biceps) {
        this.biceps = biceps;
    }

    public int getTriceps() {
        return triceps;
    }

    public void setTriceps(int triceps) {
        this.triceps = triceps;
    }

    public int getForearm() {
        return forearm;
    }

    public void setForearm(int forearm) {
        this.forearm = forearm;
    }

    public int getStomach() {
        return stomach;
    }

    public void setStomach(int stomach) {
        this.stomach = stomach;
    }

    public int getThigh() {
        return thigh;
    }

    public void setThigh(int thigh) {
        this.thigh = thigh;
    }

    public int getBum() {
        return bum;
    }

    public void setBum(int bum) {
        this.bum = bum;
    }

    public int getCalf() {
        return calf;
    }

    public void setCalf(int calf) {
        this.calf = calf;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotosPath() {
        return photosPath;
    }

    public void setPhotosPath(String photosPath) {
        this.photosPath = photosPath;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", userId=" + userId +
                ", exerciseType=" + exerciseType +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", shoulder=" + shoulder +
                ", chest=" + chest +
                ", back=" + back +
                ", biceps=" + biceps +
                ", triceps=" + triceps +
                ", forearm=" + forearm +
                ", stomach=" + stomach +
                ", thigh=" + thigh +
                ", bum=" + bum +
                ", calf=" + calf +
                '}';
   }


}
