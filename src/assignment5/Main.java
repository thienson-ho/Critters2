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
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
//		    	System.out.println("Height: " + height + " Width: " + width);
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
        
        scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
