package acroos.yardsale.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import acroos.yardsale.PasswordHash;
import acroos.yardsale.R;
import acroos.yardsale.data.DatabaseHandler;
import acroos.yardsale.models.YardSaleUser;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        DatabaseHandler.init(this);

        final EditText fullNameView = (EditText)findViewById(R.id.sign_up_full_name);
        final EditText emailView = (EditText)findViewById(R.id.sign_up_email);
        final EditText passwordView1 = (EditText)findViewById(R.id.sign_up_password_1);
        final EditText passwordView2 = (EditText)findViewById(R.id.sign_up_password_2);
        Button signUpButton = (Button)findViewById(R.id.sign_up_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = fullNameView.getText().toString();
                String email = emailView.getText().toString();
                String password1 = passwordView1.getText().toString();
                String password2 = passwordView2.getText().toString();

                if (fullName.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email.isEmpty() || !isValidEmail(email)) {
                    Toast.makeText(SignUpActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password1.isEmpty() || password2.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill in both password fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password1.equals(password2)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                YardSaleUser user = DatabaseHandler.getInstance().getUser(emailView.getText().toString());

                if (user != null) {
                    Toast.makeText(SignUpActivity.this, "Account already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                String hash;
                try {
                    hash = PasswordHash.createHash(password1);
                } catch (Exception e) {
                    Toast.makeText(SignUpActivity.this, "Internal error.  Please try again", Toast.LENGTH_SHORT).show();
                    return;
                }

                user = new YardSaleUser(email)
                        .setFullName(fullName)
                        .setPasswordHash(hash);

                DatabaseHandler.getInstance().addUser(user);

                Intent i = new Intent(SignUpActivity.this, NavigationActivity.class);
                i.putExtra("USERNAME", email);
                startActivity(i);
                SignUpActivity.this.finish();
            }
        });
    }

    private boolean isValidEmail(String email) {
        return true;
    }
}
