package telega;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface NoteService {
    void addNote(Note note) throws Exception;

    void deleteNote(int id) throws Exception;

    List<Note> getUserNoteList(long userId) throws Exception;

}
