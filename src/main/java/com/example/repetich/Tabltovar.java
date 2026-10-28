package com.example.repetich;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class Tabltovar {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private TableColumn<Tablich1, Integer> ID_TABL_TOVAR;
    @FXML private TableColumn<Tablich1, String> NAZ_TABL_TOVAR;
    @FXML private TableColumn<Tablich1, String> CHEN_TABL_TOVAR;

    @FXML private TextField chen_tovar;
    @FXML private TextField id_tovar;
    @FXML private TextField naz_tovar;
    @FXML private TextField poisk_text;

    @FXML private Button menu;
    @FXML private Button vixod;
    @FXML private Button dob_button;
    @FXML private Button izm_button;
    @FXML private Button udal_button;

    @FXML private TableView<Tablich1> tabl_tovar;

    private ObservableList<Tablich1> allData;

    @FXML
    void initialize() {
        ID_TABL_TOVAR.setCellValueFactory(new PropertyValueFactory<>("id"));
        NAZ_TABL_TOVAR.setCellValueFactory(new PropertyValueFactory<>("name"));
        CHEN_TABL_TOVAR.setCellValueFactory(new PropertyValueFactory<>("chen"));

        allData = ConnectionUtil.getDataUsers();
        tabl_tovar.setItems(allData);

        // Заполнение полей при клике по строке
        tabl_tovar.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                id_tovar.setText(String.valueOf(sel.getId()));
                naz_tovar.setText(sel.getName());
                chen_tovar.setText(sel.getChen());
            }
        });

        // Блокировка кнопок и полей для USER
        if (!Session.isAdmin()) {
            if (dob_button != null) dob_button.setDisable(true);
            if (izm_button != null) izm_button.setDisable(true);
            if (udal_button != null) udal_button.setDisable(true);
            id_tovar.setDisable(true);
            naz_tovar.setDisable(true);
            chen_tovar.setDisable(true);
        }
    }

    @FXML
    void dob_tovar(ActionEvent event) {
        if (!Session.isAdmin()) return;
        if (!validateFields()) return;

        Tablich1 t = new Tablich1(
                Integer.parseInt(id_tovar.getText().trim()),
                naz_tovar.getText().trim(),
                chen_tovar.getText().trim()
        );
        if (ConnectionUtil.addUser(t)) {
            allData.setAll(ConnectionUtil.getDataUsers());
            clearFields();
        }
    }

    @FXML
    void izm_tovar(ActionEvent event) {
        if (!Session.isAdmin()) return;
        if (!validateFields()) return;

        Tablich1 t = new Tablich1(
                Integer.parseInt(id_tovar.getText().trim()),
                naz_tovar.getText().trim(),
                chen_tovar.getText().trim()
        );
        if (ConnectionUtil.updateUser(t)) {
            allData.setAll(ConnectionUtil.getDataUsers());
            clearFields();
        }
    }

    @FXML
    void udal_tovar(ActionEvent event) {
        if (!Session.isAdmin()) return;
        try {
            int id = Integer.parseInt(id_tovar.getText().trim());
            if (ConnectionUtil.deleteUser(id)) {
                allData.setAll(ConnectionUtil.getDataUsers());
                clearFields();
            }
        } catch (NumberFormatException e) {
            showInfo("Введите ID товара для удаления");
        }
    }

    @FXML
    void Poisk(ActionEvent event) {
        String query = poisk_text.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            tabl_tovar.setItems(allData);
            return;
        }
        ObservableList<Tablich1> filtered = FXCollections.observableArrayList();
        for (Tablich1 t : allData) {
            if (t.getName().toLowerCase().contains(query)
                    || String.valueOf(t.getId()).contains(query)
                    || t.getChen().toLowerCase().contains(query)) {
                filtered.add(t);
            }
        }
        tabl_tovar.setItems(filtered);
    }

    @FXML
    void menu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) menu.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void vixod(ActionEvent event) {
        Session.clear();
        System.exit(0);
    }

    // ---------- Вспомогательное ----------
    private boolean validateFields() {
        if (id_tovar.getText().trim().isEmpty()
                || naz_tovar.getText().trim().isEmpty()
                || chen_tovar.getText().trim().isEmpty()) {
            showInfo("Заполните ID, название и цену");
            return false;
        }
        try {
            Integer.parseInt(id_tovar.getText().trim());
        } catch (NumberFormatException e) {
            showInfo("ID должен быть целым числом");
            return false;
        }
        return true;
    }

    private void clearFields() {
        id_tovar.clear();
        naz_tovar.clear();
        chen_tovar.clear();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.showAndWait();
    }
}