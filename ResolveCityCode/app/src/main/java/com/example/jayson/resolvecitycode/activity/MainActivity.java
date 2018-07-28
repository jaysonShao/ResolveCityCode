package com.example.jayson.resolvecitycode.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jayson.resolvecitycode.R;
import com.example.jayson.resolvecitycode.bean.FourCityBean;
import com.example.jayson.resolvecitycode.service.CityCalculateService;
import com.example.jayson.resolvecitycode.utils.SPUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mbt_button;
    private TextView mshow;
    private FourCityBean city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, CityCalculateService.class);
        startService(intent);
        mshow = findViewById(R.id.show);
        mbt_button = findViewById(R.id.bt_button);
        mbt_button.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == -1){
            if (data != null) {
                city = (FourCityBean)data.getSerializableExtra(MainAddressActivity.KEY_PICKED_CITY);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(city.getOne());
                stringBuilder.append("/");
                stringBuilder.append(city.getTwo());
                stringBuilder.append("/");
                stringBuilder.append(city.getThree());
                stringBuilder.append("/");
                stringBuilder.append(city.getFour());
                stringBuilder.append("/");
                stringBuilder.append("\n");
                stringBuilder.append(city.getOneCode());
                stringBuilder.append("/");
                stringBuilder.append(city.getTwoCode());
                stringBuilder.append("/");
                stringBuilder.append(city.getThreeCode());
                stringBuilder.append("/");
                stringBuilder.append(city.getFourCode());
                stringBuilder.append("/");
                mshow.setText(stringBuilder);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_button:
                if (!Boolean.valueOf(SPUtils.get(this, "districtConfigs","true"))) {
                    Intent intent = new Intent(MainActivity.this, MainAddressActivity.class);
                    startActivityForResult(intent,10);
                }else {
                    Toast.makeText(MainActivity.this,"Service正在解析城市数据，请稍等",Toast.LENGTH_LONG).show();
                }
            break;
        }
    }
}
