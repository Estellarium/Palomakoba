package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatButton;
import java.util.Random;

public class BotaoJogo extends AppCompatButton {

    private boolean ligado = false;
    private BotaoJogo[] vizinhos = {null, null, null, null};

    //Constructors
    public BotaoJogo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);}
    public BotaoJogo(Context context, AttributeSet attrs) {super(context, attrs);}
    public BotaoJogo(Context context) {super(context);}
    //
    public boolean getOn() {return ligado;}

    public void randomize() {
        Random r = new Random();
        int x = r.nextInt();
        if ((x%2 == 1)||(x%3 == 0)) trocaCor(false);
    }

    public void addNeigh(BotaoJogo[] btn) {
        System.arraycopy(btn, 0, vizinhos, 0, 4);
    }

    public void trocaCor(boolean apertado) {
        if (!ligado) {this.setBackgroundColor(Color.YELLOW); ligado = true;}
        else {this.setBackgroundColor(Color.BLUE); ligado = false;}

        if (apertado)
            for (BotaoJogo x : vizinhos)
                if (x!=null) x.trocaCor(false);
    }
}

