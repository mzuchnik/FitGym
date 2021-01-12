package pl.mzuchnik.fitgym.database.type_converter;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateConverter {

    @TypeConverter
    public static LocalDateTime toDate(Long dateLong){
        return dateLong == null ? null: LocalDateTime.ofInstant(Instant.ofEpochMilli(dateLong), ZoneId.of("Europe/Warsaw"));
    }

    @TypeConverter
    public static Long fromDate(LocalDateTime date){
        return date == null ? null : date.atZone(ZoneId.of("Europe/Warsaw")).toInstant().toEpochMilli();
    }
}
