package com.example.indalamar.myapplication2;

import android.os.OperationCanceledException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final static  String reserve ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onSaveInstanceState(Bundle outState) {
        TextView Save = (TextView) findViewById(R.id.expressionfield);
        outState.putString(reserve,Save.getText().toString());
        super.onSaveInstanceState(outState);
    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        String Saving = savedInstanceState.getString(reserve);
        TextView nameView = (TextView) findViewById(R.id.expressionfield);
        nameView.setText(Saving);
    }
    public void compute (View v)
    {
        TextView Field = (TextView) findViewById(R.id.expressionfield);
        String expression = Field.getText().toString();
        try {

            String result = Parser.parse(expression).toString();
            Field.setText(result);
        }
        catch (NullPointerException e)
        {
            Toast toast = Toast.makeText(MainActivity.this , "null dividing spoted",Toast.LENGTH_LONG );
            toast.show();
        }
        catch (ArithmeticException e )
        {
            Toast toast = Toast.makeText(MainActivity.this , "not all brackets have closed one",Toast.LENGTH_LONG );
            toast.show();
        }
        catch (NotEnoughOperands e)
        {
            Toast toast = Toast.makeText(MainActivity.this , "not enough operands or too many operators",Toast.LENGTH_LONG );
            toast.show();
        }
        catch (NotCorrectInput e)
        {
            Toast toast = Toast.makeText(MainActivity.this , "not correct number input",Toast.LENGTH_LONG );
            toast.show();
        }

    }
    public void deleteTheLast(View r)
    {
        TextView resText = (TextView) findViewById(R.id.expressionfield);
        String arg = resText.getText().toString();
        if (arg.length()!= 0 )
        {
            arg=arg.substring(0,arg.length()-1);
        }
        resText.setText(arg);
    }
    public  void deleteAll(View r)
    {
        TextView resText = (TextView) findViewById(R.id.expressionfield);
        resText.setText("");
    }
    public void writeSymbol(View r)
    {
        Button v = (Button) r;
        String arg = v.getText().toString();
        TextView resText = (TextView) findViewById(R.id.expressionfield);
        String expression = resText.getText().toString();
        expression += arg.charAt(0);
        resText.setText(expression);
    }
}
