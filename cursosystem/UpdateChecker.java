package cursosystem;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

/**
 *
 * @author borge
 */
public class UpdateChecker {

    private String version;
    private final File file = new File("C:/CursoSystem/version/Version.xml");
    private final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    public String checkVersion() {
        String ver = null;
        try {
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            ver = document.getElementsByTagName("version").item(0).getTextContent();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ver;
    }

    public void CheckForUpdates() {
        Http checkServerUpdate = new Http();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document d = db.parse(file);

            String myVersion = d.getElementsByTagName("versionNumber").item(0).getTextContent();
            String LatestVersion = checkServerUpdate.checkForUpdatesOnServer(myVersion);

            if (LatestVersion.equals("0")) {
            } else {
                showPopUp(LatestVersion, myVersion);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showPopUp(String li, String v) {

        Stage s = new Stage();
        Text txtversion = new Text();
        Text txt = new Text();
        Hyperlink link = new Hyperlink();
        AnchorPane ac = new AnchorPane();
        Button btn = new Button();

        v = v + 1;
        txtversion.setText("New Update (" + v + ")");
        txtversion.setFont(Font.font("System", 18));
        txtversion.setLayoutX(14);
        txtversion.setLayoutY(33);
        txtversion.setFill(Paint.valueOf("white"));
        txtversion.setSmooth(true);

        txt.setText("Nova atualizaçao disponivel em:");
        txt.setFont(Font.font("System", 18));
        txt.setFill(Paint.valueOf("white"));
        txt.setLayoutX(15);
        txt.setLayoutY(60);
        txt.setSmooth(true);

        link.setText(li);
        link.setLayoutX(15);
        link.setLayoutY(67);

        link.setOnMouseClicked(ev -> {
            try {
                URI url = new URI(li);
                Desktop desk = Desktop.getDesktop();
                desk.browse(url);
                btn.setText("Fechar");
            } catch (Exception e) {
            }
        });

        btn.setLayoutX(186);
        btn.setLayoutY(103);
        btn.setText("Nao quero baixar agora");
        btn.getStylesheets().add("css/style.css");
        btn.setOnAction(e -> {
            s.close();
        });

        ac.setStyle("-fx-background-color:black");
        ac.setPrefWidth(514);
        ac.setPrefHeight(142);

        ac.getChildren().add(txt);
        ac.getChildren().add(txtversion);
        ac.getChildren().add(btn);
        ac.getChildren().add(link);

        Scene sc = new Scene(ac);

        s.setScene(sc);
        s.setResizable(false);
        s.show();

    }

}
