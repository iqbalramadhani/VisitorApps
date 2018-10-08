package com.example.android.visitor;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText id_visitor;
    private Button btn_in,btn_out,submit;

    private FirebaseDatabase mFrirebaseDatabase;
    private DatabaseReference mDatabaseRefrence;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_in = (Button)findViewById(R.id.visitor_in);
        btn_out = (Button)findViewById(R.id.visiotor_out);

        btn_in.setOnClickListener(this);
        btn_out.setOnClickListener(this);

        initFirebase();


    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        mFrirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRefrence = mFrirebaseDatabase.getReference("visitor");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.visitor_in :
                startActivity(new Intent(this,FormInput.class));
                break;
            case R.id.visiotor_out :
                dialogOut();
                break;
            case R.id.submit :
                visitorOut(dialog);
                break;
        }
    }

    private void visitorOut(AlertDialog dialog) {
        try {
//            if(mDatabaseRefrence.child("key").child("id_visitor").equalTo(id_visitor.getText().toString()))
//            {
//
//            }
//            Toast.makeText(MainActivity.this,"Terima Kasih",Toast.LENGTH_LONG).show();

            mDatabaseRefrence.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot visitor:dataSnapshot.getChildren()){
                        if(visitor.child(id_visitor.getText().toString()).exists()){
                            Toast.makeText(MainActivity.this,"Data ditemukan",Toast.LENGTH_LONG).show();
                        }
                    }
//                    if(dataSnapshot.child("key").child(id_visitor.getText().toString()).exists()) {
//                        Toast.makeText(MainActivity.this,"Data ditemukan",Toast.LENGTH_LONG).show();
//                    }else{
//                        Toast.makeText(MainActivity.this,"Data tidak ditemukan",Toast.LENGTH_LONG).show();
//
//                    }
//                    if(dataSnapshot.child("key").child(id_visitor.getText().toString()).exists()){
//                        Toast.makeText(MainActivity.this,"Data ditemukan",Toast.LENGTH_LONG).show();
//                    }else{
//                        Toast.makeText(MainActivity.this,"Data tidak ditemukan",Toast.LENGTH_LONG).show();
//                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            dialog.dismiss();
        }catch (Exception e){
            Toast.makeText(MainActivity.this,"Terjadi masalah : "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    private void dialogOut() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mview = getLayoutInflater().inflate(R.layout.visitor_out,null);
        id_visitor = (EditText)mview.findViewById(R.id.id_visitor);
        submit = (Button)mview.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        mBuilder.setView(mview);
        dialog = mBuilder.create();
        dialog.show();
    }
}
