package Controller;

import Domain.Tema;
import Services.TemaService;
import Validator.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import utils.events.TemaChangeEvent;
import utils.observer.Observer;

import javafx.scene.control.TableColumn;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TemaController implements Observer<TemaChangeEvent> {
    TemaService serv;
    ObservableList<Tema> model = FXCollections.observableArrayList();

    @FXML
    Text adauga;

    @FXML
    TableView<Tema> tableView;

    @FXML
    TableColumn<Tema, String> idField;

    @FXML
    TableColumn<Tema,String> temaField;

    @FXML
    TableColumn<Tema,String> primireField;

    @FXML
    TableColumn<Tema,String> deadlineField;



    public void setTemaService(TemaService serv)
    {
        this.serv=serv;
        this.serv.addObserver(this);
        initmodel();
    }

    @Override
    public void update(TemaChangeEvent temaChangeEvent) {
        initmodel();
    }

    public void initialize()
    {
        idField.setCellValueFactory(new PropertyValueFactory<Tema,String>("id"));
        temaField.setCellValueFactory(new PropertyValueFactory<Tema,String>("descriere"));
        primireField.setCellValueFactory(new PropertyValueFactory<Tema,String>("startWeek"));
        deadlineField.setCellValueFactory(new PropertyValueFactory<Tema,String>("deadlineWeek"));
        tableView.setItems(model);
    }

    private void initmodel() {
        Iterable<Tema> teme = serv.getAll();
        List<Tema> temeList = StreamSupport.stream(teme.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(temeList);
    }


    public void handleAdd(ActionEvent actionEvent) {
        showTemaDialogController(null);
    }

    private void showTemaDialogController(Tema t) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/TemaDialog.fxml"));
            AnchorPane root= (AnchorPane) loader.load();

            Stage dialog = new Stage();
            dialog.setTitle("Edit Tema");
            dialog.initModality(Modality.WINDOW_MODAL);

            Scene scene=new Scene(root);
            dialog.setScene(scene);
            scene.getStylesheets().add("/css/dialog.css");
            dialog.getIcons().add(new Image("/images/sm_5abc881cb2505.png"));

            TemaDialogController editctrl=loader.getController();
            editctrl.setService(serv,dialog,t);

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleRemove(ActionEvent actionEvent) {
        Tema sst = (Tema) tableView.getSelectionModel().getSelectedItem();
        Tema deleted = null;
        if (sst != null)
            try {
                deleted = serv.deleteTema(sst.getId());
                if (deleted == null)
                    PopupInfo.showErrorMessage(null, "Nu exista tema!");
                else
                    PopupInfo.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Tema a fost stearsa cu succes!");
            } catch (ValidationException e) {
                PopupInfo.showErrorMessage(null, e.getMessage());
            }
        else PopupInfo.showErrorMessage(null, "Nu ati selectat nicio tema!");
    }

    public void handleUpdate(ActionEvent actionEvent) {
        Tema sst=(Tema) tableView.getSelectionModel().getSelectedItem();
        if(sst!=null)
            showTemaDialogController(sst);
        else
            PopupInfo.showErrorMessage(null, "Nu ati selectat nicio tema!");
    }
    }


