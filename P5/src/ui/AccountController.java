package ui;

import bank.*;
import bank.exceptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

/**
 * controller for account view
 */
public class AccountController implements Initializable {
    /**
     * text block
     */
    @FXML
    public Text text;
    /**
     * add transaction options button
     */
    @FXML
    public MenuButton options;
    /**
     * return to main view button
     */
    @FXML
    public Button back;
    /**
     * add transaction button
     */
    @FXML
    public MenuButton add;
    /**
     * root block
     */
    @FXML
    public Parent root;
    /**
     * display ascending
     */
    @FXML
    public MenuItem ascending;
    /**
     * display descending
     */
    @FXML
    public MenuItem descending;
    /**
     * display positive
     */
    @FXML
    public MenuItem positive;
    /**
     * display negative
     */
    @FXML
    public MenuItem negative;
    /**
     * text for account name
     */
    @FXML
    public Text accountName;
    /**
     * transaction list
     */
    @FXML
    public ListView<Transaction> transactionListView;
    /**
     * tracks changes
     */
    private final ObservableList<Transaction> transactionsList = FXCollections.observableArrayList();
    /**
     * payment display case
     */
    @FXML
    public MenuItem payment;
    /**
     * transfer display case
     */
    @FXML
    public MenuItem transfer;
    /**
     * private bank instance
     */
    private PrivateBank privateBank;
    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // back button logic
        back.setOnMouseClicked(mouseEvent ->{
            try {
                root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/mainView.fxml")));
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setTitle("Privatebank");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        });
    }

    /**
     * set up logic for ui
     * @param privateBank private bank instance
     * @param name name of bank
     */
    public void setUp(PrivateBank privateBank, String name){
        // init
        this.privateBank=privateBank;
        accountName.setText(name+" ["+privateBank.getAccountBalance(name)+"€]");
        update(privateBank.getTransactions(name));

        ContextMenu contextMenu= new ContextMenu();
        // delete button
        MenuItem delete = new MenuItem("Transaktion löschen");
        contextMenu.getItems().addAll(delete);
        transactionListView.setContextMenu(contextMenu);
        // multi threading
        AtomicReference<Transaction> selected = new AtomicReference<>();
        // menu item selected
        transactionListView.setOnMouseClicked(mouseEvent-> {
            selected.set(transactionListView.getSelectionModel().getSelectedItem());
        });

        // delete button logic
        delete.setOnAction(event -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Transasktion Löschen");
            confirm.setContentText("Sind Sie sich sicher?");
            Optional<ButtonType> result = confirm.showAndWait();
            if(result.isPresent()&& result.get() == ButtonType.OK){
                try {
                    privateBank.removeTransaction(name,selected.get());
                } catch (AccountDoesNotExistException | TransactionDoesNotExistException e) {
                    throw new RuntimeException(e);
                }
                text.setText(selected +" wurde gelöscht");
                update(privateBank.getTransactions(name));
                accountName.setText(name+" [" + privateBank.getAccountBalance(name)+"€]");
            }
        });

        // sort asc
        ascending.setOnAction(event -> update(privateBank.getTransactionsSorted(name,true)));

        // sort desc
        descending.setOnAction(event -> update(privateBank.getTransactionsSorted(name,false)));

        // sort positive
        positive.setOnAction(event -> update(privateBank.getTransactionsByType(name,true)));

        // sort negative
        negative.setOnAction(event -> update(privateBank.getTransactionsByType(name,false)));

        // add payment
        payment.setOnAction(event -> setTransaction(payment,name));

