package pl.mzuchnik.fitgym.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.ExecutionException;

import pl.mzuchnik.fitgym.R;
import pl.mzuchnik.fitgym.entity.User;
import pl.mzuchnik.fitgym.viewmodel.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    private UserViewModel userViewModel;
    private EditText loginEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registrationButton;
    private TextView errorMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        this.userViewModel = new ViewModelProvider(this,
                ViewModelProvider
                        .AndroidViewModelFactory
                        .getInstance(this.getApplication())).get(UserViewModel.class);

        this.loginEditText = findViewById(R.id.edit_text_login);
        this.passwordEditText = findViewById(R.id.edit_text_password);
        this.errorMessage = findViewById(R.id.login_error_message);
        this.loginButton = findViewById(R.id.button_log_in);
        this.registrationButton = findViewById(R.id.button_sign_up);

        errorMessage.setVisibility(View.INVISIBLE);

        loginButton.setOnClickListener(loginOnClickListener);
        registrationButton.setOnClickListener(registrationOnClickListener);

    }

    private User checkForUserWithLogin(String login){
        User user = null;
        try {
            user = userViewModel.findByLogin(login);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return user;
    }
    View.OnClickListener loginOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            String loginForm = loginEditText.getText().toString();
            User user = checkForUserWithLogin(loginForm);

            if(user != null){
                String passwordForm = passwordEditText.getText().toString();
                if(user.getPassword().equals(passwordForm)){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("userId", user.getId());
                    startActivity(intent);
                    finish();
                    Toast.makeText(LoginActivity.this, "Logged as " + user.getLogin(), Toast.LENGTH_SHORT).show();
                }else{
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText("Incorrect login or password.");
                }
            }else{
                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("Incorrect login or password.");
            }
        }
    };

    View.OnClickListener registrationOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent registrationIntent = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(registrationIntent);
        }
    };

}
