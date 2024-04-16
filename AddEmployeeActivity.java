package com.example.kpitusers;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddEmployeeActivity extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private Button buttonAdd;
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageViewEmployeePhoto;
    private Button buttonSelectPhoto;

    private DatabaseHelper databaseHelper;
    private Uri selectedImageUri; // To store the selected image URI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        imageViewEmployeePhoto = findViewById(R.id.imageViewEmployeePhoto);
        buttonSelectPhoto = findViewById(R.id.buttonSelectPhoto);

        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setEnabled(false); // Initially disable the button

        databaseHelper = new DatabaseHelper(this);

        // Set click listener for selecting photo
        buttonSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // Add text change listeners to EditText fields
        editTextName.addTextChangedListener(textWatcher);
        editTextPhoneNumber.addTextChangedListener(textWatcher);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmployee();
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            // Check if both name and phone number fields are not empty
            String name = editTextName.getText().toString().trim();
            String phoneNumber = editTextPhoneNumber.getText().toString().trim();
            buttonAdd.setEnabled(!name.isEmpty() && !phoneNumber.isEmpty());
        }
    };

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

    private void addEmployee() {
        String name = editTextName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        // Validate phone number
        if (!isValidPhoneNumber(phoneNumber)) {
            Toast.makeText(this, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please enter name and phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        Employee employee = new Employee(name, phoneNumber, selectedImageUri); // Pass selected image URI to the employee constructor
        long id = databaseHelper.addEmployee(employee, selectedImageUri); // Pass selected image URI to the addEmployee method

        if (id != -1) {
            // Load the image of the last added employee
            loadLastEmployeeImage();
            // Set result OK and finish the activity
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Failed to add employee", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to load the image of the last added employee
    private void loadLastEmployeeImage() {
        Cursor cursor = databaseHelper.getLastEmployee(); // Retrieve the last added employee from the database
        if (cursor != null && cursor.moveToFirst()) {
            String imageUriString = cursor.getString(cursor.getColumnIndex("image_uri"));
            if (imageUriString != null) {
                selectedImageUri = Uri.parse(imageUriString);
                imageViewEmployeePhoto.setImageURI(selectedImageUri);
            }
            cursor.close();
        }
    }

    // Method to validate phone number
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.length() == 10 && phoneNumber.matches("\\d+");
    }
}
