//package org.awesomeapp.messenger.ui;
//
//import android.app.Activity;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.support.annotation.ColorInt;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.flask.colorpicker.ColorPickerView;
//import com.flask.colorpicker.OnColorSelectedListener;
//import com.flask.colorpicker.builder.ColorPickerClickListener;
//import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
//
//import org.awesomeapp.messenger.ImApp;
//import org.awesomeapp.messenger.MainActivity;
//import org.awesomeapp.messenger.tasks.AddContactAsyncTask;
//
//import im.zom.messenger.R;
//
//public class MoreFragment extends Fragment {
//
////    public static MoreFragment newInstance(String param1, String param2) {
////        MoreFragment fragment = new MoreFragment();
////        Bundle args = new Bundle();
////        fragment.setArguments(args);
////        return fragment;
////    }
//
//    public MoreFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_more, container, false);
//        View btn = view.findViewById(R.id.btnOpenGroups);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ((MainActivity)getActivity()).showGroupChatDialog();
//
//            }
//        });
//        return view;
//    }
//    @Override
//    public void onResume() {
//        super.onResume();
//
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//}
