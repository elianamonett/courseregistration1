package com.example.courseregistration;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// provides implementation for activity_main.xml allowing fields, buttons, and spinner to work properly

public class MainActivity extends AppCompatActivity {

    private StudentAdapter studentAdapter;

    private EditText studentNameEdit, waitingCourseEdit;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // creates instance
        dbHandler = new DBHandler(MainActivity.this);

        // gets list from db handler
        List<Student> studentList = dbHandler.getAllStudents();

        // initializes recyclerview and sets up adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        studentAdapter = new StudentAdapter(studentList, position -> {
            if (position >= 0 && position < studentList.size()) {

                Student selectedStudent = studentList.get(position);
            }
        });


        recyclerView.setAdapter(studentAdapter);

        // sets spinner options
        String[] priorities = {"Graduate", "4th Year", "3rd Year", "2nd Year", "1st Year"};

        // initializing Spinner and set adapter
        Spinner prioritySpinner = findViewById(R.id.priorityspinner);
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, priorities);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySpinner.setAdapter(priorityAdapter);

        // handles spinner selection
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedPriority = priorities[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        // initializing variables
        studentNameEdit = findViewById(R.id.studentname);
        waitingCourseEdit = findViewById(R.id.waitingcourse);
        Button btnAddStudent = findViewById(R.id.btn_addstudent);
        Button btnEditStudent = findViewById(R.id.btn_editstudent);
        Button btnRemoveStudent = findViewById(R.id.btn_removestudent);

        // implements functionalities for add button
        btnAddStudent.setOnClickListener(v -> {

            String studentName = studentNameEdit.getText().toString();
            String waitingCourse = waitingCourseEdit.getText().toString();
            String selectedPriority = priorities[prioritySpinner.getSelectedItemPosition()];

            if (studentName.isEmpty() || waitingCourse.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHandler.addNewStudent(studentName, waitingCourse, selectedPriority);


            Toast.makeText(MainActivity.this, "Student has been added.", Toast.LENGTH_SHORT).show();

            studentNameEdit.setText("");
            waitingCourseEdit.setText("");

            List<Student> updatedStudentList = dbHandler.getAllStudents();
            studentAdapter.setStudentList(updatedStudentList);
            studentAdapter.notifyDataSetChanged();
        });

        // provides functionalities for edit button
        btnEditStudent.setOnClickListener(v -> {

            String studentName = studentNameEdit.getText().toString();
            String waitingCourse = waitingCourseEdit.getText().toString();
            String selectedPriority = prioritySpinner.getSelectedItem().toString();

            if (studentName.isEmpty() || waitingCourse.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please click student and enter information for all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedItemPosition = studentAdapter.getSelectedPosition();


            if (selectedItemPosition == RecyclerView.NO_POSITION) {
                Toast.makeText(MainActivity.this, "Please select a student to edit.", Toast.LENGTH_SHORT).show();
                return;
            }

            Student selectedStudent = studentAdapter.getStudent(selectedItemPosition);

            int studentId = selectedStudent.getId();


            dbHandler.editStudent(studentId, studentName, waitingCourse);


            selectedStudent.setName(studentName);
            selectedStudent.setCourse(waitingCourse);
            selectedStudent.setPriority(selectedPriority);

            studentAdapter.notifyItemChanged(selectedItemPosition);


            Toast.makeText(MainActivity.this, "Student information updated.", Toast.LENGTH_SHORT).show();


            studentNameEdit.setText("");
            waitingCourseEdit.setText("");
        });

        // provides implementation for removal button and confirmation
        btnRemoveStudent.setOnClickListener(v -> {
            int selectedPosition = studentAdapter.getSelectedPosition();
            if (selectedPosition != RecyclerView.NO_POSITION) {

                Student selectedStudent = studentAdapter.getStudent(selectedPosition);
                dbHandler.removeStudent(selectedStudent.getId());
                studentAdapter.removeStudent(selectedPosition);
                studentAdapter.setSelectedPosition(RecyclerView.NO_POSITION);

                studentAdapter.notifyItemChanged(selectedPosition);
                Toast.makeText(MainActivity.this, "Student removed successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Please select a student to remove.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}