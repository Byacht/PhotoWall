package com.byacht.retrofitdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

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
                    mPage++;
                    updateMeiZhiRv(meiZhiApi);
                }
                return;
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

    private void updateMeiZhiRv(MeiZhiApi meiZhiApi) {
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
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MeiZhiData> call, Throwable t) {

            }
        });
    }

    private boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
}
