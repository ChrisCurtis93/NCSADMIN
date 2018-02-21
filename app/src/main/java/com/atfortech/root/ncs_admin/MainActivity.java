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
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView daily_paid_sales,daily_unpaid_sales,monthly_sales;
    Button select_branch,click_details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        daily_paid_sales=(TextView) findViewById(R.id.d_psales);
        daily_unpaid_sales=(TextView) findViewById(R.id.d_usales);
        monthly_sales=(TextView) findViewById(R.id.d_msales);
        select_branch= (Button) findViewById(R.id.select);
        click_details=(Button)findViewById(R.id.view_details);


        select_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 new BranchSelection().execute();
            }
        });

    }
    public void SelectOutletPop (int i){
        final AlertDialog dialog= new AlertDialog.Builder(getApplicationContext()).create();
        View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_branches,null);
        ListView listView=view.findViewById(R.id.view_outlet);
        TextView no_branch=view.findViewById(R.id.no_branch);

        if(i==0){
            no_branch.setVisibility(View.VISIBLE);

        }else{
            listView.setAdapter(new BranchesAdapter(getApplicationContext(),AppConfig.branchNames));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int pos= (int) adapterView.getItemIdAtPosition(i);
                    AppConfig.selected_branch_id=AppConfig.branchIds[pos];
                    select_branch.setText(AppConfig.branchNames[pos]);
                    dialog.cancel();

                }
            });
        }

        dialog.setView(view);
        dialog.show();
    }

    public class BranchSelection extends AsyncTask<String,String,String> {
        int success=0;
        String serverMessage;
        JSONArray branch ;
        String outlet=null;



        protected  void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser=new JSONParser();
            List paramters=new ArrayList();

         //   paramters.add(new BasicNameValuePair("user_id",users.get_user_id()));

            JSONObject jsonObject=jsonParser.makeHttpRequest(AppConfig.protocol+AppConfig.hostname+
                            AppConfig.admin_select_branches,
                    "GET",paramters);
            Log.d("data recieved",jsonObject.toString());

            try {
                success=jsonObject.getInt("success");
                branch=jsonObject.getJSONArray("data");

                AppConfig.branchNames=new String[branch.length()];
                AppConfig.branchIds=new String[branch.length()];
                for(int i=0;i<branch.length();i++){
                    JSONObject jobj=branch.getJSONObject(i);
                    AppConfig.branchNames[i]=jobj.getString("shop_name");
                    AppConfig.branchIds[i]=jobj.getString("shop_id");
                }
            }catch (Exception e){
                e.printStackTrace();
            }


            return null;
        }
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(success==1){
                if(AppConfig.branchIds.length>0){
                    SelectOutletPop(1);
                }else{
                    SelectOutletPop(0);
                }

            }
        }
    }


}
