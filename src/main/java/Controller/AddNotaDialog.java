package Controller;

import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Services.NotaService;
import Validator.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AddNotaDialog {

    @FXML
    Label notaFinala;
    @FXML
    DatePicker datePredare;
    @FXML
    TextField studentField;
    @FXML
    TextField temaField;
    @FXML
    CheckBox checkBox;
    @FXML
    DatePicker date1;
    @FXML
    DatePicker date2;
    @FXML
    Label notaFinalaText;
    @FXML
    Label numeConfirm;
    @FXML
    Label temaConfirm;
    @FXML
    Label dataConfirm;
    @FXML
    Label notaConfirm;


    private NotaService serv;
    private Stage dialog;
    private Tema tema;
    private Student st;
    private String nota;
    private String feedback;

    @FXML
    private void initialize() {
    }

    private void init1()
    {
        checkBox.selectedProperty().addListener((x, y, z) -> handleCheck());
        datePredare.valueProperty().addListener((x,y,z)->handlePredare());
        date1.valueProperty().addListener((x,y,z)->handleDate1());
        date2.valueProperty().addListener((x,y,z)->handleDate2());
        setFields();
        notaFinala.setVisible(false);
        notaFinalaText.setVisible(false);
        date1.setVisible(false);
        date2.setVisible(false);
    }


    private void handleDate2() {
        ArrayList<LocalDate> motivari = new ArrayList<>();
        if(date1.getValue()!=null)
          motivari.add(date1.getValue());
        motivari.add(date2.getValue()) ;
        int asig = serv.calculeazanota(10, datePredare.getValue(), tema.getDeadlineWeek(), motivari);
        notaFinalaText.setText(Integer.toString(asig));
    }

    private void handleDate1() {
        ArrayList<LocalDate> motivari = new ArrayList<>();
        motivari.add(date1.getValue());
        int asig = serv.calculeazanota(10, datePredare.getValue(), tema.getDeadlineWeek(), motivari);
        notaFinalaText.setText(Integer.toString(asig));
    }


    private void handlePredare() {

        if(datePredare.getValue().isAfter(LocalDate.now())){
            PopupInfo.showErrorMessage(null,"Data predarii nu poate fi in viitor!");
            return;}

        notaFinalaText.setText(nota);
        notaFinalaText.setVisible(true);
        notaFinala.setVisible(true);
        LocalDate d1 = date1.getValue();
        LocalDate d2 = date2.getValue();
        ArrayList<LocalDate> motivari = new ArrayList<>();
        if (d1 != null)
            motivari.add(d1);
        if (d2 != null)
            motivari.add(d2);
        int asig = serv.calculeazanota(10, datePredare.getValue(), tema.getDeadlineWeek(), motivari);
        notaFinalaText.setText(Integer.toString(asig));
    }

    private void handleCheck() {
        if (checkBox.isSelected()) {
            date1.setVisible(true);
            date2.setVisible(true);
        } else {
            date1.setVisible(false);
            date2.setVisible(false);
        }
    }

    public void setService(NotaService serv, Stage stage, Student st, Tema tema, String nota,String feed) {
        this.serv = serv;
        this.dialog = stage;
        this.tema = tema;
        this.st = st;
        this.nota = nota;
        this.feedback=feed;
        setFields();
        notaFinala.setVisible(false);
        notaFinalaText.setVisible(false);
        date1.setVisible(false);
        date2.setVisible(false);
        init1();
    }

    private void setFields() {
        studentField.setText(st.getName() + " " + st.getPrenume());
        temaField.setText(tema.getDescriere());
    }


    public void handleSave(ActionEvent actionEvent) throws IOException {
        ArrayList<LocalDate> motivari = new ArrayList<>();
        if (datePredare.getValue() == null) {
            PopupInfo.showErrorMessage(null, "Trebuie sa selectati data predarii");
            datePredare.show();
            return;
        }
        LocalDate d1 = date1.getValue();
        LocalDate d2 = date2.getValue();
        if (d1 != null)
            motivari.add(d1);
        if (d2 != null)
            motivari.add(d2);
        String idnota=st.getId()+"_"+tema.getId();

        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/ConfirmView.fxml"));
        AnchorPane root = loader.load();
        ConfirmSaveController ctrl=loader.getController();
       // int nota= serv.calculeazanota(nota)
        ctrl.setServiceConfirm(serv,dialog,st,tema,feedback,motivari,datePredare.getValue(),notaFinalaText.getText());
        Scene scena= new Scene(root);
        if(!this.datePredare.getScene().getStylesheets().isEmpty())
            scena.getStylesheets().add("/css/dialog.css");
        dialog.setScene(scena);
        ctrl.numeConfirm.setText("Nume student: "+st.getName() +" "+st.getPrenume());
        ctrl.temaConfirm.setText("Laboratorul: "+tema.getDescriere());
        ctrl.dataConfirm.setText("Data: "+datePredare.getValue().toString());
        ctrl.notaConfirm.setText("Nota: "+notaFinalaText.getText());

    }

    public void handleCancel(ActionEvent actionEvent) {
        dialog.close();
    }



}
