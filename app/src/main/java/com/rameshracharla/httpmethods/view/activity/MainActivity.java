package com.rameshracharla.httpmethods.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rameshracharla.httpmethods.R;
import com.rameshracharla.httpmethods.interfaces.ApiClient;
import com.rameshracharla.httpmethods.interfaces.ApiInterface;
import com.rameshracharla.httpmethods.model.ResponseData;
import com.rameshracharla.httpmethods.utils.Constants;
import com.rameshracharla.httpmethods.view.adapter.ViewEmployeeAdapter;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerview;
    private ViewEmployeeAdapter adapter;
    private Constants con;
    private TextView text_norecords;
    private boolean backpressed = false;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ApiInterface apiService;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        initializeOnClickListeners();
        if (con.isConnected()) {
            getDetails();
        } else {
            con.noInternetConnectionDialog();
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(false);
                if (con.isConnected()) {
                    getDetails();
                } else {
                    con.noInternetConnectionDialog();
                }
            }
        });
    }


    private void initializeViews() {
        con = new Constants(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_main);

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(layoutManager);
        text_norecords = (TextView) findViewById(R.id.text_norecords);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void initializeOnClickListeners() {
        fab.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (backpressed) {
            super.onBackPressed();
            finish();
            return;
        }
        this.backpressed = true;
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backpressed = false;
            }
        }, 2000);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab:
                Intent intent = new Intent(MainActivity.this, EmployeeDetails.class);
                intent.putExtra("purpose", "createdetails");
                startActivity(intent);
                break;
        }

    }

    private void getDetails() {
        con.showProgressDialogue();
        disposable.add(
                apiService.getRecords()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseData>() {
                            @Override
                            public void onSuccess(ResponseData response) {
                                con.dismissProgressDialogue();
                                try {
                                    if (response.getStatus().equals("success")) {
                                        if (response.getGetData().size() != 0) {
                                            adapter = new ViewEmployeeAdapter(MainActivity.this, response.getGetData());
                                            recyclerview.setAdapter(adapter);
                                            text_norecords.setVisibility(View.GONE);
                                            recyclerview.setVisibility(View.VISIBLE);
                                        } else {
                                            recyclerview.setVisibility(View.GONE);
                                            text_norecords.setVisibility(View.VISIBLE);
                                        }

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                con.dismissProgressDialogue();
                            }
                        })
        );
    }


}
