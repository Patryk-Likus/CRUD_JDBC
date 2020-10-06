import java.sql.*;


    public class DataBase {

        private static final String JDBC_DRIVER = "org.postgresql.Driver";
        private static final String URL = "jdbc:postgresql://localhost/Praca";
        private static final String LOGIN = "postgres";
        private static final String PASSWORD = "postgres#";

        private Statement stmt;
        private Connection con;


        public void connectDb() throws ClassNotFoundException, SQLException {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            stmt = con.createStatement();
        }

        public void disconnectDb() throws SQLException {
            con.close();
            stmt.close();
        }
    }


