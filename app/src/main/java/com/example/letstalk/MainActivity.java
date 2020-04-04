package com.example.letstalk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {
    private static int SIGN_IN_CODE=1;
    private RelativeLayout activity;
    private FirebaseListAdapter<Message> adapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SIGN_IN_CODE){
            if(resultCode==RESULT_OK){
                Snackbar.make(activity, "Вы авторизованы",Snackbar.LENGTH_LONG);
               // displayAllMessages();
            } else{
                Snackbar.make(activity, "Вы не авторизованы",Snackbar.LENGTH_LONG);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity=findViewById(R.id.rel_layout);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE);
        else
            Snackbar.make(activity, "Вы авторизованы",Snackbar.LENGTH_LONG);
        displayAllMessages();
    }

    private void displayAllMessages() {
        Query query = FirebaseDatabase.getInstance().getReference().child("talkprog-9a0a5");
        FirebaseListOptions<Message> options = new FirebaseListOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLayout(R.layout.text_design)
                .build();
        ListView listmessages= findViewById(R.id.list_messages);
        adapter = new FirebaseListAdapter<Message>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Message model, int position) {
                TextView mess_text,mess_user,mess_time;
                mess_text = v.findViewById(R.id.message_text);
                mess_time = v.findViewById(R.id.message_time);
                mess_user = v.findViewById(R.id.message_user);

                mess_user.setText(model.getUserName());
                mess_text.setText(model.getTextMessage());
                mess_time.setText(DateFormat.format("dd-mm-yyyy HH:mm:ss", model.getMessageTime()));

            }
        };
        listmessages.setAdapter(adapter);
    }
}
