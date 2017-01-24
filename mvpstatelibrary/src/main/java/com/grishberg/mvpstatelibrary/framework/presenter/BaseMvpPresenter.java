package com.grishberg.mvpstatelibrary.framework.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.grishberg.mvpstatelibrary.framework.state.ModelWithNonSerializable;
import com.grishberg.mvpstatelibrary.framework.state.MvpState;
import com.grishberg.mvpstatelibrary.framework.state.StateReceiver;
import com.grishberg.mvpstatelibrary.framework.ui.BaseMvpActivity;
import com.grishberg.mvpstatelibrary.framework.view.BaseView;

import java.io.Serializable;

/**
 * Created by grishberg on 22.01.17.
 */
public abstract class BaseMvpPresenter<V extends BaseView<VS>, VS extends MvpState, PS extends MvpState>
        implements StateReceiver<PS> {
    private static final String VIEW_STATE_SUFFIX = ":VIEW_STATE";
    private static final String PRESENTER_STATE_SUFFIX = ":PRESENTER_STATE";
    private V view;
    private VS viewState;
    private PS presenterState;

    protected void updateViewState(VS viewState) {
        this.viewState = viewState;
        if (view != null) {
            view.updateView(viewState);
        }
    }

    public void attachView(final V view) {
        this.view = view;
        if (viewState != null) {
            if (viewState instanceof ModelWithNonSerializable && ((ModelWithNonSerializable) viewState).isNonSerializableNull()) {
                onNonSerializableEmpty(viewState);
                return;
            }
            view.updateView(viewState);
        }
    }

    public void restoreState(@Nullable Bundle savedInstanceState){
        if (viewState == null) {
            viewState = restoreViewState(savedInstanceState);
        }
        if (presenterState == null) {
            final PS restoredState = restorePresenterState(savedInstanceState);
            if (restoredState != null) {
                updateState(restoredState);
            }
        }
    }


    protected void onNonSerializableEmpty(VS viewState) {
        //To be overriden in subclass
    }

    private VS restoreViewState(@Nullable final Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return null;
        }
        return (VS) savedInstanceState.getSerializable(this.getClass().getName() + VIEW_STATE_SUFFIX);
    }

    private PS restorePresenterState(@Nullable final Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return null;
        }
        return (PS) savedInstanceState.getSerializable(this.getClass().getName() + PRESENTER_STATE_SUFFIX);
    }

    public void detachView() {
        view = null;
    }

    public void saveInstanceState(final Bundle savedInstanceState) {
        if (viewState != null) {
            savedInstanceState.putSerializable(this.getClass().getName() + VIEW_STATE_SUFFIX, viewState);
            savedInstanceState.putSerializable(this.getClass().getName() + PRESENTER_STATE_SUFFIX, presenterState);
        }
    }

    @Override
    public void updateState(final PS presenterState) {
        this.presenterState = presenterState;
        onStateUpdated(this.presenterState);
    }

    protected abstract void onStateUpdated(PS presenterState);

}