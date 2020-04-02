package Runners;


import Controller.StudentController;
import Domain.Student;
import Repositories.CRUDRepository;
import Repositories.StudentRepo;
import Services.StudentService;
import Validator.StudentValidator;
import Validator.ValidatorContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.config.ApplicationContext;

import java.io.IOException;

public class StudentMain extends Application {

    CRUDRepository<Long, Student> stRepo;
    StudentService serv;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws IOException {

        stRepo = new StudentRepo(ApplicationContext.getProperties().getProperty("database.catalog.studenti"), ApplicationContext.getProperties().getProperty("database.tag.student"));
        serv = new StudentService(stRepo, new ValidatorContext(new StudentValidator()));


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource(ApplicationContext.getProperties().getProperty("database.view.student")));

        AnchorPane root = loader.load();

        StudentController ctrl=loader.getController();
        ctrl.setStudentService(serv);


        Scene scene = new Scene(root, 600, 420);
        scene.getStylesheets().add("css/style.css");

        primaryStage.setTitle("Students");
        primaryStage.getIcons().add(new Image("/images/glyph_little_boy_child-512.png"));
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}
