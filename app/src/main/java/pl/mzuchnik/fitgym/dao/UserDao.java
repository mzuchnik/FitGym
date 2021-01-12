package pl.mzuchnik.fitgym.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pl.mzuchnik.fitgym.entity.User;

@Dao
public interface UserDao {

    @Query("SELECT * from users WHERE id = :id")
    User findUserById(long id);

    @Query("SELECT * from users WHERE login = :login")
    User findUserByLogin(String login);

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM users")
    void deleteAllUsers();

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();





}
