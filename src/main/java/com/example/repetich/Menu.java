package com.example.repetich;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Menu {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private Button nzd;
    @FXML private Button pol;
    @FXML private Button tov;

    // "Назад" — возврат на форму авторизации
    @FXML
    void nzd_button(ActionEvent event) {
        Session.clear();
        openScene("hello-view.fxml", "Авторизация");
    }

    @FXML
    void pol_button(ActionEvent event) {
        if (!Session.isAdmin()) {
            return; // доп. защита
        }
        openScene("tablpolz.fxml", "Пользователи");
    }

    @FXML
    void tov_button(ActionEvent event) {
        openScene("Tabltovar.fxml", "Товары");
    }

    @FXML
    void vixod_button(ActionEvent event) {
        Session.clear();
        System.exit(0);
    }

    // Универсальный метод открытия сцены
    private void openScene(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            // Берём окно из любой доступной кнопки (nzd / tov / pol)
            Stage stage = (Stage) tov.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        // Для пользователя скрываем кнопку "Пользователи"
        if (!Session.isAdmin() && pol != null) {
            pol.setVisible(false);
            pol.setManaged(false);
        }
    }
}