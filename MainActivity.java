package com.example.registroproductos;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.registroproductos.Interfaces.ProductoApi;
import com.example.registroproductos.modelo.Producto;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa el botón 'Registrar Producto'
        // Nota: el ID del botón debe coincidir con el ID en el archivo XML.
        Button insertButton = findViewById(R.id.btnRegistrar); // Cambia 'insertButton' a 'btnRegistrar' para que coincida con tu archivo XML

        // Configura el oyente onClick para el botón
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un nuevo producto
                // Nota: Aquí estoy usando valores estáticos como ejemplo.
                Producto newProduct = new Producto(1, "Producto de Ejemplo", 100.0, 10);

                // Inserta el nuevo producto en la base de datos
                new Thread(new Runnable() {
                    public void run() {
                        ProductoApi.connect();
                        ProductoApi.insertProduct(newProduct);
                        ProductoApi.disconnect();
                    }
                }).start();
            }
        });
    }
}
