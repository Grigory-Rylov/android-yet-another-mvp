package com.github.mvpstatelib.framework.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.mvpstatelib.framework.MvpDelegate;
import com.github.mvpstatelib.framework.lifecycle.LifeCycleObservable;
import com.github.mvpstatelib.framework.lifecycle.LifeCycleObserver;
import com.github.mvpstatelib.framework.presenter.BaseMvpPresenter;

/**
 * Created by grishberg on 26.01.17.
 * Mvp helper for custom views
 */
@SuppressWarnings("unchecked")
public class MvpHelper<P extends BaseMvpPresenter> implements LifeCycleObserver {
    private static final String TAG = MvpHelper.class.getSimpleName();

    @Nullable
    private LifeCycleObservable parent;
    private P presenter;
    private final DelegateTagHolder tagHolder;
    private String delegateTag;

    public MvpHelper(@NonNull DelegateTagHolder tagHolder) {
        this.tagHolder = tagHolder;
        presenter = (P) MvpDelegate.getPresenter(getDelegateTag());
    }

    public void setPresenter(final P presenter) {
        Log.d(TAG, "setPresenter: presenter created ");
        this.presenter = presenter;
        MvpDelegate.putPresenter(getDelegateTag(), presenter);
    }

    public void registerNestedView(@NonNull final LifeCycleObservable parent, Bundle savedInstanceState) {
        this.parent = parent;
        parent.registerObserver(this);

        presenter.restoreState(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle state) {
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        presenter.subscribe(tagHolder);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        presenter.unSubscribe(tagHolder);
        if (parent != null && parent.isFinishing()) {
            Log.d(TAG, "onPause: is finishing");
            parent.unRegisterObserver(this);
            MvpDelegate.removePresenter(getDelegateTag());
        }
        parent = null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        if (parent.isFinishing()) {
            Log.d(TAG, "onDestroy: is finishing");
            parent.unRegisterObserver(this);
            MvpDelegate.removePresenter(getDelegateTag());
        }
        parent = null;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        presenter.saveInstanceState(outState);
    }

    public void onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow: ");
        if (parent != null) {
            parent.registerObserver(this);
        }
        presenter.subscribe(tagHolder);
    }

    public void onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow: ");
        if (parent != null) {
            parent.unRegisterObserver(this);
        }
        presenter.unSubscribe(tagHolder);
    }

    public P getPresenter() {
        return presenter;
    }

    private String getDelegateTag() {
        if (delegateTag == null) {
            final StringBuilder sb = new StringBuilder(tagHolder.getClass().getName());
            sb.append(":");
            sb.append(tagHolder.getId());
            delegateTag = sb.toString();
        }

        return delegateTag;
    }
}
