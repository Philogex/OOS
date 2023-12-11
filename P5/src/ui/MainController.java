package ui;

import bank.PrivateBank;
import bank.exceptions.*;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * controller for main view
 */
public class MainController implements Initializable {
    /**
     * text block
     */
    @FXML
    private Text statusText;
    /**
     * add account button
     */
    @FXML
    private Button addAccountButton;
    /**
     * list of accounts
     */
    @FXML
    private ListView<String> accountListView;
    /**
     * private bank instance
     */
    private final PrivateBank privateBank = new PrivateBank("Privatebank", 0.4,0.6,"Privatebank");
    /**
     * tracks changes to accountListView
     */
    private final ObservableList<String> accountList = FXCollections.observableArrayList();
    /**
     * root object
     */
    public Parent root;
    /**
     * main stage
     */
    private Stage stage;
    /**
     * main scene
     */
    private Scene scene;

    /**
     * explicitly create constructor bc of throws
     * @throws TransactionAttributeException throwable error
     */
    public MainController() throws TransactionAttributeException {}

    /**
     * updates account list
     */
    public void uptdateList(){
        accountList.clear();
        accountList.addAll(privateBank.getAllAccounts());
        accountListView.setItems(accountList);
    }

    /**
     * Init function
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // init menu items and visuals
        uptdateList();
        ContextMenu contextMenu= new ContextMenu();
        MenuItem viewAccount = new MenuItem("Account anzeigen");
        MenuItem deleteAccount = new MenuItem("Account löschen");
        contextMenu.getItems().addAll(viewAccount, deleteAccount);
        accountListView.setContextMenu(contextMenu);
        AtomicReference<String> selectedAccount= new AtomicReference<>();

        // select account
        accountListView.setOnMouseClicked(event -> {
            selectedAccount.set(String.valueOf(accountListView.getSelectionModel().getSelectedItems()));
        });

        // delete function
        deleteAccount.setOnAction(event -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("löschen");
            confirm.setContentText("You sure about that?");
            Optional<ButtonType> result = confirm.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                try {
                    privateBank.deleteAccount(selectedAccount.toString().replace("[","").replace("]",""));
                }catch (AccountDoesNotExistException | IOException e){
                    throw new RuntimeException(e);
                }
                statusText.setText(selectedAccount+" wurde gelöscht!");
                uptdateList();
            }
        });

        // view account function
        viewAccount.setOnAction(event -> {
            stage = (Stage) root.getScene().getWindow();
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/accountView.fxml")));
                root = loader.load();
                AccountController accountController = loader.getController();
                accountController.setUp(privateBank,selectedAccount.toString().replace("[", "").replace("]",""));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            scene = new Scene(root);
            stage.setTitle("Privatebank");
            stage.setScene(scene);
            stage.show();
        });

        // add account button
        addAccountButton.setOnMouseClicked(event -> {
            statusText.setText("");
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Account hinzufügen");
            dialog.setHeaderText("Account hinzufügen");
            dialog.getDialogPane().setMinWidth(300);

            Label label = new Label("Name: ");
            TextField textField = new TextField();
            GridPane gridPane = new GridPane();
            gridPane.add(label,2,1);
            gridPane.add(textField,3,1);
            dialog.getDialogPane().setContent(gridPane);
            dialog.setResizable(true);
            ButtonType buttonTypeOK = new ButtonType("Bestätigen", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(buttonTypeOK);
            dialog.setResultConverter(buttonType -> {
                if(buttonType== buttonTypeOK){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    if(!Objects.equals(textField.getText(),"")){
                        try {
                            privateBank.createAccount(textField.getText());
                            statusText.setText("Account erstellt");
                        } catch (AccountAlreadyExistsException e) {
                            alert.setContentText("Account vorhanden");
                            Optional<ButtonType> optional = alert.showAndWait();
                            if(optional.isPresent() && optional.get() == ButtonType.OK)
                                statusText.setText("Account exisitiert");
                            throw new RuntimeException(e);
                        }
                        uptdateList();
                    }else {
                        alert.setContentText("Ungültig");
                        Optional<ButtonType> optional = alert.showAndWait();
                        if(optional.isPresent() && optional.get() == ButtonType.OK)
                            statusText.setText("Account ungültig");
                    }
                }
                return null;
            });
            dialog.show();
        });
    }
}