package com.example.registroproductos.Interfaces;

import android.os.AsyncTask;
import android.util.Log;
import com.example.registroproductos.modelo.Producto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductoApi {

    private static final String URL = "jdbc:mysql://10.0.2.2:3306/registro_productos";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection;

    public static void connect() {
        new ConnectDatabaseTask().execute();
    }

    public static void disconnect() {
        new DisconnectDatabaseTask().execute();
    }

    private static class ConnectDatabaseTask extends AsyncTask<Void, Void, Connection> {
        @Override
        protected Connection doInBackground(Void... voids) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                Log.e("DB_ERROR", "Error while connecting to the database", e);
            }
            return connection;
        }
    }

    private static class DisconnectDatabaseTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                Log.e("DB_ERROR", "Error while disconnecting from the database", e);
            }
            return null;
        }
    }

    public static void insertProduct(Producto producto) {
        new InsertProductTask(producto).execute();
    }

    private static class InsertProductTask extends AsyncTask<Void, Void, Integer> {
        private final Producto producto;

        InsertProductTask(Producto producto) {
            this.producto = producto;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            if (connection == null) {
                Log.e("DB_ERROR", "No database connection");
                return 0;
            }

            String query = "INSERT INTO productos (codigo, nombre, precio, cantidad) VALUES (?, ?, ?, ?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, producto.getCodigo());
                preparedStatement.setString(2, producto.getNombre());
                preparedStatement.setDouble(3, producto.getPrecio());
                preparedStatement.setInt(4, producto.getCantidad());

                return preparedStatement.executeUpdate();
            } catch (SQLException e) {
                Log.e("DB_ERROR", "Error while inserting a product", e);
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer rowsInserted) {
            if (rowsInserted > 0) {
                Log.d("DB_INFO", "A new product was inserted successfully!");
            }
        }
    }
}
