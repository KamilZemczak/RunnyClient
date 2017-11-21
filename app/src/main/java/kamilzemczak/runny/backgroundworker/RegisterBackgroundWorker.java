/**
 * ***********************************************************
 * Autorskie Prawa Majątkowe Kamil Zemczak
 *
 * Copyright 2017 Kamil Zemczak
 * ************************************************************
 * Utworzono 26-10-2017, Kamil Zemczak
 * ************************************************************
 */
package kamilzemczak.runny.backgroundworker;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Klasa odpowiedzialna za komunikację między serwerem a klientem (rejestracja)
 */
public class RegisterBackgroundWorker extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertDialog;

    /**
     * TODO
     *
     * @param ctx TODO
     */
    public RegisterBackgroundWorker(Context ctx) {
        context = ctx;
    }

    /**
     * Przesyła przekazane parametry związane z rejestracją na serwer
     *
     * @param params przekazane parametry
     * @return wynik
     */
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String register_url = "http://10.0.2.2:8080/registration";
        if(type.equals("register")) {
            try {
                String name = params[1];
                String surname = params[2];
                String username = params[3];
                String email = params [4];
                String age = params [5];
                String password = params[6];
                String passwordConfirm = params[7];
                String gender = params[8];
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("name", "UTF-8")+"="+URLEncoder.encode(name, "UTF-8")+"&"
                        + URLEncoder.encode("surname", "UTF-8")+"="+URLEncoder.encode(surname, "UTF-8")+"&"
                        + URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(username, "UTF-8")+"&"
                        + URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8")+"&"
                        + URLEncoder.encode("age", "UTF-8")+"="+URLEncoder.encode(age, "UTF-8")+"&"
                        + URLEncoder.encode("password", "UTF-8")+"="+URLEncoder.encode(password, "UTF-8")+"&"
                        + URLEncoder.encode("passwordConfirm", "UTF-8")+"="+URLEncoder.encode(passwordConfirm, "UTF-8")+"&"
                        + URLEncoder.encode("gender", "UTF-8")+"="+URLEncoder.encode(gender, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * TODO
     */
    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    /**
     * TODO
     *
     * @param result TODO
     */
    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();
    }

    /**
     * TODO
     *
     * @param values TODO
     */
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
