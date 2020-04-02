package Controller;

import Domain.Student;
import Services.StudentService;
import Validator.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.events.StudentChangeEvent;
import utils.observer.Observer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class StudentController implements Observer<StudentChangeEvent> {

    private StudentService serv;
    ObservableList<Student> model = FXCollections.observableArrayList();

    @FXML
    TableView<Student> tableView;

    @FXML
    TableColumn<Student, String> idField;
    @FXML
    TableColumn<Student, String> numeField;
    @FXML
    TableColumn<Student, String> prenumeField;
    @FXML
    TableColumn<Student, String> grupaField;
    @FXML
    TableColumn<Student, String> mailField;
    @FXML
    TableColumn<Student, String> indrumatorField;



    public void setStudentService(StudentService stserv) {
        this.serv = stserv;
        serv.addObserver(this);
        initmodel();
    }

    public void initialize() {
        idField.setCellValueFactory(new PropertyValueFactory<Student, String>("id"));
        numeField.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        prenumeField.setCellValueFactory(new PropertyValueFactory<Student, String>("prenume"));
        grupaField.setCellValueFactory(new PropertyValueFactory<Student, String>("grupa"));
        mailField.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        indrumatorField.setCellValueFactory(new PropertyValueFactory<Student, String>("indrumator"));
        tableView.setItems(model);
    }

    private void initmodel() {
        Iterable<Student> students = serv.getAll();
        List<Student> studentList = StreamSupport.stream(students.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(studentList);
    }

    public void update(StudentChangeEvent changeEvent)
    {
        initmodel();
    }

    @FXML
    public void handleAdd(ActionEvent actionEvent) {
        showStudentEditDialog(null);
    }

    private void showStudentEditDialog(Student st) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/StudentDialog.fxml"));
            AnchorPane root= (AnchorPane) loader.load();

            Stage dialog = new Stage();
            dialog.setTitle("Edit Student");
            dialog.initModality(Modality.WINDOW_MODAL);

            Scene scene=new Scene(root);
            dialog.setScene(scene);
            scene.getStylesheets().add("/css/dialog.css");
            dialog.getIcons().add(new Image("/images/glyph_little_boy_child-512.png"));

            StudentDialogController editctrl=loader.getController();
            editctrl.setService(serv,dialog,st);

            dialog.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleRemove(ActionEvent actionEvent) {
        Student sst = (Student) tableView.getSelectionModel().getSelectedItem();
        Student deleted = null;
        if (sst != null)
            try {
                deleted = serv.removeStudent(sst.getId());
                if (deleted == null)
                    PopupInfo.showErrorMessage(null, "Nu exista studentul!");
                else
                    PopupInfo.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Studentul a fost sters cu succes!");
            } catch (ValidationException e) {
                PopupInfo.showErrorMessage(null, e.getMessage());
            }
        else PopupInfo.showErrorMessage(null, "Nu ati selectat niciun student!");

    }

    public void handleUpdate(ActionEvent actionEvent) {
        Student sst=(Student) tableView.getSelectionModel().getSelectedItem();
        if(sst!=null)
            showStudentEditDialog(sst);
        else
            PopupInfo.showErrorMessage(null, "Nu ati selectat niciun student!");
    }
}
