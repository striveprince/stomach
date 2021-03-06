package binding.com.demo.base.binding;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.binding.model.util.BaseUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;


/**
 * project：cutv_ningbo
 * description：
 * create developer： admin
 * create time：9:51
 * modify developer：  admin
 * modify time：9:51
 * modify remark：
 *
 * @version 2.0
 */


public class DataBindingAdapter {

    @BindingAdapter({"android:background"})
    public static void setBackground(View view, String imageUrl) {
        Context mContext = view.getContext();
        Glide.with(mContext)
                .load(imageUrl)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) view.setBackground(resource);
                        else view.setBackgroundDrawable(resource);

                    }
                });
    }

    //    --------------------------ProgressBar------------------------------
    @BindingAdapter("android:secondaryProgress")
    public static void setSecondaryProgress(ProgressBar bar, int progress) {
        bar.setSecondaryProgress(progress);
    }

    //    --------------------------ImageView------------------------
    @BindingAdapter({"android:src"})
    public static void setImageDrawable(ImageView imageView, String imageUrl) {
        Context context = imageView.getContext();
        Glide.with(context).clear(imageView);
        Glide.with(context)
                .load(imageUrl)
//                .placeholder(R.mipmap.img_default2_normal)
//                .error(R.mipmap.img_default2_normal)
                .into(imageView);
    }

    @BindingAdapter("circle")
    public static void setImageCircleDrawable(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).clear(imageView);
        RequestOptions options = new RequestOptions()
                .centerCrop()
//                .placeholder(R.mipmap.ic_launcher)//预加载图片
//                .error(R.mipmap.ic_launcher)//加载失败显示图片
                .priority(Priority.HIGH)//优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE)//缓存策略
                .transform(new CircleCrop());//转化为圆角
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    @BindingAdapter("head")
    public static void head(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Glide.with(context).clear(imageView);
        RequestOptions options = new RequestOptions()
                .centerCrop()
//                .placeholder(R.mipmap.ic_launcher)//预加载图片
//                .error(R.mipmap.ic_launcher)//加载失败显示图片
                .priority(Priority.HIGH)//优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE)//缓存策略
                .transform(new CircleCrop());//转化为圆角
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    @BindingAdapter({"android:src", "radius"})
    public static void setImageRadiusDrawable(ImageView imageView, String url, int radius) {
        Context context = imageView.getContext();
        Glide.with(context).clear(imageView);
        radius = (int) (radius * BaseUtil.getDisplayMetrics(context).density + 0.5f);
        RequestOptions options2 = new RequestOptions()
                .centerCrop()
//                .placeholder(R.mipmap.ic_launcher)//预加载图片
//                .error(R.mipmap.ic_launcher)//加载失败显示图片
                .priority(Priority.HIGH)//优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE)//缓存策略
                .transform(new RoundedCorners(radius));//转化为圆角
        Glide.with(context)
                .load(url)
                .apply(options2)
                .into(imageView);
    }


    @BindingAdapter("android:src")
    public static void setImageDrawable(ImageView view, @DrawableRes int mipmapId) {
        if (mipmapId != 0) view.setImageResource(mipmapId);
    }


    //    --------------------------TextView------------------------

    @BindingAdapter({"android:drawableTop"})
    public static void setDrawableTop(TextView view, String image) {
        Context mContext = view.getContext();
        Glide.with(mContext).load(image).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                Drawable[] drawables = view.getCompoundDrawables();
                view.setCompoundDrawables(drawables[0], drawable, drawables[2], drawables[3]);
            }
        });
    }
}
