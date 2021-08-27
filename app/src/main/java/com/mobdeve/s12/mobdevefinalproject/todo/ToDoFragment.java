package com.mobdeve.s12.mobdevefinalproject.todo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobdeve.s12.mobdevefinalproject.R;
import com.mobdeve.s12.mobdevefinalproject.database.DatabaseHelper;
import com.mobdeve.s12.mobdevefinalproject.notes.Notes;
import com.mobdeve.s12.mobdevefinalproject.notes.NotesAdapter;
import com.mobdeve.s12.mobdevefinalproject.notes.NotesInput;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToDoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToDoFragment extends Fragment {

    private ArrayList<ToDo> toDoList;
    private RecyclerView rvAddToDo;
    private RecyclerView.LayoutManager layoutManager;
    private ToDoAdapter toDoAdapter;

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private DatabaseHelper dbHelper;
    private String loggedInUser;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ToDoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToDoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToDoFragment newInstance(String param1, String param2) {
        ToDoFragment fragment = new ToDoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_to_do, container, false);

        FloatingActionButton fabAddTodo = view.findViewById(R.id.fab_add_todo);
        fabAddTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ToDoInput.class);
                startActivity(intent);
            }
        });

        this.toDoList = new ArrayList<ToDo>();

        dbHelper = new DatabaseHelper(getActivity());
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        spEditor = this.sp.edit();

        // Print logged in user to Logcat for checking //
        loggedInUser = sp.getString("KEY_USERNAME_LOGGED_IN", "N/A");
        Log.d("Logged in user: ", loggedInUser);

        readAllTodoTable();
        initContent(view);
        return view;
    }

    private void readAllTodoTable(){
        Cursor cursor = dbHelper.readAllUserTodoTable(loggedInUser);
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                String title = cursor.getString(2);
                String year = cursor.getString(3);
                String month = cursor.getString(4);
                String day = cursor.getString(5);
                boolean addSpecificTime = Boolean.parseBoolean(cursor.getString(6));
                boolean setReminder = Boolean.parseBoolean(cursor.getString(7));
                String time = cursor.getString(8);
                String intervals = cursor.getString(9);
                String starting_time = cursor.getString(10);
                int priority = Integer.parseInt(cursor.getString(11));

                String date = year + "/" + month + "/" + day;
                ToDo todo = new ToDo(title, date, time, priority, setReminder, intervals, starting_time,addSpecificTime);

                toDoList.add(todo);
            }
    }
    }

    public void initContent(View view){
        this.toDoAdapter = new ToDoAdapter(toDoList);
        this.layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        this.rvAddToDo = view.findViewById(R.id.rv_add_todo);
        this.rvAddToDo.setLayoutManager(this.layoutManager);
        this.rvAddToDo.setAdapter(this.toDoAdapter);
    }
}