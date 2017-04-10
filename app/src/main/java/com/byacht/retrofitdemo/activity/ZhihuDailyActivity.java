package com.byacht.retrofitdemo.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.byacht.retrofitdemo.R;
import com.byacht.retrofitdemo.ZhihuDailyData;
import com.byacht.retrofitdemo.adapter.ZhihuAdapter;
import com.byacht.retrofitdemo.api.ZhihuApi;
import com.byacht.retrofitdemo.entity.ZhihuDaily;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ZhihuDailyActivity extends AppCompatActivity {

    @BindView(R.id.rv_zhihu)
    RecyclerView zhihuRv;

    private ArrayList<ZhihuDaily> mZhihuDailies;
    private ZhihuAdapter mZhihuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu_daily);
        ButterKnife.bind(this);

        mZhihuDailies = new ArrayList<ZhihuDaily>();
        mZhihuAdapter = new ZhihuAdapter(this, mZhihuDailies);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        zhihuRv.setLayoutManager(layoutManager);
        zhihuRv.setAdapter(mZhihuAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://news-at.zhihu.com")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final ZhihuApi zhihuApi = retrofit.create(ZhihuApi.class);
        zhihuApi.getZhihuData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ZhihuDailyData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(zhihuRv, "加载失败，请检查网络状况", Snackbar.LENGTH_LONG)
                                .setAction("重试", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                })
                                .show();
                    }

                    @Override
                    public void onNext(ZhihuDailyData zhihuDailyData) {
                        mZhihuDailies.addAll(zhihuDailyData.getStories());
                        mZhihuAdapter.notifyDataSetChanged();
                    }
                });

//        Call<ZhihuDailyData> call = zhihuApi.getZhihu();
//        call.enqueue(new Callback<ZhihuDailyData>() {
//            @Override
//            public void onResponse(Call<ZhihuDailyData> call, Response<ZhihuDailyData> response) {
//                mZhihuDailies.addAll(response.body().getStories());
//                for (ZhihuDaily zhihuDaily : response.body().getStories()) {
//                    Log.d("out", "id:" + zhihuDaily.getId() + " images:" + zhihuDaily.getImages());
//                }
//                mZhihuAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Call<ZhihuDailyData> call, Throwable t) {
//
//            }
//        });

    }
}
