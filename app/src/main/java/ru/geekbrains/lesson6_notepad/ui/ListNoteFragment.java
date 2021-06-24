package ru.geekbrains.lesson6_notepad.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import ru.geekbrains.lesson6_notepad.MainActivity;
import ru.geekbrains.lesson6_notepad.Navigation;
import ru.geekbrains.lesson6_notepad.Note;
import ru.geekbrains.lesson6_notepad.NotesSource;
import ru.geekbrains.lesson6_notepad.R;
import ru.geekbrains.lesson6_notepad.observe.Publisher;
import ru.geekbrains.lesson6_notepad.ui.ContentFragment;
import ru.geekbrains.lesson6_notepad.ui.NotesAdapter;
import static ru.geekbrains.lesson6_notepad.ui.ContentFragment.CURRENT_DATA;
import static ru.geekbrains.lesson6_notepad.ui.ContentFragment.CURRENT_NOTE;

public class ListNoteFragment extends Fragment {

    //private boolean isLandscape;
    //private Note[] notes;
    private Note currentNote;
    private NotesSource data;
    private NotesAdapter adapter;
    private RecyclerView recyclerView;
    private Navigation navigation;
    private Publisher publisher;
    private boolean moveToLastPosition;

    public static ListNoteFragment newInstance() {
        return new ListNoteFragment();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (data == null) {
            data = new NotesSource(getResources()).init();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initList(view);
        recyclerView = view.findViewById(R.id.notes_recycler_view);
        initRecyclerView(recyclerView, data);
        setHasOptionsMenu(true);

/*        // Определение, можно ли будет расположить текст в другом фрагменте
        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;*/

//      Из onActivityCreated(@Nullable Bundle savedInstanceState)
        if (savedInstanceState != null) {
            data = savedInstanceState.getParcelable(CURRENT_DATA);
            currentNote = savedInstanceState.getParcelable(CURRENT_NOTE);
        } else {
            currentNote = data.getNote(0);
        }

        // Если можно вывести рядом текст, то сделаем это
/*        if (isLandscape) {
            showLandContent(currentNote);
        }*/
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
       MainActivity activity = (MainActivity) context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

/*    // создаём список на экране
    private void initList(View view) {
*//*        notes = new Note[]{
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
        };*//*
        // В этом цикле создаём элемент TextView, заполняем его значениями, и добавляем на экран.
        // Кроме того, создаём обработку касания на элемент

*//*        for (Note note : notes) {
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
        }*//*
    }*/

    private void initRecyclerView(RecyclerView recyclerView, NotesSource data) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        if (moveToLastPosition) {
            recyclerView.smoothScrollToPosition(data.size() - 1);
            moveToLastPosition = false;
        }

        adapter = new NotesAdapter(data, this);
        adapter.setOnItemClickListener((position, note) -> {
            navigation.addFragment(ContentFragment.newInstance(data.getNote(position)), true);
            publisher.subscribe(note1 -> {
                data.changeNote(position, note1);
                adapter.notifyItemChanged(position);
            });
        });

        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getContext(), R.drawable.separator)));
        recyclerView.addItemDecoration(itemDecoration);
    }


    // Сохраним текущую позицию (вызывается перед выходом из фрагмента)
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(ContentFragment.CURRENT_NOTE, currentNote);
        outState.putParcelable(CURRENT_DATA, data);
        super.onSaveInstanceState(outState);
    }

/*    private void showContent(Note currentNote) {
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
    }*/

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v,
                                    @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getMenuPosition();
        if (item.getItemId() == R.id.menu_delete_note) {
            data.deleteNote(position);
            adapter.notifyItemRemoved(position);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem addNote = menu.findItem(R.id.menu_add_note);
        addNote.setOnMenuItemClickListener(item -> {
            navigation.addFragment(ContentFragment.newInstance(), true);
            publisher.subscribe(note -> {
                data.addNote(note);
                adapter.notifyItemInserted(data.size() - 1);
                moveToLastPosition = true;
            });
            return true;
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}