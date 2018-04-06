package assignment5;
/* EE422C Project 5 submission by
 * <ThienSon Ho>
 * <tsh848>
 * <15505>
 * <Arjun Singh>
 * <AS78363>
 * <15505>
 * Slip days used: <0>
 * Spring 2018
 */
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {

    @FXML
    private ChoiceBox critterNameChoiceBox;

    @FXML
    private TextField makeTextField;

    @FXML
    private TextField seedTextField;

    @FXML
    private TextField stepTextField;

    @FXML
    private ToggleButton toggleButton;

    @FXML
    private TextArea messageBox;

    private AnimationTimer a;

    private ObservableList<String> critterNameList;

    private ObservableList<String> statsList;

    /**
     * Initializes control components of the scene
     */
    @FXML
    private void initialize() {

        File file = new File("src/assignment5");

        ArrayList<String> fileNames = new ArrayList<String>(Arrays.asList(file.list()));
        ArrayList<Class> classNames = new ArrayList<>();

        fileNames.removeIf(s -> s.equals("Critter.java"));

        for(String s: fileNames) {
            try {
                classNames.add(Class.forName(Critter.myPackage + "." + s.substring(0,s.indexOf('.'))));
            } catch (ClassNotFoundException e) {

            }
        }

        classNames.removeIf(aClass -> !Critter.class.isAssignableFrom(aClass));

        ArrayList<String> critterTypes = new ArrayList<>();

        for(Class c: classNames) {
            String critterName = c.toString();
            critterName = critterName.substring(critterName.indexOf('.') + 1);
            critterTypes.add(critterName);
        }

        critterNameList = FXCollections.observableArrayList();
        critterNameList.addAll(critterTypes);

        critterNameChoiceBox.setItems(critterNameList);
        makeTextField.setText("1");
        stepTextField.setText("1");

        a = new AnimationTimer() {

            private long lastUpdate = 0 ;
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 500_000_000) {
                    try {
                        animate();
                    } catch (InvalidCritterException e) {
                        e.printStackTrace();
                    }
                    lastUpdate = now ;
                }
            }

        };

        toggleButton.setStyle("-fx-base: green;");

        String s = "Welcome to Critter Simulation by Arjun and ThienSon\n";
        messageBox.appendText(s);
    }

    /**
     * Animates the critter world based on the step/play speed
     * @throws InvalidCritterException
     */
    private void animate() throws InvalidCritterException {
        int num;
        String input = stepTextField.getText();

        if(isInteger(input)) {
            num = Integer.parseInt(input);
        } else {
//            System.out.println("Not an int!!");
            messageBox.appendText("Please enter a positive integer\n");
            return;
        }

        for(int i = 0; i < num; i++){
            Critter.worldTimeStep();
        }

        Critter.displayWorld(Main.canvas);
        outputStats();
    }

    /**
     * Checks if a parsed String is a valid integer
     * @param string to parse
     * @return true or false
     */
    private boolean isInteger(String string) {
        try {
            Integer.valueOf(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Button command to make critters depending on the selection from the drop down menu
     * @param event
     */
    public void makeButton(ActionEvent event) {
//        System.out.println("Make Button");
        int num;
        String input = makeTextField.getText();

        if(isInteger(input)) {
            num = Integer.parseInt(input);
            if(num < 0) {
                messageBox.appendText("Please enter a positive integer\n");
                return;
            }
        } else {
            messageBox.appendText("Please enter a positive integer\n");
            return;
        }

        try {
            for(int i = 0; i < num; i++){
                Critter.makeCritter(critterNameChoiceBox.getValue().toString());
            }

            Critter.displayWorld(Main.canvas);
        } catch (Exception e) {
            messageBox.appendText("Invalid Critter\n");
//            System.out.println("Invalid Critter");
        }


    }

    /**
     * Displays stats of the selected critters to the message box
     * @throws InvalidCritterException
     */
    public void outputStats() throws InvalidCritterException {
        if(statsList == null || statsList.isEmpty()) {
            return;
        }

        statsList.removeIf(s -> s.equals("Critters"));

        for(String critterName: statsList) {
            List<Critter> critterList = Critter.getInstances(critterName);

            try {
                Class critterType = Class.forName(Critter.myPackage + "." + critterName);
                Method m = critterType.getMethod("runStats", java.util.List.class);
                String output = (String)m.invoke(critterList.getClass(), critterList);
                messageBox.appendText(output + "\n");
            } catch (Exception e) {
                System.out.println("No method");
            }
        }
        messageBox.appendText("-------------------------------------------------------------------\n");
    }

    /**
     * Sets the random seed
     * @param event
     */
    public void seedButton(ActionEvent event) {
        if(seedTextField.getText() == null) {
            messageBox.appendText("Invalid seed\n");
            return;
        }

        String input = seedTextField.getText();
        int num;

        if(isInteger(input)) {
            num = Integer.parseInt(input);
        } else {
            messageBox.appendText("Invalid seed. Please enter an integer\n");
            return;
        }

        Critter.setSeed(num);
    }

    /**
     * Button to step through the grid depending on the number of desired times
     * @param event
     * @throws InvalidCritterException
     */
    public void stepButton(ActionEvent event) throws InvalidCritterException {
        int num;
        String input = stepTextField.getText();

        if(isInteger(input)) {
            num = Integer.parseInt(input);
            if(num < 0) {
//                System.out.println("No negative ints!");
                messageBox.appendText("Please enter a positive integer\n");
                return;
            }
        } else {
            messageBox.appendText("Please enter a positive integer\n");
            return;
        }

        for(int i = 0; i < num; i++){
            Critter.worldTimeStep();
        }

        Critter.displayWorld(Main.canvas);
        outputStats();
    }

    /**
     * Calls the animate function to animate the world. Stops animating when pressed again.
     * @param event
     */
    public void playButton(ActionEvent event) {
        if(toggleButton.isSelected()) {
            a.start();
        } else {
            a.stop();
        }
    }

    /**
     * Toggles the grid of the map
     * @param event
     * @throws InvalidCritterException
     */
    public void toggleGridButton(ActionEvent event) throws InvalidCritterException {
        Critter.gridFlag = !Critter.gridFlag;
        Critter.displayWorld(Main.canvas);
    }

    /**
     * public function to display anything to the message box
     * @param message
     */
    public void displayMessage(String message) {
        messageBox.appendText(message + "\n");
    }

    /**
     * Button to display stats to messagebox
     * @param event
     */
    public void statsButton(ActionEvent event) {
        Stage stage = new Stage();
        VBox box = new VBox();
        box.setPadding(new Insets(10));
        // How to center align content in a layout manager in JavaFX
        box.setAlignment(Pos.CENTER);
        Label label = new Label("Select which Critters to display stats");

        CheckBoxTreeItem checkBox = new CheckBoxTreeItem<>();
        CheckBoxTreeItem<String> critterNames = new CheckBoxTreeItem<String>("Critters");
        critterNames.setExpanded(true);

        for(String s: critterNameList) {
            CheckBoxTreeItem<String> c = new CheckBoxTreeItem<String>(s);
            critterNames.getChildren().add(c);
        }

        // create the treeView
        final TreeView<String> treeView = new TreeView<String>();
        treeView.setRoot(critterNames);

        // set the cell factory
        treeView.setCellFactory(CheckBoxTreeCell.<String>forTreeView());

        Button btnLogin = new Button();
        btnLogin.setText("OK");
        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                statsList = FXCollections.observableArrayList();
                findCheckedItems((CheckBoxTreeItem<?>) treeView.getRoot(), statsList);
                statsList.removeIf(s -> s.equals("Critter"));
                stage.close(); // return to main window
            }
        });
        box.getChildren().add(label);
        box.getChildren().add(treeView);
        box.getChildren().add(btnLogin);
        Scene scene = new Scene(box, 250, 400);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Finds the selected Critters of the stats function
     * @param item to check
     * @param checkedItems
     */
    private void findCheckedItems(CheckBoxTreeItem<?> item, ObservableList<String> checkedItems) {
        if (item.isSelected()) {
            checkedItems.add((String)item.getValue());
        }
        for (TreeItem<?> child : item.getChildren()) {
            findCheckedItems((CheckBoxTreeItem<?>) child, checkedItems);
        }
    }

    /**
     * Displays simulation controls in message box
     * @param event
     */
    public void instructionButton(ActionEvent event) {
        String output =
        "Select the type of critter to make with the drop down menu.\n"
        + "Enter the number of critters to make (default is 1)\n"
        + "Enter the number of steps or simulation speed.\n"
        + "Press Play to start and stop simulation\n"
        + "Enter the seed number for the random generator to use.\n"
        + "Use toggle grid to turn on/off the grid lines.\n"
        + "Press the stats button to select which critters to display stats for.\n"
        + "Click and drag to resive the window and simulation grid";

        messageBox.appendText(output);
    }
}
