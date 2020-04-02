package Runners;

import Controller.TemaController;
import Domain.Tema;
import Repositories.CRUDRepository;
import Repositories.TemaRepo;
import Services.TemaService;
import Validator.TemaValidator;
import Validator.ValidatorContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.config.ApplicationContext;

public class TemaMain extends Application {

    CRUDRepository<Long, Tema> temaRepo;
    private TemaService serv;

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
            temaRepo=new TemaRepo(ApplicationContext.getProperties().getProperty("database.catalog.teme"),ApplicationContext.getProperties().getProperty("database.tag.tema"));
            serv= new TemaService(temaRepo, new ValidatorContext(new TemaValidator()));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource(ApplicationContext.getProperties().getProperty("database.view.tema")));

        AnchorPane root = loader.load();

        TemaController ctrl=loader.getController();
        ctrl.setTemaService(serv);


        Scene scene = new Scene(root, 600, 280);
        scene.getStylesheets().add("css/style.css");

        primaryStage.setTitle("Teme");
        primaryStage.getIcons().add(new Image("/images/sm_5abc881cb2505.png"));
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
