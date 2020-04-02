package Controller;

import Domain.Student;
import Services.StudentService;
import Validator.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StudentDialogController {

    @FXML
    private TextField numeText;
    @FXML
    private TextField prenumeText;
    @FXML
    private TextField grupaText;
    @FXML
    private TextField mailText;
    @FXML
    private TextField indrumatorText;

    private StudentService stserv;
    Stage dialog;
    Student st;

    @FXML
    private void initialize() {
    }

    public void setService(StudentService serv, Stage stage, Student st) {
        this.stserv = serv;
        this.dialog = stage;
        this.st = st;
        if (null != st) {
            setFields(st);
        }

    }

    private void setFields(Student st) {
        numeText.setText(st.getName());
        prenumeText.setText(st.getPrenume());
        grupaText.setText(st.getGrupa());
        mailText.setText(st.getEmail());
        indrumatorText.setText(st.getIndrumator());
    }

    private void clearFields()
    {
        numeText.clear();
        prenumeText.clear();
        grupaText.clear();
        mailText.clear();
        indrumatorText.clear();
    }

    public void handleSave() {
        String nume = numeText.getText();
        String prenume = prenumeText.getText();
        String grupa = grupaText.getText();
        String mail = mailText.getText();
        String indrumator = indrumatorText.getText();
        try {
            if (st == null) {
                stserv.addStudent(nume, prenume, grupa, mail, indrumator);
                PopupInfo.showMessage(null, Alert.AlertType.INFORMATION, "Add", "Student adaugat!");
            } else {
                stserv.updateStudent(st.getId(), nume, prenume, grupa, mail, indrumator);
                PopupInfo.showMessage(null, Alert.AlertType.INFORMATION, "Update", "Student actualizat!");
            }
        } catch (ValidationException e) {
            PopupInfo.showErrorMessage(null, e.getMessage());
        }
        //dialog.close();
        clearFields();
    }

    public void handleCancel(ActionEvent actionEvent) {
        dialog.close();
    }
}
