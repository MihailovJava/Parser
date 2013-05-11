package com.example.arithmetic.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.arithmetic.*;


/**
 * User: Nixy
 * Date: 29.03.13
 * Time: 11:56
 */
public class main_activity extends Activity {
       TextView text;
       EditText e1;

       public static final int RANK = 8;
       public static final int WEIGHT = 4;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initUI();
    }
    private void initUI(){
        text = (TextView) findViewById(R.id.textview);

         e1 = (EditText) findViewById(R.id.editText);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Interpreter interpreter = new Interpreter();
                interpreter.parse(e1.getText().toString());

                double my_exp = FonNeiman.getInstance().getREG(19);
                double standart_exp = Math.exp(FonNeiman.getInstance().getRAM(18));
                text.setTextColor(Color.WHITE);
                text.setText(String.valueOf(my_exp)+"\n"+
                        String.valueOf(standart_exp)+"\n"+
                        String.valueOf(Math.abs(standart_exp-my_exp))
                );
                if (Exeptions.getInstance().isFlag()){
                    text.setTextColor(Color.RED);
                    text.setText(Exeptions.getInstance().getText());

                }
            }
        });
    }

}