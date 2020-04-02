package Runners;

import Controller.NotaController;
import Domain.Nota;
import Domain.Student;
import Domain.Tema;
import Repositories.CRUDRepository;
import Repositories.NotaRepo;
import Repositories.StudentRepo;
import Repositories.TemaRepo;
import Services.NotaService;
import Services.StudentService;
import Services.TemaService;
import Validator.NotaValidator;
import Validator.StudentValidator;
import Validator.TemaValidator;
import Validator.ValidatorContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.config.ApplicationContext;

public class NotaMain extends Application {

    private CRUDRepository<String, Nota> notarepo;
    private CRUDRepository<Long, Student> strepo;
    private CRUDRepository<Long, Tema> temaRepo;
    private StudentService stserv;
    private TemaService temaserv;
    private NotaService notaserv;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        strepo = new StudentRepo(ApplicationContext.getProperties().getProperty("database.catalog.studenti"), ApplicationContext.getProperties().getProperty("database.tag.student"));
        stserv = new StudentService(strepo, new ValidatorContext(new StudentValidator()));

        temaRepo=new TemaRepo(ApplicationContext.getProperties().getProperty("database.catalog.teme"),ApplicationContext.getProperties().getProperty("database.tag.tema"));
        temaserv= new TemaService(temaRepo, new ValidatorContext(new TemaValidator()));

        notarepo=new NotaRepo(ApplicationContext.getProperties().getProperty("database.catalog.note"),ApplicationContext.getProperties().getProperty("database.tag.nota"));
        notaserv=new NotaService(notarepo, new ValidatorContext(new NotaValidator()),stserv,temaserv);

        FXMLLoader loader= new FXMLLoader();
        loader.setLocation(this.getClass().getResource(ApplicationContext.getProperties().getProperty("database.view.nota")));
      //  loader.setLocation(this.getClass().getResource("/views/NotaView.fxml"));
        AnchorPane root=loader.load();

        NotaController ctrl=loader.getController();
        ctrl.setService(notaserv);

        Scene scene = new Scene(root, 1100, 400);
        primaryStage.getIcons().add(new Image("/images/notaicon.png"));
        scene.getStylesheets().add("css/style.css");

        primaryStage.setTitle("Note");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}
