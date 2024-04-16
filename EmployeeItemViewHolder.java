package com.example.kpitusers;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmployeeItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageViewEmployeePhoto;
    public TextView textViewEmployeeName, textViewPhoneNumber;
    public Button buttonUpdate, buttonDelete;
    private List<Employee> employeeList;
    public interface OnEmployeeActionListener {
        void onDeleteEmployee(Employee employee);
        void onUpdateEmployee(Employee employee);
    }



    public EmployeeItemViewHolder(@NonNull View itemView, List<Employee> employeeList, OnEmployeeUpdateListener updateListener, OnEmployeeUpdateListener deleteListener) {
        super(itemView);
        textViewEmployeeName = itemView.findViewById(R.id.textViewEmployeeName);
        textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
        buttonDelete = itemView.findViewById(R.id.buttonDelete);
        buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
        this.employeeList = employeeList;



        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the employee to the onDeleteEmployee method of the listener
                deleteListener.onDeleteEmployee(employeeList.get(getAdapterPosition()));
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the employee to the onUpdateEmployee method of the listener
                updateListener.onUpdateEmployee(employeeList.get(getAdapterPosition()));
            }
        });
    }
    public void bind(Employee employee) {
        textViewEmployeeName.setText(employee.getName());
        textViewPhoneNumber.setText(employee.getPhoneNumber());
    }
}