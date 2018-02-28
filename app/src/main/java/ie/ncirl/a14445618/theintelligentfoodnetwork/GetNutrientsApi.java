package ie.ncirl.a14445618.theintelligentfoodnetwork;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.rapidapi.rapidconnect.Argument;
import com.rapidapi.rapidconnect.RapidApiConnect;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leon on 05/02/2018.
 */
//AsyncTask Code From: https://stackoverflow.com/questions/9671546/asynctask-android-example
//What each element of AsyncTask Does From: https://stackoverflow.com/questions/6053602/what-arguments-are-passed-into-asynctaskarg1-arg2-arg3
public class GetNutrientsApi extends AsyncTask<String, Void, Map<String, Object>> {
    Map<String, Object> response;
    public static final String TAG = "";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Map<String, Object> doInBackground(String...search) {
        String searchString = search[0];
                try  {
                    try {
                        RapidApiConnect connect = new RapidApiConnect("leonrapidapi_5a6ddd31e4b04737db92df77", "dd0794ae-5f4e-408d-a19e-cae245e00273");
                        Map<String, Argument> body = new HashMap<>();

                        body.put("applicationSecret", new Argument("data", "ca779495f21bc56c5feb950103517992"));
                        body.put("applicationId", new Argument("data", "8e2110d7"));
                        body.put("foodDescription", new Argument("data", searchString));

                        response = connect.call("Nutritionix", "getFoodsNutrients", body);
                        if(response.get("success") != null) { //If Success is not null (Not
                            System.out.println("Successful API Call--------------------");
                            System.out.println(response);
                            //setData();// This sub thread cannot alter the main threads views - to combat this, it simply calls another method called setData()
                            //responseToString = response.toString();


                        } else{ //If success is anything but null
                            System.out.println("Else Statement--------------------");

                        }
                    } catch(Exception e){ //Catch Clause
                        System.out.println("Catch Clause--------------------");
                        Log.d(TAG, "searchUsingAPI: " + e);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        //setData();
        return response;
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Map<String, Object> stringObjectMap) {
        //super.onPostExecute(stringObjectMap);



    }
}
