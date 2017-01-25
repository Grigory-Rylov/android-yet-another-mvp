package com.grishberg.mviexample.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grishberg.mviexample.R;
import com.grishberg.mviexample.mvp.presenters.SecondFragmentPresenter;
import com.grishberg.mviexample.mvp.state.view.SecondFragmentViewState;
import com.grishberg.mvpstatelibrary.framework.ui.BaseMvpFragment;

public class SecondFragment extends BaseMvpFragment<SecondFragmentPresenter, SecondFragmentViewState> {
    private static final String ARG_POSITION = "argPosition";

    private int position;
    private TextView textView;

    private FragmentInteractionListener listener;

    public SecondFragment() {
        // Required empty public constructor
    }

    public static SecondFragment newInstance(final int position) {
        final SecondFragment fragment = new SecondFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View inflate = inflater.inflate(R.layout.fragment_second, container, false);
        textView = (TextView) inflate.findViewById(R.id.fragment_second_text);
        textView.setText(String.format("%d", position));
        return inflate;
    }

    @Override
    protected SecondFragmentPresenter createPresenter() {
        return new SecondFragmentPresenter();
    }

    @Override
    public void onModelUpdated(SecondFragmentViewState viewStateModel) {

    }

    public void onButtonPressed() {
        if (listener != null) {
            listener.onFragmentAction();
        }
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            listener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
