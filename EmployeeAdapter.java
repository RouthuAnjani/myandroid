package com.example.kpitusers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kpitusers.Employee;
import com.example.kpitusers.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private Context context;
    private List<Employee> employeeList;
    private OnEmployeeActionListener listener;

    public EmployeeAdapter(Context context, List<Employee> employeeList, OnEmployeeActionListener listener) {
        this.context = context;
        this.employeeList = employeeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.employee_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        holder.textViewEmployeeName.setText(employee.getName());
        holder.textViewPhoneNumber.setText(employee.getPhoneNumber());

        // Load the image using Picasso or any other image loading library
        if (employee.getImageUri() != null) {
            Picasso.get().load(employee.getImageUri()).into(holder.imageViewEmployee);
        } else {
            // If no image URI is available, you can set a default image
            holder.imageViewEmployee.setImageResource(R.drawable.icon);
        }

        // Set click listener for the phone icon
        holder.imageViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initiate phone call intent
                String phoneNumber = employee.getPhoneNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                context.startActivity(intent);
            }
        });

        // Set click listener for the update button
        holder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onUpdateEmployee(employee);
                }
            }
        });

        // Set click listener for the delete button
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteEmployee(employee);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public void updateEmployeeList(List<Employee> newEmployeeList) {
        employeeList = newEmployeeList;
        notifyDataSetChanged();
    }

    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView textViewEmployeeName;
        TextView textViewPhoneNumber;
        ImageView imageViewEmployee;
        ImageView imageViewPhone;

        Button buttonUpdate;
        Button buttonDelete;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewEmployeeName = itemView.findViewById(R.id.textViewEmployeeName);
            textViewPhoneNumber = itemView.findViewById(R.id.textViewPhoneNumber);
            imageViewEmployee = itemView.findViewById(R.id.imageViewEmployeePhoto);
            imageViewPhone = itemView.findViewById(R.id.imageViewPhone);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

    public interface OnEmployeeActionListener {
        void onUpdateEmployee(Employee employee);

        void onDeleteEmployee(Employee employee);
    }
}
