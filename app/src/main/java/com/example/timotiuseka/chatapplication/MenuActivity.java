package com.example.timotiuseka.chatapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class MenuActivity extends AppCompatActivity {
    private static final int SIGN_IN_REQUEST_CODE = 10;
    private FirebaseListAdapter<SignedInUser> loginAdapter;
    private MainMenuAdapter customAdapter;
    private int userCount = 0;
    private SignedInUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);



        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    SIGN_IN_REQUEST_CODE
            );
        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            user = new SignedInUser(
                    FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getEmail());

            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();

            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            final Query applesQuery = ref.orderByChild("userEmail").equalTo(FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .getEmail());

            applesQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                        Log.d("userEmail", String.valueOf(appleSnapshot.child("userLastLogInTime").getValue()));
                        if (user.getUserLastLogInTime() != (long) appleSnapshot.child("userLastLogInTime").getValue()) {
                            appleSnapshot.getRef().removeValue();
                        }

                    }
//                    ref.removeEventListener(this);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

            FirebaseDatabase.getInstance()
                    .getReference()
                    .push()
                    .setValue(user)
            ;
            setUserList();


            /**
             * Todo : bikin method buat masukin data login ke db
             */

            // Load chat room contents

        }
    }

    private void setUserList() {
        final ListView listOfMessages = (ListView) findViewById(R.id.list_of_users_and_messages);
        final ArrayList<SignedInUser> arrayOfUser = new ArrayList<>();
        final ArrayList<SignedInUser> dummyArray = new ArrayList<>();
        loginAdapter = new FirebaseListAdapter<SignedInUser>(this, SignedInUser.class,
                R.layout.last_login, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, SignedInUser model, int position) {
                // Get references to the views of message.xml
                if (userCount < loginAdapter.getCount()) {
                    userCount++;
                }
                Log.d("userCount", String.valueOf(userCount));

                TextView userEmail = (TextView) v.findViewById(R.id.user_email);
                TextView userLastLogin = (TextView) v.findViewById(R.id.user_last_login);

                // Set their text
                dummyArray.add(model);
//                if(model.getUserEmail() != null) {
                userEmail.setText(model.getUserEmail());

                // Format the date before showing it
                userLastLogin.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getUserLastLogInTime()));
                if (userCount == loginAdapter.getCount()) {
                    for (int a = 0; a < loginAdapter.getCount(); a++) {
                        if (loginAdapter.getItem(a).getUserEmail() != null) {
                            arrayOfUser.add(loginAdapter.getItem(a));
                            Log.d("userEmail", loginAdapter.getItem(a).getUserEmail());
                        }
                       
                    }
                }

//                    arrayOfUser.add(model);
//
//
//                if(userCount == loginAdapter.getCount()){
//                    setUserDisplay(arrayOfUser, listOfMessages);
//                }


                Log.d("adapterSize", String.valueOf(loginAdapter.getCount()));
            }
        };
        listOfMessages.setAdapter(loginAdapter);

    }

    private void setUserDisplay(ArrayList<SignedInUser> arrayOfUser, ListView listUser) {
        customAdapter = new MainMenuAdapter(this, arrayOfUser);
        listUser.setAdapter(customAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MenuActivity.this,
                                    "You have been signed out.",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // Close activity
                            finish();
                        }
                    });
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                user = new SignedInUser(
                        FirebaseAuth.getInstance()
                                .getCurrentUser()
                                .getEmail());
                Toast.makeText(this,
                        "Successfully signed in. Welcome!",
                        Toast.LENGTH_LONG)
                        .show();

                /**
                 * Todo : bikin method buat masukin data ke login ke db
                 */

                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                final Query applesQuery = ref.orderByChild("userEmail").equalTo(FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getEmail());

                applesQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            Log.d("userEmail", String.valueOf(appleSnapshot.child("userLastLogInTime").getValue()));
                            if (user.getUserLastLogInTime() != (long) appleSnapshot.child("userLastLogInTime").getValue()) {
                                appleSnapshot.getRef().removeValue();
                            }

                        }
//                    ref.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(user)
                ;

                setUserList();
            } else {
                Toast.makeText(this,
                        "We couldn't sign you in. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show();

                // Close the app
                finish();
            }
        }

    }
}
