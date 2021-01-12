package pl.mzuchnik.fitgym.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import pl.mzuchnik.fitgym.R;
import pl.mzuchnik.fitgym.entity.User;
import pl.mzuchnik.fitgym.viewmodel.UserViewModel;

public class RegistrationActivity extends AppCompatActivity {

    private TextView errorTextView;
    private EditText loginEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private EditText nameEditText;
    private EditText surnameEditText;
    private Button processRegistrationButton;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setTitle("Registration");

        errorTextView = findViewById(R.id.registration_error_message);
        loginEditText = findViewById(R.id.edit_text_login_registration);
        passwordEditText = findViewById(R.id.edit_text_password_registration);
        passwordConfirmEditText = findViewById(R.id.edit_text_password_registration_confirm);
        processRegistrationButton = findViewById(R.id.button_process_registration);
        nameEditText = findViewById(R.id.edit_text_name_registration);
        surnameEditText = findViewById(R.id.edit_text_surname_registration);

        userViewModel = new ViewModelProvider(this,
                ViewModelProvider
                        .AndroidViewModelFactory
                        .getInstance(this.getApplication())).get(UserViewModel.class);

        errorTextView.setVisibility(View.INVISIBLE);


        processRegistrationButton.setOnClickListener(processRegistrationButtonClick());
    }

    private Button.OnClickListener processRegistrationButtonClick() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String passwordConfirm = passwordConfirmEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String surname = surnameEditText.getText().toString();

                boolean hasError = false;

                if (!isPasswordMatch(password, passwordConfirm)) {
                    errorTextView.setText("Please insert the same password.");
                    hasError = true;
                }
                if (!isLoginUnique(login)) {
                    errorTextView.setText("Inserted login is already in use.");
                    hasError = true;
                }
                if(name.isEmpty() || surname.isEmpty()){
                    errorTextView.setText("Please insert correct name or surname");
                    hasError = true;
                }

                if(!hasError){
                    User user = new User();
                    user.setLogin(login);
                    user.setName(name);
                    user.setSurname(surname);
                    user.setPassword(password);
                    userViewModel.insert(user);
                    Toast.makeText(getApplicationContext(), "Added user: "+login, Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    errorTextView.setVisibility(View.VISIBLE);
                }
            }
        };
        return onClickListener;
    }

    private boolean isLoginUnique(String login) {
        boolean isUnique = false;
        try {
            isUnique = userViewModel.findByLogin(login) == null ? true : false;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return isUnique;
    }

    private boolean isPasswordMatch(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }
}