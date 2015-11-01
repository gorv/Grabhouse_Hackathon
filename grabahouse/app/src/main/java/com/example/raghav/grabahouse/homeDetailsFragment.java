package com.example.raghav.grabahouse;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class homeDetailsFragment extends Fragment {
    private String url = ServerUrl.getSingleHomeDetails_url;
    private String url1 = ServerUrl.storetimeinterval_url;
    ImageView home_image;
    TextView hometitle;
    TextView rentText;
    TextView homedescription;
    TextView owner_email_textbox;
    TextView owner_phone_textbox;
    private static int MIN_OFF_SET =20;
    WebView home_location_map;
    String id;
    String dateToVisit,fromTime,toTime;
    Button dateToVisitBtn;
    Button fromTimeBtn;
    Button toTimeBtn;
    Button bookBtn;
    private int year;
    private int month;
    private int day;
    private int fromhours,fromminutes;
    private int tohours,tominutes;
    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID =1;
    static final String userName = "ebc@gmail.com";
    private DatePickerDialog.OnDateSetListener pDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int yyear,
                                      int monthOfYear, int dayOfMonth) {
                    year = yyear;
                    month = monthOfYear;
                    day = dayOfMonth;
                    displayDateToast();
                }
            };

    private void displayDateToast() {
        dateToVisit=(year) + "-" + (month + 1) + "-" + (day);
           Toast.makeText(getActivity().getApplicationContext(), new StringBuilder().append("Date choosen is ").append(new StringBuilder()
                   .append(month + 1).append("/")
                   .append(day).append("/")
                   .append(year).append(" ")), Toast.LENGTH_SHORT).show();

    }

    private TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            fromhours =  hourOfDay;
            fromminutes =  minute;
            fromTime=fromhours+":"+fromminutes;
            displayTimeToast();
        }
    };
    private void displayTimeToast() {
        Toast.makeText(getActivity().getApplicationContext(), new StringBuilder().append("Time choosen is ").append(new StringBuilder().append(fromhours).append(" : ").append(fromminutes).append(" ")), Toast.LENGTH_SHORT).show();

    }

    private TimePickerDialog.OnTimeSetListener toTimeSetter = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            tohours =  hourOfDay;
            tominutes =  minute;
            displayTimeToast1();
        }
    };
    private void displayTimeToast1() {
        toTime=tohours+":"+tominutes;
        Toast.makeText(getActivity().getApplicationContext(), new StringBuilder().append("Time choosen is ").append(new StringBuilder().append(tohours).append(" : ").append(tominutes).append(" ")), Toast.LENGTH_SHORT).show();

    }
    ArrayList<HashMap<String, String>> homeList;
    private static final String HOMES = "HOMES";
    private static final String HOME_ID = "HOME_ID";
    private static final String HOME_TITLE = "HOME_TITLE";
    private static final String HOME_LOCATION = "HOME_LOCATION";
    private static final String HOME_RENT = "HOME_RENT";
    private static final String HOME_LOCATION_URL = "HOME_LOCATION_URL";
    private static final String HOME_IMAGE_URL = "HOME_IMAGE_URL";

    private static final String OWNER_EMAIL = "OWNER_EMAIL";
    private static final String OWNER_PHONE = "OWNER_PHONE";

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

        dateToVisitBtn = (Button)rootView.findViewById(R.id.visitDate);
        hometitle = (TextView)rootView.findViewById(R.id.single_home_title);
        rentText = (TextView)rootView.findViewById(R.id.rentLabel);
        homedescription = (TextView)rootView.findViewById(R.id.descriptionLabel);

        owner_email_textbox = (TextView)rootView.findViewById(R.id.owner_email_textbox);
        owner_phone_textbox = (TextView)rootView.findViewById(R.id.owner_phone_textbox);
        fromTimeBtn = (Button)rootView.findViewById(R.id.timefromBtn);
        toTimeBtn = (Button)rootView.findViewById(R.id.timetoBtn);

        bookBtn = (Button)rootView.findViewById(R.id.bookBtn);
        setCurrentDateOnView();
        setCurrentTime();
        setCurrentTime1();
        dateToVisitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                createdDialog(DATE_DIALOG_ID).show();
            }

            protected Dialog createdDialog(int id) {
                switch (id) {
                    case DATE_DIALOG_ID:
                        return new DatePickerDialog(rootView.getContext(),
                                pDateSetListener,
                                year, month, day);
                }
                return null;
            }
        });


        fromTimeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                createTimeDialog(TIME_DIALOG_ID).show();
            }
            protected Dialog createTimeDialog(int id){
                switch (id) {
                    case TIME_DIALOG_ID:
                        return new TimePickerDialog(rootView.getContext(),
                                t,
                                fromhours, fromminutes,false);
                }
                return null;
            }
        });

        toTimeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                createTimeDialog(TIME_DIALOG_ID).show();
            }
            protected Dialog createTimeDialog(int id){
                switch (id) {
                    case TIME_DIALOG_ID:
                        return new TimePickerDialog(rootView.getContext(),
                                toTimeSetter,
                                tohours, tominutes,false);
                }
                return null;
            }
        });
        /*
        String mapPath = "https://maps.google.com/?ll=37.0625,-95.677068&spn=29.301969,56.513672&t=h&z=4";


        WebView home_location_map = (WebView)rootView.findViewById(R.id.home_location_map);
        home_location_map.getSettings().setJavaScriptEnabled(true);
        home_location_map.setWebViewClient(new WebViewClient());
        home_location_map.getSettings().setDomStorageEnabled(true);
        URLEncoder.encode(mapPath, "UTF-8");
        home_location_map.loadUrl(mapPath);

        /*
        * will be getting start date and time from dialog soon
        * */
        /*
        String startDate="20151011091520";
        String endDate="2015-10-11 10:15:20";
*/

        String[] params = new String[] { url,Integer.toString(position)};
        SendHttpRequestTask task = new SendHttpRequestTask();
        task.execute(params);



        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValidTime=true;
                if (dateToVisit == null || fromTime == null || toTime == null) {
                    isValidTime=false;
                    Toast.makeText(getActivity(), "Date time can not be empty", Toast.LENGTH_LONG).show();
                }
                if((fromhours*60 + fromminutes + MIN_OFF_SET)>(tohours*60 + tominutes)) {
                    isValidTime=false;
                    Toast.makeText(getActivity(), "Exit time needs to be atleast 20 minutes ahead of Entry time", Toast.LENGTH_LONG).show();
                }
                final Calendar c = Calendar.getInstance();
                if(year<c.get(Calendar.YEAR) || month<c.get(Calendar.MONTH) || day<c.get(Calendar.DAY_OF_MONTH)){
                    isValidTime=false;
                    Toast.makeText(getActivity(), "Date cant be older than current date", Toast.LENGTH_LONG).show();
                }

                if(isValidTime==true) {
                    String[] availparams = new String[]{url1, id, dateToVisit, fromTime, toTime, userName};
                    SendHttpRequestTaskForCheckAvailability availtask = new SendHttpRequestTaskForCheckAvailability();
                    availtask.execute(availparams);
                }

            }
        });

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

                        id = c.getString(HOME_ID);
                        String description = c.getString(HOME_TITLE);
                        String rent = c.getString(HOME_RENT);
                        String location = c.getString(HOME_LOCATION);
                        String location_url=c.getString(HOME_LOCATION_URL);
                        String image_url = c.getString(HOME_IMAGE_URL);

                        String owner_email=c.getString(OWNER_EMAIL);
                        String owner_phone=c.getString(OWNER_PHONE);
                        HashMap<String, String> home = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        home.put(HOME_ID, id);
                        home.put(HOME_TITLE, description);
                        home.put(HOME_RENT, rent);
                        home.put(HOME_LOCATION, location);
                        home.put(HOME_LOCATION_URL, location_url);
                        home.put(HOME_IMAGE_URL, image_url);
                        home.put(OWNER_EMAIL, owner_email);
                        home.put(OWNER_PHONE, owner_phone);

                        Ion.with(home_image)
                                .placeholder(R.drawable.home_big_icon)
                                .error(R.drawable.home_big_icon)
                                .animateLoad(R.anim.fadein)
                                .load(image_url);

                        hometitle.setText(description);

                        rentText.setText(rentText.getText() + rent);
                        homedescription.setText(homedescription.getText()+location);
                        owner_email_textbox.setText(owner_email_textbox.getText()+owner_email);
                        owner_phone_textbox.setText(owner_phone_textbox.getText()+owner_phone);
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

    private class SendHttpRequestTaskForCheckAvailability extends AsyncTask<String, Void, String> {
        HttpURLConnection urlConnection = null;
        @Override
        protected String doInBackground(String... params) {
            String home_id = params[1];
            Log.e("HOME ID  :", "home id " + home_id);
            String dateToVisit = params[2];
            Log.e("VISIT DATE :", "visit date " + dateToVisit);
            String fromtime = params[3];
            Log.e("FROM TIME :", "from time  " + fromtime);
            String totime = params[4];
            Log.e("TO TIME :", "to time  " + totime);
            String username = params[5];
            Log.e("USERNAME :", "username  " + username);


            try {
                Log.e("URL IS ", params[0]);
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("home_id", home_id)
                        .appendQueryParameter("startDate", dateToVisit)
                        .appendQueryParameter("fromtime", fromtime)
                        .appendQueryParameter("totime", totime)
                        .appendQueryParameter("username", username);

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

                InputStream is =urlConnection.getInputStream();
                String jsondata = new JSONParser().getStringFromStream(is);
                Log.e("SINGLE LINE ",jsondata);
                return jsondata;

            } catch (Exception e) {
                Log.e("GETTING ERROR ", e.toString());
                e.printStackTrace();
            }

            return null;
        }
    }


    public void setCurrentDateOnView() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        //tvDisplayDate.setText(new StringBuilder()
        //	.append(month + 1).append("-").append(day).append("-")
        //.append(year).append(" "));
    }

    private void setCurrentTime() {
        final Calendar c = Calendar.getInstance();
        fromhours = c.get(Calendar.HOUR_OF_DAY);
        fromminutes = c.get(Calendar.MINUTE);

    }
    private void setCurrentTime1() {
        final Calendar c = Calendar.getInstance();
        tohours = c.get(Calendar.HOUR_OF_DAY);
        tominutes = c.get(Calendar.MINUTE);

    }
}
