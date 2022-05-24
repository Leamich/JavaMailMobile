package it.school.mailproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

public class MainActivity extends AppCompatActivity {
    MailHandler mh;
    ListView lw;
    SimpleAdapter ada;
    ArrayList<HashMap<String, String>> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lw = findViewById(R.id.listView);
        ada = new SimpleAdapter(this, messages, android.R.layout.simple_list_item_2,
                new String[] {"s", "f"},
                new int[]{android.R.id.text1, android.R.id.text2});
        lw.setAdapter(ada);

        startActivityForResult(new Intent(this, LoginActivity.class), 16);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mh = data.getParcelableExtra("mh");
        fillListView();
    }

    public void fillListView() {
        Runnable r = () -> {
            try {
                Folder inbox = mh.getInbox();
                inbox.open(Folder.READ_ONLY);
                Message[] msgs = inbox.getMessages();
                messages.clear();
                for (int i = msgs.length - 1; i >= 0; i--) {
                    Message m = msgs[i];
                    HashMap<String, String> res = new HashMap<>();
                    res.put("f", Arrays.toString(m.getFrom()));

                    m.getFrom()[0].getType();

                    res.put("s", m.getSubject());
                    messages.add(res);
                    runOnUiThread(() -> ada.notifyDataSetChanged());
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        };
        new Thread(r).start();
    }
}