package com.example.hirensamtani.pizzafeedback.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.example.hirensamtani.pizzafeedback.R;
import com.squareup.picasso.Picasso;

/**
 * Created by HirenS on 24-06-2016.
 */
public class ScreenUtils {
    public float dp_to_pixels(Context context,float dps){
        float ret_val = 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        ret_val = (int) (dps * scale + 0.5f);

        return ret_val;
    }
    public void loadImage(Context context, int resID, View imgView){
        Picasso
                .with(context)
                .load(resID)
                .fit()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.img_not_available)
                .into((ImageView) imgView);

        //imgView.setBackgroundResource(resID);

    }
}
