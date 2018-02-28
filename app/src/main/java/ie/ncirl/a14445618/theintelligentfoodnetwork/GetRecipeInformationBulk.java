package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Leon on 27/02/2018.
 */

public class GetRecipeInformationBulk extends AsyncTask<String, Void, String> {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
    @Override
    protected String doInBackground(String... params){
        String stringUrl = params[0];
        String result;
        String inputLine;
        try {
            //Create a URL object holding our url
            URL myUrl = new URL(stringUrl);
            //Create a connection
            HttpURLConnection connection =(HttpURLConnection)
                    myUrl.openConnection();
            //Set methods and timeouts
            connection.setRequestMethod(REQUEST_METHOD);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setRequestProperty("X-Mashape-Key", "Qm8iJ7jAq7mshee2CvoJ3if8A80tp1UdRd0jsn3XUwjFf2zycC");
            connection.setRequestProperty("Accept", "application/json");

            //Connect to our url
            connection.connect();
            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());//Reads our request input
            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);//Allow us to iterate through response

            StringBuilder stringBuilder = new StringBuilder();
            //Check if the line we are reading is not null
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();
            //Set our result equal to our stringBuilder
            result = stringBuilder.toString();
        }
        catch(IOException e){
            e.printStackTrace();
            result = null;
        }
        return result;
    }
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }
}