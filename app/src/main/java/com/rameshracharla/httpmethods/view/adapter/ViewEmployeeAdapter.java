package com.rameshracharla.httpmethods.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.rameshracharla.httpmethods.R;
import com.rameshracharla.httpmethods.interfaces.ApiClient;
import com.rameshracharla.httpmethods.interfaces.ApiInterface;
import com.rameshracharla.httpmethods.model.Data;
import com.rameshracharla.httpmethods.model.ResponseModifyData;
import com.rameshracharla.httpmethods.utils.Constants;
import com.rameshracharla.httpmethods.view.activity.EmployeeDetails;
import com.rameshracharla.httpmethods.view.activity.MainActivity;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ViewEmployeeAdapter extends RecyclerView.Adapter<ViewEmployeeAdapter.MyViewHolder> {

    ArrayList<Data> getData;
    private Context context;
    private Constants con;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ApiInterface apiService;

    public ViewEmployeeAdapter(MainActivity context, ArrayList<Data> getData) {
        this.getData = getData;
        this.context = context;
        con = new Constants(context);
        apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    @Override
    public ViewEmployeeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_viewdata, parent, false);
        return new ViewEmployeeAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewEmployeeAdapter.MyViewHolder holder, int position) {
        Data getdata = getData.get(position);
        try {
            holder.id.setText(getdata.getId());
            holder.name.setText(getdata.getEmployee_name());
            holder.age.setText(getdata.getEmployee_age());
            holder.salary.setText(getdata.getEmployee_salary());


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return getData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, name, salary, age;
        CardView cardview;
        ImageView update_employee, delete_employee;

        public MyViewHolder(View itemView) {
            super(itemView);

            id = (TextView) itemView.findViewById(R.id.id);
            name = (TextView) itemView.findViewById(R.id.name);
            salary = (TextView) itemView.findViewById(R.id.salary);
            age = (TextView) itemView.findViewById(R.id.age);
            update_employee = (ImageView) itemView.findViewById(R.id.update_employee);
            delete_employee = (ImageView) itemView.findViewById(R.id.delete_employee);
            cardview = (CardView) itemView.findViewById(R.id.card_view);
            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EmployeeDetails.class);
                    intent.putExtra("empID", getData.get(getLayoutPosition()).getId());
                    intent.putExtra("purpose", "showdetails");
                    context.startActivity(intent);
                }
            });
            update_employee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EmployeeDetails.class);
                    intent.putExtra("empID", getData.get(getLayoutPosition()).getId());
                    intent.putExtra("purpose", "updatedetails");
                    context.startActivity(intent);
                }
            });
            delete_employee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteEmployee(getData.get(getLayoutPosition()).getId());
                }
            });
        }
    }

    private void deleteEmployee(String id) {

        con.showProgressDialogue();
        disposable.add(
                apiService.deleteRecord(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseModifyData>() {
                            @Override
                            public void onSuccess(ResponseModifyData response) {
                                con.dismissProgressDialogue();

                                try {
                                    if (response.getStatus().equals("success")) {
                                        Toast.makeText(context, "" + response.getMessage(), Toast.LENGTH_LONG).show();


                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                con.dismissProgressDialogue();
                            }
                        })
        );
    }
}
