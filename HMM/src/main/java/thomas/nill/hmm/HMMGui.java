package thomas.nill.hmm;

import javafx.application.Application;
import javafx.stage.Stage;

import org.apache.log4j.Logger;
import org.janus.appbuilder.AppBuilder;
import org.janus.gui.basis.JanusApplication;
import org.janus.gui.basis.JanusPage;
import org.janus.gui.basis.JanusSession;
import org.janus.gui.basis.PageContext;
import org.janus.gui.builder.GuiElementBuilder;
import org.janus.gui.javafx.StageConnector;
import org.janus.gui.javafx.builder.JavaFXGuiElementBuilder;

public class HMMGui extends Application {
    private static final Logger LOG = Logger.getLogger(HMMGui.class);

    public static void main(String[] args) {
        launch(args);
    }

    public HMMGui() {

    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Hello World!");

            GuiElementBuilder elementBuilder = new JavaFXGuiElementBuilder();
            AppBuilder builder = new AppBuilder(elementBuilder);
            builder.setPageListe("data");
            JanusApplication app = builder.getApplication("hmm");

            JanusSession session = app.newContext();

            JanusPage login = session.searchPage("login");

            PageContext context = session.getPageContext(login);
            StageConnector frameConnector = (StageConnector) login.getGui();
            frameConnector.setContext(context);

            primaryStage.setScene(frameConnector.getScene());
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Fehler", e);
        }
    }
}
