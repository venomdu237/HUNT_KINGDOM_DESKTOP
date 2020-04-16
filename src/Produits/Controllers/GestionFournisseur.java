package Produits.Controllers;

import Produits.Model.Fournisseurs;
import Produits.Model.Produits;
import Produits.Services.CrudFournisseur;
import Produits.Services.CrudProduit;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class GestionFournisseur implements Initializable {
    Connection cnx=DataSource.getInstance().getCnx();

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML private TableView<Fournisseurs> tableF ;
    @FXML private TableColumn<Fournisseurs,String> nomFournisseur;
    @FXML private TableColumn<Fournisseurs,Integer>idFournisseur;
    @FXML private TableColumn<Fournisseurs,String>emailFournisseur;
    @FXML private Button AjoutFBtn;
    @FXML private Button ModifFBtn;
    @FXML private Button MailBtn;
    @FXML private TextField mail;
    @FXML private TextField objet;
    @FXML private TextField contenu;
    public ObservableList<Fournisseurs> data =FXCollections.observableArrayList();

    //Integration

    @FXML private StackPane parent;
    @FXML private HBox top;
    @FXML private Pane menu;
    @FXML private Pane btn_menu;
    @FXML private FontAwesomeIcon btn_menubars;
    @FXML private Pane btn_menu_exit;
    @FXML private FontAwesomeIcon btn_menu_exitbars;
    @FXML private Pane maximize;
    @FXML private Pane minimize;



    /////////////

    @FXML
    public void afficheFournisseur() {
        String req="SELECT * FROM fournisseur";
        try {

            PreparedStatement pst=cnx.prepareStatement(req);
            ResultSet rs=pst.executeQuery();

            while (rs.next()){
                data.add(new Fournisseurs(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        nomFournisseur.setCellValueFactory(new PropertyValueFactory<Fournisseurs,String>("nomFournisseur"));
        idFournisseur.setCellValueFactory(new PropertyValueFactory<Fournisseurs, Integer>("idFournisseur"));
        emailFournisseur.setCellValueFactory(new PropertyValueFactory<Fournisseurs, String>("emailFournisseur"));
        tableF.setItems(data);
        tableF.setEditable(true);
        nomFournisseur.setCellFactory(TextFieldTableCell.forTableColumn());
        //quantite.setCellFactory(TextFieldTableCell.forTableColumn());

        emailFournisseur.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_menu_exit.setVisible(false);
        btn_menu_exitbars.setVisible(false);
        minimize.setVisible(false);
        handleDragged();

    }
    public void fenetreAjoutF(javafx.event.ActionEvent actionEvent) {
        try {
            Stage stage= new Stage() ;
            Parent root = FXMLLoader.load(getClass().getResource("../View/AjoutFournisseur.fxml"));

            stage.setTitle("Ajout Fournisseur");
            stage.setScene(new Scene(root, 430, 580));
            stage.show();

            AjoutFBtn.getScene().setRoot(root);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

        public void fenetreModifF(javafx.event.ActionEvent actionEvent) {
        try {
            Stage stage= new Stage() ;
            Parent root = FXMLLoader.load(getClass().getResource("../View/ModifFournisseur.fxml"));

            stage.setTitle("Modification Fournisseur");
            stage.setScene(new Scene(root, 430, 530));
            stage.show();

            ModifFBtn.getScene().setRoot(root);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void supprimer(ActionEvent event) {
        CrudFournisseur ecr = new CrudFournisseur();
        ecr.supprF(tableF.getSelectionModel().getSelectedItem().getId());
        Alert dialogC = new Alert(Alert.AlertType.CONFIRMATION);
        dialogC.setTitle("A confirmation dialog-box");
        dialogC.setHeaderText(null);
        dialogC.setContentText("Voulez vous supprimer le Fournisseur?");
        Optional<ButtonType> answer = dialogC.showAndWait();
        if (answer.get() == ButtonType.OK) {

            tableF.getItems().removeAll(tableF.getSelectionModel().getSelectedItem());}
        else {
            System.out.println("User chose Cancel or closed the dialog-box");
        }

    }
    @FXML
    private void onEditChanged(TableColumn.CellEditEvent<Produits,String> event) throws SQLException {
        Fournisseurs f = tableF.getSelectionModel().getSelectedItem();
        f.setNomFournisseur(event.getNewValue());
        f.setEmailFournisseur(event.getNewValue());

        CrudFournisseur ecr = new CrudFournisseur();
        //ecr.modifP(p);
        ecr.updatetab(f);


    }


    @FXML
    private void showmenu(MouseEvent event){
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.7));
        slide.setNode(menu);

        slide.setToX(294);
        slide.play();

        btn_menu_exit.setVisible(true);
        btn_menu_exitbars.setVisible(true);
        btn_menu.setVisible(false);
        btn_menubars.setVisible(false);

        slide.setOnFinished((e ->{

        } ));
    }



    @FXML
    private void hidemenu(MouseEvent event){
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.7));
        slide.setNode(menu);

        slide.setToX(0);
        slide.play();

        btn_menu_exit.setVisible(false);
        btn_menu_exitbars.setVisible(false);
        btn_menu.setVisible(true);
        btn_menubars.setVisible(true);

        slide.setOnFinished((e ->{

        } ));
    }

    @FXML
    private void handleClose(MouseEvent event){
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.close();
    }

    @FXML
    private void handleMin(MouseEvent event){
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.setIconified(true);
    }

    @FXML
    private void handleMax(MouseEvent event){
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.setMaximized(true);
        minimize.setVisible(true);
        maximize.setVisible(false);
    }

    @FXML
    private void handleMinimize(MouseEvent event){
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        s.setMaximized(false);
        minimize.setVisible(false);
        maximize.setVisible(true);
    }

    private void handleDragged(){
        top.setOnMousePressed((event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        }));
        top.setOnMouseDragged((event -> {
            Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
            s.setX(event.getScreenX() - xOffset);
            s.setY(event.getScreenY() - yOffset);
            s.setOpacity(0.8f);
        }));
        top.setOnDragDone((event -> {
            Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
            s.setOpacity(1.0f);
        }));
        top.setOnMouseReleased((event -> {
            Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
            s.setOpacity(1.0f);
        }));
    }


    //Mail
    public void EnvoiMail(javafx.event.ActionEvent event) {
        String email=mail.getText();
        String object=objet.getText();
        String content=contenu.getText();

        //Envoi du mail
        final String username = "huntkingdom216@gmail.com";
        final String password = "esprit2020";
        String fromEmail ="huntkingdom216@gmail.com";
        String toEmail = email;

        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        // start our mail message


        Message message = new MimeMessage(session);
        try{
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("[HuntKingdom] " + object );

            //   message.setText("Email Body Text!");  utilisé dans le cas d'un simple email

            /***** With attachements ********/
            Multipart emailContent = new MimeMultipart();
            //Text body part
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(content);


            //More Body part .. more attachments!
            //Attach body parts
            emailContent.addBodyPart(textBodyPart);


            //Attach multiparts to message
            message.setContent(emailContent);

            /**************************************************************/

            Transport.send(message);
            System.out.println("Message sent =) !");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Succès");
            alert.setContentText("Mail Envoyé Avec Succès!");
            alert.showAndWait();

        } catch(MessagingException e){
            e.printStackTrace();
        }
    }


    public void Mailing(ActionEvent event) {
        try {
            Stage stage= new Stage() ;
            Parent root = FXMLLoader.load(getClass().getResource("../View/MailFournisseur.fxml"));

            stage.setTitle("Mailing");
            stage.setScene(new Scene(root, 430, 580));
            stage.show();

            MailBtn.getScene().setRoot(root);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
