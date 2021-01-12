package pl.mzuchnik.fitgym.viewmodel;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import pl.mzuchnik.fitgym.entity.User;
import pl.mzuchnik.fitgym.repository.UserRepository;

public class UserViewModel extends AndroidViewModel{

    private UserRepository userRepository;
    private LiveData<List<User>> allUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);

        userRepository = new UserRepository(application);
        allUsers = userRepository.getAllUsers();
    }

    public User findByLogin(String login) throws ExecutionException, InterruptedException {
        return userRepository.findByLogin(login);
    }

    public User findUserById(long id) throws ExecutionException, InterruptedException {
        return userRepository.findUserById(id);
    }

    public void insert(User user){
        userRepository.insert(user);
    }

    public void update(User user){
        userRepository.update(user);
    }

    public void delete(User user){
        userRepository.delete(user);
    }

    public void deleteAllUsers(){
        userRepository.deleteAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
}
