package com.aironbruce.registroscep.otherclasses;

import android.content.Context;
import android.util.AttributeSet;

public class ImageViewDynamic extends androidx.appcompat.widget.AppCompatImageView {

    public ImageViewDynamic(Context context, AttributeSet attrs) {super(context, attrs);}

    //Deixar a altura da imagem igual à largura da tela
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(widthMeasureSpec));//Deixar a altura da imagem
        // igual à largura da tela
    }
}
