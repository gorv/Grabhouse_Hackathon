package com.example.raghav.grabahouse;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.BufferedInputStream;
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
import java.util.Map;

public class HomeFragment extends Fragment {
	
	public HomeFragment(){}

    private String url = ServerUrl.storetimeinterval_url;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ArrayList<HashMap<String, String>> homeList = new ArrayList<HashMap<String, String>>();
        ListView lv= (ListView) rootView.findViewById(R.id.allhomeslist);




        HashMap<String, String> book = new HashMap<String, String>();

        book.put("HOME_ID", "123");
        book.put("HOME_DESCRIPTION", "Semi Furnished 1RK Flat");
        book.put("HOME_RENT", "Rs. 8500");
        book.put("HOME_LOCATION", "Kormangala,Sarjapur road,Bengaluru, 560034");

        homeList.add(book);


        ListAdapter adapter = new SimpleAdapter(
                getActivity().getApplicationContext(), homeList,
                R.layout.home_list_item, new String[] {"HOME_DESCRIPTION", "HOME_RENT",
                "HOME_LOCATION" }, new int[] { R.id.homedescription,
                R.id.homerent, R.id.homelocation });

        lv.setAdapter(adapter);


        String startDate="20151011091520";
        String endDate="2015-10-11 10:15:20";

        String[] params = new String[] { url,startDate, endDate};

        SendHttpRequestTask task = new SendHttpRequestTask();
        task.execute(params);

        return rootView;
    }
    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {
        HttpURLConnection urlConnection = null;
        @Override
        protected String doInBackground(String... params) {
            String startDate = params[1];
            Log.e("START DATE :", "Start date " + startDate);

            String enddate = params[2];
            Log.e("END DATE :", "end date " + enddate);
            try {
                Log.e("URL IS ",params[0]);
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                //List<NameValuePair> params = new ArrayList<NameValuePair>();
                //params.add(new BasicNameValuePair("firstParam", paramValue1));

                /*
                HashMap<String,String> hashParam = new HashMap<String,String>();
                hashParam.put("startDate",startDate);
                Log.e("POST DATA :", "Post data string " + getPostDataString(hashParam));
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(hashParam));
                */

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("startDate", startDate)
                        .appendQueryParameter("endDate",enddate);

                String query = builder.build().getEncodedQuery();
                Log.e("BEFORE WRITE","its fine");

                //urlConnection.connect();
                try {
                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));

                    writer.write(query);
                    Log.e("AFTER WRITE", "not fine");
                    writer.flush();
                    writer.close();
                    os.close();
                }
                catch (Exception e){
                    Log.e("SILENT ERROR",e.toString());
                }


                //Log.e("POST STRING :", "Post data string " + getPostDataString(hashParam));

                int responseCode=urlConnection.getResponseCode();
                Log.e("CODE :","Your response code is "+responseCode);


                /*
                conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
                 */

            }catch(Exception e){
                Log.e("GETTING ERROR ",e.toString());
                e.printStackTrace();
            }


/*
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);

            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            paramList.add(new BasicNameValuePair("name", name));
            paramList.add(new BasicNameValuePair("user", user));
            try {
                post.setEntity(new UrlEncodedFormEntity(paramList));
                HttpResponse resp = client.execute(post);
                InputStream is = resp.getEntity().getContent();
                inputStream = is;
                int contentSize = (int) resp.getEntity().getContentLength();
                totalSize = contentSize;
                System.out.println("Content size [" + contentSize + "]");
                BufferedInputStream bis = new BufferedInputStream(is, 512);
                data = new byte[contentSize];
                int bytesRead = 0;
                int offset = 0;

                while (bytesRead != -1 && offset < contentSize) {
                    Log.e("CALLED", "CALLED");

                    bytesRead = bis.read(data, offset, contentSize - offset);

                    offset += bytesRead;
                    publishProgress(offset);

                }
                System.out.println("Data [" + data.length + "]");
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return data;
            */
            return null;
        }
        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            Log.e("POST STRING FUNCTION ","RESULT : "+result.toString());
            return result.toString();
        }

    }

}
