package it.school.mailproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.slider.Slider;

import java.util.function.Consumer;

import javax.mail.MessagingException;

public class LoginActivity extends Activity {
    class ChangeLoginEditTextListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            LoginActivity.this.resetButton();
        }
    }

    public Button sendBtn;
    public EditText edServer;
    public EditText edPort;
    public EditText edMail;
    public EditText edPassword;
    public MailHandler mh;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sendBtn = findViewById(R.id.sendBtn);
        edServer = findViewById(R.id.editTextServer);
        edPort = findViewById(R.id.editTextPort);
        edMail = findViewById(R.id.editTextMail);
        edPassword = findViewById(R.id.editTextPassword);

        setForAllEditText((ed -> ed.addTextChangedListener(new ChangeLoginEditTextListener())));

        sendBtn.setOnClickListener(btn -> {
            Runnable r = () ->
            {
                try {
                    String port = edPort.getText().toString();
                    String mail = edMail.getText().toString();
                    String pass = edPassword.getText().toString();
                    String serv = edServer.getText().toString();

                    mh = new MailHandler(
                        mail, pass, serv, port
                    );
                    mh.connect();

                    Intent i = new Intent();
                    i.putExtra("mh", mh);
                    setResult(RESULT_OK, i);
                    finish();
                } catch (MessagingException e) {
                    e.printStackTrace();
                    new Thread(this::setDangerForButton).start();
                }
            };

            new Thread(r).start();
        });

        resetButton();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setForAllEditText(Consumer<EditText> consumer) {
        consumer.accept(edPort);
        consumer.accept(edPassword);
        consumer.accept(edMail);
        consumer.accept(edServer);
    }

    public void resetButton() {
        sendBtn.setBackgroundColor(Color.CYAN);
    }

    public void setDangerForButton() {
        sendBtn.setBackgroundColor(Color.RED);
    }
}