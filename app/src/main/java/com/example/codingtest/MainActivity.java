package com.example.codingtest;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.codingtest.adapters.FactsListAdapter;
import com.example.codingtest.beans.Fact;
import com.example.codingtest.beans.FactsDataModel;
import com.example.codingtest.retrofit.APIService;
import com.example.codingtest.retrofit.RetrofitHelper;
import com.example.codingtest.utils.Utils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private List<Fact> mDataSet = new ArrayList<>();
    private FactsListAdapter adapter;
    private ProgressDialog mProgressDialog;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private Toolbar mToolbar;
    private boolean manualRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //Initially no title at top
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FactsListAdapter(this, mDataSet);
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        DemoApplication.eventBus.register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //start the data fetch
        getData();
        handleSwipeRefresh();
    }

    @Override
    protected void onStop() {
        super.onStop();
        DemoApplication.eventBus.unregister(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "new Configuration : " + newConfig.orientation);
    }

    //Use retrofit to call the web API

    private void getData() {
        if (Utils.isDataNetworkConnected(this)) {
            //show the progress dialog for the first time only. after that pull to refresh progress indicator will be shown
            if (mSwipeRefresh == null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage(getResources().getString(R.string.msg_fetch_contents));
                mProgressDialog.show();
            }

            Call<FactsDataModel> tests = RetrofitHelper.newInstance().getAPIService(APIService.BASE_URL).getFacts();
            RetrofitHelper.newInstance().sendRequest(tests);
        } else {
            showNoConnectionToast();
        }
    }

    // register for pull to refresh listener and handle the callback method
    private void handleSwipeRefresh() {
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isDataNetworkConnected(MainActivity.this)) {
                    manualRefresh = true;
                    getData();
                } else {
                    showNoConnectionToast();
                }
            }
        });
    }

    //Show toast message for no data connection
    private void showNoConnectionToast() {
        Toast.makeText(MainActivity.this, getResources().getString(R.string.msg_no_data_network), Toast.LENGTH_SHORT).show();
        dismissProgressDialogs();
    }

    private void dismissProgressDialogs() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        if (mSwipeRefresh != null && mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }

    }

    //Subscribe for the events from event bus, this will contain Response object in case web service call is successful
    //This event is posted from RetrofitHelper
    @Subscribe
    public void getMessage(Response response) {
        Log.d(TAG, " " + response.body());
        FactsDataModel factsDataModel = (FactsDataModel) response.body();
        mToolbar.setTitle(factsDataModel.getTitle());
        dismissProgressDialogs();

        List<Fact> result = factsDataModel.getRows();

        if (!manualRefresh) {
            mDataSet.clear();
        }

        //remove any row from json which has no title OR no description
        mDataSet.addAll(Utils.cleanFactsData(result));

        //set the values to adapter and notify the same to refresh the list
        adapter.setModels(mDataSet);
        adapter.notifyDataSetChanged();

        if (mSwipeRefresh != null) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.msg_list_update_success), Toast.LENGTH_SHORT).show();
        }

        manualRefresh = false;
    }

    //Subscribe for the events from event bus, this will contain Throwable object in case web service call is failed
    //This event is posted from RetrofitHelper
    @Subscribe
    public void getMessage(Throwable error) {
        Log.d(TAG, " " + error.getLocalizedMessage());
        Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
}