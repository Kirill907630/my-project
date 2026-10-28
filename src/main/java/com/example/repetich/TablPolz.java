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

public class TablPolz {

    @FXML private ResourceBundle resources;
    @FXML private URL location;

    @FXML private TableColumn<Polzovatel, Integer> ID_TABL_POLZ;
    @FXML private TableColumn<Polzovatel, String> NAME_TABL_POLZ;
    @FXML private TableColumn<Polzovatel, String> FAMILY_TABL_POL;
    @FXML private TableColumn<Polzovatel, String> LOGIN_TABL_POL;
    @FXML private TableColumn<Polzovatel, String> PAROL_TABL_POL;

    @FXML private TextField id_pol;
    @FXML private TextField name_pol;
    @FXML private TextField family_pol;
    @FXML private TextField login_pol;
    @FXML private TextField parol_pol;
    @FXML private TextField poisk_text;

    @FXML private Button menu;
    @FXML private TableView<Polzovatel> tabl_polzo;

    private ObservableList<Polzovatel> allData;

    @FXML
    void initialize() {
        // Только админ
        if (!Session.isAdmin()) {
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setContentText("Доступ только для администратора");
            a.showAndWait();
            return;
        }

        ID_TABL_POLZ.setCellValueFactory(new PropertyValueFactory<>("id"));
        NAME_TABL_POLZ.setCellValueFactory(new PropertyValueFactory<>("name"));
        FAMILY_TABL_POL.setCellValueFactory(new PropertyValueFactory<>("family"));
        LOGIN_TABL_POL.setCellValueFactory(new PropertyValueFactory<>("login"));
        PAROL_TABL_POL.setCellValueFactory(new PropertyValueFactory<>("parol"));

        allData = ConnectionUtil.getPolzovateli();
        tabl_polzo.setItems(allData);

        tabl_polzo.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                id_pol.setText(String.valueOf(sel.getId()));
                name_pol.setText(sel.getName());
                family_pol.setText(sel.getFamily());
                login_pol.setText(sel.getLogin());
                parol_pol.setText(sel.getParol());
            }
        });
    }

    @FXML
    void dob_pol(ActionEvent event) {
        if (!Session.isAdmin()) return;
        if (!validateFields()) return;

        Polzovatel p = new Polzovatel(
                Integer.parseInt(id_pol.getText().trim()),
                name_pol.getText().trim(),
                family_pol.getText().trim(),
                login_pol.getText().trim(),
                parol_pol.getText().trim()
        );
        if (ConnectionUtil.addPolzovatel(p)) {
            allData.setAll(ConnectionUtil.getPolzovateli());
            clearFields();
        }
    }

    @FXML
    void izm_pol(ActionEvent event) {
        if (!Session.isAdmin()) return;
        if (!validateFields()) return;

        Polzovatel p = new Polzovatel(
                Integer.parseInt(id_pol.getText().trim()),
                name_pol.getText().trim(),
                family_pol.getText().trim(),
                login_pol.getText().trim(),
                parol_pol.getText().trim()
        );
        if (ConnectionUtil.updatePolzovatel(p)) {
            allData.setAll(ConnectionUtil.getPolzovateli());
            clearFields();
        }
    }

    @FXML
    void udal_pol(ActionEvent event) {
        if (!Session.isAdmin()) return;
        try {
            int id = Integer.parseInt(id_pol.getText().trim());
            if (ConnectionUtil.deletePolzovatel(id)) {
                allData.setAll(ConnectionUtil.getPolzovateli());
                clearFields();
            }
        } catch (NumberFormatException e) {
            showInfo("Введите ID пользователя для удаления");
        }
    }

    @FXML
    void Poisk(ActionEvent event) {
        String query = poisk_text.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            tabl_polzo.setItems(allData);
            return;
        }
        ObservableList<Polzovatel> filtered = FXCollections.observableArrayList();
        for (Polzovatel p : allData) {
            if (p.getName().toLowerCase().contains(query)
                    || p.getFamily().toLowerCase().contains(query)
                    || p.getLogin().toLowerCase().contains(query)) {
                filtered.add(p);
            }
        }
        tabl_polzo.setItems(filtered);
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

    private boolean validateFields() {
        if (id_pol.getText().trim().isEmpty()
                || name_pol.getText().trim().isEmpty()
                || family_pol.getText().trim().isEmpty()
                || login_pol.getText().trim().isEmpty()
                || parol_pol.getText().trim().isEmpty()) {
            showInfo("Заполните все поля");
            return false;
        }
        try {
            Integer.parseInt(id_pol.getText().trim());
        } catch (NumberFormatException e) {
            showInfo("ID должен быть целым числом");
            return false;
        }
        return true;
    }

    private void clearFields() {
        id_pol.clear();
        name_pol.clear();
        family_pol.clear();
        login_pol.clear();
        parol_pol.clear();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(msg);
        a.showAndWait();
    }
}