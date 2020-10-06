
import java.sql.*;
import java.util.Scanner;

public class DataBase {

    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://localhost/Praca";
    private static final String LOGIN = "postgres";
    private static final String PASSWORD = "postgres#";

    private Statement stmt;
    private Connection con;
    String name1;
    String surname1;
    int age1;
    String gender1;
    ResultSet rs;
    PreparedStatement ps;
    String querySelect = "SELECT * FROM PRACOWNICY";
    String queryInsert = "INSERT INTO PRACOWNICY VALUES (?, ?, ?, ?, ?)";


    public void start() throws SQLException, ClassNotFoundException {
        System.out.println("Witaj w bazie danych");
        System.out.println("Co chcesz wykonać? Dodać, usunąć, wyświetlić, zakutualizować dane?");
        System.out.println("1 = Add | 2 = Delete | 3 = Select | 4 = Update");

        Scanner scanner = new Scanner(System.in);
        int info = scanner.nextInt();

        if (info == 1) {

            System.out.println("Podaj imie");
            name1 = scanner.next();

            System.out.println("Podaj nazwisko");
            surname1 = scanner.next();

            System.out.println("Podaj wiek");
            age1 = scanner.nextInt();

            System.out.println("Podaj płeć");
            gender1 = scanner.next();

            add();

        } else if (info == 2) {
            System.out.println("Podaj numer ID użytkownika, którego chcesz usunąć:");
            int userID = scanner.nextInt();
            delete(userID);

        } else if (info == 3) {

            select();

        } else if (info == 4) {
            System.out.println("Podaj numer ID użytkownika, którego chcesz zaktualizować:");
            int userID = scanner.nextInt();

            System.out.println("co chcesz zakutalizować? ");
            System.out.println("1 = imie | 2 = nazwisko | 3 = wiek | 4 = płeć ");

            int number = scanner.nextInt();

            if (number == 1) {
                System.out.println("Podaj imie:");
                String name2 = scanner.next();
                update(userID, name2, number);
            } else if (number == 2) {
                System.out.println("Podaj nazwisko:");
                String surname2 = scanner.next();
                update(userID, surname2, number);
            } else if (number == 3) {
                System.out.println("Podaj wiek:");
                String age2 = String.valueOf(scanner.nextInt());
                update(userID, age2, number);
            } else if (number == 4) {
                System.out.println("Podaj płeć:");
                String gender2 = scanner.next();
                update(userID, gender2, number);
            }

        }
    }

    public void add() throws SQLException, ClassNotFoundException {
        try {
            connectDb();

            rs = stmt.executeQuery(querySelect);

            ps = con.prepareStatement(queryInsert);

            while (rs.next()) {
                int id = rs.getInt("id_pracownicy");

                if (rs.isLast()) {
                    ps.setInt(1, id + 1);
                    ps.setString(2, name1);
                    ps.setString(3, surname1);
                    ps.setInt(4, age1);
                    ps.setString(5, gender1);
                    ps.executeUpdate();
                    System.out.println("Pomyślnie dodano użytkownika do bazy danych.");
                }
            }
            ps.close();
            rs.close();
            disconnectDb();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int userID) {
        try {
            connectDb();
            rs = stmt.executeQuery(querySelect);

            while (rs.next()) {
                int id = rs.getInt("id_pracownicy");
                if (id == userID) {
                    ps = con.prepareStatement("Delete FROM PRACOWNICY WHERE id_pracownicy = " + userID + ";");
                    ps.executeUpdate();
                }
            }
            System.out.println("Usunięto użytkownika z bazy danych.");
            rs.close();
            ps.close();
            disconnectDb();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void select() throws SQLException, ClassNotFoundException {
        try {
            connectDb();
            rs = stmt.executeQuery(querySelect);
            while (rs.next()) {
                int id = rs.getInt("id_pracownicy");
                String name = rs.getString("imie");
                String surname = rs.getString("nazwisko");
                int age = rs.getInt("wiek");
                String gender = rs.getString("plec");

                System.out.printf("ID: %-10d\t Imie: %-10s\t Nazwisko: %-10s\t Wiek: %-10d\t Płeć: %s\n", id, name, surname, age, gender);
            }
            System.out.println("Baza danych wyświetlona pomyślnie.");
            rs.close();
            disconnectDb();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int userID, String data, int number) {
        try {
            connectDb();
            rs = stmt.executeQuery(querySelect);

            while (rs.next()) {
                int id = rs.getInt("id_pracownicy");
                if (id == userID) {
                    if (number == 1) {
                        ps = con.prepareStatement("UPDATE PRACOWNICY SET IMIE ='" + data + "' WHERE id_pracownicy = " + userID + ";");
                        ps.executeUpdate();
                    } else if (number == 2) {
                        ps = con.prepareStatement("UPDATE PRACOWNICY SET NAZWISKO ='" + data + "' WHERE id_pracownicy = " + userID + ";");
                        ps.executeUpdate();
                    } else if (number == 3) {
                        ps = con.prepareStatement("UPDATE PRACOWNICY SET WIEK ='" + data + "' WHERE id_pracownicy = " + userID + ";");
                        ps.executeUpdate();
                    } else if (number == 4) {
                        ps = con.prepareStatement("UPDATE PRACOWNICY SET PŁEĆ ='" + data + "' WHERE id_pracownicy = " + userID + ";");
                        ps.executeUpdate();
                    }
                }
            }
            System.out.println("Zaktualizowano użytkownika w bazie danych.");
            rs.close();
            ps.close();
            disconnectDb();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


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
