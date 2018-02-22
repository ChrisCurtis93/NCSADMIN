package com.atfortech.root.ncs_admin;

import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView daily_paid_sales, daily_unpaid_sales, monthly_sales;
    Button select_branch, click_details;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        daily_paid_sales = (TextView) findViewById(R.id.d_psales);
        daily_unpaid_sales = (TextView) findViewById(R.id.d_usales);
        monthly_sales = (TextView) findViewById(R.id.d_msales);
        select_branch = (Button) findViewById(R.id.select);
        click_details = (Button) findViewById(R.id.view_details);


        select_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BranchSelect();
            }
        });

    }

    public void BranchSelect() {
        View view = LayoutInflater.from(this).inflate(R.layout.list_branches, null);
        TextView textView = view.findViewById(R.id.branch);

        final ListView lin = view.findViewById(R.id.view_outlet);
        //lin.setAdapter(new BranchesAdapter(getApplicationContext(),AppConfig.branchNames));
       // textView.setText(AppConfig.branchNames[1]);
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
        alertDialog.setView(view);
        alertDialog.setCancelable(true);
        alertDialog.show();


    }
}




