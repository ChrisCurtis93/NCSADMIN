package com.atfortech.root.ncs_admin;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    TextView todays, month, all;
    Button select_branch, click_details;
    ListView listView;
    CardView cardView;


    List<ViewSalesInterface> branchDetails;

    public static String tod,mon,al;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        todays = (TextView) findViewById(R.id.total_today_sale);
        month = (TextView) findViewById(R.id.total_month_sale);
        all = (TextView) findViewById(R.id.total_all_sales);
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
        final ListView lin = view.findViewById(R.id.view_outlet);
        lin.setAdapter(new BranchesAdapter(getApplicationContext(), AppConfig.branchNames));
        cardView = view.findViewById(R.id.list_card);

        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
        alertDialog.setView(view);
        alertDialog.setCancelable(true);
        alertDialog.show();

        lin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = (int) adapterView.getItemIdAtPosition(i);
                AppConfig.selected_branch_id = AppConfig.branchIds[pos];
                AppConfig.selected_branch_name=AppConfig.branchNames[pos];
                select_branch.setText(AppConfig.branchNames[pos]);

                new ViewSale().execute();
                alertDialog.cancel();

            }
        });


    }
@OnClick(R.id.view_details)
public void showBottomSheetDialog() {
    View view = getLayoutInflater().inflate(R.layout.bottomsheet_details, null);

    CardView c_today=view.findViewById(R.id.card_today);
    CardView c_month=view.findViewById(R.id.card_month);
    CardView c_all=view.findViewById(R.id.card_all);

    c_today.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String url="http://system.nagalaske.co.ke/app/csvExporter/data-exporter.php?branchId="+AppConfig.selected_branch_id+"&time=today";

            Toast.makeText(getApplicationContext(),"file is downloading...",Toast.LENGTH_SHORT).show();
             download_report(url,"Today's");
        }
    });
    c_month.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String url="http://system.nagalaske.co.ke/app/csvExporter/data-exporter.php?branchId="+AppConfig.selected_branch_id+"&time=month";

            Toast.makeText(getApplicationContext(),"file is downloading...",Toast.LENGTH_SHORT).show();
            download_report(url,"Month");
        }
    });

    c_all.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String url="http://system.nagalaske.co.ke/app/csvExporter/data-exporter.php?branchId="+AppConfig.selected_branch_id+"&time=all";

            Toast.makeText(getApplicationContext(),"file is downloading...",Toast.LENGTH_SHORT).show();
            download_report(url,"All");
        }
    });
    BottomSheetDialog dialog = new BottomSheetDialog(this);
    dialog.setContentView(view);
    dialog.show();
}


    public class ViewSale extends AsyncTask<String,String,String>{
        int success=0;
        String serverMessage;
        JSONArray sale;
        String branch_daily_paid_sales=null;
        String branch_daily_unpaid_sales=null;
        String branch_month_sales=null;


        protected  void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            JSONParser jsonParser=new JSONParser();
            List paramters=new ArrayList();
            branchDetails=new ArrayList<>();
            paramters.add(new BasicNameValuePair("branchId",AppConfig.selected_branch_id));

            JSONObject jsonObject=jsonParser.makeHttpRequest(AppConfig.protocol+AppConfig.hostname+ AppConfig.get_today_sales,
                    "GET",paramters);
            Log.d("data recieved",jsonObject.toString());

            try {
                success=jsonObject.getInt("success");
//                sale=jsonObject.getJSONArray("branch");

                tod=jsonObject.getString("today");
                mon=jsonObject.getString("month");
                al=jsonObject.getString("all");

                if(tod.equals("null")){
                    tod="0";
                }else if(mon.equals("null")){
                    mon="0";

                }else if(al.equals("null")){
                    al="0";
                }
                branchDetails.add(new ViewSalesInterface(AppConfig.selected_branch_id,
                        AppConfig.selected_branch_name,
                        jsonObject.getString("today"),
                        jsonObject.getString("all"),
                        jsonObject.getString("month")));


            }catch (Exception e){
                e.printStackTrace();
            }


            return null;
        }
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(success==1){
                todays.setText(tod);
                month.setText(mon);
                all.setText(al);
            }else{
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void download_report(String url,String title){
        File rootDirectory=new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS),"MAUZO REPORTS");
        if(!rootDirectory.exists()){
            rootDirectory.mkdirs();
        }

        String timeNow= new Date().toLocaleString();
        String fileName=title+" Report as at:"+timeNow;


        String nameOfFile= URLUtil.guessFileName(url,null, MimeTypeMap.getFileExtensionFromUrl(url));
        File file = new File(rootDirectory,fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Nagalas Chakula Reports");
        request.setDescription("File is being Downloaded...");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);

        DownloadManager manager = (DownloadManager)this.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
    public void onBackPressed(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setMessage((CharSequence) "Are you sure you want to log out?");
        alertDialog.setPositiveButton((CharSequence) "Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);

                finish();
            }
        });
        alertDialog.setNegativeButton((CharSequence) "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();

    }
}




