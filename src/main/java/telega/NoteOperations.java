package telega;

import java.sql.*;

public class NoteOperations {
    String url = "jdbc:mysql://127.0.0.1:3306/telega";
    String user = "root";
    String password = "";

    void addNote(Note note) throws Exception {
        System.out.println("Title: " + note.title + " Text: " + note.text);

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

    void deleteNote(int noteId) throws Exception {
        System.out.println("Удаляется заметка " + noteId);

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = "delete from notes where id=(?)";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, noteId);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    void listNote(long userId) throws Exception {
        //System.out.println("Список заметок:");

        try (Connection con = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT id, title FROM notes WHERE user_id = ?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(1, userId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                int id = rs.getInt("id");
                String title = rs.getString("title");
                //String text = rs.getString("text");

                System.out.printf("%d. %s. \n", id, title);

            }

            //ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new Exception(e);
        }

    }

}
