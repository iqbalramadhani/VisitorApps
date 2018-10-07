package com.example.android.visitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mnoId;
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
        mDatabaseRefrence = mFrirebaseDatabase.getReference();
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
            mDatabaseRefrence.child("visitor").child(mnoId.getText().toString()).child("waktu_keluar").setValue(ServerValue.TIMESTAMP);
            Toast.makeText(MainActivity.this,"Terima Kasih",Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }catch (Exception e){
            Toast.makeText(MainActivity.this,"Terjadi masalah : "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }

    private void dialogOut() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mview = getLayoutInflater().inflate(R.layout.visitor_out,null);
        mnoId = (EditText)mview.findViewById(R.id.id_visitor);
        submit = (Button)mview.findViewById(R.id.submit);
        submit.setOnClickListener(this);
        mBuilder.setView(mview);
        dialog = mBuilder.create();
        dialog.show();
    }
}
