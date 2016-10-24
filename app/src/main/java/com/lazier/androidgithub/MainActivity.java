package com.lazier.androidgithub;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lazier.githubsdk.DataManager;
import com.lazier.githubsdk.model.RepositoryForSearch;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements View.OnKeyListener,
        DataManager.SearchListener,
        NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.et_search_text)
    EditText mSearchText;

    @BindView(R.id.rv_list)
    RecyclerView mRepositoriesView;

    @BindView(R.id.ll_loading_layout)
    LinearLayout mLoadingLayout;

    @BindView(R.id.nav_drawer)
    NavigationView mNavigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.tool_bar)
    Toolbar mToolBar;

    private RepositoriesAdapter mRepositoriesAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private int mLastVisibleItem = 0;
    private String mLastSearchKey;
    private int mLastSearchPage;
    private boolean mEnd;
    private boolean mSearching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initActionBar();
        initView();
    }

    private void initView() {
        mSearchText.setOnKeyListener(this);

        mRepositoriesAdapter = new RepositoriesAdapter(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRepositoriesView.setLayoutManager(mLinearLayoutManager);
        mRepositoriesView.setAdapter(mRepositoriesAdapter);
        mRepositoriesView.setOnScrollListener(new ScrollListener());

        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void initActionBar() {
        setSupportActionBar(mToolBar);
        ActionBar bar = getSupportActionBar();
        if (bar == null) {
            return;
        }
        bar.setHomeButtonEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.ic_menu_white);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchKeyBoard() {
        Editable editable = mSearchText.getText();
        if (editable == null || TextUtils.isEmpty(editable.toString())) {
            return;
        }

        mRepositoriesAdapter.clear();
        mRepositoriesAdapter.notifyDataSetChanged();

        mLastSearchKey = editable.toString();
        search(mLastSearchKey, 1);
    }

    private void search(String key, int page) {
        mSearching = true;
        DataManager.getIns().search(key, page, this);
        showLoadingProgress();
    }

    private void showLoadingProgress() {
        mLoadingLayout.setVisibility(View.VISIBLE);
    }

    private void dismissLoadingProgress() {
        mLoadingLayout.setVisibility(View.GONE);
    }

    private void hideKeyBoard() {
        InputMethodManager inputMethodManager = ((InputMethodManager)
                getSystemService(INPUT_METHOD_SERVICE));
        View view = getCurrentFocus();
        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private class ScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView view, int scrollState) {
            if (mEnd) {
                return ;
            }

            if (scrollState == RecyclerView.SCROLL_STATE_IDLE
                    && mLastVisibleItem + 1 == mRepositoriesAdapter.getItemCount()
                    && !mSearching) {
                search(mLastSearchKey, ++mLastSearchPage);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            mLastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
        }

    }

    @Override
    public void onResponse(List<RepositoryForSearch> result, boolean end) {
        mSearching = false;
        mEnd = end;
        if (result == null || result.size() == 0) {
            return;
        }

        mRepositoriesAdapter.addItems(result);
        mRepositoriesAdapter.notifyItemRangeInserted(
                mRepositoriesAdapter.getItemCount() - result.size(),
                result.size());
        dismissLoadingProgress();
    }

    @Override
    public void onError(Throwable t) {
        mSearching = false;
        t.printStackTrace();
        dismissLoadingProgress();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            hideKeyBoard();
            searchKeyBoard();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, "正在开发中~", Toast.LENGTH_SHORT).show();
        item.setChecked(true);
        mDrawerLayout.closeDrawers();
        return false;
    }
}
