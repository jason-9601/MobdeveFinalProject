package com.mobdeve.s12.mobdevefinalproject.todo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ToDoFragment extends Fragment {

    private ArrayList<ToDo> toDoList;
    private RecyclerView rvAddToDo;
    private RecyclerView.LayoutManager layoutManager;
    private ToDoAdapter toDoAdapter;

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private DatabaseHelper dbHelper;
    private String loggedInUser;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ToDoFragment() {

    }

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
    public void onResume() {
        super.onResume();
        toDoList.clear();
        readAllTodoTable();
        toDoAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_to_do, container, false);

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

    private void readAllTodoTable() {
        Cursor cursor = dbHelper.readAllUserTodoTable(loggedInUser);
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String title = cursor.getString(2);
                String year = cursor.getString(3);
                String month = cursor.getString(4);
                String day = cursor.getString(5);
                boolean addSpecificTime = Boolean.parseBoolean(cursor.getString(6));
                int setReminder = Integer.parseInt(cursor.getString(7));
                String time = cursor.getString(8);
                String intervals = cursor.getString(9);
                String starting_time = cursor.getString(10);
                int priority = Integer.parseInt(cursor.getString(11));
                String hour = cursor.getString(12);
                String minute = cursor.getString(13);

                String date = year + "/" + month + "/" + day;
                ToDo todo = new ToDo(id, title, date, time,
                        priority, setReminder, intervals, starting_time,
                        addSpecificTime, hour, minute);

                toDoList.add(todo);
            }
        }
    }

    public void initContent(View view) {
        this.toDoAdapter = new ToDoAdapter(toDoList);
        this.layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        this.rvAddToDo = view.findViewById(R.id.rv_add_todo);
        this.rvAddToDo.setLayoutManager(this.layoutManager);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvAddToDo);
        this.rvAddToDo.setAdapter(this.toDoAdapter);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
            String swipedTodoId = toDoList.get(viewHolder.getAdapterPosition()).getTodo_id();
            dbHelper.deleteTodo(swipedTodoId);
            toDoList.remove(viewHolder.getAdapterPosition());
            toDoAdapter.notifyDataSetChanged();
        }
    };

}
