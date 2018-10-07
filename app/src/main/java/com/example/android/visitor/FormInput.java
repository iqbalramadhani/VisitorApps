package com.example.android.visitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.UUID;

public class FormInput extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageView mbtn_back;
    private EditText id,nama,instansi,keperluan;
    private Button submit;

    private FirebaseDatabase mFrirebaseDatabase;
    private DatabaseReference mDatabaseRefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_input);

        toolbar = (Toolbar)findViewById(R.id.tollbar);
        mbtn_back = (ImageView) findViewById(R.id.btn_back);
        id = (EditText)findViewById(R.id.id_visitor);
        nama = (EditText)findViewById(R.id.nama_lengkap);
        instansi = (EditText)findViewById(R.id.instansi);
        keperluan = (EditText)findViewById(R.id.keperluan);
        submit = (Button)findViewById(R.id.submit);

        mbtn_back.setOnClickListener(this);
        submit.setOnClickListener(this);

        //Firebase
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
            case R.id.btn_back:
                startActivity(new Intent(FormInput.this,MainActivity.class));
                finish();
                break;
            case R.id.submit:
                simpanVisitor();
        }
    }

    private void simpanVisitor() {
        try {
            Visitor visitor = new Visitor(id.getText().toString(),nama.getText().toString(),instansi.getText().toString(),keperluan.getText().toString(), ServerValue.TIMESTAMP,null);
            mDatabaseRefrence.child("visitor").child(visitor.getId_visitor()).setValue(visitor);
            Toast.makeText(FormInput.this,"Data berhasil ditambahkan",Toast.LENGTH_LONG).show();
            startActivity(new Intent(FormInput.this,MainActivity.class));
            finish();
        }catch (Exception e){
            Toast.makeText(FormInput.this,"Terjadi masalah : "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
