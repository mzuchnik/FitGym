package pl.mzuchnik.fitgym.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import pl.mzuchnik.fitgym.R;
import pl.mzuchnik.fitgym.activity.MainActivity;
import pl.mzuchnik.fitgym.entity.User;
import pl.mzuchnik.fitgym.viewmodel.UserViewModel;

public class AccountFragment extends Fragment {

    private UserViewModel userViewModel;
    private User user;

    private TextView errorTextView;
    private EditText loginEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private EditText nameEditText;
    private EditText surnameEditText;
    private Button editButton;
    private Button logoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        long userId = getArguments().getLong("userId");

        this.userViewModel = new ViewModelProvider((MainActivity) requireActivity()).get(UserViewModel.class);
        try {
            this.user = userViewModel.findUserById(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inflater.inflate(R.layout.fragment_account, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        errorTextView = view.findViewById(R.id.error_message_account);
        loginEditText = view.findViewById(R.id.edit_text_login_account);
        passwordEditText = view.findViewById(R.id.edit_text_password_account);
        passwordConfirmEditText = view.findViewById(R.id.edit_text_password_registration_account);
        nameEditText = view.findViewById(R.id.edit_text_name_account);
        surnameEditText = view.findViewById(R.id.edit_text_surname_account);
        logoutButton = view.findViewById(R.id.button_logout_account);
        editButton = view.findViewById(R.id.button_process_account);


        loginEditText.setText(user.getLogin());
        nameEditText.setText(user.getName());
        surnameEditText.setText(user.getSurname());
        passwordEditText.setText(user.getPassword());


        errorTextView.setVisibility(View.INVISIBLE);

        editButton.setOnClickListener(new View.OnClickListener() {
            String errorMessage = "";
            String name, surname, password;
            @Override
            public void onClick(View v) {
                if((name = nameEditText.getText().toString()).isEmpty()){
                    errorMessage = "Name field cannot be empty";
                }
                if((surname=surnameEditText.getText().toString()).isEmpty()){
                    errorMessage = "Surname filed cannot be empty";
                }
                if((password=passwordEditText.getText().toString()).isEmpty() || passwordEditText.getText().toString().length() < 4){
                    errorMessage = "New password cannot be empty and less than 4 chars";
                }
                if(!passwordConfirmEditText.getText().toString().equals(user.getPassword())){
                    errorMessage = "Confirm password dont match to user password";
                }
                if(!errorMessage.isEmpty()){
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setTextColor(Color.RED);
                    errorTextView.setText(errorMessage);
                    return;
                }

                user.setName(name);
                user.setSurname(surname);
                user.setPassword(password);

                userViewModel.update(user);
                errorMessage = "User account updated";
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setTextColor(Color.GREEN);
                errorTextView.setText(errorMessage);
            }
        });

    }

}
