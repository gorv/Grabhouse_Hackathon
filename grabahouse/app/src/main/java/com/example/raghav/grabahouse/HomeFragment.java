package com.example.raghav.grabahouse;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    ListView lv;
    ArrayList<HashMap<String, String>> homeList;
    private String url = ServerUrl.getHomeDetails_url;

    private static final String HOMES = "HOMES";
    private static final String HOME_ID = "HOME_ID";
    private static final String HOME_TITLE = "HOME_TITLE";
    private static final String HOME_LOCATION = "HOME_LOCATION";
    private static final String HOME_RENT = "HOME_RENT";
    JSONArray allHomes = null;


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        homeList = new ArrayList<HashMap<String, String>>();
        lv= (ListView) rootView.findViewById(R.id.allhomeslist);




        /*
        * Test data
        * */

        /*
        HashMap<String, String> book = new HashMap<String, String>();

        book.put("HOME_ID", "123");
        book.put("HOME_DESCRIPTION", "Semi Furnished 1RK Flat");
        book.put("HOME_RENT", "Rs. 8500");
        book.put("HOME_LOCATION", "Kormangala,Sarjapur road,Bengaluru, 560034");

        homeList.add(book);

        */

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterView, View view, int position, long id) {

                Toast.makeText(getActivity().getApplicationContext(), "CLICKED " + position, Toast.LENGTH_LONG).show();
                Fragment newFragment = new homeDetailsFragment();
                Bundle aa = new Bundle();
                aa.putInt("currentPos", position + 1);
                newFragment.setArguments(aa);
                android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        new JSONParse().execute();

        return rootView;
    }


    private class JSONParse extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(
                    getActivity().getApplicationContext(), homeList,
                    R.layout.home_list_item, new String[] {"HOME_TITLE", "HOME_RENT",
                    "HOME_LOCATION" }, new int[] { R.id.homedescription,
                    R.id.homerent, R.id.homelocation });
            lv.setAdapter(adapter);
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            JSONParser jParser = new JSONParser();
            String jsonStr = jParser.getJSONFromUrl(url);
            Log.e("results","RESULTS "+jsonStr);

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


                        HashMap<String, String> home = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        home.put(HOME_ID, id);
                        home.put(HOME_TITLE, description);
                        home.put(HOME_RENT, rent);
                        home.put(HOME_LOCATION, location);



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

            return null;
        }

    }

}
