package com.rd.hnlf.module.pure.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rd.basic.BaseLazyLoadFragment;
import com.rd.hnlf.R;
import com.rd.hnlf.common.BundleKeys;
import com.rd.hnlf.databinding.CommonRecyclerViewWithButtonBinding;
import com.rd.hnlf.module.pure.ui.activity.PureOrderAct;
import com.rd.hnlf.module.pure.viewControl.PureListCtrl;

/**
 * Author: TinhoXu
 * E-mail: xth@erongdu.com
 * Date: 2017/8/24 16:00
 * <p/>
 * Description: 纯票订单列表
 * 会员用户 - 纯票订单{@link PureOrderAct}
 */
public class PureListFrag extends BaseLazyLoadFragment {
    private PureListCtrl viewCtrl;
    /** 监听刷新动作 */
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            viewCtrl.getListener().refresh();
        }
    };

    public static PureListFrag newInstance(String type) {
        return newInstance(type, 0);
    }

    public static PureListFrag newInstance(String type, int title) {
        PureListFrag fragment = new PureListFrag();
        Bundle       args     = new Bundle();
        args.putString(BundleKeys.TYPE, type);
        args.putInt(BundleKeys.TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CommonRecyclerViewWithButtonBinding binding = DataBindingUtil.inflate(inflater, R.layout.common_recycler_view_with_button, container, false);
        viewCtrl = new PureListCtrl(getArguments().getString(BundleKeys.TYPE));
        binding.setViewCtrl(viewCtrl);

        int title = getArguments().getInt(BundleKeys.TITLE);
        if (title != 0) {
            binding.toolBar.setTitle(title);
        } else {
            binding.toolBar.setTitleBarVisibility(false);
        }
        getActivity().registerReceiver(receiver, new IntentFilter(BundleKeys.REFRESH_LIST));
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void fetchData() {
        viewCtrl.reqData();
    }
}