package com.example.repetich;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class ConnectionUtil {

    private static final String ADMIN_LOGIN = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    // ---------- Подключение ----------
    public static Connection ConnectDb() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.1.141:3306/toor11",
                    "toor11",
                    "toor11"
            );
            System.out.println("Соединение установлено");
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
            showAlert("Ошибка подключения", "Не удалось подключиться к БД: " + e.getMessage());
            return null;
        }
    }

    // ---------- Авторизация ----------
    public static String authorize(String login, String password) {
        // 1. Админ — жёстко задан
        if (ADMIN_LOGIN.equals(login) && ADMIN_PASSWORD.equals(password)) {
            return "ADMIN";
        }
        // 2. Обычный пользователь — ищем в БД
        String sql = "SELECT id FROM polzovateli WHERE login = ? AND parol = ?";
        try (Connection conn = ConnectDb();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (conn == null) return null;
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return "USER";
        } catch (SQLException e) {
            System.err.println("Ошибка авторизации: " + e.getMessage());
        }
        return null;
    }

    // ==================== ТОВАРЫ ====================
    public static ObservableList<Tablich1> getDataUsers() {
        ObservableList<Tablich1> list = FXCollections.observableArrayList();
        String sql = "SELECT id, name, chen FROM tovar";
        try (Connection conn = ConnectDb();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (conn == null) return list;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Tablich1(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("chen")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка получения товаров: " + e.getMessage());
            showAlert("Ошибка данных", "Ошибка получения товаров: " + e.getMessage());
        }
        return list;
    }

    public static boolean addUser(Tablich1 user) {
        if (!Session.isAdmin()) {
            showAlert("Доступ запрещён", "Только администратор может добавлять товары");
            return false;
        }
        String sql = "INSERT INTO tovar (id, name, chen) VALUES (?, ?, ?)";
        try (Connection conn = ConnectDb();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getChen());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка добавления товара: " + e.getMessage());
            showAlert("Ошибка", "Не удалось добавить товар: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateUser(Tablich1 user) {
        if (!Session.isAdmin()) {
            showAlert("Доступ запрещён", "Только администратор может изменять товары");
            return false;
        }
        String sql = "UPDATE tovar SET name = ?, chen = ? WHERE id = ?";
        try (Connection conn = ConnectDb();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getChen());
            pstmt.setInt(3, user.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка изменения товара: " + e.getMessage());
            showAlert("Ошибка", "Не удалось изменить товар: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteUser(int id) {
        if (!Session.isAdmin()) {
            showAlert("Доступ запрещён", "Только администратор может удалять товары");
            return false;
        }
        String sql = "DELETE FROM tovar WHERE id = ?";
        try (Connection conn = ConnectDb();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка удаления товара: " + e.getMessage());
            showAlert("Ошибка", "Не удалось удалить товар: " + e.getMessage());
            return false;
        }
    }

    // ==================== ПОЛЬЗОВАТЕЛИ ====================
    public static ObservableList<Polzovatel> getPolzovateli() {
        ObservableList<Polzovatel> list = FXCollections.observableArrayList();
        String sql = "SELECT id, name, family, login, parol FROM polzovateli";
        try (Connection conn = ConnectDb();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (conn == null) return list;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Polzovatel(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("family"),
                        rs.getString("login"),
                        rs.getString("parol")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка получения пользователей: " + e.getMessage());
            showAlert("Ошибка данных", "Ошибка получения пользователей: " + e.getMessage());
        }
        return list;
    }

    public static boolean addPolzovatel(Polzovatel p) {
        if (!Session.isAdmin()) {
            showAlert("Доступ запрещён", "Только администратор может добавлять пользователей");
            return false;
        }
        String sql = "INSERT INTO polzovateli (id, name, family, login, parol) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectDb();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, p.getId());
            pstmt.setString(2, p.getName());
            pstmt.setString(3, p.getFamily());
            pstmt.setString(4, p.getLogin());
            pstmt.setString(5, p.getParol());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка добавления пользователя: " + e.getMessage());
            showAlert("Ошибка", "Не удалось добавить пользователя: " + e.getMessage());
            return false;
        }
    }

    public static boolean updatePolzovatel(Polzovatel p) {
        if (!Session.isAdmin()) {
            showAlert("Доступ запрещён", "Только администратор может изменять пользователей");
            return false;
        }
        String sql = "UPDATE polzovateli SET name = ?, family = ?, login = ?, parol = ? WHERE id = ?";
        try (Connection conn = ConnectDb();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getName());
            pstmt.setString(2, p.getFamily());
            pstmt.setString(3, p.getLogin());
            pstmt.setString(4, p.getParol());
            pstmt.setInt(5, p.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка изменения пользователя: " + e.getMessage());
            showAlert("Ошибка", "Не удалось изменить пользователя: " + e.getMessage());
            return false;
        }
    }

    public static boolean deletePolzovatel(int id) {
        if (!Session.isAdmin()) {
            showAlert("Доступ запрещён", "Только администратор может удалять пользователей");
            return false;
        }
        String sql = "DELETE FROM polzovateli WHERE id = ?";
        try (Connection conn = ConnectDb();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Ошибка удаления пользователя: " + e.getMessage());
            showAlert("Ошибка", "Не удалось удалить пользователя: " + e.getMessage());
            return false;
        }
    }

    // ---------- Вспомогательное ----------
    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}