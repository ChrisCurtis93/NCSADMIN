package com.atfortech.root.ncs_admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2/21/18.
 */

public class Login extends AppCompatActivity {

    EditText usermail,userpwd;
    Button login;

    public static String stremail,strpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        usermail=findViewById(R.id.email);
        userpwd=findViewById(R.id.password_admin);
        login=findViewById(R.id.log);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stremail=usermail.getText().toString();
                strpassword=userpwd.getText().toString();
                new LoadData().execute();

            }
        });


    }
    public class LoadData extends AsyncTask<String,String,String>{

        int success;
        JSONArray data;
        ProgressDialog progressDialog=new ProgressDialog(Login.this);

        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog.setMessage("Authenticating. Please Wait...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... strings) {

            JSONParser jsonParser=new JSONParser();
            List parameter=new ArrayList();
            parameter.add(new BasicNameValuePair("email",stremail));
            parameter.add(new BasicNameValuePair("password",strpassword));

            JSONObject jsonObject=jsonParser.makeHttpRequest(AppConfig.protocol+AppConfig.hostname+AppConfig.admin_login_url,
                    "GET",parameter);

            try{
                success=jsonObject.getInt("success");
                data=jsonObject.getJSONArray("branches");

                AppConfig.branchIds=new String[data.length()];
                AppConfig.branchNames=new String[data.length()];

                for (int i=0;i<data.length();i++){
                    JSONObject jobj=data.getJSONObject(i);
                     AppConfig.branchNames[i]=jobj.getString("branch_name");
                     AppConfig.branchIds[i]=jobj.getString("branch_id");
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String s){
            super.onPostExecute(s);

            if (success==1){

                startActivity(new Intent(Login.this,MainActivity.class));
            }else {
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            }

            progressDialog.cancel();
        }
    }

}
