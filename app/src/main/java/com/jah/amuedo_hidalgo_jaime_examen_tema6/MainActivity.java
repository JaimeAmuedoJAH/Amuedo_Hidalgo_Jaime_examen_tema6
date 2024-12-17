package com.jah.amuedo_hidalgo_jaime_examen_tema6;

import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageView imgPlay, imgPause, imgStop, imgZambomba, imgPandereta, imgFondo;
    SeekBar sbBarra;
    ListView lvCanciones;
    ArrayAdapter<String> adapter;
    int[] sonidosCargados;
    int[] arrCanciones;
    int[] arrImagenes;
    String[] arrNombres;
    Runnable handlerTask;
    Handler handler = new Handler();
    SoundPool soundPool;
    MediaPlayer mediaPlayer;
    int cancionReproducida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        lvCanciones.setOnItemClickListener((adapterView, view, posicion, l) -> reproducirCancion(arrCanciones[posicion], arrImagenes[posicion]));
        sbBarra.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int posicion, boolean fromUser) {
                if(mediaPlayer != null && fromUser) mediaPlayer.seekTo(posicion);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        imgPlay.setOnClickListener(view -> play());
        imgPause.setOnClickListener(view -> pause());
        imgStop.setOnClickListener(view -> stop());

        imgZambomba.setOnClickListener(view -> soundPool.play(sonidosCargados[1], 1, 1, 0, 0, 1));
        imgPandereta.setOnClickListener(view -> soundPool.play(sonidosCargados[0], 1, 1, 0, 0, 1));
    }

    private void play() {
        if(mediaPlayer != null & !mediaPlayer.isPlaying()) mediaPlayer.start();
    }

    private void pause() {
        if(mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.pause();
    }

    private void stop() {
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), cancionReproducida);
            sbBarra.setProgress(0);
        }
    }

    private void reproducirCancion(int cancion, int imagen) {
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), cancion);
            mediaPlayer.start();
        }else if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), cancion);
            mediaPlayer.start();
        }else if(mediaPlayer != null && !mediaPlayer.isPlaying()){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), cancion);
            mediaPlayer.start();
        }
        imgFondo.setImageResource(imagen);
        sbBarra.setMax(mediaPlayer.getDuration());
        cancionReproducida = cancion;
        startTimer();
    }

    private void startTimer() {
        handlerTask = () -> {
            if(mediaPlayer != null && mediaPlayer.isPlaying()){
                int posicion = mediaPlayer.getCurrentPosition();
                sbBarra.setProgress(posicion);
            }
            handler.postDelayed(handlerTask, 1000);
        };
        handler.post(handlerTask);
    }

    private void initComponents() {
        imgPlay = findViewById(R.id.imgPlay);
        imgPlay.setImageResource(R.drawable.baseline_play_arrow_24);
        imgPause = findViewById(R.id.imgPause);
        imgPause.setImageResource(R.drawable.baseline_pause_24);
        imgStop = findViewById(R.id.imgStop);
        imgStop.setImageResource(R.drawable.baseline_stop_24);
        imgZambomba = findViewById(R.id.imgZambomba);
        imgZambomba.setImageResource(R.drawable.zambomba);
        imgPandereta = findViewById(R.id.imgPandereta);
        imgPandereta.setImageResource(R.drawable.pandereta);
        sbBarra = findViewById(R.id.sbBarra);
        lvCanciones = findViewById(R.id.lvCanciones);
        imgFondo = findViewById(R.id.imgFondo);
        soundPool = new SoundPool.Builder().setMaxStreams(2).build();
        arrNombres = new String[]{
                "Burrito sabanero", "Campana sobre campana", "Ay mi chiquirritin", "La marimonera", "Peces en el rio"
        };
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, arrNombres);
        lvCanciones.setAdapter(adapter);
        sonidosCargados = new int[]{
                soundPool.load(getApplicationContext(), R.raw.pandereta, 1),
                soundPool.load(getApplicationContext(), R.raw.zambomba, 1)
        };
        arrCanciones = new int[]{
                R.raw.burrito, R.raw.campana, R.raw.chiquirritin, R.raw.marimonera, R.raw.peces
        };
        arrImagenes = new int[]{
                R.drawable.burrito, R.drawable.campana, R.drawable.chiquirritin, R.drawable.marimonera, R.drawable.peces
        };
    }
}