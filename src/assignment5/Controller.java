package assignment5;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {

    @FXML
    private ChoiceBox critterTypeChoiceBox;

    @FXML
    private TextField makeTextField;

    @FXML
    private TextField seedTextField;

    @FXML
    private TextField stepTextField;

    @FXML
    private GridPane world;




    @FXML
    private void initialize() {
        File file = new File("src/assignment5");

        ArrayList<String> fileNames = new ArrayList<String>(Arrays.asList(file.list()));
        ArrayList<Class> classNames = new ArrayList<>();

        for(String s: fileNames) {
            try {
                classNames.add(Class.forName(Critter.myPackage + "." + s.substring(0,s.indexOf('.'))));
            } catch (ClassNotFoundException e) {
//                System.out.println(s + " is not a class");
            }
        }

        classNames.removeIf(aClass -> !Critter.class.isAssignableFrom(aClass));

        ArrayList<String> critterTypes = new ArrayList<>();

        for(Class c: classNames) {
            String critterName = c.toString();
            critterName = critterName.substring(critterName.indexOf('.') + 1);
            critterTypes.add(critterName);
        }

        ObservableList<String> critterNameList = FXCollections.observableArrayList();
        critterNameList.addAll(critterTypes);

//        System.out.println(fileNames);
//        System.out.println(classNames);
//        System.out.println(critterTypes);

        critterTypeChoiceBox.setItems(critterNameList);
        makeTextField.setText("1");
        stepTextField.setText("1");


        world.setGridLinesVisible(true);

        world.setPrefSize(Params.prefDimension,Params.prefDimension);
        world.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, Insets.EMPTY)));

        while(world.getRowConstraints().size() > 0){
            world.getRowConstraints().remove(0);
        }

        while(world.getColumnConstraints().size() > 0){
            world.getColumnConstraints().remove(0);
        }

        for (int i = 0; i < Params.world_width; i++) {
            ColumnConstraints colConst = new ColumnConstraints(Params.prefDimension/Params.minParam);
            world.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < Params.world_height; i++) {
            RowConstraints rowConst = new RowConstraints(Params.prefDimension/Params.minParam);
            world.getRowConstraints().add(rowConst);
        }
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
            if(num < 0) {
                System.out.println("No negative ints!");
                return;
            }
        } else {
            System.out.println("Not an int!!");
            return;
        }

        try {
            for(int i = 0; i < num; i++){
                Critter.makeCritter(critterTypeChoiceBox.getValue().toString());
            }
        } catch (Exception e) {
            System.out.println("Invalid Critter");
        }

    }

    public void statsButton(ActionEvent event) throws InvalidCritterException {
        String critterName = critterTypeChoiceBox.getValue().toString();

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
        Critter.displayWorld(world);
    }


}
