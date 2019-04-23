package com.example.mymapapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mymapapplication.database.MyUserDBHelper;
import com.example.mymapapplication.database.UserListAdapter;

public class UserListFragment extends Fragment {

    private View view;
    private MyUserDBHelper userDbHelper;
    private UserListAdapter mCursorAdapter;

    private Cursor cursor;

    String userEmailId;

    public UserListFragment() {
        //Required
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_list, container, false);

        userDbHelper = new MyUserDBHelper(getContext());

        loadUserData();
        return view;
    }

    public void loadUserData(){

        ListView usersListView = view.findViewById(R.id.listView_Users);
        TextView emptyView = view.findViewById(R.id.empty_text_view);
        usersListView.setEmptyView(emptyView);

        Cursor readCursor = userDbHelper.readUsers();
        mCursorAdapter = new UserListAdapter(view.getContext(), readCursor);
        usersListView.setAdapter(mCursorAdapter);

        synchronized(mCursorAdapter){
            mCursorAdapter.notify();
        }


    }
}
