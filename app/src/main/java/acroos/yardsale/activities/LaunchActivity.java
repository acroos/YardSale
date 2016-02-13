package acroos.yardsale.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import acroos.yardsale.PasswordHash;
import acroos.yardsale.R;
import acroos.yardsale.data.DatabaseHandler;
import acroos.yardsale.models.YardSaleUser;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        DatabaseHandler.init(this);

        Button btnLogin = (Button)findViewById(R.id.btn_log_in);
        Button btnSignUp = (Button)findViewById(R.id.btn_sign_up);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LaunchActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.fragment_login, null);
        final EditText emailField = (EditText)layout.findViewById(R.id.email_field);
        final EditText passwordField = (EditText)layout.findViewById(R.id.password_field);

        final AlertDialog dialog = builder.setView(layout)
                .setPositiveButton(R.string.login_dialog_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .setNegativeButton(R.string.login_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                })
                .create();

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();

                YardSaleUser user = DatabaseHandler.getInstance().getUser(email);

                if (user == null || user.getPasswordHash() == null) {
                    Toast.makeText(LaunchActivity.this, "Email address not found", Toast.LENGTH_LONG).show();
                    emailField.requestFocus();
                    return;
                }
                String password = passwordField.getText().toString();

                try {
                    if (password.isEmpty()) {
                        Toast.makeText(LaunchActivity.this, "Please enter a password", Toast.LENGTH_LONG).show();
                    } else if(PasswordHash.validatePassword(password, user.getPasswordHash())) {
                        Intent i = new Intent(LaunchActivity.this, NavigationActivity.class);
                        i.putExtra("USERNAME", email);
                        startActivity(i);
                        LaunchActivity.this.finish();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(LaunchActivity.this, "Invalid password", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(LaunchActivity.this, "Internal error, please try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class GetRequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String dataUrl = "http://localhost:3100/api/v0";
            String dataParams = params[0];
            URL url;
            HttpURLConnection connection = null;

            try {
                url = new URL(dataUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Content-Length", Integer.toString(dataParams.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
                writer.writeBytes(dataParams);
                writer.flush();
                writer.close();

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                reader.close();

                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    class PostRequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}