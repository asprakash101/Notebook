package com.simpad.pathaknotebook;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.simpad.pathaknotebook.models.NotebookData;

import java.util.List;

public class CheckOutActivity extends AppCompatActivity {

    private static final String TAG = "CheckOutActivity" ;
    FrameLayout personalInfo,summary,payment;
    View personalTosummary,summaryTopayment;
    ImageView tick;
    TextView detailsNumber;
    public List<NotebookData> cartProducts;
    public Float sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(CartActivity.GET_BUNDLE);
        cartProducts = (List<NotebookData>) bundle.getSerializable(CartActivity.GET_CART_ITEMS);
        Float sum = (Float) bundle.getFloat(CartActivity.GET_SUM);
        personalInfo = findViewById(R.id.personalInfo);
        summary = findViewById(R.id.orderSummary);
        payment = findViewById(R.id.payment);
        personalTosummary = findViewById(R.id.linePersonal);
        summaryTopayment = findViewById(R.id.lineOrder);
        tick = findViewById(R.id.detailsStatus);
        detailsNumber = findViewById(R.id.detailsNumber);
        tick.setVisibility(View.INVISIBLE);
        detailsNumber.setVisibility(View.VISIBLE);
        personalInfo.setBackground(getDrawable(R.drawable.shape_current));
        personalTosummary.setBackgroundColor(Color.parseColor("#84a9ac"));
        summary.setBackground(getDrawable(R.drawable.shape_incomplete));
        summaryTopayment.setBackgroundColor(Color.parseColor("#84a9ac"));
        payment.setBackground(getDrawable(R.drawable.shape_incomplete));
    }
}