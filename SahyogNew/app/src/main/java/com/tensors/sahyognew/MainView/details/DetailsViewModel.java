package com.tensors.sahyognew.MainView.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tensors.sahyognew.MainView.MainActivity;
import com.tensors.sahyognew.Student;

import java.util.List;

public class DetailsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    public LiveData<List<Student>> mStudents;

    public DetailsViewModel() {
        mText = new MutableLiveData<>();
        mStudents = new MutableLiveData<>();
        mText.setValue("This is details fragment");
        mStudents = MainActivity.studentDatabase.studentDao().getStudents();
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<List<Student>> getstud(){return mStudents;}
}