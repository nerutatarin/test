package telega;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBNoteServiceImpl implements NoteService {

    ReadFileLineByLine test = new ReadFileLineByLine();

    Map<String, String> map = test.getDBPropConn();

    String url = map.get("url");
    String user = map.get("user");
    String password = map.get("password");

    public void addNote(Note note) throws Exception {

        System.out.println(note.getTitle() + " " + note.getText());

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = getAddNoteQuery();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, note.getTitle());
            ps.setString(2, note.getText());
            ps.setLong(3, note.getUserId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    protected String getAddNoteQuery() {
        return "insert into notes (title, text, user_id) values (?, ?, ?)";
    }

    public void deleteNote(int id) throws Exception {
        System.out.println("Удаляется заметка " + id);

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = getDeleteNoteQuery();

            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, id);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    protected String getDeleteNoteQuery() {
        return "delete from notes where id = ? ";
    }

    public List<Note> getUserNoteList(long userId) throws Exception {

        List<Note> list = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = getUserNoteListQuery();

            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Note note = new Note(rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        userId);

                list.add(note);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new Exception(e);
        }
        return list;
    }

    protected String getUserNoteListQuery() {
        return "SELECT id, title, text FROM notes WHERE user_id = ?";
    }

}
