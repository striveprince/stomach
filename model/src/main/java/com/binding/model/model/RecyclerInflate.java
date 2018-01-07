package com.binding.model.model;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ViewDataBinding;
import android.support.annotation.CallSuper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.binding.model.adapter.AdapterType;
import com.binding.model.adapter.IEventAdapter;
import com.binding.model.adapter.IModelAdapter;
import com.binding.model.model.inter.Event;
import com.binding.model.model.inter.HttpObservable;
import com.binding.model.model.inter.HttpObservableRefresh;
import com.binding.model.model.inter.Inflate;
import com.binding.model.util.BaseUtil;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by arvin on 2018/1/6.
 */

public class RecyclerInflate<Binding extends ViewDataBinding, E extends Inflate,Adapter extends IModelAdapter<E>> extends RecyclerView.OnScrollListener implements Inflate<Binding>, Event {
    private final ViewInflate<Binding> inflate = new ViewInflate<>(BaseUtil.findModelView(getClass()));
    private final Adapter adapter;
    private final boolean page;
    private int lastVisibleItem;
    private int dy;

    public final ObservableBoolean empty = new ObservableBoolean(true);
    public final ObservableBoolean enabled = new ObservableBoolean(true);
    public final ObservableBoolean loading = new ObservableBoolean(false);
    public final ObservableField<String> error = new ObservableField<>("暂无数据");
    public final ObservableField<RecyclerView.LayoutManager> layoutManager = new ObservableField<>();
    private HttpObservable<List<E>> rcHttp;
    private int offset = 0;
    private boolean pagerFlag = true;
    private int pageCount = 16;
    private Disposable disposable;


    public RecyclerInflate(Adapter adapter, boolean page) {
        this.adapter = adapter;
        this.page = page;
    }

    public RecyclerInflate(Adapter adapter) {
        this(adapter, false);
    }

    @Override
    public final ModelView getModelView() {
        return inflate.getModelView();
    }

    @Override
    public final Binding attachView(Context context, ViewGroup co, boolean attachToParent, Binding set) {
        setLayoutManager(new LinearLayoutManager(context));
        return inflate.attachView(context, co, attachToParent, set);
    }

    @Override
    public final void setModelIndex(int modelIndex) {
        inflate.setModelIndex(modelIndex);
    }

    @Override
    public final int getModelIndex() {
        return inflate.getModelIndex();
    }

    @Override
    public final int getLayoutId() {
        return inflate.getLayoutId();
    }

    @Override
    public final void removeBinding() {
        inflate.removeBinding();
    }

    @Override
    public final void setIEventAdapter(IEventAdapter iEventAdapter) {
        inflate.setIEventAdapter(iEventAdapter);
    }

    @Override
    public final IEventAdapter getIEventAdapter() {
        return inflate.getIEventAdapter();
    }

    @Override
    public final ViewDataBinding getDataBinding() {
        return inflate.getDataBinding();
    }

    @Override
    public final void registerEvent() {
        for (int eventId : getModelView().event()) {
            eventSet.put(eventId, this);
        }
    }

    @Override
    public final void unRegisterEvent() {
        for (int eventId : getModelView().event()) {
            eventSet.remove(eventId);
        }
    }

    public final int event(int eventId, View view, Object... args) {
        return Event.event(eventId, this, view, args);
    }


    public final Disposable getDisposable() {
        return disposable;
    }

    public final void setPagerFlag(boolean pagerFlag) {
        this.pagerFlag = pagerFlag;
    }

    public final Adapter getAdapter() {
        return adapter;
    }

    public final void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager.set(layoutManager);
    }

    public final void setRcHttp(HttpObservableRefresh<List<E>> rcHttp1){
        setRoHttp((offset1, refresh) -> rcHttp1.http(offset1,refresh>0));
    }

    public final void setRoHttp(HttpObservable<List<E>> rcHttp) {
        this.rcHttp = rcHttp;
        onHttp(offset, 0);
    }

    public final void onRefresh() {
        onHttp(0, 1);
    }

    public final void onHttp(int offset, int state) {
        this.offset = offset;
        int p = page ? offset / pageCount + 1 : offset;
        if (rcHttp != null)
            rcHttp.http(p, state).subscribe(this::accept, this::onThrowable, this::onComplete,this::onSubscribe);
    }

    private void onSubscribe(Disposable disposable) {
        this.disposable = disposable;
        loading.set(true);
    }

    private void accept(List<E> list) throws Exception {
        int p = page ? offset / pageCount * pageCount : offset;
        adapter.setList(p, list, AdapterType.refresh);
    }

    private void onThrowable(Throwable throwable) {
        BaseUtil.toast(throwable);
        error.set(throwable.getMessage());
        onComplete();
    }

    @CallSuper
    public void onComplete() {
        loading.set(false);
        empty.set(adapter.size()== 0);
    }

    @Override
    public int onEvent(View view, Event event, Object... args) {
        return 0;
    }

    @Override
    public final void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (!pagerFlag || loading.get() || newState != RecyclerView.SCROLL_STATE_IDLE || dy < 0) return;
        if (lastVisibleItem + 1 >= getAdapter().size()) onHttp(getAdapter().size(), 2);
    }

    @Override
    public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        this.dy = dy;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        }
    }

    public void onErrorClick(View view){
        onHttp(offset,3);
    }


    public void setEventAdapter(IEventAdapter<E> iEventAdapter) {
        getAdapter().setIEventAdapter(iEventAdapter);
    }
}
