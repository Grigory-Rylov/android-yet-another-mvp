package com.github.mvpstatelibexample.mvp.presenters.first;

import com.github.mvpstatelib.framework.state.PresenterState;
import com.github.mvpstatelib.state.annotations.SubscribeState;
import com.github.mvpstatelibexample.mvp.models.first.FirstModel;
import com.github.mvpstatelibexample.mvp.state.first.FirstPresenterStateModel.RequestState;
import com.github.mvpstatelibexample.mvp.state.first.FirstPresenterStateModel.ResponseState;
import com.github.mvpstatelibexample.mvp.state.first.FirstViewStateModel.ProgressState;
import com.github.mvpstatelibexample.mvp.state.first.FirstViewStateModel.SuccessState;
import com.github.mvpstatelib.framework.presenter.BaseMvpPresenter;
import com.github.mvpstatelib.framework.state.MvpState;

/**
 * Created by grishberg on 23.01.17.
 */
public class FirstScreenPresenter extends BaseMvpPresenter {
    private FirstModel model;

    public FirstScreenPresenter() {
        model = new FirstModel();
    }

    /**
     * События от модели и вью
     *
     * @param state событие
     */
    @Override
    protected void onStateUpdated(final PresenterState state) {
        GeneratedFirstScreenPresenterSubscriber.processState(this, state);
    }

    @SubscribeState
    void processRequest(RequestState state) {
        updateViewState(new ProgressState(true));
        model.getData(this);
    }

    /**
     * Process response form model
     *
     * @param state - response state
     */
    @SubscribeState
    void processResponse(final ResponseState state) {
        updateViewState(new ProgressState(false));
        updateViewState(new SuccessState(state.getTitle(),
                state.getDescription(),
                state.getCount()));
    }
}
