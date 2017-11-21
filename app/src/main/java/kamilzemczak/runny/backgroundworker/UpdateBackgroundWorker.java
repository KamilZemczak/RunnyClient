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
 * Created by Kamil Zemczak on 17.11.2017.
 */

public class UpdateBackgroundWorker extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertDialog;

    /**
     * TODO
     *
     * @param ctx TODO
     */
    public UpdateBackgroundWorker(Context ctx) {
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
        String update_user_url = "http://10.0.2.2:8080/user_update";
        if(type.equals("update_user")) {
            try {
                String id = params[1];
                String username = params[2];
                String email = params[3];
                String age = params[4];
                String weight = params[5];
                String height = params[6];
                URL url = new URL(update_user_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("id", "UTF-8")+"="+URLEncoder.encode(id, "UTF-8")+"&"
                        + URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(username, "UTF-8")+"&"
                        + URLEncoder.encode("email", "UTF-8")+"="+URLEncoder.encode(email, "UTF-8")+"&"
                        + URLEncoder.encode("age", "UTF-8")+"="+URLEncoder.encode(age, "UTF-8")+"&"
                        + URLEncoder.encode("weight", "UTF-8")+"="+URLEncoder.encode(weight, "UTF-8")+"&"
                        + URLEncoder.encode("height", "UTF-8")+"="+URLEncoder.encode(height, "UTF-8");
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
        //alertDialog.setMessage(result);
        //alertDialog.show();
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
