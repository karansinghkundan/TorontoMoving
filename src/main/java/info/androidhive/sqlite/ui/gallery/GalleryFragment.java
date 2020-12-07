package info.androidhive.sqlite.ui.gallery;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.DatabaseHelper;
import info.androidhive.sqlite.database.model.Note;
import info.androidhive.sqlite.utils.CustomImageView;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private DatabaseHelper db;
    private List<Note> notesList = new ArrayList<>();
    String firstNameStr ="", lastNameStr="";

    String imagePath = "";


    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    ImageView back;

    CustomImageView profilePic;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
//        final TextView textView = root.findViewById(R.id.text_gallery);

        final EditText firstName = root.findViewById(R.id.firstName);
        final EditText lastName = root.findViewById(R.id.lastName);
        final Button saveBtn = root.findViewById(R.id.saveBtn);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        back = (ImageView) root.findViewById(R.id.back);
        back.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

          profilePic = root.findViewById(R.id.profile_pic);

        db = new DatabaseHelper(getContext());
        notesList.addAll(db.getAllNotes());

        if(notesList != null && notesList.size()>0){
            Note profile = notesList.get(notesList.size()-2);
            if(profile != null){
                String userDetails = profile.getNote().toString();
                String[] parts = userDetails.split("-");
                String part1 = parts[0]; // 004
                String part2 = parts[1];
                String imgPath = parts[2];
                if(part1 != null)
                    firstName.setText(part1);
                if(part2 != null)
                    lastName.setText(part2);
                if(imgPath != null){

//                    Bitmap bmImg = BitmapFactory.decodeFile(imgPath);
//                    profilePic.setImageBitmap(bmImg);
                    imagePath = imgPath;

                    File imgFile = new  File(imgPath);
                    if(imgFile.exists()){

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


                        profilePic.setImageBitmap(myBitmap);

                    };
                }
            }
        }


        saveBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                firstNameStr = firstName.getText().toString();
                lastNameStr = lastName.getText().toString();

               imagePath = imagePath;

                if(firstNameStr.trim().length()>0){

                    Note n = null;
                    if(notesList.size()>0){
                         n = notesList.get(notesList.size()-2);
                    }

                    if(n !=null){
                        updateNote(firstNameStr+"-"+lastNameStr+"-"+imagePath,notesList.size()-2);
                        Toast.makeText(getActivity(),
                                "Profile Saved Successfully", Toast.LENGTH_LONG).show();

                    }else{
                        createNote(firstNameStr+"-"+lastNameStr+"-"+imagePath);
                        Toast.makeText(getActivity(),
                                "Profile Saved Successfully", Toast.LENGTH_LONG).show();

                    }

                }else{
                    Toast.makeText(getActivity(),
                            "Please enter First Name", Toast.LENGTH_LONG).show();
                }
            }
        });


        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(),
//                        "The favorite list would appear on clicking this icon",
//                        Toast.LENGTH_LONG).show();

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });



        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    profilePic.setImageDrawable(getResources().getDrawable(R.drawable.profile, getContext().getTheme()));
//                } else {
//                    profilePic.setImageDrawable(getResources().getDrawable(R.drawable.profile));
//                }
            }
        });
        return root;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
//           imagePath = data.getData().getPath();


            profilePic.setImageBitmap(photo);

            saveImage(photo);



        }
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

    public void saveImage(Bitmap bitmap){

        FileOutputStream outStream = null;

// Write to SD Card
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/camtest");
            dir.mkdirs();

            String fileName = String.format("%d.jpg", System.currentTimeMillis());
            File outFile = new File(dir, fileName);
            imagePath = dir+"/"+fileName;

            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();


//            refreshGallery(outFile);
        } catch (FileNotFoundException e) {
//            print("FNF");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

}