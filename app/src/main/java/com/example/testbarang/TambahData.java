package com.example.testbarang;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;


public class TambahData extends AppCompatActivity {
    private DatabaseReference database;
    private Button btSubmit;
    private EditText etNama;
    private EditText etKode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data);

        etKode = (EditText) findViewById(R.id.editNo);
        etNama = (EditText) findViewById(R.id.editNama);
        btSubmit = (Button) findViewById(R.id.btnOk);

        database = FirebaseDatabase.getInstance().getReference();

        final Barang barang = (Barang) getIntent().getSerializableExtra("data");

        if (barang != null) {
            etKode.setText((barang.getKode()));
            etNama.setText(barang.getNama());
            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    barang.setKode(etKode.getText().toString());
                    barang.setNama(etNama.getText().toString());

                    updateBarang(barang);
                }
            });
        } else {
            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!(etKode.getText().toString().isEmpty()) && !(etNama.getText().toString().isEmpty()))

                        submitBrg(new Barang(etKode.getText().toString(), etNama.getText().toString()));

                    else
                        Toast.makeText(getApplicationContext(), "Data tidak boleh kosong", Toast.LENGTH_LONG).show();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etKode.getWindowToken(), 0);
                }
            });
        }
    }

    public boolean isEmpty (String s){
        return TextUtils.isEmpty(s);
    }

    public void updateBarang(Barang barang) {
        database.child("Barang").child(barang.getKode()).setValue(barang).
                addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Snackbar.make(findViewById(R.id.btnOk),"Data Berhasil diupdate", Snackbar.LENGTH_LONG)
                        .setAction("Oke", new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        finish();
                    }
                }).show();
            }
        });
    }
    public void submitBrg(Barang brg) {
        database.child("Barang").push().setValue(brg).addOnSuccessListener(this,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        etKode.setText("");
                        etNama.setText("");
                        Toast.makeText(getApplicationContext(),"Data berhasil ditambahkan",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
    public static Intent getActIntent(Activity activity){
        return new Intent(activity, TambahData.class);
    }
}
