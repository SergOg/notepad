package ru.geekbrains.lesson6_notepad.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import ru.geekbrains.lesson6_notepad.MainActivity;
import ru.geekbrains.lesson6_notepad.data.Note;
import ru.geekbrains.lesson6_notepad.R;
import ru.geekbrains.lesson6_notepad.observe.Publisher;

public class ContentFragment extends Fragment {

    public static final String CURRENT_NOTE = "currentNote";
    //public static final String CURRENT_DATA = "currentData";
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

        if (note != null) {
            creationData = note.getCreationDate();
            contentView(view);
        }

        if (isNewNote) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
            creationData = String.format("%s: %s", "Дата создания", formatter.format(Calendar.getInstance().getTime()));
            contentView(view);
        }

        setHasOptionsMenu(true);
        return view;
    }

    private void contentView(View view) {
        if (isNewNote) {
            dateOfCreationText.setText(creationData);
        } else {
            dateOfCreationText.setText((CharSequence) note.getCreationDate());
            titleText.setText(note.getTitle());
            contentText.setText(note.getContent());
        }
    }

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
        if (note != null) {
            Note answer = new Note(title, content, creationData);
            answer.setId(note.getId());
            return answer;
        } else {
            return new Note(title, content, creationData);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem addNote = menu.findItem(R.id.menu_add_note);
        MenuItem search = menu.findItem(R.id.menu_search);
        MenuItem sort = menu.findItem(R.id.menu_sort);
        addNote.setVisible(false);
        search.setVisible(false);
        sort.setVisible(false);
        MenuItem send = menu.findItem(R.id.menu_send);
        send.setOnMenuItemClickListener(item -> {
            Toast.makeText(getActivity(), R.string.menu_send, Toast.LENGTH_SHORT).show();
            return true;
        });
        MenuItem addPhoto = menu.findItem(R.id.menu_add_photo);
        addPhoto.setOnMenuItemClickListener(item -> {
            Toast.makeText(getActivity(), R.string.menu_add_photo, Toast.LENGTH_SHORT).show();
            return true;
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}