package assignment5;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Main extends Application {
	
	Scene scene;
	public static scaleCanvas canvas;
	public static GraphicsContext gc;
	
	class scaleCanvas extends Canvas{
		public double height;
		public double width;
		
		public scaleCanvas() {
		      // Redraw canvas when size changes.
			  height = getHeight();
			  width = getWidth();
		      widthProperty().addListener(evt -> draw());
		      heightProperty().addListener(evt -> draw());
		    }
		 
		    private void draw() {
		      double width = getWidth();
		      double height = getHeight();
		      
		      gc = getGraphicsContext2D();
		      Critter.displayWorld(this);
		    }
		 
		    @Override
		    public boolean isResizable() {
		    	height = scene.getHeight() - 240;
		    	width = scene.getWidth();
		    	System.out.println("Height: " + height + " Width: " + width);
		    	return true;
		    }
		 
		    @Override
		    public double prefWidth(double height) {
		      return scene.getHeight() - 240;
		    }
		    
		    @Override
		    public double prefHeight(double width) {
		      return scene.getHeight() - 240;
		    }

	}
	
	
    @Override
    public void start(Stage primaryStage) throws Exception{
    	VBox root = new VBox();
    	
        Parent parental = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Critter World");
        
        canvas = new scaleCanvas();
        StackPane scaleable = new StackPane(); 
        scaleable.getChildren().add(canvas);
        canvas.widthProperty().bind(scaleable.widthProperty());
        canvas.heightProperty().bind(scaleable.heightProperty());
        
        ObservableList list = root.getChildren();
        list.addAll(scaleable, parental);
        
        scene = new Scene(root, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();

        File file = new File("src/assignment5");

        ArrayList<String> fileNames = new ArrayList<String>(Arrays.asList(file.list()));
        ArrayList<Class> classNames = new ArrayList<>();

        for(String s: fileNames) {
            try {
                classNames.add(Class.forName(Critter.myPackage + "." + s.substring(0,s.indexOf('.'))));
            } catch (ClassNotFoundException e) {
                //System.out.println(s + " is not a class");
            }
        }

        classNames.removeIf(aClass -> !Critter.class.isAssignableFrom(aClass));

        ArrayList<String> critterTypes = new ArrayList<>();
        
        for(Class c: classNames) {
            String critterName = c.toString();
            critterName = critterName.substring(critterName.indexOf('.') + 1);
            critterTypes.add(critterName);
        }


        /*
        System.out.println(fileNames);
        System.out.println(classNames);
        System.out.println(critterTypes);
        */
    }


    public static void main(String[] args) {
        launch(args);
    }
}
