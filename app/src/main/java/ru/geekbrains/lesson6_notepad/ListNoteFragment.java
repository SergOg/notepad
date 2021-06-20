package ru.geekbrains.lesson6_notepad;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.Objects;

public class ListNoteFragment extends Fragment {

    private boolean isLandscape;
    private Note[] notes;
    private Note currentNote;

    // Сохраним текущую позицию (вызывается перед выходом из фрагмента)
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(ContentFragment.CURRENT_NOTE, currentNote);
        super.onSaveInstanceState(outState);
    }

/*    // activity создана, можно к ней обращаться. Выполним начальные действия
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Определение, можно ли будет расположить текст в другом фрагменте
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState != null) {
            currentNote = savedInstanceState.getParcelable(ContentFragment.CURRENT_NOTE);
        } else {
            currentNote = notes[0];
        }
        // Если можно вывести рядом текст, то сделаем это
        if (isLandscape) {
            showLandContent(currentNote);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
        RecyclerView recyclerView = view.findViewById(R.id.notes_recycler_view);
        initRecyclerView(recyclerView, notes);

        // Определение, можно ли будет расположить текст в другом фрагменте
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState != null) {
            currentNote = savedInstanceState.getParcelable(ContentFragment.CURRENT_NOTE);
        } else {
            currentNote = notes[0];
        }
        // Если можно вывести рядом текст, то сделаем это
        if (isLandscape) {
            showLandContent(currentNote);
        }
    }

    // создаём список на экране
    private void initList(View view) {
        notes = new Note[]{
                new Note(getString(R.string.first_note_title), getString(R.string.first_note_content), Calendar.getInstance()),
                new Note(getString(R.string.second_note_title), getString(R.string.second_note_content), Calendar.getInstance()),
                new Note(getString(R.string.third_note_title), getString(R.string.third_note_content), Calendar.getInstance()),
                new Note(getString(R.string.fourth_note_title), getString(R.string.fourth_note_content), Calendar.getInstance()),
                new Note(getString(R.string.fifth_note_title), getString(R.string.fifth_note_content), Calendar.getInstance()),
                new Note(getString(R.string.sixth_note_title), getString(R.string.sixth_note_content), Calendar.getInstance()),
                new Note(getString(R.string.seventh_note_title), getString(R.string.seventh_note_content), Calendar.getInstance()),
                new Note(getString(R.string.eighth_note_title), getString(R.string.eighth_note_content), Calendar.getInstance()),
                new Note(getString(R.string.ninth_note_title), getString(R.string.ninth_note_content), Calendar.getInstance()),
                new Note(getString(R.string.tenth_note_title), getString(R.string.tenth_note_content), Calendar.getInstance()),
                new Note(getString(R.string.eleventh_note_title), getString(R.string.eleventh_note_content), Calendar.getInstance()),
                new Note(getString(R.string.twelfth_note_title), getString(R.string.twelfth_note_content), Calendar.getInstance()),
        };
        // В этом цикле создаём элемент TextView, заполняем его значениями, и добавляем на экран.
        // Кроме того, создаём обработку касания на элемент

/*        for (Note note : notes) {
            Context context = getContext();
            if (context != null) {
                LinearLayout linearView = (LinearLayout) view;      //наш view кастим в LinearLayout
                TextView firstTextView = new TextView(context);
                TextView secondTextView = new TextView(context);
                firstTextView.setText(note.getTitle());
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
                secondTextView.setText(formatter.format(note.getCreationDate().getTime()));

                linearView.addView(firstTextView);
                linearView.addView(secondTextView);
                firstTextView.setPadding(0, 30, 0, 0);

                firstTextView.setOnClickListener(v -> showContent(note));
            }
        }*/
    }

    private void initRecyclerView(RecyclerView recyclerView, Note[] notes) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        NotesAdapter adapter = new NotesAdapter(notes);
        adapter.setOnItemClickListener((position, note) -> {
            currentNote = note;
            showContent(currentNote);
        });
        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(),
                LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getContext(), R.drawable.separator)));
        recyclerView.addItemDecoration(itemDecoration);
    }

    private void showContent(Note currentNote) {
        if (isLandscape) {
            showLandContent(currentNote);
        } else {
            showPortContent(currentNote);
        }
    }

    // Показать text в ландшафтной ориентации
    private void showLandContent(Note currentNote) {
        ContentFragment fragment = ContentFragment.newInstance(currentNote);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.note_content, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    // Показать текст в портретной ориентации.
    private void showPortContent(Note currentNote) {
        ContentFragment fragment = ContentFragment.newInstance(currentNote);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack("list_fragment");
        fragmentTransaction.replace(R.id.list_notes_fragment, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }
}