package ec.richardnarvaez.varianza;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.evrencoskun.tableview.TableView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<Integer> l;
    ArrayList<Double> puntMed;
    ArrayList<Integer> frecAbs;
    double media, varianza;

    int max = 32, min = 17;
    TextView nClases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = findViewById(R.id.rv);
        Button add = findViewById(R.id.add);
        nClases = findViewById(R.id.nClases);


        l = new ArrayList<>();
        puntMed = new ArrayList<>();
        frecAbs = new ArrayList<>();
        rv.setLayoutManager(new LinearLayoutManager(this));
        final MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this, l);
        rv.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.add((int) ((Math.random() * ((max - min) + 1)) + min));
                if (l.size() >= 2) {
                    Collections.sort(l);
                }

            }
        });

        findViewById(R.id.normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                normal();
            }
        });

        findViewById(R.id.simplificada).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simple();
            }
        });

    }

    public void normal() {

        long startTime = System.nanoTime();

        if (l.size() < 2) {
            Toast.makeText(this, "No hay datos suficientes", Toast.LENGTH_SHORT).show();
            return;
        }

        double first = l.get(0);
        int ultimo = 0;
        int u = 0;
        double hastaAqui, puntoMedio;


        int clases = (int) Math.round(Math.sqrt(l.size()));
        long rango = l.get(l.size() - 1) - l.get(0);

        double longClase = (double) rango / clases;
        varianza = 0;
        media = 0;

        if (l.size() >= 2) {

            puntMed.clear();
            frecAbs.clear();


            Log.e("TAG", ">>>>>>>>>>>>>>>>>>-------------------");
            Log.e("TAG", "Ultimo: " + l.get(l.size() - 1));
            Log.e("TAG", "Longitud de Clase: " + longClase);


            //Creando clases
            for (int i = 1; i <= clases; i++) {

                int cont = 0;
                hastaAqui = (first + longClase);

                if (ultimo != 0 && i != 1) u = ultimo + 1;
                else u = 0;

                Log.e("TAG", "Rango [" + first + " - " + hastaAqui + ")");

                puntoMedio = (first + hastaAqui) / 2;

                if (i == clases) hastaAqui += 1;

                try {
                    while (l.get(u) != null && (l.get(u) < hastaAqui)) {
                        Log.e("TAG", u + ": >>>" + l.get(u));
                        ultimo = u;
                        cont++;
                        u++;
                    }
                } catch (Exception e) {

                }

                frecAbs.add(i - 1, cont);
                puntMed.add(i - 1, puntoMedio);

                media += puntoMedio * cont;

                Log.e("TAG", "PM: " + puntoMedio + " Frecuencia: " + cont);
                first += longClase;
            }

            media = media / l.size();

            Log.e("TAG", "La media es: " + media);

            //Calcular la varianza
            for (int i = 1; i <= clases; i++) {
                varianza += Math.pow(puntMed.get(i - 1) - media, 2) * frecAbs.get(i - 1);
            }

            varianza = varianza / (l.size() - 1);

            Log.e("TAG", "La varianza es: " + varianza);

        }


        long endTime = System.nanoTime() - startTime;

        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }
            TextView tv = (TextView) findViewById(R.id.nLogs);
            tv.setText(log.toString());
        } catch (IOException e) {
            // Handle Exception
        }


        nClases.setText("Total Datos: " + l.size()
                + " Clases: " + clases
                + " Longitud de Clases: " + longClase
                + " \nRango: " + rango
                + " Media: " + media
                + " Varianza: " + varianza
                + " Tiempo: " + (endTime / 1000000) + "Ms"
                + "");

        Log.e("TAG", "El algoritmo tardo:: " + endTime + "Ns," + (endTime / 1000000) + "Ms");

    }


    public void simple() {

        long startTime = System.nanoTime();

        int clases = (int) Math.round(Math.sqrt(l.size()));
        long rango = l.get(l.size() - 1) - l.get(0);

        double longClase = (double) rango / clases;

        if (l.size() < 2) {
            Toast.makeText(this, "No hay datos suficientes", Toast.LENGTH_SHORT).show();
            return;
        }


        varianza = 0;
        media = 0;


        puntMed.clear();
        frecAbs.clear();


        Log.e("TAG", ">>>>>>>>>>>>>>>>>>-------------------");
        Log.e("TAG", "Ultimo: " + l.get(l.size() - 1));
        Log.e("TAG", "Longitud de Clase: " + longClase);


        double first = l.get(0);
        int ultimo = 0;
        int u = 0;
        double hastaAqui, puntoMedio;
        Log.e("TAG", "-------------------");
        //Creando clases
        for (int i = 1; i <= clases; i++) {

            int cont = 0;
            hastaAqui = (first + longClase);

            if (ultimo != 0) u = ultimo + 1;
            else u = 0;

            Log.e("TAG", "Rango [" + first + " - " + hastaAqui + ")");

            puntoMedio = (double) (first + hastaAqui) / 2;

            if (i == clases) hastaAqui += 1;

            try {
                while (l.get(u) != null && (l.get(u) < hastaAqui)) {
                    Log.e("TAG", u + ": >>>" + l.get(u));
                    ultimo = u;
                    cont++;
                    u++;
                }
            } catch (Exception e) {

            }

            frecAbs.add(i - 1, cont);
            puntMed.add(i - 1, puntoMedio);

            media += puntoMedio * cont;

            Log.e("TAG", "PM: " + puntoMedio + " Frecuencia: " + cont);
            first += longClase;
        }

        media = media / l.size();

        Log.e("TAG", "La media es: " + media);

        //Calcular la varianza
        for (int i = 1; i <= clases; i++) {
            varianza += (frecAbs.get(i - 1) * Math.pow(puntMed.get(i - 1), 2)) - (l.size() * Math.pow(media, 2));
        }

        varianza = varianza / (l.size() - 1);

        Log.e("TAG", "La varianza es: " + varianza);


        long endTime = System.nanoTime() - startTime;

        nClases.setText("Total Datos: " + l.size()
                + " Clases: " + clases
                + " Longitud de Clases: " + longClase
                + " \nRango: " + rango
                + " Media: " + media
                + " Varianza: " + varianza
                + " Tiempo: " + (endTime / 1000000) + "Ms"
                + "");

        Log.e("TAG", "El algoritmo tardo:: " + endTime + "Ns," + (endTime / 1000000) + "Ms");

    }

    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        private List<Integer> mData;
        private LayoutInflater mInflater;
//        private ItemClickListener mClickListener;

        // data is passed into the constructor
        MyRecyclerViewAdapter(Context context, List<Integer> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mData = data;
        }

        // inflates the row layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_data, parent, false);
            return new ViewHolder(view);
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int animal = mData.get(position);
            holder.myTextView.setText("" + animal);
        }

        // total number of rows
        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void add(int num) {
            mData.add(num);
            notifyDataSetChanged();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView myTextView;

            ViewHolder(View itemView) {
                super(itemView);
                myTextView = itemView.findViewById(R.id.text);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
//                if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            }
        }

    }


}
