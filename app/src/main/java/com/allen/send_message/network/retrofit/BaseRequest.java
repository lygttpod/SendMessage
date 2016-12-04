package com.allen.send_message.network.retrofit;

import com.allen.send_message.MyApplication;
import com.allen.send_message.network.api.ApiAddress;
import com.allen.send_message.utils.NetUtils;
import com.allen.send_message.utils.ToastUtils;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by allen on 2016/7/6.
 *
 * 基础的网络请求类
 * 如果需要在请求前后展示Loading框，请使用SimpleRequest类
 * 示例代码：
 *
 *  new Request<SimpleResult>() {
 *
 *       @Override
 *       public void beforeRequest() {
 *           //发送请求前的操作
 *       }
 *
 *       @Override
 *       public Observable<SimpleResult> doBackground() {
 *           //后台请求
 *           addParam("phone", phone);
 *           addParam("authMethod", authMethod);
 *           addParam("loginPwd", md5Psw);
 *           return createApi(AccountApi.class).login(getParams());
 *       }
 *
 *       @Override
 *       public void onResponse(SimpleResult userBean) {
 *           //接收到请求结果后的操作
 *       }
 *
 *       @Override
 *       public void onFailure(Throwable e) {
 *           //请求错误后的处理
 *       }
 *
 *   }.send();
 */
public abstract class BaseRequest<T> {

    protected ParamUtils mapUtils = new ParamUtils();

    public BaseRequest<T> addParam(String key, Object value) {
        mapUtils.addParams(key, value);
        return this;
    }

    public Map getParams() {
        return mapUtils.getParams();
    }

    public void clearParams() {
        mapUtils.clearParams();
    }

    public <K> K createApi(final Class<K> cls) {
        return RetrofitUtils.getInstance().getRetrofit().create(cls);
    }
    public <K> K createApi1(final Class<K> cls) {
        return RetrofitUtils.getInstance().getRetrofit(ApiAddress.BaseImgUrl).create(cls);
    }

    /**
     * 发送请求前的操作，在UI线程执行
     */
    protected abstract void beforeRequest();

    /**
     * 后台发送请求的操作，在IO线程执行
     * @return
     */
    protected abstract Observable<T> doBackground();

    /**
     * 成功接收到请求后的操作，在UI线程执行
     * @param result
     */
    protected abstract void onResponse(T result);

    /**
     * 请求失败后的操作，在主线程执行
     * @param e
     */
    protected abstract void onFailure(Throwable e);

    /**
     * 成功接收到请求后的操作，在onResponse()之后执行
     */
    protected void onComplete() {}

    /**
     * 检测不到网络的回调
     */
    protected void onNoNetwork() {
        ToastUtils.showGlobalToast("暂无网络");
    }

    /**
     * 发送请求
     */
    public void send() {
        if (!NetUtils.isConnected(MyApplication.getAppContext())) {
            onNoNetwork();
            return;
        }
        Subscriber<T> subscriber = new BaseSubscriber<T>(){
            @Override
            public void doOnCompleted() {
                onComplete();
            }

            @Override
            public void doOnNext(T t) {
                onResponse(t);
            }

            @Override
            public void doOnError(Throwable e) {
                onFailure(e);
            }

        };
        Observable<T> observable = doBackground();
        observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //网络请求开始前的初始化工作  可以再次弹出loading提示
                        beforeRequest();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

}
