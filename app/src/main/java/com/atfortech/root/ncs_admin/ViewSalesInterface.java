package com.atfortech.root.ncs_admin;

/**
 * Created by root on 2/22/18.
 */

public class ViewSalesInterface {

    public String branch_id;
    public String branch_name;
    public String branch_daily_paid_sales;
    public String branch_daily_unpaid_sales;
    public String branch_month_sales;

    public ViewSalesInterface(String branch_id, String branch_name, String branch_daily_paid_sales, String branch_daily_unpaid_sales, String branch_month_sales){
        this.branch_id=branch_id;
        this.branch_name=branch_name;
        this.branch_daily_paid_sales=branch_daily_paid_sales;
        this.branch_daily_unpaid_sales=branch_daily_unpaid_sales;
        this.branch_month_sales= branch_month_sales;
    }
}
