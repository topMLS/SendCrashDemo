package demo.sendcrashdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt = (Button) findViewById(R.id.bt);
        //String s=tv.getText().toString();
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] a = new int[2];
                Toast.makeText(MainActivity.this,"数值为："+a[4],Toast.LENGTH_SHORT).show();
            }
        });

    }
}
