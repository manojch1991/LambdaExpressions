package DatabaseConnect;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseOperations {

    public void insertFile(File file) {
        Connection connection = Connections.getConnection();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            DataInputStream fin = new DataInputStream(fileInputStream);
            byte byteArray[] = new byte[(int) file.length()];
            fin.readFully(byteArray);
            PreparedStatement pStatement = connection.prepareStatement("insert into files (filename, content) VALUES ( ?,? )");
            Blob blob = connection.createBlob();
            blob.setBytes(1, byteArray);
            pStatement.setString(1, file.getName());
            pStatement.setBlob(2, blob);
            pStatement.executeUpdate();
            System.out.println(file.getName() + " has been inserted successfully.");
            pStatement.close();
        } catch (Exception e) {
            System.err.println("SQLException : " + e.getMessage());
        } finally {
            closeConnection(connection);
        }
    }

    public boolean isFileAvailable(String fileName) {
        boolean isFileAvailable = false;
        Connection connection = Connections.getConnection();

        try {
            PreparedStatement pStatement = connection.prepareStatement("select count(*) counts from files where filename = ?");
            pStatement.setString(1, fileName);
            ResultSet rs = pStatement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("counts");
                if (count > 0) {
                    isFileAvailable = true;
                }
            }
            pStatement.close();
            rs.close();
        } catch (Exception e) {
            System.err.println("SQLException : " + e.getMessage());
        } finally {
            closeConnection(connection);
        }
        return isFileAvailable;
    }

    public byte[] getFile(String fileName) {
        Connection connection = Connections.getConnection();
        byte[] byteArray = null;

        try {
            PreparedStatement pStatement = connection.prepareStatement("select content from files where filename = ?");
            pStatement.setString(1, fileName);
            ResultSet rs = pStatement.executeQuery();
            if (rs.next()) {
                Blob blob = rs.getBlob(1);
                int bloblength = (int) blob.length();
                byteArray = blob.getBytes(1, bloblength);
                blob.free();
            }
            pStatement.close();
            rs.close();
        } catch (Exception e) {
            System.err.println("SQLException : " + e.getMessage());
        } finally {
            closeConnection(connection);
        }
        return byteArray;
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Problem while closing Connection.");
            }
        }
    }
}
