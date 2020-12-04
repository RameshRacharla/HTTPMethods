package com.rameshracharla.httpmethods.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rameshracharla.httpmethods.R;
import com.rameshracharla.httpmethods.interfaces.ApiClient;
import com.rameshracharla.httpmethods.interfaces.ApiInterface;
import com.rameshracharla.httpmethods.model.Data;
import com.rameshracharla.httpmethods.model.ResponseModifyData;
import com.rameshracharla.httpmethods.model.ResponseSingleData;
import com.rameshracharla.httpmethods.utils.Constants;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class EmployeeDetails extends AppCompatActivity implements View.OnClickListener {
    private EditText empid, empname, empsal, empage;
    private String selectedempid, purpose;
    private Button update_create;
    private Constants con;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ApiInterface apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empdetails);
        initializeViews();
        if (purpose.equals("showdetails")) {
            getDetails();
            setTitle("Employee Details");
            update_create.setVisibility(View.GONE);
            disableFields();
        } else if (purpose.equals("updatedetails")) {
            setTitle("Update Details");
            getDetails();
            update_create.setVisibility(View.VISIBLE);
            update_create.setText("Update");
        } else if (purpose.equals("createdetails")) {
            setTitle("Create Employee");
            update_create.setVisibility(View.VISIBLE);
            update_create.setText("Create");
        }

    }

    private void initializeViews() {
        con = new Constants(this);
        empid = (EditText) findViewById(R.id.empid);
        empname = (EditText) findViewById(R.id.empname);
        empsal = (EditText) findViewById(R.id.empsal);
        empage = (EditText) findViewById(R.id.empage);
        update_create = (Button) findViewById(R.id.button_update_create);
        selectedempid = getIntent().getStringExtra("empID");
        purpose = getIntent().getStringExtra("purpose");
        apiService = ApiClient.getClient().create(ApiInterface.class);
        update_create.setOnClickListener(this);
    }

    private void getDetails() {

        con.showProgressDialogue();
        disposable.add(
                apiService.getSingleRecord(selectedempid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseSingleData>() {
                            @Override
                            public void onSuccess(ResponseSingleData response) {
                                con.dismissProgressDialogue();
                                try {
                                    if (response.getStatus().equals("success")) {
                                        fetchData(response.getGetData());
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

    private void fetchData(Data getData) {
        empid.setText(getData.getId());
        empname.setText(getData.getEmployee_name());
        empsal.setText(getData.getEmployee_salary());
        empage.setText(getData.getEmployee_age());

    }

    private void disableFields() {
        empid.setEnabled(false);
        empname.setEnabled(false);
        empsal.setEnabled(false);
        empage.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        if (update_create.getText().toString().equals("Create")) {
            checkValidations(1);
        } else if (update_create.getText().toString().equals("Update")) {
            checkValidations(2);

        }
    }

    private void checkValidations(int i) {
        if (empid.getText().toString().equals("")) {
            Toast.makeText(this, "Employee id required", Toast.LENGTH_LONG).show();
            return;
        }
        if (empname.getText().toString().equals("")) {
            Toast.makeText(this, "Employee Name required", Toast.LENGTH_LONG).show();
            return;
        }
        if (empsal.getText().toString().equals("")) {
            Toast.makeText(this, "Employee Salary required", Toast.LENGTH_LONG).show();
            return;
        }
        if (empage.getText().toString().equals("")) {
            Toast.makeText(this, "Employee Age required", Toast.LENGTH_LONG).show();
            return;
        }
        if (i == 1) {
            createEmployee(empid.getText().toString(), empname.getText().toString(),
                    empsal.getText().toString(), empage.getText().toString());
        } else {
            updateEmployee(empid.getText().toString(), empname.getText().toString(),
                    empsal.getText().toString(), empage.getText().toString());
        }
    }

    private void createEmployee(String id, String name, String salary, String age) {
        Data data = new Data();
        data.setId(id);
        data.setEmployee_name(name);
        data.setEmployee_salary(salary);
        data.setEmployee_age(age);
        con.showProgressDialogue();
        disposable.add(
                apiService.createNewRecord(data)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseSingleData>() {
                            @Override
                            public void onSuccess(ResponseSingleData response) {
                                con.dismissProgressDialogue();
                                try {
                                    if (response.getStatus().equals("success")) {
                                        Toast.makeText(EmployeeDetails.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                                        finish();

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

    private void updateEmployee(String id, String name, String salary, String age) {
        Data data = new Data();
        data.setId(id);
        data.setEmployee_name(name);
        data.setEmployee_salary(salary);
        data.setEmployee_age(age);
        con.showProgressDialogue();
        disposable.add(
                apiService.updateRecord(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<ResponseModifyData>() {
                            @Override
                            public void onSuccess(ResponseModifyData response) {
                                con.dismissProgressDialogue();
                                try {
                                    if (response.getStatus().equals("success")) {
                                        Toast.makeText(EmployeeDetails.this, "" + response.getMessage(), Toast.LENGTH_SHORT).show();
                                        finish();

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
