package info.androidhive.sqlite.ui.slideshow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.DatabaseHelper;
import info.androidhive.sqlite.database.model.Note;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    ImageView back;
    private DatabaseHelper db;
    private List<Note> notesList = new ArrayList<>();
    String creditCardStr ="", expiryStr="";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);

        final EditText creditCard = root.findViewById(R.id.creditCard);
        final EditText expiry = root.findViewById(R.id.expiry);
        final Button saveCC = root.findViewById(R.id.saveCC);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        back = (ImageView) root.findViewById(R.id.back);
        back.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        db = new DatabaseHelper(getContext());
        notesList.addAll(db.getAllNotes());

        if(notesList != null && notesList.size()>0){
            Note payment = notesList.get(notesList.size()-1);
            if(payment != null){
                String userDetails = payment.getNote().toString();
                String[] parts = userDetails.split("-");
                String part1 = parts[0]; // 004
                String part2 = parts[1];
                if(part1 != null)
                    creditCard.setText(part1);
                if(part2 != null)
                    expiry.setText(part2);
            }
        }



        saveCC.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                creditCardStr = creditCard.getText().toString();
                expiryStr = expiry.getText().toString();

                if(creditCardStr.trim().length()>0 && expiryStr.trim().length()>0){

                    Note n = null;
                    if(notesList.size()>0){
                        n = notesList.get(notesList.size()-1);
                    }

                    if(n !=null){
                        updateNote(creditCardStr+"-"+expiryStr,notesList.size()-1);
                        Toast.makeText(getActivity(),
                                "Payment Details Saved Successfully", Toast.LENGTH_LONG).show();

                    }else{
                        createNote(creditCardStr+"-"+expiryStr);
                        Toast.makeText(getActivity(),
                                "Payment Details Saved Successfully", Toast.LENGTH_LONG).show();

                    }

                }else if(creditCardStr.trim().length()>0){
                    Toast.makeText(getActivity(),
                            "Please enter Expiry", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(),
                            "Please enter Credit Card Number", Toast.LENGTH_LONG).show();
                }
            }
        });

        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    private void createNote(String note) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertNote(note);

        // get the newly inserted note from db
        Note n = db.getNote(id);

        if (n != null) {
            // adding new note to array list at 0 position
            notesList.add(0, n);

//            // refreshing the list
//            mAdapter.notifyDataSetChanged();
//
//            toggleEmptyNotes();
        }
    }
    private void updateNote(String note, int position) {
        Note n = notesList.get(position);
        // updating note text
        n.setNote(note);

        // updating note in db
        db.updateNote(n);

        // refreshing the list
        notesList.set(position, n);
//        mAdapter.notifyItemChanged(position);
//
//        toggleEmptyNotes();
    }

}