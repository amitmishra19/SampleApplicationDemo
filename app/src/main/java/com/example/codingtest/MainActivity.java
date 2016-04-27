package com.example.codingtest;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.codingtest.adapters.DataAdapter;
import com.example.codingtest.beans.DataModel;
import com.example.codingtest.beans.Row;
import com.example.codingtest.retrofit.APIService;
import com.example.codingtest.retrofit.Callback;
import com.example.codingtest.retrofit.RetrofitHelper;
import com.example.codingtest.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private List<Row> mDataSet = new ArrayList<>();
    private DataAdapter adapter;
    private ProgressDialog mProgressDialog;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private Toolbar mToolbar;

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
        adapter = new DataAdapter(this, mDataSet);
        mRecyclerView.setAdapter(adapter);

        //start the data fetch
        getData();
        handleSwipeRefresh();
    }

    //Use retrofit to call the web API, fetch and parse the data.
    private void getData() {
        if (Utils.isDataNetworkConnected(this)) {
            //show the progress dialog for the first time only. after that pull to refresh progress indicator will be shown
            if (mSwipeRefresh == null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage(getResources().getString(R.string.msg_fetch_contents));
                mProgressDialog.show();
            }

            Call<DataModel> tests = RetrofitHelper.newInstance().getAPIService(APIService.BASE_URL).getFacts();
            RetrofitHelper.newInstance().sendRequest(tests, new Callback<DataModel>() {
                @Override
                public void onResponse(DataModel response) {
                    Log.d("SWAT", " " + response.getRows().size());
                    mToolbar.setTitle(response.getTitle());
                    dismissProgressDialogs();

                    List<Row> result = response.getRows();
                    //remove any row from json which has no title OR no description
                    mDataSet.addAll(Utils.cleanFactsData(result));

                    //set the values to adapter and notify the same to refresh the list
                    adapter.setModels(mDataSet);
                    adapter.notifyDataSetChanged();

                    if(mSwipeRefresh != null){
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.msg_list_update_success), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onError(String error) {
                    dismissProgressDialogs();
                    if (error != null) {
                        Log.d("SWAT", " " + error.toString());
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
}
