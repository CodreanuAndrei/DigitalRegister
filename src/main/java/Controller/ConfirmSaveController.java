package Controller;

import Domain.Student;
import Domain.Tema;
import Services.NotaService;
import Validator.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ConfirmSaveController {

    private NotaService serv;
    private Stage dialog;
    private Tema tema;
    private Student st;
    private String feedback;
    private LocalDate predare;
    private ArrayList<LocalDate> motivari;
    private String nota;

    @FXML
    Label numeConfirm;
    @FXML
    Label temaConfirm;
    @FXML
    Label dataConfirm;
    @FXML
    Label notaConfirm;

    @FXML
    private void initialize() {
    }

    public void setServiceConfirm(NotaService serv, Stage stage, Student st, Tema tema,String feedback, ArrayList<LocalDate> motivari, LocalDate predare,String nota) {
        this.serv = serv;
        this.dialog = stage;
        this.tema = tema;
        this.st = st;
        this.feedback=feedback;
        this.motivari=motivari;
        this.predare=predare;
        this.nota=nota;
    }

    public void handleConfirm(ActionEvent actionEvent) {
        String idnota=st.getId()+"_"+tema.getId();
        try {
            serv.addNota(idnota, 10, predare, st.getIndrumator(), feedback, motivari);
        }catch (ValidationException e) {
            PopupInfo.showErrorMessage(null,"Studentul are deja o nota la aceasta tema");
        }
        dialog.close();
    }


    public void handleBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/AddNotaView.fxml"));
        AnchorPane root = loader.load();
        AddNotaDialog ctrlc=loader.getController();
        ctrlc.setService(serv,dialog,st,tema,nota,feedback);
        Scene scena= new Scene(root);
        if(!this.numeConfirm.getScene().getStylesheets().isEmpty())
            scena.getStylesheets().add("/css/dialog.css");
        dialog.setScene(scena);
    }


}
