package com.grishberg.mviexample.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.grishberg.mviexample.R;
import com.grishberg.mviexample.mvp.presenters.BalanceViewPresenter;
import com.grishberg.mviexample.mvp.state.balance.BalancePresenterState;
import com.grishberg.mviexample.mvp.state.balance.BalanceViewState.UpdateBalanceState;
import com.grishberg.mvpstatelibrary.framework.state.MvpState;
import com.grishberg.mvpstatelibrary.framework.view.MvpLinearLayout;

/**
 * Created by grishberg on 26.01.17.
 * Example nested view with presenter
 */
public class BalanceView extends MvpLinearLayout<BalanceViewPresenter> {
    private static final String TAG = BalanceView.class.getSimpleName();
    private TextView balanceTextView;

    public BalanceView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        View rootView = inflate(context, R.layout.view_custom_balance, this);
        balanceTextView = (TextView) rootView.findViewById(R.id.custom_box_text);

        getPresenter().updateState(new BalancePresenterState.RequestState());
    }

    @Override
    public void onModelUpdated(final MvpState state) {
        Log.d(TAG, "onModelUpdated: " + state);
        if(state instanceof UpdateBalanceState) {
            updateBalance((UpdateBalanceState) state);
        }
    }

    private void updateBalance(UpdateBalanceState state) {
        balanceTextView.setText(state.getBalance());
    }

    @Override
    protected BalanceViewPresenter providePresenter() {
        return new BalanceViewPresenter();
    }
}
