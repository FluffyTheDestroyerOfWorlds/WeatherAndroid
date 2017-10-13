package com.example.dboesen.weatherandroid;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;

public class MainActivity extends AppCompatActivity {

    String JSONtext = "";
    String cWindBear = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnRefresh = (Button) findViewById(R.id.btnRefresh);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONtask().execute("https://api.darksky.net/forecast/bc8b076254e97ff7a44dde5303b54840/37.8267,-122.4233");








            }

        });


    }

    public class JSONtask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();


                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                JSONtext = buffer.toString();



                return JSONtext;

                //  tvData.setText(buffer.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return JSONtext;
        }

        @Override
        protected void onPostExecute(String s) {
            String holdParse = "";
            String holdParseJSON = "";
            JSONObject pObj = null;
            Integer x = 0;




            super.onPostExecute(s);
            JSONtext = s;

            TextView cCity = (TextView) findViewById(R.id.txtCity);
            try {
                pObj = new JSONObject(JSONtext);
                holdParse = pObj.getString("timezone");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            x = holdParse.indexOf("/");
            holdParse = holdParse.substring(x + 1);

            cCity.setText(holdParse);

            TextView cTemp = (TextView) findViewById(R.id.currentTemp);
            holdParse = JsonValue(JSONtext,"currently","temperature");
            cTemp.setText(holdParse + "° F");

            TextView cDew = (TextView) findViewById(R.id.idDewPoint);
            holdParse = JsonValue(JSONtext,"currently","dewPoint");
            cDew.setText("Dewpoint: " + holdParse);

            TextView cBaram = (TextView) findViewById(R.id.idBaram);
            holdParse = JsonValue(JSONtext,"currently","pressure");
            cBaram.setText("Barameter: " + holdParse + " mb");

            TextView cWindSpeed = (TextView) findViewById(R.id.idWindSpeed);
            holdParse = JsonValue(JSONtext,"currently","windSpeed");
            cWindSpeed.setText(holdParse + " mph");

            TextView cWindBear = (TextView) findViewById(R.id.idWindDirection);
            holdParse = JsonValue(JSONtext,"currently","windBearing");


            int iHoldParse = Integer.parseInt(holdParse);

            if (iHoldParse > 0 && iHoldParse < 80){
                cWindBear.setText("North");
            } else if (iHoldParse >= 80 && iHoldParse <=179) {
                cWindBear.setText("East");
            } else if (iHoldParse >=180 && iHoldParse <=269) {
                cWindBear.setText("South");
            } else {
                cWindBear.setText("West");
            }


            ImageView imgMrSun = (ImageView) findViewById(R.id.imgSun);
            holdParse = JsonValue(JSONtext,"currently","icon");

            Log.d(holdParse, "onPostExecute: ");

            imgMrSun.setImageResource(FindTheSun(holdParse));


            holdParseJSON = JSONobj(JSONtext,"daily");
            TextView extOutHighT1 = (TextView) findViewById(R.id.extOutHighT1);
            TextView extOutHighT2 = (TextView) findViewById(R.id.extOutHighT2);
            TextView extOutHighT3 = (TextView) findViewById(R.id.extOutHighT3);
            TextView extOutLowT1 = (TextView) findViewById(R.id.extOutLowT1);
            TextView extOutLowT2 = (TextView) findViewById(R.id.extOutLowT2);
            TextView extOutLowT3 = (TextView) findViewById(R.id.extOutLowT3);

            ImageView ext1 = (ImageView) findViewById(R.id.imgSun1);
            ImageView ext2 = (ImageView) findViewById(R.id.imgSun2);
            ImageView ext3 = (ImageView) findViewById(R.id.imgSun3);
            ImageView extOutLow1 = (ImageView) findViewById(R.id.extOutLow1);
            ImageView extOutLow2 = (ImageView) findViewById(R.id.extOutLow2);
            ImageView extOutLow3 = (ImageView) findViewById(R.id.extOutLow3);

            try {
                pObj = new JSONObject(holdParseJSON);


                JSONArray array = (JSONArray) pObj.get("data");
                String holdIcon = "";


                extOutHighT1.setText(array.getJSONObject(0).getString("temperatureHigh").substring(0,3));
                holdIcon = array.getJSONObject(0).getString("icon");
                ext1.setImageResource(FindTheSun(holdIcon));
           //     extOutHigh1.getLayoutParams().height = (int) Double.parseDouble(extOutHighT1.toString());
                extOutHighT2.setText(array.getJSONObject(1).getString("temperatureHigh").substring(0,3));
                holdIcon = array.getJSONObject(1).getString("icon");
                ext2.setImageResource(FindTheSun(holdIcon));
            //    extOutHigh2.getLayoutParams().height = (int) Double.parseDouble(extOutHighT2.toString());
                extOutHighT3.setText(array.getJSONObject(2).getString("temperatureHigh").substring(0,3));
                holdIcon = array.getJSONObject(2).getString("icon");
                ext3.setImageResource(FindTheSun(holdIcon));
            //    extOutHigh3.getLayoutParams().height = (int) Double.parseDouble(extOutHighT3.toString());


                extOutLowT1.setText(array.getJSONObject(1).getString("temperatureLow").substring(0,3));
                extOutLowT2.setText(array.getJSONObject(2).getString("temperatureLow").substring(0,3));
                extOutLowT3.setText(array.getJSONObject(3).getString("temperatureLow").substring(0,3));


            } catch (JSONException e) {
                e.printStackTrace();
            }
            //   JSONObject innerObj = (JSONObject) array.get(0);
          //  holdParseJSON = JSONobj(holdParseJSON,"data");
         //   holdParseJSON = JSONobj(holdParseJSON,"0");

        }


    }


    String JsonValue(String JsonTxt, String JsonParent, String JsonKey){
        String returnValue = "";
        JSONObject finalObject = null;
        JSONArray pArray;

//        try {
//            pArray = new JSONArray(JsonTxt);
//            returnValue = pArray.getJSONObject(0).getString(JsonKey);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        try {

            JSONObject parentObject = new JSONObject(JsonTxt);
            JSONArray parentArray = null;

            finalObject = parentObject.getJSONObject(JsonParent);

          //  parentArray = finalObject.getJSONArray(JsonParent);
            //finalObject = parentArray.getJSONObject(0);
            returnValue = finalObject.getString(JsonKey);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    //    try {
       //     returnValue = finalObject.getString(JsonKey);
     //   } catch (JSONException e) {
     //      e.printStackTrace();
     //   }


        return returnValue;
    }

    String JSONobj (String JsonTxt, String JsonParent){
        String returnValue = "";
        JSONObject finalObject = null;
        JSONArray pArray;

//        try {
//            pArray = new JSONArray(JsonTxt);
//            returnValue = pArray.getJSONObject(0).getString(JsonKey);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        try {





            JSONObject parentObject = new JSONObject(JsonTxt);
          //  JSONArray parentArray = null;

            finalObject = parentObject.getJSONObject(JsonParent);

            //  parentArray = finalObject.getJSONArray(JsonParent);
            //finalObject = parentArray.getJSONObject(0);
            returnValue = finalObject.toString();


        } catch (JSONException e) {
            e.printStackTrace();
        }


        //    try {
        //     returnValue = finalObject.getString(JsonKey);
        //   } catch (JSONException e) {
        //      e.printStackTrace();
        //   }


        return returnValue;
    }

    Integer FindTheSun(String whereIsSun){
        Integer x;

        switch(whereIsSun) {
            case "clear-day":
            case "clear-night":
                x = R.drawable.sunny;
                break;
            case "rain":
            case "sleet":
                x = R.drawable.rainy;
                break;
            case "wind":
            case "fog":
            case "partly-cloudy":
            case "partly-cloudy-night":
                x = R.drawable.cloudy;
                break;
            case "snow":
                x = R.drawable.snowy;
                break;
            default:
                x = R.drawable.sunny;
                break;
        }


        return x;
    }
}
