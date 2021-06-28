package ru.geekbrains.lesson6_notepad.observe;

import ru.geekbrains.lesson6_notepad.data.Note;

public interface Observer {
    void updateNote(Note note);
}
