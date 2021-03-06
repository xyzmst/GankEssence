package com.huchiwei.gankessence.commonui.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.huchiwei.gankessence.R;

/**
 * Fragment基础封装类，实现懒加载模式
 *
 * @author huchiwei
 * @version 1.0.0
 */
public abstract class BaseFragment extends Fragment {
    private View mRootView;
    private AppCompatActivity mAppCompatActivity;
    private Toolbar mToolbar;

    private boolean isDataFetched = false;        // 数据加载是否已执行
    private boolean isViewInitiated = false;      // 视图是否已初始化

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(null == mRootView)
            mRootView = inflater.inflate(getFragmentLayoutId(), container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAppCompatActivity = (AppCompatActivity) getActivity();
        onAfterViewCreated(view, savedInstanceState);

        this.isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        prepareFetchData();
    }

    // ======================================================
    // 设置Toolbar ===========================================
    public void setToolBar(View view, int toolBarId){
        mToolbar = (Toolbar) view.findViewById(toolBarId);
        if(null != mToolbar && null != mAppCompatActivity){
            mAppCompatActivity.setSupportActionBar(mToolbar);

            ActionBar actionBar = mAppCompatActivity.getSupportActionBar();
            if(null !=  actionBar){
                // 获取并设置标题, 0表示不设置标题
                int titleResourceId = getToolbarTitle();
                if(titleResourceId > 0){
                    actionBar.setTitle(titleResourceId);
                } else {
                    actionBar.setTitle(null);
                }
            }
            //mAppCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

            // 设置透明状态栏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mAppCompatActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }

    // ======================================================
    // 加载数据 ===============================================
    private void prepareFetchData(){
        if(!isDataFetched && getUserVisibleHint() && isViewInitiated){
            isDataFetched = true;

            fetchData();
        }
    }

    protected void setIsDataFetched(boolean isDataFetched){
        this.isDataFetched = isDataFetched;
    }

    public void fadeOut(){
        if (mRootView != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            mRootView.startAnimation(fadeOut);
        }
    }

    public void fadeIn(){
        if (mRootView != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            mRootView.startAnimation(fadeOut);
        }
    }

    // ==================================================================
    // 重载方法 ==========================================================
    protected int getToolbarTitle(){
        return R.string.app_name;
    }

    /**
     * 获取fragment的View layoutId
     * @return layout id
     */
    protected abstract int getFragmentLayoutId();

    /**
     * 实现数据加载方法
     */
    protected abstract void fetchData();

    /**
     * view创建后方法调用,view初始化方法请放在此方法内
     */
    protected abstract void onAfterViewCreated(View view, @Nullable Bundle savedInstanceState);
}