        // add transfer
        transfer.setOnAction(event -> setTransaction(transfer,name));
    }

    /**
     * updates visible transaction list
     * @param transactionList new transaction list
     */
    private void update(List<Transaction> transactionList){
        transactionsList.clear();
        transactionsList.addAll(transactionList);
        transactionListView.setItems(transactionsList);
    }

    /**
     * adds transaction
     * @param menuItem type of transaction
     * @param name name of transaction
     */
    private void setTransaction(MenuItem menuItem, String name){
        Dialog<Transaction> dialog = new Dialog<>();
        GridPane gridPane = new GridPane();

        // ui input setup
        Label date = new Label("Datum: ");
        Label amount = new Label("Betrag: ");
        Label description = new Label("Beschreibung: ");
        Label incomingInterest_sender = new Label();
        Label outgoingInterest_recipient = new Label();

        TextField dateText = new TextField();
        TextField amountText = new TextField();
        TextField descriptionText = new TextField();
        TextField incomingInterest_senderText = new TextField();
        TextField outgoingInterest_recipientText = new TextField();

        gridPane.add(date, 1, 1);
        gridPane.add(dateText, 2, 1);
        gridPane.add(description, 1, 2);
        gridPane.add(descriptionText, 2, 2);
        gridPane.add(amount, 1, 3);
        gridPane.add(amountText, 2, 3);
        gridPane.add(incomingInterest_sender, 1, 4);
        gridPane.add(incomingInterest_senderText, 2, 4);
        gridPane.add(outgoingInterest_recipient, 1, 5);
        gridPane.add(outgoingInterest_recipientText, 2, 5);

        // submit button setup
        ButtonType okButton = new ButtonType("Hinzufügen", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResizable(true);
        dialog.getDialogPane().getButtonTypes().add(okButton);

        // show dialog
        dialog.show();

        // error alert
        Alert invalid = new Alert(Alert.AlertType.ERROR);

        // payment case
        if(Objects.equals(menuItem.getId(),"payment")){
            dialog.setTitle("Payment");
            incomingInterest_sender.setText("Incoming interest: ");
            outgoingInterest_recipient.setText("Outgoing interest: ");
            dialog.setResultConverter(buttonType -> {
                if(buttonType == okButton){
                    if(Objects.equals(dateText.getText(),"")||
                            Objects.equals(amountText.getText(), "") ||
                            Objects.equals(descriptionText.getText(),"") ||
                            Objects.equals(incomingInterest_senderText.getText(), "") ||
                            Objects.equals(outgoingInterest_recipientText.getText(), "")){
                        invalid.setContentText("Ungültige Werte");
                        Optional<ButtonType> optional = invalid.showAndWait();
                        if(optional.isPresent() && optional.get() == ButtonType.OK){
                            text.setText("Es wurde nichts gemacht");
                        }
                    } else {
                        Payment payment = null;
                        try {
                            payment = new Payment(dateText.getText(),
                                    Double.parseDouble(amountText.getText()),
                                    descriptionText.getText(),
                                    Double.parseDouble(incomingInterest_senderText.getText()),
                                    Double.parseDouble(outgoingInterest_recipientText.getText()));
                        } catch (TransactionAttributeException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            privateBank.addTransaction(name,payment);
                            text.setText("Neues Payment hinzugefügt");
                        } catch (TransactionAlreadyExistException | AccountDoesNotExistException | TransactionAttributeException e) {
                            throw new RuntimeException(e);
                        }
                        update(privateBank.getTransactions(name));
                        accountName.setText(name + " ["+privateBank.getAccountBalance(name)+" €]");
                    }
                }
                return null;
            });
        // transfer case
        } else if(Objects.equals(menuItem.getId(),"transfer")){
            incomingInterest_sender.setText("Sender: ");
            outgoingInterest_recipient.setText("Empfänger: ");

            dialog.setResultConverter(buttonType->{
                if(buttonType == okButton){
                    if (Objects.equals(dateText.getText(), "") ||
                            Objects.equals(amountText.getText(), "") ||
                            Objects.equals(descriptionText.getText(),"") ||
                            Objects.equals(incomingInterest_senderText.getText(), "") ||
                            Objects.equals(outgoingInterest_recipientText.getText(), ""))
                    {
                        invalid.setContentText("Ungültige Werte");
                        Optional<ButtonType> optional = invalid.showAndWait();
                        if(optional.isPresent() && optional.get() == ButtonType.OK){
                            text.setText("Es wurde nicht gemacht");
                        }
                    } else {
                        // incoming transfer case
                        if(outgoingInterest_recipientText.getText().equals(name)){
                            dialog.setTitle("Incomingtransfer");
                            IncomingTransfer incomingTransfer = null;
                            try {
                                incomingTransfer = new IncomingTransfer(dateText.getText(),
                                        Double.parseDouble(amountText.getText()),
                                        descriptionText.getText(),
                                        incomingInterest_senderText.getText(),
                                        outgoingInterest_recipientText.getText());
                            } catch (TransactionAttributeException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                privateBank.addTransaction(name,incomingTransfer);
                                text.setText("Der Incomingtransfer wurde hinzugefügt");
                            } catch (TransactionAlreadyExistException | AccountDoesNotExistException | TransactionAttributeException e) {
                                throw new RuntimeException(e);
                            }
                            update(privateBank.getTransactions(name));
                            accountName.setText(name + " [" + privateBank.getAccountBalance(name) + "€]");
                        }
                        // outgoing transfer case
                        else if (incomingInterest_senderText.getText().equals(name)) {
                            dialog.setTitle("Outgoingtransfer");
                            OutgoingTransfer outgoingTransfer = null;
                            try {
                                outgoingTransfer = new OutgoingTransfer(dateText.getText(),
                                        Double.parseDouble(amountText.getText()),
                                        descriptionText.getText(),
                                        incomingInterest_senderText.getText(),
                                        outgoingInterest_recipientText.getText());
                            } catch (TransactionAttributeException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                privateBank.addTransaction(name,outgoingTransfer);
                                text.setText("Outgoing Transfer hinzugefügt");
                            } catch (TransactionAlreadyExistException | AccountDoesNotExistException |
                                     TransactionAttributeException e) {
                                throw new RuntimeException(e);
                            }
                            update(privateBank.getTransactions(name));
                            accountName.setText(name + " [" + privateBank.getAccountBalance(name) + "€]");
                        }
                    }
                }
                return null;
            });
        }
    }
}