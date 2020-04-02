package Controller;

import Domain.*;
import Services.NotaService;
import Validator.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.w3c.dom.Text;
import utils.StructuraSemestrului;
import utils.events.NotaChangeEvent;
import utils.observer.Observable;
import utils.observer.Observer;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class NotaController implements Observer<NotaChangeEvent> {

    private NotaService noteServ;
    private ObservableList<NotaDto> model = FXCollections.observableArrayList();
    private ObservableList<Student> studentObservable = FXCollections.observableArrayList();
    private ObservableList<MedieStudent> medieObservable = FXCollections.observableArrayList();
    private ObservableList<Student> rapoarteObservable=FXCollections.observableArrayList();

    //-------------TABLES AND LISTS---------
    @FXML
    TableView<NotaDto> tableView;
    @FXML
    ListView<Student> listView;
    @FXML
    ListView rapoarteList;

    //-=------------TABLE COLUMNS------------
    @FXML
    TableColumn<NotaDto, String> studentColumn;
    @FXML
    TableColumn<NotaDto, String> temaColumn;
    @FXML
    TableColumn<NotaDto, String> notaColumn;
    @FXML
    TableColumn<NotaDto, String> grupaColumn;
    @FXML
    TableColumn<NotaDto, String> profColumn;

    //-------TEXT FIELDS-----------
    @FXML
    TextField searchNotaField;
    @FXML
    TextField searchStudentField;
    @FXML
    TextField notamaximaField;
    @FXML
    TextField updateNota;

    //-------COMBO BOX-------
    @FXML
    ComboBox<Tema> comboTema;

    //--------Text Area------
    @FXML
    TextArea feedbackArea;
    @FXML
    TextArea selectedArea;

    @FXML
    Button themeButton;
    @FXML
    Label temaGrea;

    public void setService(NotaService noteServ) {
        this.noteServ = noteServ;
        this.noteServ.addObserver(this);
        initmodel();
        initstudents();
        initteme();
        setNotamaximaField();
        Image image = new Image("/images/theme.png");
        themeButton.setGraphic(new ImageView(image));
        updateNota.getStyleClass().remove("text-field");
        temaGrea.setText("");


    }

    @Override
    public void update(NotaChangeEvent notaChangeEvent) {
        initmodel();
    }

    public void initialize() {
        //------Setting listeners-------------
        searchStudentField.textProperty().addListener((x, y, z) -> handleSearchStudent());
        searchStudentField.focusedProperty().addListener((x, y, z) -> {
            if (searchStudentField.isFocused()) {
                searchStudentField.getStyleClass().remove("text-field");
            } else
                searchStudentField.getStyleClass().addAll("text-field");
        });
        searchNotaField.textProperty().addListener((x, y, z) -> handleSearchNote());
        searchNotaField.focusedProperty().addListener((x, y, z) -> {
            if (searchNotaField.isFocused()) {
                searchNotaField.getStyleClass().remove("text-field");
            } else searchNotaField.getStyleClass().addAll("text-field", "text-input");
        });
        comboTema.getSelectionModel().selectedItemProperty().addListener((x, y, z) -> setNotamaximaField());
        tableView.getSelectionModel().selectedItemProperty().addListener((x, y, z) -> handleFeedback());
        tableView.focusModelProperty().addListener((x, y, z) -> handleFocus());
        //---------table---------------------
        studentColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("studentName"));
        temaColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("temaId"));
        notaColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("nota"));
        grupaColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("grupa"));
        profColumn.setCellValueFactory(new PropertyValueFactory<NotaDto, String>("profesor"));
        tableView.setItems(model);
        listView.setItems(studentObservable);

    }

    private void handleFocus() {
        //   if(tableView.getSelectionModel().is)
    }

    private void handleFeedback() {
        if (tableView.getSelectionModel().getSelectedItem() != null) {
            selectedArea.setText(tableView.getSelectionModel().getSelectedItem().getFeedback());
            selectedArea.setEditable(false);
        } else
            selectedArea.setText("");

    }

    private void handleSearchStudent() {
        Predicate<Student> byName = x -> (x.getName() + " " + x.getPrenume()).startsWith(searchStudentField.getText());
        List<Student> students;
        students = noteServ.getAllStudents().stream()
                .filter(byName)
                .collect(Collectors.toList());
        studentObservable.setAll(students);
    }

    private void handleSearchNote() {
        Predicate<NotaDto> byName = x -> x.getStudentName().startsWith(searchNotaField.getText());
        Predicate<NotaDto> byGrupa = x -> x.getGrupa().startsWith(searchNotaField.getText());
        List<NotaDto> note;
        if (searchNotaField.getText().matches("^[0-9]+$"))
            note = noteServ.getAll().stream()
                    .filter(byGrupa)
                    .collect(Collectors.toList());
        else note = noteServ.getAll().stream()
                .filter(byName)
                .collect(Collectors.toList());
        model.setAll(note);
    }

    private void initmodel() {
        List<NotaDto> noteList = new ArrayList<>(noteServ.getAll());
        model.setAll(noteList);
    }

    private void initstudents() {
        List<Student> students = new ArrayList<>(noteServ.getAllStudents());
        studentObservable.setAll(students);
    }

    private void initteme() {
        comboTema.getItems().setAll(noteServ.getAllTeme());
        comboTema.getSelectionModel().selectLast();
    }

    private void setNotamaximaField() {
        notamaximaField.getStyleClass().remove("text-field");
        int nr = comboTema.getSelectionModel().getSelectedItem().getDeadlineWeek();
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int week = date.get(weekFields.ISO.weekOfWeekBasedYear());
        weekFields = WeekFields.of(Locale.getDefault());
        int beginweek = StructuraSemestrului.startSemester.get(weekFields.ISO.weekOfWeekBasedYear());
        int thisweek = week - beginweek + 1;

        if (thisweek >= nr + 3)
            notamaximaField.setText("1");
        else {
            int valoare = nr - thisweek + 10;
            if (valoare > 10) notamaximaField.setText("10");
            else
                notamaximaField.setText(Integer.toString(valoare));
        }
    }


    public void handleAdd(ActionEvent actionEvent) {
        AddNotaDialogF();
        feedbackArea.clear();
    }

    private void AddNotaDialogF() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/AddNotaView.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            Stage dialog = new Stage();
            dialog.setTitle("Adauga o nota");
            dialog.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialog.setScene(scene);
            dialog.setResizable(false);
            if (!this.tableView.getScene().getStylesheets().isEmpty())
                scene.getStylesheets().add("/css/dialog.css");
            dialog.getIcons().add(new Image("/images/notaicon.png"));

            AddNotaDialog ctrl = loader.getController();
            if (listView.getSelectionModel().isEmpty())
                PopupInfo.showMessage(null, Alert.AlertType.INFORMATION, "Eroare", "Nu ati selectat niciun student!");
            else {
                int nota = Integer.parseInt(notamaximaField.getText());
                if (nota > 10) nota = 10;
                String fb = "";
                if (feedbackArea.getText().equals("")) {
                    if (nota != 10)
                        fb = "Nota a fost diminuata cu " + Integer.toString(10 - nota) + " puncte datorita intarzierilor";
                    else
                        fb = "Laboratorul a fost predat la timp!";

                } else
                    fb = feedbackArea.getText();
                ctrl.setService(noteServ, dialog, listView.getSelectionModel().getSelectedItem(), comboTema.getSelectionModel().getSelectedItem(), Integer.toString(nota), fb);
                dialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleRemove(ActionEvent actionEvent) {
        NotaDto nota = (NotaDto) tableView.getSelectionModel().getSelectedItem();
        selectedArea.clear();
        Nota deleted = null;
        if (nota != null) {
            deleted = noteServ.removeNota(nota.getNotaid());
            PopupInfo.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Nota a fost stearsa cu succes!");
        } else PopupInfo.showErrorMessage(null, "Nu ati selectat nicio nota!");
    }

    public void handleUpdate(ActionEvent actionEvent) {
        NotaDto notaDto = (NotaDto) tableView.getSelectionModel().getSelectedItem();
        if (updateNota.getText().equals("")) {
            PopupInfo.showErrorMessage(null, "Nu ati introdus nota!");
            return;
        }
        if (notaDto != null)
            try {
                Nota nota = noteServ.findNota(notaDto.getNotaid());
                noteServ.updateNota(notaDto.getNotaid(), Integer.parseInt(updateNota.getText()), nota.getData(), notaDto.getProfesor(), notaDto.getFeedback());
                updateNota.clear();

            } catch (ValidationException e) {
                PopupInfo.showErrorMessage(null, e.getMessage());
            }
        else
            PopupInfo.showErrorMessage(null, "Nu ati selectat nicio tema!");
    }


    public void handleTheme(ActionEvent actionEvent) {
        if (!this.tableView.getScene().getStylesheets().isEmpty())
            this.tableView.getScene().getStylesheets().clear();
        else
            this.tableView.getScene().getStylesheets().add("/css/style.css");
    }


    public void handleMedia(ActionEvent actionEvent) {
        List<MedieStudent> medii = new ArrayList<>();
        Map<String, List<NotaDto>> grouped = noteServ.getAll().stream()
                .collect(Collectors.groupingBy(x -> x.getStudentName()));
        grouped.entrySet()
                .forEach(x ->
                {
                    final double[] numitor = {0};
                    double numarator = x.getValue().stream()
                            .map(y ->
                            {
                                Tema tema = noteServ.getTema(Long.parseLong(y.getNotaid().split("_")[1]));
                                int alocare = tema.getDeadlineWeek() - tema.getStartWeek();
                                numitor[0] = numitor[0] + alocare;
                                return y.getNota() * alocare;
                            })
                            .reduce(0d, (a, b) -> a + b);
                    medii.add(new MedieStudent(x.getKey(), numarator / numitor[0]));
                });
        medieObservable.setAll(medii);
        rapoarteList.setItems(medieObservable);
    }

    public void handleTemaGrea(ActionEvent actionEvent) {
        List<TemaGrea> teme = new ArrayList<>();
        Map<String, List<NotaDto>> grouped = noteServ.getAll().stream()
                .collect(Collectors.groupingBy(x -> x.getTemaId()));
        grouped.entrySet()
                .forEach(x ->
                {
                    final double[] numitor = {0};
                    double numarator = x.getValue().stream()
                            .map(y ->
                            {
                                Tema tema = noteServ.getTema(Long.parseLong(y.getNotaid().split("_")[1]));
                                int alocare = tema.getDeadlineWeek() - tema.getStartWeek();
                                numitor[0] = numitor[0] + alocare;
                                return y.getNota() * alocare;
                            })
                            .reduce(0d, (a, b) -> a + b);
                    double medie = numarator / numitor[0];
                    teme.add(new TemaGrea(x.getKey(), numarator / numitor[0]));
                });
        Double min = 11d;
        String rez = "";
        for (TemaGrea tema : teme) {
            if (tema.getMedie() < min) {
                min = tema.getMedie();
                rez = tema.getTema();
            }
        }
        temaGrea.setText(rez);

    }

    public void handlePromovati(ActionEvent actionEvent) {
        List<MedieStudent> medii = new ArrayList<>();
        Map<String, List<NotaDto>> grouped = noteServ.getAll().stream()
                .collect(Collectors.groupingBy(x -> x.getStudentName()));
        grouped.entrySet()
                .forEach(x ->
                {
                    final double[] numitor = {0};
                    double numarator = x.getValue().stream()
                            .map(y ->
                            {
                                Tema tema = noteServ.getTema(Long.parseLong(y.getNotaid().split("_")[1]));
                                int alocare = tema.getDeadlineWeek() - tema.getStartWeek();
                                numitor[0] = numitor[0] + alocare;
                                return y.getNota() * alocare;
                            })
                            .reduce(0d, (a, b) -> a + b);
                    medii.add(new MedieStudent(x.getKey(), numarator / numitor[0]));
                });
        Predicate<MedieStudent> byMedie = x -> x.getMedie() >= 4;
        List<MedieStudent> rez = medii.stream()
                .filter(byMedie)
                .collect(Collectors.toList());
        medieObservable.setAll(rez);
        rapoarteList.setItems(medieObservable);
    }

    public void handleLaTimp(ActionEvent actionEvent) {
        Predicate<Nota> latimp = x ->
        {
            Tema tema = noteServ.getTema(Long.parseLong(x.getId().split("_")[1]));
            WeekFields weekFields = WeekFields.of(Locale.getDefault());
            int startSem = StructuraSemestrului.startSemester.get(weekFields.ISO.weekOfWeekBasedYear()) - 1;
            int weekpredare = x.getData().get(weekFields.ISO.weekOfWeekBasedYear()) - startSem;
            return weekpredare <= tema.getDeadlineWeek();
        };
        List<Student> students = new ArrayList<>();
        Map<Student, List<NotaDto>> grouped = noteServ.getAll().stream()
                .collect(Collectors.groupingBy(x ->
                {
                   return noteServ.getStudent(Long.parseLong(x.getNotaid().split("_")[0]));
                }));
        grouped.entrySet().forEach(x ->
                {
                    boolean r = x.getValue().stream()
                            .map(y -> noteServ.findNota(y.getNotaid()))
                            .allMatch(latimp);
                    if(r)
                        students.add(x.getKey());
                }
        );
        rapoarteObservable.setAll(students);
        rapoarteList.setItems(rapoarteObservable);
    }
}

