package telega;

import java.util.List;

public interface NoteService {
    void addNote(Note note) throws Exception;

    void deleteNote(int id) throws Exception;

    List<Note> getUserNoteList(long userId) throws Exception;
}
