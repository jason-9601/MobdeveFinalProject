package com.mobdeve.s12.mobdevefinalproject.notes;

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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NotesFragment extends Fragment {

    private ArrayList<Notes> notesList;
    private RecyclerView rvAddNotes;
    private RecyclerView.LayoutManager layoutManager;
    private NotesAdapter notesAdapter;

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private DatabaseHelper dbHelper;
    private String loggedInUser;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public NotesFragment() {

    }

    public static NotesFragment newInstance(String param1, String param2) {
        NotesFragment fragment = new NotesFragment();
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
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        FloatingActionButton fabAddNotes = view.findViewById(R.id.fab_add_notes);
        fabAddNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotesInput.class);
                startActivity(intent);
            }
        });

        this.notesList = new ArrayList<Notes>();

        dbHelper = new DatabaseHelper(getActivity());
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        spEditor = this.sp.edit();

        // Print logged in user to Logcat for checking //
        loggedInUser = sp.getString("KEY_USERNAME_LOGGED_IN", "N/A");
        Log.d("Logged in user: ", loggedInUser);

        readAllNotesTable();
        initContent(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        notesList.clear();
        readAllNotesTable();
        notesAdapter.notifyDataSetChanged();
    }

    private void readAllNotesTable() {
        Cursor cursor = dbHelper.readAllUserNotesTable(loggedInUser);
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String text = cursor.getString(2);
                String backgroundColor = cursor.getString(3);
                String fontColor = cursor.getString(4);

                notesList.add(new Notes(id, text, Integer.parseInt(fontColor), Integer.parseInt(backgroundColor)));
            }
        }
    }

    public void initContent(View view) {
        this.notesAdapter = new NotesAdapter(notesList);
        this.layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        this.rvAddNotes = view.findViewById(R.id.rv_add_notes);
        this.rvAddNotes.setLayoutManager(this.layoutManager);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvAddNotes);
        this.rvAddNotes.setAdapter(this.notesAdapter);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
            String swipedNoteId = notesList.get(viewHolder.getAdapterPosition()).getNoteId();
            dbHelper.deleteNote(swipedNoteId);
            notesList.remove(viewHolder.getAdapterPosition());
            notesAdapter.notifyDataSetChanged();
        }
    };

}
