package com.example.courseregistration;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


// this will populate the recyclerview with student information
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private OnStudentClickListener clickListener;

    // updates data set with a new list and notifies the recyclerview that data has changed
    public void setStudentList(List<Student> newList) {
        studentList = newList;
        notifyDataSetChanged();
    }

    public interface OnStudentClickListener {
        void onStudentClick(int position);
    }

    // method to remove student based on position
    public void removeStudent(int position) {
        if (position >= 0 && position < studentList.size()) {
            studentList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public StudentAdapter(List<Student> studentList, OnStudentClickListener clickListener) {
        this.studentList = studentList;
        this.clickListener = clickListener;
    }

    // references views of each item in the studentdisplay
    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.studentdisplay, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(holder.getAdapterPosition());
        holder.bind(student);

        // changes the color of a selected item to confirm clicking
        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.lavender));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT); // default color
        }
        // click listeners for items
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // updates position
                setSelectedPosition(holder.getAdapterPosition());
                // notifies listener
                if (clickListener != null) {
                    clickListener.onStudentClick(holder.getAdapterPosition());
                }
                // refresh the view with changed data
                notifyDataSetChanged();
            }
        });
    }

    // returns total number of items in dataset
    @Override
    public int getItemCount() {
        return studentList.size();
    }

    // update selected position
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    // returns current selected position
    public int getSelectedPosition() {
        return selectedPosition;
    }

    // return student object
    public Student getStudent(int position) {
        return studentList.get(position);
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView studentNameText, priorityText, courseWaitingText;

        StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameText = itemView.findViewById(R.id.studentName);
            priorityText = itemView.findViewById(R.id.priority);
            courseWaitingText = itemView.findViewById(R.id.courseWaiting);
        }

        void bind(Student student) {
            studentNameText.setText(student.getName());
            priorityText.setText("Priority: " + student.getPriority());
            courseWaitingText.setText("Course Waiting: " + student.getCourse());
        }
    }
}