package com.codemobiles.myauthen;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codemobiles.myauthen.YoutubeBean.YoutubesBean;
import com.google.gson.Gson;
import com.thefinestartist.ytpa.utils.YouTubeApp;

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
public class FeedJSONFragment extends Fragment
{

    private ListView mListView;
    private View view;
    private List<YoutubesBean> mDataArray = new LinkedList<>();
    private ListViewAdapter mAdapter;
    private UserBean userbean;

    public static final String kFeed_trainings = "training";
    public static final String kFeed_foods = "foods";
    public static final String kFeed_superheros = "superhero";
    public static final String kFeed_songs = "songs";
    private String mFeed_type = kFeed_trainings;

    public FeedJSONFragment()
    {
        // Required empty public constructor

    }

    private void bindWidget()
    {
        mListView = (ListView) view.findViewById(R.id.mylistview);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feed_json, container, false);

        bindWidget();

        mAdapter = new ListViewAdapter();
        mListView.setAdapter(mAdapter);

        //Add Header View
        TextView headerView = new TextView(getActivity());
        headerView.setHeight((int) getResources().getDimension(R.dimen.header_height));
        mListView.addHeaderView(headerView);

        //Add Footer View
        TextView footView = new TextView(getActivity());
        footView.setHeight((int) getResources().getDimension(R.dimen.footer_height));
        mListView.addFooterView(footView);

        //Background  Thread Sync
        new FeedTask().execute("http://codemobiles.com/adhoc/youtubes/index_new.php");

        //Setup Blur

        CMBlurUtil.setup(getActivity(), mListView, R.drawable.bg, headerView);

        return view;
    }

    public void feed(String type)
    {
        mFeed_type = type;
        new FeedTask().execute("http://codemobiles.com/adhoc/youtubes/index_new.php");
    }

    private class ListViewAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return mDataArray.size();
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder = null;

            if (convertView == null)
            {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_listview, null);

                holder = new ViewHolder();
                holder.titleTextView = (TextView) convertView.findViewById(R.id.item_listview_title);
                holder.subtitleTextView = (TextView) convertView.findViewById(R.id.item_listview_subtitle);
                holder.avatarImageView = (ImageView) convertView.findViewById(R.id.item_listview_avatarIcon);
                holder.youtubeImageView = (ImageView) convertView.findViewById(R.id.item_listview_youtube_image);

                //Set Event for click the view.
                holder.youtubeImageView.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String youtubeID = (String) v.getTag(R.id.item_listview_youtube_image);
                        //Toast.makeText(getActivity(), youtubeID, Toast.LENGTH_SHORT).show();
                        YouTubeApp.startVideo(getActivity(), youtubeID);
                    }
                });
                //set tag
                convertView.setTag(holder);
            } else
            {
                holder = (ViewHolder) convertView.getTag();
            }
            YoutubesBean item = mDataArray.get(position);

            //get obj id with Specific tag and save in obj v.
            holder.youtubeImageView.setTag(R.id.item_listview_youtube_image, item.getId());

            //Update Content
            holder.titleTextView.setText(item.getTitle());
            holder.subtitleTextView.setText(item.getSubtitle());
            Glide.with(getActivity()).load(item.getYoutube_image()).into(holder.youtubeImageView);
            Glide.with(getActivity()).load(item.getAvatar_image()).bitmapTransform(new CropCircleTransformation(getActivity())).into(holder.avatarImageView);

            return convertView;
        }
    }

    /*
    public class ViewHolder
    {
        TextView titleTextView;
        TextView subtitleTextView;
        ImageView avatarImageView;
        ImageView youtubeImageView;
    }*/

    public class FeedTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute()
        {
            //Called in main Therd - canoot run infinite task
            //Can set Loading UI
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s)
        {
            //Called in main Therd - canoot run infinite task
            super.onPostExecute(s);

            Gson gson = new Gson();
            YoutubeBean result = gson.fromJson(s, YoutubeBean.class);
            //Toast.makeText(getActivity(), result.getYoutubes().get(0).getTitle(), Toast.LENGTH_SHORT).show();

            mDataArray = result.getYoutubes();
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                userbean = getArguments().getParcelable(UserBean.TABLE_NAME);

                String url = params[0];
                OkHttpClient client = new OkHttpClient();
                RequestBody data = new FormBody.Builder()
                        .add("username", userbean.username)
                        .add("password", userbean.password)
                        .add("type", mFeed_type)
                        .build();

                Request req = new Request.Builder().url(url).post(data).build();
                Response response = client.newCall(req).execute();
                String result = response.body().string();

                return result;

                //Log.i("CodeMobiles_google", result);
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }
    }
}

