package com.quarterlife.detectuserinactivitytest1.Fragment;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    private static final String TAG = BaseFragment.class.getSimpleName();
    private boolean isFragmentVisible;
    private boolean isReuseView;
    private boolean isFirstVisible;
    private View rootView;

    /*  setUserVisibleHint() 在 Fragment 創建時會先被調用一次，傳入isVisibleToUser = false
        如果當前 Fragment 可見，那麼 setUserVisibleHint() 會再次被調用一次，傳入 isVisibleToUser = true
        如果 Fragment 從可見 -> 不可見，那麼 setUserVisibleHint() 也會被調用，傳入 isVisibleToUser = false
        總結：setUserVisibleHint() 除了 Fragment 的可見狀態發生變化時會被回調外，在 new Fragment() 時也會被回調
        如果我們需要在 Fragment 可見與不可見時做點事，用這個的話就會有多餘的回調了，那麼就需要重新封裝一個  */

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // setUserVisibleHint() 有可能在 fragment 的生命周期外被调用
        if (rootView == null) {
            return;
        }
        if (isFirstVisible && isVisibleToUser) {
            onFragmentFirstVisible();
            isFirstVisible = false;
        }
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            isFragmentVisible = false;
            onFragmentVisibleChange(false);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        /*  如果 setUserVisibleHint() 在 rootView 創建前調用時，那麼
            就等到 rootView 創建完後才回調 onFragmentVisibleChange(true)
            保證 onFragmentVisibleChange() 的回調發生在 rootView 創建完成之後，以便支持 UI 操作  */
        if (rootView == null) {
            rootView = view;
            if (getUserVisibleHint()) {
                if (isFirstVisible) {
                    onFragmentFirstVisible();
                    isFirstVisible = false;
                }
                onFragmentVisibleChange(true);
                isFragmentVisible = true;
            }
        }
        super.onViewCreated(isReuseView ? rootView : view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        initVariable();
        super.onDestroy();
    }

    private void initVariable() {
        isFirstVisible = true;
        isFragmentVisible = false;
        rootView = null;
        isReuseView = true;
    }

    /*  設置是否使用 view 的複用，默認開啟
        view 的複用是指，ViewPager 在銷毀和重建 Fragment 時會不斷調用 onCreateView() -> onDestroyView()
        之間的生命函數，這樣可能會出現重複創建 view 的情況，導致界面上顯示多個相同的 Fragment
        view 的複用其實就是指保存第一次創建的 view，後面再 onCreateView() 時直接返回第一次創建的 view
        @param isReuse */

    protected void reuseView(boolean isReuse) {
        isReuseView = isReuse;
    }

    /*  去除 setUserVisibleHint() 多餘的回調場景，保證只有當 fragment 可見狀態發生變化時才回調
        回調時機在 view 創建完後，所以支持 UI 操作，解決在 setUserVisibleHint() 裡進行 UI 操作有可能報 null 異常的問題
        可在該回調方法裡進行一些 UI 顯示與隱藏，比如加載框的顯示和隱藏
        @param isVisible true 不可見 -> 可見
        false 可見 -> 不可見  */

    protected void onFragmentVisibleChange(boolean isVisible) {
    }

    /*  在 fragment 首次可見時回調，可在這裡進行加載數據，保證只在第一次打開 fragment 時才會加載數據，
        這樣就可以防止每次進入都重複加載數據。
        該方法會在 onFragmentVisibleChange() 之前調用，所以第一次打開時，可以用一個全局變量表示數據下載狀態，
        然後在該方法內將狀態設置為下載狀態，接著去執行下載的任務
        最後在 onFragmentVisibleChange() 裡根據數據下載狀態來控制下載進度 UI 控件的顯示與隱藏  */

    protected void onFragmentFirstVisible() {
    }

    protected boolean isFragmentVisible() {
        return isFragmentVisible;
    }
}
