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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HelloController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private Label avtor_text;
    @FXML private TextField login_text;
    @FXML private PasswordField password_text;
    @FXML private Label textininforma;
    @FXML private Button voti;

    @FXML
    void avtor_button(ActionEvent event) {
        avtor_text.setText("Работу выполнил: Красилов Кирилл ИСП-231");
    }

    @FXML
    void vixod_button(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void voiti_button(ActionEvent event) {
        String login = login_text.getText().trim();
        String password = password_text.getText().trim();

        if (login.isEmpty() || password.isEmpty()) {
            textininforma.setText("Введите логин и пароль!");
            return;
        }

        String role = ConnectionUtil.authorize(login, password);

        if (role == null) {
            textininforma.setText("Неверный логин или пароль!");
            return;
        }

        Session.currentUser = login;
        Session.role = role;

        textininforma.setText("Успешный вход как " + role);
        openMenuScene();
    }

    private void openMenuScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) voti.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Меню — " + Session.role);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            textininforma.setText("Ошибка загрузки меню: " + e.getMessage());
        }
    }

    @FXML
    void initialize() {
        assert avtor_text != null : "fx:id=\"avtor_text\" not injected";
        assert login_text != null : "fx:id=\"login_text\" not injected";
        assert textininforma != null : "fx:id=\"textininforma\" not injected";
        assert voti != null : "fx:id=\"voti\" not injected";
    }
}