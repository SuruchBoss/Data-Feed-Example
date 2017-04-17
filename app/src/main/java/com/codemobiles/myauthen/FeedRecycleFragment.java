package com.codemobiles.myauthen;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codemobiles.myauthen.YoutubeBean.YoutubesBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedRecycleFragment extends Fragment
{
    private View view;
    private RecyclerView mRecycleView;
    private List<YoutubesBean> mDataArray = new LinkedList<>();
    private CustomAdapter mAdapter;

    public FeedRecycleFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feed_recycle, container, false);

        mRecycleView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mAdapter = new CustomAdapter();
        mRecycleView.setAdapter(mAdapter);

        new FeedTask().execute("http://codemobiles.com/adhoc/youtubes/index_new.php");

        return view;
    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomHolder>
    {

        @Override
        public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            view = getActivity().getLayoutInflater().inflate(R.layout.item_listview, null);
            CustomHolder cusHolder = new CustomHolder(view);
            return cusHolder;
        }

        @Override
        public void onBindViewHolder(CustomHolder holder, int position)
        {
            YoutubesBean item = mDataArray.get(position);

            //get obj id with Specific tag and save in obj v.
            holder.youtubeImageView.setTag(R.id.item_listview_youtube_image, item.getId());

            //Update Content
            holder.titleTextView.setText(item.getTitle());
            holder.subtitleTextView.setText(item.getSubtitle());
            Glide.with(getActivity()).load(item.getYoutube_image()).into(holder.youtubeImageView);
            Glide.with(getActivity()).load(item.getAvatar_image()).bitmapTransform(new CropCircleTransformation(getActivity())).into(holder.avatarImageView);

        }

        @Override
        public int getItemCount()
        {
            return mDataArray.size();
        }
    }

    public class FeedTask extends AsyncTask<String, Void, String>
    {
        protected void onPreExecute()
        {
            //Called in main Therd - canoot run infinite task
            //can update  UI
            //Can set Loading UI

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                String url = params[0];
                OkHttpClient client = new OkHttpClient();
                RequestBody data = new FormBody.Builder()
                        .add("username", "admin")
                        .add("password", "password")
                        .add("type", "foods")
                        .build();

                Request req = new Request.Builder().url(url).post(data).build();
                Response response = client.newCall(req).execute();
                String result = response.body().string();

                //return all JSON data from website
                return result;

                //Log.i("CodeMobiles_google", result);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String s)
        {   // After onBackground get Data this method will send to foreground
            //Called in main Therd - canoot run infinite task
            //can update  UI
            super.onPostExecute(s);

            //Decare GSON obj to collect JSON data
            Gson gson = new Gson();
            YoutubeBean result = gson.fromJson(s, YoutubeBean.class);
            mDataArray = result.getYoutubes();
            mAdapter.notifyDataSetChanged();
        }
    }

    private class CustomHolder extends RecyclerView.ViewHolder
    {
        TextView titleTextView;
        TextView subtitleTextView;
        ImageView avatarImageView;
        ImageView youtubeImageView;

        public CustomHolder(View convertView)
        {
            //Class for bind view to listxml
            super(convertView);

            titleTextView = (TextView) convertView.findViewById(R.id.item_listview_title);
            subtitleTextView = (TextView) convertView.findViewById(R.id.item_listview_subtitle);
            avatarImageView = (ImageView) convertView.findViewById(R.id.item_listview_avatarIcon);
            youtubeImageView = (ImageView) convertView.findViewById(R.id.item_listview_youtube_image);
        }
    }
}
