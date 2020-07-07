package telega;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class NoteOperations {
    String url = "jdbc:mysql://127.0.0.1:3306/telega";
    String user = "root";
    String password = "root";

    void addNote(Note note) throws Exception {
        System.out.println(note.title + " " + note.text);

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = "insert into notes (title, text, user_id) values (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, note.title);
            ps.setString(2, note.text);
            ps.setLong(3, note.userId);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    void deleteNote(Note note) throws Exception {
        System.out.println("Удаляется заметка " + note.id);

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = "delete from notes where id=(?)";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, note.id);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    public ArrayList getListNote(Note note) throws Exception {

        ArrayList<String> list = null;
        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT id, title FROM notes WHERE user_id = ?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, note.userId);

            ResultSet rs = ps.executeQuery();

            list = new ArrayList<String>();

            while (rs.next()) {

                note.id = rs.getInt("id");
                String s = String.valueOf(note.id);
                note.title = rs.getString("title");
                Collections.addAll(list, s, note.title);
            }

            for (String x : list) {
                System.out.println(x);
            }
            //ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new Exception(e);
        }
        return list;
    }

}
