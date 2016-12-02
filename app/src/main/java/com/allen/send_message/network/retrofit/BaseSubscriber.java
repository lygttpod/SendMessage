package com.allen.send_message.network.retrofit;

import rx.Subscriber;

/**
 * Created by hoomsun01 on 2016/6/15.
 *
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {


    private static final String TAG = "allen";

    public abstract void doOnCompleted();
    public abstract void doOnNext(T t);
    public abstract void doOnError(Throwable e);


    @Override
    public void onCompleted() {
        doOnCompleted();
    }

    @Override
    public void onError(Throwable e) {
        //此处处理被踢下线及其他情况
        doOnError(e);
    }

    @Override
    public void onNext(T t) {
        doOnNext(t);
    }
}
