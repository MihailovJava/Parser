package com.example.arithmetic.activity;

import android.app.Activity;
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
       Button b;
       EditText e1,e2;
       Interpreter interpreter = new Interpreter();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initUI();
    }
    private void initUI(){
         text = (TextView) findViewById(R.id.textview);
         b = (Button) findViewById(R.id.button);
         e1 = (EditText) findViewById(R.id.editText);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interpreter.parse(e1.getText().toString());
                double my_exp = Double.valueOf(interpreter.getRam(19).getNumber());
                double standart_exp = Math.exp(interpreter.getRam(18).getNumber());
                text.setText(String.valueOf(my_exp)+"\n"+
                        String.valueOf(standart_exp)+"\n"+
                        String.valueOf(Math.abs(standart_exp-my_exp))
                );
            }
        });
    }
}