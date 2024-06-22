package com.example.fruitmathsapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Nivel1Activity extends AppCompatActivity {
    private TextView tvNombre, tvPuntuacion;
    private ImageView ivVidas, ivNumero1, ivNumero2;
    private EditText etRespuesta;
    private MediaPlayer mpCorrecto, mpIncorrecto;

    int puntuacion, numeroAleatorio1, numeroAleatorio2, resultado, vidas = 3;
    String nombreJugador, puntuacionSt, vidasSt;
    String numero[] = {"cero", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nivel1);

        tvNombre = findViewById(R.id.tvNombre);
        tvPuntuacion = findViewById(R.id.tvPuntuacion);
        ivVidas = findViewById(R.id.ivVidas);
        ivNumero1 = findViewById(R.id.ivNumero1);
        ivNumero2 = findViewById(R.id.ivNumero2);
        etRespuesta = findViewById(R.id.etRespuesta);

        nombreJugador = getIntent().getStringExtra("jugador");
        //tvNombre.setText("Jugador: " + nombreJugador);
        tvNombre.setText(nombreJugador);

        mpCorrecto = MediaPlayer.create(this, R.raw.correct);
        mpIncorrecto = MediaPlayer.create(this, R.raw.incorrect);

        numeroAleatorio();
    }

    public void comprobar(View view) {
        String respuesta = etRespuesta.getText().toString();

        if (!respuesta.equals("")) {
            int respuestaJugador = Integer.parseInt(respuesta);

            if (resultado == respuestaJugador) {
                mpCorrecto.start();
                puntuacion++;
                //tvPuntuacion.setText("Puntuación: " + puntuacion);
                tvPuntuacion.setText(puntuacion + " pts");
                etRespuesta.setText("");

                baseDatos();
            } else {
                mpIncorrecto.start();
                vidas--;

                baseDatos();

                switch (vidas) {
                    case 3:
                        ivVidas.setImageResource(R.drawable.tresvidas);
                        break;
                    case 2:
                        Toast.makeText(this, "Te quedan 2 vidas.", Toast.LENGTH_SHORT).show();
                        ivVidas.setImageResource(R.drawable.dosvidas);
                        break;
                    case 1:
                        Toast.makeText(this, "Te queda 1 vida.", Toast.LENGTH_SHORT).show();
                        ivVidas.setImageResource(R.drawable.unavida);
                        break;
                    case 0:
                        Toast.makeText(this, "Te quedan 0 vidas.", Toast.LENGTH_SHORT).show();
                        ivVidas.setImageResource(0);
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                etRespuesta.setText("");
            }
            numeroAleatorio();
        } else {
            //Toast.makeText(this, "Escribe una respuesta.", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, getString(R.string.toast_message_answer), Toast.LENGTH_LONG).show();
        }
    }

    public void numeroAleatorio() {
        if (puntuacion < 10) {
            numeroAleatorio1 = (int) (Math.random() * 10);
            numeroAleatorio2 = (int) (Math.random() * 10);
            resultado = numeroAleatorio1 + numeroAleatorio2;

            if (resultado <= 10) {
                for (int i = 0; i < numero.length; i++) {
                    int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());
                    if (numeroAleatorio1 == i) {
                        ivNumero1.setImageResource(id);
                    }
                    if (numeroAleatorio2 == i) {
                        ivNumero2.setImageResource(id);
                    }
                }
            } else {
                numeroAleatorio();
            }
        } else {
            Intent intent = new Intent(this, Nivel2Activity.class);

            puntuacionSt = String.valueOf(puntuacion);
            vidasSt = String.valueOf(vidas);
            intent.putExtra("jugador", nombreJugador);
            intent.putExtra("puntuacion", puntuacionSt);
            intent.putExtra("vidas", vidasSt);

            startActivity(intent);
            finish();
        }
    }

    public void baseDatos() {
        BBDDHelper helper = new BBDDHelper(this, "db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        Cursor consulta = db.rawQuery(
                "SELECT * FROM Puntuacion WHERE Puntos = (SELECT MAX(Puntos) FROM Puntuacion)", null);
        if (consulta.moveToFirst()) {
            String nombreTemporal = consulta.getString(0);
            String puntosTemporal = consulta.getString(1);

            int mejorPuntuacion = Integer.parseInt(puntosTemporal);

            if (puntuacion > mejorPuntuacion) {
                ContentValues modificacion = new ContentValues();
                modificacion.put("Nombre", nombreJugador);
                modificacion.put("Puntos", puntuacion);

                db.update("Puntuacion", modificacion, "Puntos = " + mejorPuntuacion, null);
            }
            db.close();
        } else {
            ContentValues insercion = new ContentValues();
            insercion.put("Nombre", nombreJugador);
            insercion.put("Puntos", puntuacion);

            db.insert("Puntuacion", null, insercion);
            db.close();
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