package application;
	
import java.util.ArrayList;

import cabinetError.InvalidCabinetException;
import cabinetError.NoDrawersException;
import counter.DrawerCounter;
import counter.FileHolderObject;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import woodWorking.*;
import java.io.*;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			int stageLength = 1024;
			int stageHeight = 768;
			double cabinetWidth = 40;
			double cabinetHeight = 20;
			double cabinetDepth = 20;
			double cameraZoom = 800;
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
			FileHolderObject cabinetFile = new FileHolderObject();
			primaryStage.setTitle("Cabinet Builder"); //sets stage and stage name for stage 1
			GridPane root = new GridPane();
			Scene infoInputScene = new Scene(root,stageLength,stageHeight);
			infoInputScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(infoInputScene);
			
			Alert openExistingFileAlert = new Alert(Alert.AlertType.CONFIRMATION);
			openExistingFileAlert.setTitle("Do you want to open an existing file?");
			openExistingFileAlert.setContentText("Open a file or choose a place to save a new file");
			ButtonType openExistingFileButton = new ButtonType("Open an existing file",ButtonBar.ButtonData.YES);
			ButtonType newFileButton = new ButtonType("Create a new file", ButtonBar.ButtonData.OK_DONE);
			ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.NO);
			openExistingFileAlert.getButtonTypes().setAll(openExistingFileButton, newFileButton, cancelButton);
			openExistingFileAlert.showAndWait().ifPresent(type ->{
				if(type == openExistingFileButton)
				{
					fileChooser.setTitle("Please choose a cabinet project CSV file to use");
					cabinetFile.setFile(fileChooser.showOpenDialog(primaryStage));
				}
				else if(type == newFileButton)
				{
					fileChooser.setTitle("Please choose a place to save the project");
					cabinetFile.setFile(fileChooser.showSaveDialog(primaryStage));
				}
				else
				{
					
				}
			});
			
			if(cabinetFile.getFile()==null)
			{
				throw new IOException("Exited Prematurely");
			}
			DrawerCounter drawerCount = new DrawerCounter(0);
			//Creates the Cabinet Object
			SingleCabinet cabinetObject = new SingleCabinet(cabinetWidth, cabinetHeight, cabinetDepth);
			try(BufferedReader br = new BufferedReader(new FileReader(cabinetFile.getFile())))
			{
				br.readLine();
				String[] dimensions = br.readLine().split(",");
				cabinetObject.setDimensions(Double.parseDouble(dimensions[0]), Double.parseDouble(dimensions[1]), Double.parseDouble(dimensions[2]));
				br.readLine();
				String line;
				
				while((line = br.readLine())!=null)
				{
					String[] drawDims = line.split(",");
					
					cabinetObject.addDrawer(Double.parseDouble(drawDims[0]),Double.parseDouble(drawDims[1]),Double.parseDouble(drawDims[2]));
					
				}
				if(cabinetObject.checkDimensions()==false)
				{
					throw new InvalidCabinetException();
				}
					
			}
			catch(InvalidCabinetException e)
			{
				throw new InvalidCabinetException("This file is corrupted and cannot be used");
			}
			catch(IOException e)
			{
				//System.out.println(e);
				System.out.println("File not found, Creating new file");
				
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			
			primaryStage.setTitle("Cabinet Builder: "+cabinetFile.getFile().getName());
			primaryStage.show();
			//sets alignment settings
			root.setAlignment(Pos.TOP_CENTER);
			root.setPadding(new Insets(25,25,25,25));
			root.setVgap(10);
			root.setHgap(10);
			//adds some labels and text fields
			root.add(new Label("Cabinet Dimentions"),0,0,2,1);
			root.add(new Label("Height:"),0,1);
			root.add(new Label("Width:"),0,2);
			root.add(new Label("Depth:"),0,3);
			TextField cabinetWidthField = new TextField(Double.toString(cabinetObject.getWidth()));
			TextField cabinetHeightField = new TextField(Double.toString(cabinetObject.getHeight()));
			TextField cabinetDepthField = new TextField(Double.toString(cabinetObject.getDepth()));
			root.add(cabinetHeightField, 1, 1);
			root.add(cabinetWidthField, 1, 2);
			root.add(cabinetDepthField, 1, 3);
			/* Create two buttons
			 * One to store measurements
			 * One to Cancel
			 * */
			Button displayButton = new Button("Display");
			Button exitButton = new Button("Save and Exit");
			//create a horizontal box and add the buttons to that box
			HBox cabButtonBox = new HBox(10);
			cabButtonBox.getChildren().add(displayButton);
			cabButtonBox.getChildren().add(exitButton);
			cabButtonBox.setAlignment(Pos.BOTTOM_RIGHT);
			//Add the buttonBox to the bottom of the grid
			root.add(cabButtonBox, 0, 4, 2, 1);
			//controls for button
			/*
			 * Labels, Fields and Buttons for Drawers
			 * Starting with labels
			 */
			Label drawerNumberLabel = new Label("Dimentions for Drawer: 1");
			root.add(drawerNumberLabel,0,5,2,1);
			root.add(new Label("Height:"),0,6);
			root.add(new Label("Width:"),0,7);
			root.add(new Label("Depth:"),0,8);
			//Fields
			TextField drawerWidthField;
			TextField drawerHeightField;
			TextField drawerDepthField;
			if(cabinetObject.isCabinetEmpty())
			{
				drawerNumberLabel.setText("Dimentions for Drawer: There are currently no drawers");
				drawerWidthField = new TextField("");
				drawerHeightField = new TextField("");
				drawerDepthField = new TextField("");
			} 
			else 
			{
				drawerWidthField = new TextField(Double.toString(cabinetObject.getDrawerWidth(drawerCount.index)));
				drawerHeightField = new TextField(Double.toString(cabinetObject.getDrawerHeight(drawerCount.index)));
				drawerDepthField = new TextField(Double.toString(cabinetObject.getDrawerDepth(drawerCount.index)));
			}
			
			
			root.add(drawerHeightField, 1, 6);
			root.add(drawerWidthField, 1, 7);
			root.add(drawerDepthField, 1, 8);
			//adds Buttons
			Button nextDrawerButton =new Button("Next");
			Button prevDrawerButton = new Button("Previous");
			Button addDrawerButton = new Button("Add Drawer");
			Button removeDrawerButton = new Button("Remove Current Drawer");
			HBox drawerButtonBox = new HBox(47);
			drawerButtonBox.getChildren().add(prevDrawerButton);
			drawerButtonBox.getChildren().add(removeDrawerButton);
			drawerButtonBox.getChildren().add(addDrawerButton);
			drawerButtonBox.getChildren().add(nextDrawerButton);
			drawerButtonBox.setAlignment(Pos.BOTTOM_RIGHT);
			root.add(drawerButtonBox, 0,9,2,1);
			/*
			 * Creates things to go on scene two
			 * */
			Box cabinet = new Box(); //creates place holder cabinet
			cabinet.setWidth(cabinetWidth);
			cabinet.setHeight(cabinetHeight);
			cabinet.setDepth(cabinetDepth);
			cabinet.setDrawMode(DrawMode.FILL);
			cabinet.setCullFace(CullFace.BACK);
			cabinet.setTranslateX(stageLength/2);
			cabinet.setTranslateY(stageHeight/2);
			cabinet.setTranslateZ(0);
			PhongMaterial wood = new PhongMaterial(); //sets new Material to the cabinet
			wood.setDiffuseColor(Color.SANDYBROWN);
			cabinet.setMaterial(wood);
			Group cabinetDisplayGroup = new Group(cabinet);
			Button backToInfoButton = new Button("Back To Previous Screen");
			backToInfoButton.setTranslateX(10);
			backToInfoButton.setTranslateY(5);
			backToInfoButton.setTranslateZ(cameraZoom);
			cabinetDisplayGroup.getChildren().add(backToInfoButton);
			PhongMaterial drawerWood = new PhongMaterial(); //Declares the material for the Drawers
			drawerWood.setDiffuseColor(Color.SADDLEBROWN);
			ArrayList<Box> displayDrawers = new ArrayList<>();
			for(int i = 0; i<cabinetObject.getDrawerListSize(); i++)
			{
				displayDrawers.add(new Box());
			}
			for(int i = 0; i<cabinetObject.getDrawerListSize(); i++)
			{
				displayDrawers.get(i).setWidth(cabinetObject.getDrawerWidth(i));
				displayDrawers.get(i).setHeight(cabinetObject.getDrawerHeight(i));
				displayDrawers.get(i).setDepth(cabinetObject.getDrawerDepth(i));
				cabinet.setDrawMode(DrawMode.FILL);
				cabinet.setCullFace(CullFace.BACK);
				displayDrawers.get(i).setTranslateX((stageLength/2)+0);
				displayDrawers.get(i).setTranslateY((stageHeight/2)+cabinetObject.getDrawerYPosition(i, stageHeight));
				
				//System.out.println(cabinetObject.getDrawerYPosition(i, stageHeight)); //SYSOUT
				
				displayDrawers.get(i).setTranslateZ(-2);
				displayDrawers.get(i).setMaterial(drawerWood);
				cabinetDisplayGroup.getChildren().add(displayDrawers.get(i));
			}
			
			/*
			 * Code that will create the second scene
			 * This scene will display the cabinet as a 3D Shape
			 * */
			Scene cabinetDisplayArea = new Scene(cabinetDisplayGroup, stageLength,stageHeight);
			PerspectiveCamera cabinetDisplayCamera = new PerspectiveCamera(false);
			cabinetDisplayCamera.setTranslateX(0);
			cabinetDisplayCamera.setTranslateY(0);
			cabinetDisplayCamera.setTranslateZ(cameraZoom);
			cabinetDisplayArea.setCamera(cabinetDisplayCamera);
			/*
			 * 
			 * BUTTON CONTROLS
			 * 
			 * */
			nextDrawerButton.setOnAction(event -> {
				try
				{
					String heightString = cabinetHeightField.getText();
					String widthString = cabinetWidthField.getText();
					String depthString = cabinetDepthField.getText();
					Double heightNumInput = Double.parseDouble(heightString);
					Double widthNumInput = Double.parseDouble(widthString);
					Double depthNumInput = Double.parseDouble(depthString);
					if(cabinetObject.isCabinetEmpty())
					{
						cabinetObject.setAndCheckDimensions(widthNumInput, heightNumInput, depthNumInput);
						cabinet.setWidth(cabinetObject.getWidth());
						cabinet.setHeight(cabinetObject.getHeight());
						cabinet.setDepth(cabinetObject.getDepth());
					}
					if(cabinetObject.isCabinetEmpty())
					{
						throw new NoDrawersException();
					}
					String drawerWidthString = drawerWidthField.getText();
					String drawerHeightString = drawerHeightField.getText();
					String drawerDepthString = drawerDepthField.getText();
					Double drawerWidthNumInput = Double.parseDouble(drawerWidthString);
					Double drawerHeightNumInput = Double.parseDouble(drawerHeightString);
					Double drawerDepthNumInput = Double.parseDouble(drawerDepthString);
					cabinetObject.setAndCheckDimensions(widthNumInput, heightNumInput, depthNumInput, drawerWidthNumInput, drawerHeightNumInput, drawerDepthNumInput,drawerCount.index);
					cabinet.setWidth(cabinetObject.getWidth());
					cabinet.setHeight(cabinetObject.getHeight());
					cabinet.setDepth(cabinetObject.getDepth());
					cabinetObject.setDrawerDimensions(drawerCount.index, drawerWidthNumInput, drawerHeightNumInput, drawerDepthNumInput);
					//increments counter
					if(drawerCount.index>=cabinetObject.getDrawerListSize()-1)
					{
						throw new ArrayIndexOutOfBoundsException();
					}
					drawerCount.incrementCounter();
					drawerNumberLabel.setText("Dimentions for Drawer: "+(drawerCount.index+1));
					drawerWidthField.setText(Double.toString(cabinetObject.getDrawerWidth(drawerCount.index)));
					drawerHeightField.setText(Double.toString(cabinetObject.getDrawerHeight(drawerCount.index)));
					drawerDepthField.setText(Double.toString(cabinetObject.getDrawerDepth(drawerCount.index)));
				}
				catch(NoDrawersException e)
				{
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("There are no drawers in this cabinet");
					alert.setContentText("Please add drawers in order to use this button");
					alert.showAndWait();
				}
				catch(InvalidCabinetException e)
				{
					//System.err.println(e);
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Cabinet Dimensions are incorrect");
					alert.setContentText("The Dimensions for the cabinet are not possible.");
					alert.showAndWait();
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("You are already on the Last Drawer");
					alert.setContentText("You cannot go past this drawer.\n"
							+ "If you want to then click the add drawer button");
					alert.showAndWait();
				}
				catch(Exception e)
				{
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Please Enter a Number");
					alert.setContentText("Please correct dimensions before attempting to go to the next drawer.");
					alert.showAndWait();
				}
			});
			prevDrawerButton.setOnAction(event -> {
				try
				{
					String heightString = cabinetHeightField.getText();
					String widthString = cabinetWidthField.getText();
					String depthString = cabinetDepthField.getText();
					Double heightNumInput = Double.parseDouble(heightString);
					Double widthNumInput = Double.parseDouble(widthString);
					Double depthNumInput = Double.parseDouble(depthString);
					if(cabinetObject.isCabinetEmpty())
					{
						cabinetObject.setAndCheckDimensions(widthNumInput, heightNumInput, depthNumInput);
						cabinet.setWidth(cabinetObject.getWidth());
						cabinet.setHeight(cabinetObject.getHeight());
						cabinet.setDepth(cabinetObject.getDepth());
					}
					if(cabinetObject.isCabinetEmpty())
					{
						throw new NoDrawersException();
					}
					String drawerWidthString = drawerWidthField.getText();
					String drawerHeightString = drawerHeightField.getText();
					String drawerDepthString = drawerDepthField.getText();
					Double drawerWidthNumInput = Double.parseDouble(drawerWidthString);
					Double drawerHeightNumInput = Double.parseDouble(drawerHeightString);
					Double drawerDepthNumInput = Double.parseDouble(drawerDepthString);
					cabinetObject.setAndCheckDimensions(widthNumInput, heightNumInput, depthNumInput, drawerWidthNumInput, drawerHeightNumInput, drawerDepthNumInput,drawerCount.index);
					cabinet.setWidth(cabinetObject.getWidth());
					cabinet.setHeight(cabinetObject.getHeight());
					cabinet.setDepth(cabinetObject.getDepth());
					cabinetObject.setDrawerDimensions(drawerCount.index, drawerWidthNumInput, drawerHeightNumInput, drawerDepthNumInput);
					//Decrements counter
					drawerCount.decrementCounter();
					drawerNumberLabel.setText("Dimentions for Drawer: "+(drawerCount.index+1));
					drawerWidthField.setText(Double.toString(cabinetObject.getDrawerWidth(drawerCount.index)));
					drawerHeightField.setText(Double.toString(cabinetObject.getDrawerHeight(drawerCount.index)));
					drawerDepthField.setText(Double.toString(cabinetObject.getDrawerDepth(drawerCount.index)));
				}
				catch(NoDrawersException e)
				{
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("There are no drawers in this cabinet");
					alert.setContentText("Please add drawers in order to use this button");
					alert.showAndWait();
				}
				catch(InvalidCabinetException e)
				{
					//System.err.println(e);
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Cabinet Dimensions are incorrect");
					alert.setContentText("The Dimensions for the cabinet are not possible.");
					alert.showAndWait();
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("You are already on the first Drawer");
					alert.setContentText("You cannot go past 1.");
					alert.showAndWait();
				}
				catch(Exception e)
				{
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Please enter a number");
					alert.setContentText("Please correct dimensions before moving to the previous drawer.");
					alert.showAndWait();
				}
			});
			addDrawerButton.setOnAction(event -> {
				try
				{
					String heightString = cabinetHeightField.getText();
					String widthString = cabinetWidthField.getText();
					String depthString = cabinetDepthField.getText();
					Double heightNumInput = Double.parseDouble(heightString);
					Double widthNumInput = Double.parseDouble(widthString);
					Double depthNumInput = Double.parseDouble(depthString);
					if(cabinetObject.isCabinetEmpty())
					{
						cabinetObject.setAndCheckDimensions(widthNumInput, heightNumInput, depthNumInput);
						cabinet.setWidth(cabinetObject.getWidth());
						cabinet.setHeight(cabinetObject.getHeight());
						cabinet.setDepth(cabinetObject.getDepth());
					}
					if(!cabinetObject.isCabinetEmpty())
					{
						String drawerWidthString = drawerWidthField.getText();
						String drawerHeightString = drawerHeightField.getText();
						String drawerDepthString = drawerDepthField.getText();
						Double drawerWidthNumInput = Double.parseDouble(drawerWidthString);
						Double drawerHeightNumInput = Double.parseDouble(drawerHeightString);
						Double drawerDepthNumInput = Double.parseDouble(drawerDepthString);
						cabinetObject.setAndCheckDimensions(widthNumInput, heightNumInput, depthNumInput, drawerWidthNumInput, drawerHeightNumInput, drawerDepthNumInput,drawerCount.index);
						cabinet.setWidth(cabinetObject.getWidth());
						cabinet.setHeight(cabinetObject.getHeight());
						cabinet.setDepth(cabinetObject.getDepth());
						cabinetObject.setDrawerDimensions(drawerCount.index, drawerWidthNumInput, drawerHeightNumInput, drawerDepthNumInput);
					}
					
					//Works with the Drawers
					if(cabinetObject.getDrawerListSize()!=0) 
					{
						String drawerWidthString = drawerWidthField.getText();
						String drawerHeightString = drawerHeightField.getText();
						String drawerDepthString = drawerDepthField.getText();
						Double  drawerWidthNumInput = Double.parseDouble(drawerWidthString);
						Double drawerHeightNumInput = Double.parseDouble(drawerHeightString);
						Double drawerDepthNumInput = Double.parseDouble(drawerDepthString);
						cabinetObject.setDrawerDimensions(drawerCount.index, drawerWidthNumInput, drawerHeightNumInput, drawerDepthNumInput);
					}
					
					
					if(cabinetObject.canAddDrawer()==false)
					{
						throw new InvalidCabinetException("There is not enough space for another drawer!");
					}
					
					cabinetObject.addDrawer();
					drawerCount.index = cabinetObject.getDrawerListSize()-1;
					drawerNumberLabel.setText("Dimentions for Drawer: "+(drawerCount.index+1));
					drawerWidthField.setText(Double.toString(cabinetObject.getDrawerWidth(drawerCount.index)));
					drawerHeightField.setText(Double.toString(cabinetObject.getDrawerHeight(drawerCount.index)));
					drawerDepthField.setText(Double.toString(cabinetObject.getDrawerDepth(drawerCount.index)));
					
					displayDrawers.add(new Box());
					cabinet.setDrawMode(DrawMode.FILL);
					cabinet.setCullFace(CullFace.BACK);
					displayDrawers.get(drawerCount.index).setMaterial(drawerWood);
					cabinetDisplayGroup.getChildren().add(displayDrawers.get(drawerCount.index));
				}
				catch(InvalidCabinetException e)
				{
					//System.err.println(e);
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Cabinet Dimensions are incorrect");
					alert.setContentText("Please correct dimensions before attempting to add a new drawer.");
					alert.showAndWait();
				}
				catch(Exception e)
				{
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Please enter a number");
					alert.setContentText("Please correct dimensions before attempting to add a new drawer.");
					alert.showAndWait();
				}
			});
			removeDrawerButton.setOnAction(event ->{
				try
				{
					
					cabinetObject.removeDrawer(drawerCount.index);
					if(!(cabinetObject.isCabinetEmpty())&&drawerCount.index!=0)
					{
						drawerCount.decrementCounter();
					}
					else if(cabinetObject.isCabinetEmpty())
					{
						drawerCount.index = -1;
					}
					if(cabinetObject.isCabinetEmpty())
					{
						drawerNumberLabel.setText("Dimentions for Drawer: There are currently no drawers");
						drawerWidthField.setText("");
						drawerHeightField.setText("");
						drawerDepthField.setText("");
					}
					else
					{
						drawerNumberLabel.setText("Dimentions for Drawer: "+(drawerCount.index+1));
						drawerWidthField.setText(Double.toString(cabinetObject.getDrawerWidth(drawerCount.index)));
						drawerHeightField.setText(Double.toString(cabinetObject.getDrawerHeight(drawerCount.index)));
						drawerDepthField.setText(Double.toString(cabinetObject.getDrawerDepth(drawerCount.index)));
					}
					cabinetDisplayGroup.getChildren().remove(displayDrawers.get(drawerCount.index));
					displayDrawers.remove(drawerCount.index);
					
				}
				catch(Exception e)
				{
					//System.err.print(e);
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Unable to remove a drawer");
					alert.setContentText("There are no drawers to remove");
					alert.showAndWait();
				}
			});
			displayButton.setOnAction(event -> {
				try 
				{
					String heightString = cabinetHeightField.getText();
					String widthString = cabinetWidthField.getText();
					String depthString = cabinetDepthField.getText();
					Double heightNumInput = Double.parseDouble(heightString);
					Double widthNumInput = Double.parseDouble(widthString);
					Double depthNumInput = Double.parseDouble(depthString);
					if(cabinetObject.isCabinetEmpty())
					{
						cabinetObject.setAndCheckDimensions(widthNumInput, heightNumInput, depthNumInput);
						cabinet.setWidth(cabinetObject.getWidth());
						cabinet.setHeight(cabinetObject.getHeight());
						cabinet.setDepth(cabinetObject.getDepth());
					}
					
					
					//Works with the Drawers
					if(cabinetObject.getDrawerListSize()!=0)
					{
						String drawerWidthString = drawerWidthField.getText();
						String drawerHeightString = drawerHeightField.getText();
						String drawerDepthString = drawerDepthField.getText();
						Double drawerWidthNumInput = Double.parseDouble(drawerWidthString);
						Double drawerHeightNumInput = Double.parseDouble(drawerHeightString);
						Double drawerDepthNumInput = Double.parseDouble(drawerDepthString);
						cabinetObject.setAndCheckDimensions(widthNumInput, heightNumInput, depthNumInput, drawerWidthNumInput, drawerHeightNumInput, drawerDepthNumInput,drawerCount.index);
						cabinet.setWidth(cabinetObject.getWidth());
						cabinet.setHeight(cabinetObject.getHeight());
						cabinet.setDepth(cabinetObject.getDepth());
						cabinetObject.setDrawerDimensions(drawerCount.index, drawerWidthNumInput, drawerHeightNumInput, drawerDepthNumInput);
						
						for(int i = 0; i<cabinetObject.getDrawerListSize(); i++)
						{
							displayDrawers.get(i).setWidth(cabinetObject.getDrawerWidth(i));
							displayDrawers.get(i).setHeight(cabinetObject.getDrawerHeight(i));
							displayDrawers.get(i).setDepth(cabinetObject.getDrawerDepth(i));
							displayDrawers.get(i).setTranslateX((stageLength/2)+0);
							displayDrawers.get(i).setTranslateY(cabinetObject.getDrawerYPosition(i, stageHeight));
							displayDrawers.get(i).setTranslateZ(-2);
						}
					}
				primaryStage.setScene(cabinetDisplayArea);
					
				}
				catch(InvalidCabinetException e)
				{
					//System.err.println(e);
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Cabinet Dimensions are incorrect");
					alert.setContentText("The Dimensions for the cabinet are not possible.");
					alert.showAndWait();
				}
				catch(Exception e)
				{
					//System.err.println(e);
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Please enter a number");
					alert.setContentText("The Dimensions for the cabinet are not possible and cannot correctly be displayed\n"
							+ "Please Correct and try again.");
					alert.showAndWait();
				}
				
			});
			backToInfoButton.setOnAction(event -> {
				primaryStage.setScene(infoInputScene);
			});
			exitButton.setOnAction(event -> {
				//saves data to variables
				try
				{
				String heightString = cabinetHeightField.getText();
				String widthString = cabinetWidthField.getText();
				String depthString = cabinetDepthField.getText();
				Double heightNumInput = Double.parseDouble(heightString);
				Double widthNumInput = Double.parseDouble(widthString);
				Double depthNumInput = Double.parseDouble(depthString);
				if(cabinetObject.isCabinetEmpty())
				{
					cabinetObject.setAndCheckDimensions(widthNumInput, heightNumInput, depthNumInput);
					cabinet.setWidth(cabinetObject.getWidth());
					cabinet.setHeight(cabinetObject.getHeight());
					cabinet.setDepth(cabinetObject.getDepth());
				}
				if(cabinetObject.getDrawerListSize()!=0) 
				{
					String drawerWidthString = drawerWidthField.getText();
					String drawerHeightString = drawerHeightField.getText();
					String drawerDepthString = drawerDepthField.getText();
					Double drawerWidthNumInput = Double.parseDouble(drawerWidthString);
					Double drawerHeightNumInput = Double.parseDouble(drawerHeightString);
					Double drawerDepthNumInput = Double.parseDouble(drawerDepthString);
					cabinetObject.setAndCheckDimensions(widthNumInput, heightNumInput, depthNumInput, drawerWidthNumInput, drawerHeightNumInput, drawerDepthNumInput,drawerCount.index);
					cabinet.setWidth(cabinetObject.getWidth());
					cabinet.setHeight(cabinetObject.getHeight());
					cabinet.setDepth(cabinetObject.getDepth());
					cabinetObject.setDrawerDimensions(drawerCount.index, drawerWidthNumInput, drawerHeightNumInput, drawerDepthNumInput);
				}
				primaryStage.close();
				System.out.println("Exited Sucessfully");
				try(FileWriter myWriter = new FileWriter(cabinetFile.getFile()))
				{
					myWriter.write(cabinetObject.getText());
				}
				catch(IOException e)
				{
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("File is missing");
					alert.setContentText("The file is missing.");
					alert.showAndWait();
				}
				}
				catch(InvalidCabinetException e)
				{
					//System.err.println(e);
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Cabinet Dimensions are incorrect");
					alert.setContentText("The Dimensions for the cabinet are not possible.");
					alert.showAndWait();
				}
			});
			System.out.println("Setup Completed");
		} 
		catch(InvalidCabinetException e)
		{
			Alert fileCorrupedAlert = new Alert(Alert.AlertType.ERROR);
			fileCorrupedAlert.setTitle("Error");
			fileCorrupedAlert.setHeaderText("The file entered is corruped");
			fileCorrupedAlert.setContentText("The file chosen is corruped and cannot be used. Please restart and choose a new file.");
			fileCorrupedAlert.showAndWait();
		}
		catch(IOException e)
		{
			System.out.println("Session ended abruptly due to file error");
		}
		catch(Exception e) {
			
			e.printStackTrace();
		}
		
	} //end of start
	
	public static void main(String[] args) {
		launch(args);
	}
}
