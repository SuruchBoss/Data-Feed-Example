package com.codemobiles.myauthen;


import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedSOAPFragment extends Fragment
{

    private TextView mDieselTextView;
    private TextView mE85TextView;
    private TextView mE20TextView;
    private TextView mGas91TextView;
    private TextView mGas95TextView;
    private View v;

    public FeedSOAPFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_feed_soap, container, false);
        bindWidget();
        setFont();

        new WebServiceTask().execute();

        return v;
    }

    private void setFont()
    {
        // set custom font
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "ds_digital.TTF");
        mDieselTextView.setTypeface(type);
        mE85TextView.setTypeface(type);
        mE20TextView.setTypeface(type);
        mGas91TextView.setTypeface(type);
        mGas95TextView.setTypeface(type);
    }

    private void bindWidget()
    {
        mDieselTextView = (TextView) v.findViewById(R.id.dieselTextView);
        mE85TextView = (TextView) v.findViewById(R.id.e85TextView);
        mE20TextView = (TextView) v.findViewById(R.id.e20TextView);
        mGas91TextView = (TextView) v.findViewById(R.id.gas91TextView);
        mGas95TextView = (TextView) v.findViewById(R.id.gas95TextView);
    }

    public class WebServiceTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Toast.makeText(getActivity(), "Loading....", Toast.LENGTH_SHORT).show();
            mDieselTextView.setText("Loading....");
            mGas91TextView.setText("Loading....");
            mGas95TextView.setText("Loading....");
            mE20TextView.setText("Loading....");
            mE85TextView.setText("Loading....");

        }

        @Override
        protected String doInBackground(String... params)
        {
            String result = CMWebservice.getCurrentOilPrice();
            Log.i("Codemobiles_log", result);
            String output = CMWeatherWS.GetCitiesByCountry("Japan");

            Log.i("Codemobiles_Weather", output);
            return result;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            CMXMLParser parser = new CMXMLParser();
            Document doc = parser.getDomElement(s);
            NodeList nl = doc.getElementsByTagName("DataAccess");

           /* Element item = (Element) nl.item(0);
            String price = parser.getValue(item, "PRICE");
            mE20TextView.setText(price);*/

            //Loop insert value
            for (int j = 0; j < nl.getLength(); j++)
            {
                Element e = (Element) nl.item(j);

                String product = parser.getValue(e, "PRODUCT").trim();
                String price = parser.getValue(e, "PRICE").trim();
                if (product.equals("Blue Diesel"))
                {
                    mDieselTextView.setText(price);
                } else if (product.equals("Blue Gasohol E85"))
                {
                    mE85TextView.setText(price);
                } else if (product.equals("Blue Gasohol E20"))
                {
                    mE20TextView.setText(price);
                } else if (product.equals("Blue Gasohol 91"))
                {
                    mGas91TextView.setText(price);
                } else if (product.equals("Blue Gasohol 95"))
                {
                    mGas95TextView.setText(price);
                }
            }
        }
    }

}
