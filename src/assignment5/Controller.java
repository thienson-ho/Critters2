package assignment5;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.lang.reflect.Method;
import java.util.List;

public class Controller {

    @FXML
    private ChoiceBox makeChoiceBox;

    @FXML
    private TextField makeTextField;

    @FXML
    private TextField seedTextField;

    @FXML
    private TextField stepTextField;

    @FXML
    private ChoiceBox statsChoiceBox;

    ObservableList<String> critterNameList = FXCollections.
            observableArrayList("Algae", "Craig", "Critter1", "Critter2", "Critter3", "Critter4");

    @FXML
    private void initialize() {
        makeChoiceBox.setItems(critterNameList);
        makeTextField.setText("1");
        stepTextField.setText("1");
        statsChoiceBox.setItems(critterNameList);
    }

    private boolean isInteger(String string) {
        try {
            Integer.valueOf(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void makeButton(ActionEvent event) {
        System.out.println("Make Button");
        int num;
        String input = makeTextField.getText();

        if(isInteger(input)) {
            num = Integer.parseInt(input);
        } else {
            System.out.println("Not an int!!");
            return;
        }

        try {
            for(int i = 0; i < num; i++){
                Critter.makeCritter(makeChoiceBox.getValue().toString());
            }
        } catch (Exception e) {
            System.out.println("Invalid Critter");
        }

    }

    public void statsButton(ActionEvent event) throws InvalidCritterException {
        String critterName = statsChoiceBox.getValue().toString();

        List<Critter> critterList = Critter.getInstances(critterName);

        try {
            Class critterType = Class.forName(Critter.myPackage + "." + critterName);
            Method m = critterType.getMethod("runStats", java.util.List.class);
            m.invoke(critterList.getClass(), critterList);
        } catch (Exception e) {
            System.out.println("No method");
        }

    }

    public void seedButton(ActionEvent event) {
        if(seedTextField.getText() == null) {
            System.out.println("Invalid seed");
            return;
        }
        System.out.println("Seed Button");
        String input = seedTextField.getText();
        int num;

        if(isInteger(input)) {
            num = Integer.parseInt(input);
        } else {
            System.out.println("Not an int!!");
            return;
        }

        Critter.setSeed(num);
    }

    public void stepButton(ActionEvent event) {
        System.out.println("Step Button");

        int num;
        String input = stepTextField.getText();

        if(isInteger(input)) {
            num = Integer.parseInt(input);
        } else {
            System.out.println("Not an int!!");
            return;
        }

        for(int i = 0; i < num; i++){
            Critter.worldTimeStep();
        }
    }

    public void displayButton(ActionEvent event) {
        Critter.displayWorld();
    }


}
