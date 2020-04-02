package Controller;

import Domain.Tema;
import Services.TemaService;
import Validator.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class TemaDialogController {


    @FXML
    Label tema;
    @FXML
    Label deadline;

    @FXML
    TextField temaText;
    @FXML
    TextField deadlineText;

    private TemaService serv;
    Stage dialog;
    Tema t;

    @FXML
    private void initialize() {
    }

    public void setService(TemaService serv, Stage stage, Tema t) {
        this.serv = serv;
        this.dialog = stage;
        this.t = t;
        if (null != t) {
            setFields(t);
        }

    }

    private void setFields(Tema t) {
        temaText.setText(t.getDescriere());
        deadlineText.setText(Integer.toString(t.getDeadlineWeek()));
    }

    private void clearFields()
    {
        temaText.clear();
        deadlineText.clear();
    }

    public void handleSave() {
        String lab="Lab"+temaText.getText();
        int deadline=Integer.parseInt(deadlineText.getText());
        try {
            if (t == null) {
                serv.addTema(lab,deadline);
                PopupInfo.showMessage(null, Alert.AlertType.INFORMATION, "Adaugare", "Tema adaugata!");
            } else {
                serv.updateTema(t.getId(),lab,deadline);
                PopupInfo.showMessage(null, Alert.AlertType.INFORMATION, "Update", "Tema actualizata!");
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
