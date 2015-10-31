package com.example.raghav.grabahouse;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.callback.Callback;

public class homeDetailsFragment extends Fragment {
    private String url = ServerUrl.getSingleHomeDetails_url;
    ImageView home_image;
    TextView hometitle;
    TextView rentText;
    TextView homedescription;
    WebView home_location_map;
    //MapView mapView;
    //GoogleMap map;
    ArrayList<HashMap<String, String>> homeList;
    private static final String HOMES = "HOMES";
    private static final String HOME_ID = "HOME_ID";
    private static final String HOME_TITLE = "HOME_TITLE";
    private static final String HOME_LOCATION = "HOME_LOCATION";
    private static final String HOME_RENT = "HOME_RENT";
    private static final String HOME_LOCATION_URL = "HOME_LOCATION_URL";
    private static final String HOME_IMAGE_URL = "HOME_IMAGE_URL";

    JSONArray allHomes = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.homedetailspagefragment, container, false);
        getActivity().getActionBar().setTitle("Home details");
        int position = getArguments().getInt("currentPos");
        homeList = new ArrayList<HashMap<String, String>>();

        Log.e("CURRENT HOUSE ID IS ", "POSITION" + position);
        home_image = (ImageView)rootView.findViewById(R.id.single_home_image);

        hometitle = (TextView)rootView.findViewById(R.id.single_home_title);
         rentText = (TextView)rootView.findViewById(R.id.rentLabel);
         homedescription = (TextView)rootView.findViewById(R.id.descriptionLabel);

        String mapPath = "https://maps.google.com/?ll=37.0625,-95.677068&spn=29.301969,56.513672&t=h&z=4";


        WebView home_location_map = (WebView)rootView.findViewById(R.id.home_location_map);
        home_location_map.getSettings().setJavaScriptEnabled(true);
        home_location_map.setWebViewClient(new WebViewClient());
        home_location_map.getSettings().setDomStorageEnabled(true);
        try {
            URLEncoder.encode(mapPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        home_location_map.loadUrl(mapPath);

        /*
        * will be getting start date and time from dialog soon
        * */
        /*
        String startDate="20151011091520";
        String endDate="2015-10-11 10:15:20";

        String[] params = new String[] { url,startDate, endDate};
        SendHttpRequestTask task = new SendHttpRequestTask();
        task.execute(params);
        */
        String[] params = new String[] { url,Integer.toString(position)};
        SendHttpRequestTask task = new SendHttpRequestTask();
        task.execute(params);
        return rootView;
    }

    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {
        HttpURLConnection urlConnection = null;

        @Override
        protected void onPostExecute(String jsonStr) {

            Log.e("POST OUTPUT",jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    allHomes = jsonObj.getJSONArray(HOMES);

                    // looping through All Homes
                    for (int i = 0; i < allHomes.length(); i++) {
                        JSONObject c = allHomes.getJSONObject(i);

                        String id = c.getString(HOME_ID);
                        String description = c.getString(HOME_TITLE);
                        String rent = c.getString(HOME_RENT);
                        String location = c.getString(HOME_LOCATION);
                        String location_url=c.getString(HOME_LOCATION_URL);
                        String image_url = c.getString(HOME_IMAGE_URL);

                        HashMap<String, String> home = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        home.put(HOME_ID, id);
                        home.put(HOME_TITLE, description);
                        home.put(HOME_RENT, rent);
                        home.put(HOME_LOCATION, location);
                        home.put(HOME_LOCATION_URL, location_url);
                        home.put(HOME_IMAGE_URL, image_url);

                        Ion.with(home_image)
                                .placeholder(R.drawable.home_big_icon)
                                .error(R.drawable.home_big_icon)
                                .animateLoad(R.anim.fadein)
                                .load(image_url);

                        hometitle.setText(description);

                        rentText.setText(rentText.getText() + rent);
                        homedescription.setText(homedescription.getText()+location);
                        /*
                        Log.e("LOCATION URL : ",location_url);
                        home_location_map.loadUrl(location_url);
                        home_location_map.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String location_url) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(location_url));
                                startActivity(intent);
                                return false;
                            }
                        });

                        home_location_map.setWebViewClient(new WebViewClient());
                        home_location_map.getSettings().setJavaScriptEnabled(true);
                        home_location_map.setWebViewClient(new Callback());
                        home_location_map.loadUrl("http://maps.google.com/maps?q=43.0054446,-87.9678884&t=m&z=7");
                        */
                        int flag=0;
                        for(int j=0;j<allHomes.length();j++){
                            if(id.equals(allHomes.get(j)))
                            {
                                flag=1;
                                break;
                            }
                        }
                        if(flag==0)
                            homeList.add(home);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            //home_image.setImageURI(Uri.parse("http://insurancebroker.ca/ib-includes/images/home.jpg"));



            super.onPostExecute(jsonStr);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.e("URL IS ", params[0]);
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("homeID", params[1]);

                String query = builder.build().getEncodedQuery();
                Log.e("BEFORE WRITE", "its fine"+query);
                try {
                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));

                    writer.write(query);
                    Log.e("AFTER WRITE", "not fine");
                    writer.flush();
                    writer.close();
                    os.close();
                } catch (Exception e) {
                    Log.e("SILENT ERROR", e.toString());
                }


                int responseCode = urlConnection.getResponseCode();
                Log.e("CODE :", "Your response code is " + responseCode);

                InputStream is =urlConnection.getInputStream();
                String jsondata = new JSONParser().getStringFromStream(is);
                Log.e("SINGLE LINE ",jsondata);
                return jsondata;
            }

            catch(Exception e){
                Log.e("EXCEPTION ",e.toString());
            }

            return null;
        }

        }
    /*
    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {
        HttpURLConnection urlConnection = null;
        @Override
        protected String doInBackground(String... params) {
            String startDate = params[1];
            Log.e("START DATE :", "Start date " + startDate);
            String enddate = params[2];
            Log.e("END DATE :", "end date " + enddate);
            try {
                Log.e("URL IS ", params[0]);
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("startDate", startDate)
                        .appendQueryParameter("endDate", enddate);

                String query = builder.build().getEncodedQuery();
                Log.e("BEFORE WRITE", "its fine");
                try {
                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));

                    writer.write(query);
                    Log.e("AFTER WRITE", "not fine");
                    writer.flush();
                    writer.close();
                    os.close();
                } catch (Exception e) {
                    Log.e("SILENT ERROR", e.toString());
                }
                int responseCode = urlConnection.getResponseCode();
                Log.e("CODE :", "Your response code is " + responseCode);
            } catch (Exception e) {
                Log.e("GETTING ERROR ", e.toString());
                e.printStackTrace();
            }

            return null;
        }
    }

*/

}
