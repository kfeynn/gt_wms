package com.grand.gt_wms.ui.activity;

import android.app.DatePickerDialog;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.grand.gt_wms.R;
import com.grand.gt_wms.service.wmsService;
import com.grand.gt_wms.tool.PopUtil;
import com.grand.gt_wms.ui.adapter.GoodsIptAndEptAdapter;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Administrator on 2019/1/10.
 */

public class GoodsIptAndEptActivity extends BaseActivity{
    private TextView tv_tlf902, tv_tlf01, tv_ima02, tv_ima021,tv_img10;
    private EditText start_time, end_time;
    private EditText tlf902;
    private String box_num;
    private RecyclerView mRecyclerView;
    private GoodsIptAndEptAdapter goodsIptAndEptAdapter;
    private List<BasicNameValuePair> list = null;
    private JSONObject jsonObject;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsiptandept);
        init();

    }

    private void init(){
        setTitle(itemArray[7]);
        back();
        setSearchEdit("包装号");
        onSearchButtonListen();

        tv_tlf902 = findViewById(R.id.tv_tlf902);
        tv_tlf01 = findViewById(R.id.tv_tlf01);
        tv_ima02 = findViewById(R.id.tv_ima02);
        tv_ima021 = findViewById(R.id.tv_ima021);
        tv_img10 = findViewById(R.id.tv_img10);
        start_time = findViewById(R.id.start_time);
        start_time.setInputType(InputType.TYPE_NULL);
        end_time = findViewById(R.id.end_time);
        end_time.setInputType(InputType.TYPE_NULL);
        start_time.setText(wmsService.START_TIME);
        end_time.setText(wmsService.END_TIME);
        tlf902=findViewById(R.id.tlf902);   //仓库
//        tlf902.setInputType(InputType.TYPE_NULL);
        tlf902.setInputType(InputType.TYPE_CLASS_NUMBER);  //设置为只输入数字
        tlf902.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                wmsService.TLF902=tlf902.getText().toString();   //赋值

            }
        });
        start_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    closeKeyboard();
                    showDatePickerDialog_satrt();
                }
            }
        });
        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                showDatePickerDialog_satrt();
            }
        });
        end_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    closeKeyboard();
                    showDatePickerDialog_end();
                }
            }
        });
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                showDatePickerDialog_end();
            }
        });
        mRecyclerView = findViewById(R.id.gridview_goodsiptandept);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        goodsIptAndEptAdapter = new GoodsIptAndEptAdapter(this,box_num);
/*        groundingAdapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                      int groupPosition) {
                GroundingAdapter expandableAdapter = (GroundingAdapter) adapter;
                if (expandableAdapter.isExpand(groupPosition)) {
                    expandableAdapter.collapseGroup(groupPosition);
                } else {
                    expandableAdapter.expandGroup(groupPosition);
                }
            }
        });*/
        mRecyclerView.setAdapter(goodsIptAndEptAdapter);
    }

    /**
     * 展示日期选择对话框
     */
    private void showDatePickerDialog_satrt() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(GoodsIptAndEptActivity.this,R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                start_time.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                wmsService.START_TIME = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showDatePickerDialog_end() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(GoodsIptAndEptActivity.this,R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                end_time.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                wmsService.END_TIME = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void showItems(String boxNum) {
        super.showItems(boxNum);
        box_num = boxNum;
        et_search.setText(boxNum);
        if (wmsService.imptAndEmptItemsList != null && wmsService.imptAndEmptItemsList.size()>0) {
            closeKeyboard();
//            tv_tlf902.setText("仓库："+wmsService.imptAndEmptItemsList.get(0).getTlf902());
//            tlf902.setText("仓库："+wmsService.imptAndEmptItemsList.get(0).getTlf902());
            tlf902.setText(wmsService.imptAndEmptItemsList.get(0).getTlf902());
            tv_tlf01.setText("料号："+wmsService.imptAndEmptItemsList.get(0).getTlf01());
            tv_ima02.setText("品名："+wmsService.imptAndEmptItemsList.get(0).getIma02());
            tv_ima021.setText("规格："+wmsService.imptAndEmptItemsList.get(0).getIma021());
            tv_img10.setText("当前库存："+wmsService.imptAndEmptItemsList.get(0).getImg10());
            goodsIptAndEptAdapter.notifyDataChanged();
        }else{
            closeKeyboard();
            goodsIptAndEptAdapter.notifyDataChanged();
            PopUtil.showPopUtil("没找到此物料数据");
        }
    }

    @Override
    protected void showBoxList(String boxNum) {
        super.showBoxList(boxNum);
        box_num = boxNum;
        et_search.setText(boxNum);
        if (wmsService.RnBoxList != null) {
            closeKeyboard();
            goodsIptAndEptAdapter.notifyDataChanged();
        }else{
            closeKeyboard();
            goodsIptAndEptAdapter.notifyDataChanged();
        }
    }


}
