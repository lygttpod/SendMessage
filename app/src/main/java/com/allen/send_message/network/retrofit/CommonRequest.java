package com.allen.send_message.network.retrofit;


import com.allen.send_message.base.ILoadingView;

/**
 * Created by Allen on 2016/7/7.
 *
 * 加入loading框处理的请求类
 */
public abstract class CommonRequest<T> extends BaseRequest<T>{

    /**
     * 是否显示loading  默认显示
     */
    private boolean mIsShowLoading = true;

    private ILoadingView mLoadingView;

    public CommonRequest(ILoadingView view) {
        this.mLoadingView = view;
    }

    /**
     * 请求失败后的操作，在主线程执行
     * @param e
     */
    protected abstract void onError(Throwable e);

    /**
     * 成功接收到请求后的操作，在onResponse()之后执行
     */
    protected void onFinish() {}

    @Override
    protected void beforeRequest() {
        if (mIsShowLoading){
            showLoading();
        }
    }

    @Override
    protected void onFailure(Throwable e) {
        if (mIsShowLoading){
            dismissLoading();
        }
        onError(e);
    }

    @Override
    protected void onComplete() {
        if (mIsShowLoading){
            dismissLoading();
        }
        onFinish();
    }

    public CommonRequest<T> isShowLoading(boolean isShowLoading){
        this.mIsShowLoading = isShowLoading;
        return this;
    }

    /**
     * 显示loading框
     */
    private void showLoading() {
        if (mLoadingView != null)
            mLoadingView.showLoading();
    }

    /**
     * 取消loading框
     */
    private void dismissLoading() {
        if (mLoadingView != null)
            mLoadingView.dismissLoading();
    }

}
