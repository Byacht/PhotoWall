package com.byacht.retrofitdemo;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.byacht.retrofitdemo.activity.ZhihuDailyActivity;
import com.byacht.retrofitdemo.adapter.MeiZhiAdapter;
import com.byacht.retrofitdemo.api.MeiZhiApi;
import com.byacht.retrofitdemo.entity.MeiZhi;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private Gson mGson = new Gson();
    private MeiZhiAdapter mAdapter;
    private int mPage = 1;
    private ArrayList<MeiZhi> mMeiZhis;
    private ArrayList<Integer> mPhotoHeight;

    @BindView(R.id.rv_meizhi)
    RecyclerView meizhiRv;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mMeiZhis = new ArrayList<MeiZhi>();
        mPhotoHeight = new ArrayList<Integer>();

        mAdapter = new MeiZhiAdapter(MainActivity.this, mMeiZhis, mPhotoHeight);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        meizhiRv.setLayoutManager(layoutManager);
        meizhiRv.setAdapter(mAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gank.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        final MeiZhiApi meiZhiApi = retrofit.create(MeiZhiApi.class);
        updateMeiZhiRv(meiZhiApi);

        meizhiRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isSlideToBottom(recyclerView)){
                    refreshLayout.setRefreshing(true);
                    mPage++;
                    updateMeiZhiRv(meiZhiApi);
                }
                return;
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                mMeiZhis.clear();
                mPhotoHeight.clear();
                mAdapter.notifyDataSetChanged();
                updateMeiZhiRv(meiZhiApi);
            }
        });

//        meiZhiApi.getMeiZhiData(2)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<MeiZhiData>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(MeiZhiData meiZhiData) {
//                        for (MeiZhi meiZhi : meiZhiData.getResults()) {
//                            Log.d("out", meiZhi.getUrl());
//                        }
//                        Log.d("out", "111");
//                        mAdapter = new MeiZhiAdapter(MainActivity.this, meiZhiData.getResults());
//                        Log.d("out", "222");
//                        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//                        meizhiRv.setLayoutManager(layoutManager);
//                        meizhiRv.setAdapter(mAdapter);
//                    }
//                });

    }

    private void updateMeiZhiRv(final MeiZhiApi meiZhiApi) {
        Call<MeiZhiData> call = meiZhiApi.getMeiZhi(mPage);
        call.enqueue(new Callback<MeiZhiData>() {
            @Override
            public void onResponse(Call<MeiZhiData> call, Response<MeiZhiData> response) {
                Log.d("out", mGson.toJson(response.body()));
                MeiZhiData meiZhiData = response.body();
                mMeiZhis.addAll(meiZhiData.getResults());
                for (int i = 0; i < meiZhiData.getResults().size(); i++){
                    mPhotoHeight.add((int) (350 + Math.random() * 300));
                }
                mAdapter.notifyItemRangeChanged((mPage - 1) * 10, 10);
                stopRefreshAnimation();

            }

            @Override
            public void onFailure(Call<MeiZhiData> call, Throwable t) {
                stopRefreshAnimation();
                Snackbar.make(meizhiRv, "加载失败，请检查网络状况", Snackbar.LENGTH_LONG)
                        .setAction("重试", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateMeiZhiRv(meiZhiApi);
                            }
                        })
                        .show();
            }
        });

    }

    private void stopRefreshAnimation() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    private boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_zhihu:
                Intent intent = new Intent(this, ZhihuDailyActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
