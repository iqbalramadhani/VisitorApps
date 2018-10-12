package com.example.android.visitor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class FormInput extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageView mbtn_back;
    private EditText id,nama,instansi,keperluan;
    private Button submit,hapus_ttd;
    private Uri filepatch;

    private FirebaseDatabase mFrirebaseDatabase;
    private DatabaseReference mDatabaseRefrence;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private File file;
    private Dialog dialog;
    private LinearLayout mContent;
    private View view;
    private signature mSignature;
    private Bitmap bitmap;



    // Creating Separate Directory for saving Generated Images
    private String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DigitSign/";
    private String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    private String StoredPath = DIRECTORY + pic_name + ".jpg";

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
        hapus_ttd = (Button)findViewById(R.id.hapus_ttd);


        mbtn_back.setOnClickListener(this);
        submit.setOnClickListener(this);
        hapus_ttd.setOnClickListener(this);

        // Method to create Directory, if the Directory doesn't exists
        file = new File(DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }

        mContent = (LinearLayout)findViewById(R.id.linearLayout);
        mSignature = new signature(getApplicationContext(),null);
        mSignature.setBackgroundColor(Color.WHITE);
        // Dynamically generating Layout through java code
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        view = mContent;

        //Firebase
        initFirebase();

    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        mFrirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRefrence = mFrirebaseDatabase.getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

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
                break;
            case R.id.hapus_ttd:
                hapus_ttd();
                break;
        }
    }

    private void hapus_ttd() {
        Log.v("Log_tag","Panel Cleared");
        mSignature.clear();
    }

    private void simpanVisitor() {
        try {
            view.setDrawingCacheEnabled(true);
            mSignature.save(view,StoredPath);
            filepatch = Uri.fromFile(new File(StoredPath));
            StorageReference ref = storageReference.child("images/"+UUID.randomUUID().toString());
            ref.putFile(filepatch)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Visitor visitor = new Visitor(id.getText().toString(),nama.getText().toString(),instansi.getText().toString(),keperluan.getText().toString(),taskSnapshot.getDownloadUrl().toString(),ServerValue.TIMESTAMP);
                            mDatabaseRefrence.child("visitor").push().setValue(visitor);
                            Toast.makeText(FormInput.this,"Data berhasil ditambahkan",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(FormInput.this,MainActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FormInput.this,"Failed : "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
//            Visitor visitor = new Visitor(id.getText().toString(),nama.getText().toString(),instansi.getText().toString(),keperluan.getText().toString(), ServerValue.TIMESTAMP,null);
//            mDatabaseRefrence.child("visitor").push().setValue(visitor);

//            mDatabaseRefrence.child("visitor").push().setValue(visitor);
            //filepatch = Uri.fromFile(new File(StoredPath));
            //uploadSignature();
//            Toast.makeText(FormInput.this,"Data berhasil ditambahkan",Toast.LENGTH_LONG).show();
//            startActivity(new Intent(FormInput.this,MainActivity.class));
//            finish();
        }catch (Exception e){
            Toast.makeText(FormInput.this,"Terjadi masalah : "+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void uploadSignature() {

    }

    public class signature extends View {

        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v, String StoredPath) {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap);
            try {
                // Output the file
                FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);

                // Convert the output file to Image such as .png
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();

            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }

        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            //mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {

            Log.v("log_tag", string);

        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }
}
