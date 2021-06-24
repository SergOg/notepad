package ru.geekbrains.lesson6_notepad.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import ru.geekbrains.lesson6_notepad.MainActivity;
import ru.geekbrains.lesson6_notepad.Note;
import ru.geekbrains.lesson6_notepad.R;
import ru.geekbrains.lesson6_notepad.observe.Publisher;

public class ContentFragment extends Fragment {

    public static final String CURRENT_NOTE = "currentNote";
    public static final String CURRENT_DATA = "currentData";
    private Note note;
    private Publisher publisher;

    private TextInputEditText titleText;
    private TextInputEditText contentText;
    private TextView dateOfCreationText;

    private String creationData;
    private boolean isNewNote = false;

    // Фабричный метод создания фрагмента
    // Фрагменты рекомендуется создавать через фабричные методы.
    public static ContentFragment newInstance(Note note) {
        ContentFragment fragment = new ContentFragment();   // создание
        Bundle args = new Bundle();
        args.putParcelable(CURRENT_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    public static ContentFragment newInstance() {
        return new ContentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(CURRENT_NOTE);
        }
        if (note == null) {
            isNewNote = true;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);

        titleText = view.findViewById(R.id.note_title);
        contentText = view.findViewById(R.id.note_content);
        dateOfCreationText = view.findViewById(R.id.note_date);

        if (note != null){
            creationData = note.getCreationDate();
            contentView(view);
        }

        if (isNewNote) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
            creationData = String.format("%s: %s", "Дата создания", formatter.format(Calendar.getInstance().getTime()));
            contentView(view);
        }
/*        titleText.setText(note.getTitle());
        contentText.setText(note.getContent());*/

        //view.setBackgroundColor(note.getColor());
        setHasOptionsMenu(true);
        return view;
    }

    private void contentView(View view) {
        if (isNewNote) {
            dateOfCreationText.setText(creationData);
            //view.setBackgroundColor(color);
        } else {
            dateOfCreationText.setText((CharSequence) note.getCreationDate());
            titleText.setText(note.getTitle());
            contentText.setText(note.getContent());
            //view.setBackgroundColor(note.getColor());
        }
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

    @Override
    public void onStop() {
        super.onStop();
        note = collectNote();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.notifySingle(note);
    }
    private Note collectNote() {
        String title = Objects.requireNonNull(this.titleText.getText()).toString();
        String content = Objects.requireNonNull(this.contentText.getText()).toString();
        if (isNewNote) {
            isNewNote = false;
        }
        return new Note(title, content, creationData);
    }
}