package com.image.characterrecognition.module;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.NinePatch;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.image.characterrecognition.apiservice.BaiduOCRService;
import com.image.characterrecognition.bean.AccessTokenBean;
import com.image.characterrecognition.bean.RecognitionResultBean;
import com.image.characterrecognition.utils.Base64Util;
import com.image.characterrecognition.utils.FileUtil;
import com.image.characterrecognition.utils.HttpUtil;
import com.image.characterrecognition.utils.RegexUtils;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.schedulers.IoScheduler;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private BaiduOCRService baiduOCRService;

    private static final String CLIENT_CREDENTIALS = "client_credentials";
    private static final String API_KEY = "zTiDZcZvpWz4COsMChRGeOGB";
    private static final String SECRET_KEY = "99XXcQXMQDboyrtmGSVzbF9Ml3sN7c2V";
    private String ACCESS_TOKEN = "";
    private String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";

    public MainPresenter(MainContract.View mView) {

        this.mView = mView;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://aip.baidubce.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        baiduOCRService = retrofit.create(BaiduOCRService.class);

    }

    public String getToken() {
        return ACCESS_TOKEN;
    }

    @Override
    public void getAccessToken() {
        baiduOCRService.getAccessToken(CLIENT_CREDENTIALS, API_KEY, SECRET_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AccessTokenBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AccessTokenBean accessTokenBean) {
                        Log.e("Access token", accessTokenBean.getAccess_token());
                        ACCESS_TOKEN = accessTokenBean.getAccess_token();
                        if (mView != null) {
                            mView.getTokenSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) {
                            mView.errorMessage(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    @SuppressLint("CheckResult")
    @Override
    public void getRecognitionResultByImage(final String path) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
//            // 本地文件路径
                    byte[] imgData = FileUtil.readFileByBytes(path);
                    String imgStr = Base64Util.encode(imgData);
                    String imgParam = URLEncoder.encode(imgStr, "UTF-8");
                    String param = "image=" + imgParam;
                    // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
                    String accessToken = ACCESS_TOKEN;
                    String result = HttpUtil.post(url, accessToken, param);
                    RecognitionResultBean recognitionResultBean = new Gson().fromJson(result, RecognitionResultBean.class);
                    ArrayList<String> wordList = new ArrayList<>();
                    List<RecognitionResultBean.WordsResultBean> wordsResult = recognitionResultBean.getWords_result();
                    for (RecognitionResultBean.WordsResultBean words : wordsResult) {
                        wordList.add(words.getWords());
                    }
                    StringBuilder s = new StringBuilder();
                    for (String numb : wordList) {
                        s.append(numb + "\n");
                    }

                    emitter.onNext(s.toString());
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if (mView != null) {
                    mView.updateUI(s);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (mView != null) {
                    mView.errorMessage(throwable.getMessage());
                }
            }
        });


    }

    @Override
    public void clear() {
        mView = null;
    }

}
