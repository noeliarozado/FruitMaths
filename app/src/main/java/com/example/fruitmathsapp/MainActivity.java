package com.example.fruitmathsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText etNombre;
    private ImageView ivPersonaje;
    private TextView tvMejorPuntuacion;
    private MediaPlayer mp;

    int aleatorio = (int)(Math.random() * 10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNombre = findViewById(R.id.etNombre);
        ivPersonaje = findViewById(R.id.ivPersonaje);
        tvMejorPuntuacion = findViewById(R.id.tvMejorPuntuacion);

        int id;

        if(aleatorio == 0|| aleatorio == 10){
            id = getResources().getIdentifier("manzana", "drawable", getPackageName());
            ivPersonaje.setImageResource(id);
        } else if(aleatorio == 1|| aleatorio == 9) {
            id = getResources().getIdentifier("cerezas", "drawable", getPackageName());
            ivPersonaje.setImageResource(id);
        } else if(aleatorio == 2|| aleatorio == 8){
            id = getResources().getIdentifier("frambuesa", "drawable", getPackageName());
            ivPersonaje.setImageResource(id);
        } else if(aleatorio == 3|| aleatorio == 7){
            id = getResources().getIdentifier("mango", "drawable", getPackageName());
            ivPersonaje.setImageResource(id);
        } else if(aleatorio == 4|| aleatorio == 6){
            id = getResources().getIdentifier("platano", "drawable", getPackageName());
            ivPersonaje.setImageResource(id);
        } else if(aleatorio == 5) {
            id = getResources().getIdentifier("sandia", "drawable", getPackageName());
            ivPersonaje.setImageResource(id);
        }

        BBDDHelper helper = new BBDDHelper(this, "db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor consulta = db.rawQuery(
                "SELECT * FROM Puntuacion WHERE Puntos = (SELECT MAX(Puntos) FROM Puntuacion)", null);
        if(consulta.moveToFirst()){
            String nombreTemporal = consulta.getString(0);
            String puntosTemporal = consulta.getString(1);
            tvMejorPuntuacion.setText("Record: " +  puntosTemporal + " " + nombreTemporal);
            db.close();
        } else {
            db.close();
        }

    }

    public void jugar(View view){
        String nombre = etNombre.getText().toString();

        if(!nombre.equals("")){
            Intent intent = new Intent(this, Nivel1Activity.class);

            intent.putExtra("jugador", nombre);
            startActivity(intent);
            finish();
        } else {
            //Toast.makeText(this, "Por favor, escribe tu nombre.", Toast.LENGTH_LONG).show();
            Toast.makeText(this, getString(R.string.toast_message_name), Toast.LENGTH_LONG).show();

            // Abrir el teclado para escribir en etNombre
            etNombre.requestFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etNombre, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void salir(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_exit_message))
                .setPositiveButton(getString(R.string.button_yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.button_no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}