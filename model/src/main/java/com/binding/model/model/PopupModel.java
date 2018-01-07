package com.binding.model.model;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.binding.model.App;
import com.binding.model.cycle.Container;

import io.reactivex.functions.Consumer;

/**
 * Created by arvin on 2017/11/19.
 */

public class PopupModel<T extends Container, Binding extends ViewDataBinding>  extends ViewModel<T,Binding> {
    private final PopupWindow window = new PopupWindow();

    @Override
    public void attachView(Bundle savedInstanceState, T t) {
        super.attachView(savedInstanceState, t);
        window.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setContentView(getDataBinding().getRoot());
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setOutsideTouchable(true);
        window.setTouchable(true);
    }

    public PopupWindow getWindow() {
        return window;
    }


    public void show(Consumer<PopupWindow> consumer){
        if(window.isShowing()){
            window.dismiss();
        }else{
            try{
                getDataBinding().setVariable(App.vm,this);
                consumer.accept(window);
            }catch (Throwable e){
                e.printStackTrace();
            }
        }

    }
}
