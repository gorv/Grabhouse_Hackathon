package com.example.raghav.grabahouse;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {
	
	public HomeFragment(){}


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


        String startDate="2015-10-11 09:15:20";
        String endDate="2015-10-11 10:15:20";



        return rootView;
    }
    private class SendHttpRequestTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String param1 = params[1];
            String param2 = params[2];
            //String fileNameArray[] = param1.split("/");
            String fileName = param1;
            try {
                
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            return null;
        }


    }
}
