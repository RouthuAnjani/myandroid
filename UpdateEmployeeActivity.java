package com.example.kpitusers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateEmployeeActivity extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private Button buttonUpdate;
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageViewEmployeePhoto;
    private Button buttonSelectPhoto;

    private DatabaseHelper databaseHelper;
    private Uri selectedImageUri; // To store the selected image URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_employee);
        imageViewEmployeePhoto = findViewById(R.id.imageViewEmployeePhoto);
        buttonSelectPhoto = findViewById(R.id.buttonSelectPhoto);

        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        databaseHelper = new DatabaseHelper(this);

        // Set click listener for selecting photo
        buttonSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("employee_name");
            String phone = intent.getStringExtra("employee_phone");
            String imageUriString = intent.getStringExtra("employee_image_uri");
            if (name != null && phone != null) {
                editTextName.setText(name);
                editTextPhoneNumber.setText(phone);
                if (imageUriString != null) {
                    imageViewEmployeePhoto.setImageURI(Uri.parse(imageUriString));
                    selectedImageUri = Uri.parse(imageUriString);
                }
            }
        }

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmployee();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imageViewEmployeePhoto.setImageURI(selectedImageUri);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void updateEmployee() {
        String name = editTextName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        // Validate phone number
        if (!isValidPhoneNumber(phoneNumber)) {
            Toast.makeText(this, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        // Validate phone number
        if (name.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please enter name and phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        int employeeId = getIntent().getIntExtra("employee_id", -1);
        if (employeeId != -1) {
            Employee updatedEmployee = new Employee(name, phoneNumber);
            updatedEmployee.setId(employeeId);

            // Pass the selected image URI to the updateEmployee() method
            long id = databaseHelper.updateEmployee(updatedEmployee, selectedImageUri);
            if (id != -1) {
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Failed to update employee", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Employee ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to validate phone number
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.length() == 10 && phoneNumber.matches("\\d+");
    }
}
