package ru.geekbrains.lesson6_notepad;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ContentFragment extends Fragment {

    public static final String CURRENT_NOTE = "currentNote";
    private Note note;

    // Фабричный метод создания фрагмента
    // Фрагменты рекомендуется создавать через фабричные методы.
    public static ContentFragment newInstance(Note note) {
        ContentFragment fragment = new ContentFragment();   // создание
        Bundle args = new Bundle();
        args.putParcelable(CURRENT_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(CURRENT_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        TextInputEditText titleText = view.findViewById(R.id.note_title);
        TextInputEditText contentText = view.findViewById(R.id.note_content);
        TextView dateOfCreationText = view.findViewById(R.id.note_date);

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
        dateOfCreationText.setText(String.format("%s: %s", "Created", formatter.format(note.getCreationDate().getTime())));
        titleText.setText(note.getTitle());
        contentText.setText(note.getContent());
        //view.setBackgroundColor(note.getColor());
        setHasOptionsMenu(true);
        return view;
    }

/*    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // найти в контейнере элемент
        TextView titleText = view.findViewById(R.id.note_title);
        TextView noteContentText = view.findViewById(R.id.note_content);
        TextView dateText = view.findViewById(R.id.note_date);

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
        dateText.setText(String.format("%s", formatter.format(note.getCreationDate().getTime())));
        titleText.setText(note.getTitle());
        noteContentText.setText(note.getContent());
    }*/
}