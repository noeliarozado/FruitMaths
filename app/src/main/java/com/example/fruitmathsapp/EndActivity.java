package com.example.fruitmathsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {
    private TextView tvFelicidades;

    String nombreJugador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        tvFelicidades = findViewById(R.id.tvFelicidades);

        nombreJugador = getIntent().getStringExtra("jugador");
        // tvFelicidades.setText("¡Excelente " + nombreJugador + "!");
        tvFelicidades.setText(nombreJugador);
    }

    /*public void salir(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Seguro que quieres salir?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }*/
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