package com.it.hightsample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.renj.hightlight.HighLightBuilder;
import com.renj.hightlight.HighLightManager;
import com.renj.hightlight.callback.OnClickCallback;


/**
 * ======================================================================
 * <p/>
 * 作者：Renj
 * <p/>
 * 创建时间：2016-08-03    15:01
 * <p/>
 * 描述：
 * <p/>
 * 修订历史：
 * <p/>
 * ======================================================================
 */
public class ThreeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);

        addViewToLayout();
    }

    private void addViewToLayout() {
        HighLightBuilder.newInstance(this)
                .setOnClickCallback(new OnClickCallback() {
                    @Override
                    public void onClick() {
                        Toast.makeText(ThreeActivity.this, "覆盖层被点击了", Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .addLayout(R.layout.layout_three);
    }
}
