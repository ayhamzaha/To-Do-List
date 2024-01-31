package application;

import java.io.IOException;


import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import javafx.scene.image.*;

import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Controller{
	
	@FXML
	public VBox taskCenter;
	
	@FXML
	public ScrollPane taskArea;
	
	@FXML
	public Button NewTask;
	
	@FXML
	public TextField initText;
	
	@FXML
	public Button create;
	
	@FXML
	public Button calbtn;
	
	private List<HBox> tasks = new ArrayList<>();
	private List<Slider> slids = new ArrayList<>();
	private List<CheckBox> cbs = new ArrayList<>();
	private List<DatePicker> dps = new ArrayList<>();
	private List<TextField> lbs = new ArrayList<>();
	
	
	
	@FXML
	private MenuItem edit;
	
	@FXML
	private MenuItem delete;
	
	public double locY;
	private int i=0;
	public int num = i;
	private Label errorMessage = new Label("Please fill in empty tasks before making a new one!");
	
	@FXML
	private MenuButton Filter;
	
	@FXML
	private MenuItem Default;
	
	@FXML
	private MenuItem Rating;
	
	@FXML
	private MenuItem Completion;
	
	@FXML
	private MenuItem Newest;
	
	private String filt = "Default";
	private int op=0;
	private AnchorPane root1;

//CREATING A BUTTON AND SETTING FILTER VALUE
	
	private void errorDisp(Label a) {
		Image tasF = new Image("tasF.png");
		
		BorderPane apc = new BorderPane();
		Scene nsc = new Scene(apc,350,150);
		Stage s1 = new Stage();
		s1.setTitle("Error!");
		s1.setResizable(false);
		s1.setAlwaysOnTop(true);
		s1.getIcons().add(tasF);
		s1.setScene(nsc);
		apc.setCenter(a);
		s1.show();
		
		
		s1.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				apc.getChildren().clear();
						
			}
					
		});
	}
	
	private boolean newTaskReady() {
		
		for(int i=1; i<lbs.size();i++) {
			if(lbs.get(i).getText().isBlank()) {
				System.out.println("Error there is a blank task!");
				errorDisp(errorMessage);
				return false;
			}
		}
		
		System.out.println("No blank tasks here");
		return true;
	}
	
	private void maintenance() {
		if(tasks.isEmpty()) {
			tasks.add(hb(new HBox()));
		}
	}
	public void createTask() {
		if(newTaskReady()) {
			maintenance();
			i++;
		
			tasks.add(hb(new HBox()));
		
			taskCenter.getChildren().add(tasks.get(i));
		}
		
		switch(filt) {
			case "Default":
				defaultFilt();
				break;
			case "Newest":
				newsetFilt();
				break;
			case "Rating":
				ratingFilt();
				break;
			case "Completed":
				completionFilt();
				break;
		}
		
		
	}
	
	
	//SETTING TASK NODES ATTRIBUTES AND RETURNING THEM, HANDLING OF CONTEXT MENU (EDIT AND DELETE), SETTING COLOR VALUE BASED ON RATING
	
	private CheckBox cb(CheckBox c) {
		c.setPrefHeight(87);
		c.setPrefWidth(85);
		c.setText("Done");
		c.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
		cbs.add(c);
		
		return c;
	}
	
	private TextField ta(TextField t, int num) {
		MenuItem edit = new MenuItem("Edit");
		MenuItem delete = new MenuItem("Delete");
		ContextMenu cm = new ContextMenu();
		
		cm.getItems().addAll(edit,delete);
		
		t.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e1) {
				
				
					delete.setOnAction(e->{
						for(int f=0;f<tasks.size();f++) {
							if(tasks.get(f)==t.getParent()) {
								taskCenter.getChildren().remove(tasks.get(f));
								tasks.remove(f);
								slids.remove(f);
								cbs.remove(f);
								dps.remove(f);
								lbs.remove(f);
								i--;
							}
						}
				});
				
			}
			
		}); 
		


		
		
		edit.setOnAction(e->{
			t.setEditable(true);
		});
		
		
		t.setPrefHeight(87);
		t.setPrefWidth(276);
		
		javafx.scene.text.Font t1 = javafx.scene.text.Font.font(13.0);
		t.setFont(t1);
		t.setPromptText("Enter Task Name Here...");
		t.setContextMenu(cm);
		t.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
		
		 t.setOnAction(e -> {
			 if(!t.getText().isBlank()) {
				 t.setEditable(false);
			 }
		 });

		 lbs.add(t);
		return t;
	}
	
	private Slider sr(Slider s) {
		s.setPrefHeight(43);
		s.setPrefWidth(100);
		s.setBlockIncrement(1.0);
		s.setMajorTickUnit(1.0);
		s.setMax(5.0);
		s.setMin(1.0);
		s.setMinorTickCount(0);
		s.setShowTickLabels(true);
		s.setShowTickMarks(true);
		s.setSnapToTicks(true);
		s.setStyle("#ffffff");
		s.setValue(1.0);
		slids.add(s);
		s.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e1) {
						for(int f=0;f<tasks.size();f++) {
							if(tasks.get(f)==s.getParent().getParent() && s.getValue() > 4.0) {
								//#dc143c
								tasks.get(f).setBackground(new Background(new BackgroundFill(Color.rgb(220, 20, 60),CornerRadii.EMPTY,Insets.EMPTY)));
							}
						}
			
						for(int f=0;f<tasks.size();f++) {
							if(tasks.get(f)==s.getParent().getParent() && s.getValue() == 4.0) {
								//#ffd700
								tasks.get(f).setBackground(new Background(new BackgroundFill(Color.rgb(255, 215, 0),CornerRadii.EMPTY,Insets.EMPTY)));
							}
						}
				
						for(int f=0;f<tasks.size();f++) {
							if(tasks.get(f)==s.getParent().getParent() && s.getValue() == 3.0) {
								//#32cd32
								tasks.get(f).setBackground(new Background(new BackgroundFill(Color.rgb(50, 205, 50),CornerRadii.EMPTY,Insets.EMPTY)));
							}
						}

						for(int f=0;f<tasks.size();f++) {
							if(tasks.get(f)==s.getParent().getParent() && s.getValue() == 2.0) {
								//#87ceeb
								tasks.get(f).setBackground(new Background(new BackgroundFill(Color.rgb(135, 206, 235),CornerRadii.EMPTY,Insets.EMPTY)));
							}
						}
				

						for(int f=0;f<tasks.size();f++) {
							if(tasks.get(f)==s.getParent().getParent() && s.getValue() < 2.0) {
								//#c8c8c8
								tasks.get(f).setBackground(new Background(new BackgroundFill(Color.rgb(200, 200, 200),CornerRadii.EMPTY,Insets.EMPTY)));
							}
						}
				
						
				}
		});
		return s;
	}
	private HBox hb(HBox h) {
		
		h.prefHeight(87);
		h.prefWidth(481);
		h.setMinSize(481, 87);
		h.setBackground(new Background(new BackgroundFill(Color.rgb(200, 200, 200),CornerRadii.EMPTY,Insets.EMPTY)));
		//h.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
		h.getChildren().addAll(cb(new CheckBox()),ta(new TextField(),i),dpANDsr(new VBox()));

		return h;
		
	}
	private VBox dpANDsr(VBox h){
		h.prefHeight(76);
		h.prefWidth(104);
		h.setMinSize(104,76);
		h.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
		h.getChildren().addAll(sr(new Slider()),dp(new DatePicker()));
		
		return h;
	}
	private DatePicker dp(DatePicker d) {
		d.setPrefHeight(40);
		d.setPrefWidth(104);
		d.setPromptText("Due date");
		d.setValue(LocalDate.now());
		dps.add(d);
		return d;
		
	}
	
	//WHEN FILTER BUTTON IS PRESSED APPLIES THE BUTTONS FILTER TO THE LIST AND SETS AN ARROW TO THE RIGHT OF THE FILTER NAME IN THE MENU
	
	public void newsetFilt() {
		taskCenter.getChildren().clear();
		filt = "Newest";
		Completion.setText("Completed");
		Rating.setText("Rating");
		Default.setText("Oldest");
		Newest.setText("Newest <--");
		for(int i=tasks.size()-1;i>0;i--) {
			taskCenter.getChildren().add(tasks.get(i));
		}
}
	public void defaultFilt() {
			taskCenter.getChildren().clear();
			filt = "Default";
			Completion.setText("Completed");
			Rating.setText("Rating");
			Default.setText("Oldest <--");
			Newest.setText("Newest");
			for(int i=1;i<tasks.size();i++) {
				taskCenter.getChildren().add(tasks.get(i));
			}
	}
	public void ratingFilt() {
			taskCenter.getChildren().clear();
			filt = "Rating";
			Completion.setText("Completed");
			Rating.setText("Rating <--");
			Default.setText("Oldest");
			Newest.setText("Newest");
			for(int i=1;i<slids.size();i++) {
					if(slids.get(i).getValue() == 5.0) {
						taskCenter.getChildren().add(slids.get(i).getParent().getParent());
					}
			}
			for(int i=1;i<slids.size();i++) {
				if(slids.get(i).getValue() == 4.0) {
					taskCenter.getChildren().add(slids.get(i).getParent().getParent());
				}
		}
			for(int i=1;i<slids.size();i++) {
				if(slids.get(i).getValue() == 3.0) {
					taskCenter.getChildren().add(slids.get(i).getParent().getParent());
				}
		}
			for(int i=1;i<slids.size();i++) {
				if(slids.get(i).getValue() == 2.0) {
					taskCenter.getChildren().add(slids.get(i).getParent().getParent());
				}
		}
			for(int i=1;i<slids.size();i++) {
				if(slids.get(i).getValue() == 1.0) {
					taskCenter.getChildren().add(slids.get(i).getParent().getParent());
				}
		}
			
	}
	public void completionFilt() {
		taskCenter.getChildren().clear();
		filt = "Completed";
		Completion.setText("Completed <--");
		Rating.setText("Rating");
		Default.setText("Oldest");
		Newest.setText("Newest");
		for(int i=1;i<cbs.size();i++) {
			if(!cbs.get(i).isSelected()) {
				taskCenter.getChildren().add(cbs.get(i).getParent());
			}
		}
		for(int i=1;i<cbs.size();i++) {
			if(cbs.get(i).isSelected()) {
				taskCenter.getChildren().add(cbs.get(i).getParent());
			}
		}
	}
	
	
	//SETS NEW WINDOW CREATION VALUES AND ATTRIBUTES FOR CALENDAR VIEW
	
	public void calMode() throws IOException {
	
		        Image icon = new Image("calvtemp.png");
		        Stage stage = new Stage();
				if(op==0) {
					
			        root1 = new AnchorPane();
			        
			        setup();
			        
					
					Scene sc = new Scene(root1);
					
					stage.setScene(sc);
				    stage.setTitle("Calendar View");
					stage.setResizable(false);
					stage.getIcons().add(icon);
				    stage.show(); 
				    op=1;
					}
				else if(op==1) {
					System.out.println("Calendar is already open!");
					stage.requestFocus();
				}
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

					@Override
					public void handle(WindowEvent arg0) {
						root1.getChildren().clear();
						calendar.getChildren().clear();
						weekDays.getChildren().clear();
						ribbon.getChildren().clear();
						op=0;
						
					}
					
				});
		
		
	} 
	
	 //ALL VARIABLES AND NODES NECESSARY FOR CALENDAR VIEW WINDOW
	 
	private ZonedDateTime dateVal;
	private ZonedDateTime today;
	private Text year = new Text("####");
	private Text month = new Text("####");
	private FlowPane calendar = new FlowPane();
	private HBox ribbon = new HBox();
	private Button backward = new Button("<-");
	private Button forward = new Button("->");
	private HBox weekDays = new HBox();
	private Text Su = new Text("Su");
	private Text Mo = new Text("Mo");
	private Text Tu = new Text("Tu");
	private Text We = new Text("We");
	private Text Tr = new Text("Th");
	private Text Fr = new Text("Fr");
	private Text Sa = new Text("Sa");
	private FlowPane gp = new FlowPane();
	private Button refr = new Button();
	private String monCol = "";
		
	//SETS ALL CALENDAR VIEW NODES AND VARIABLES ATTRIBUTES AND PLACES THEM
	
	private void nodeSetup() {
		
		//year(Text): <Text fx:id="year" strokeType="OUTSIDE" strokeWidth="0.0" text="####">
		year.setStrokeWidth(0.0);
		year.setStrokeType(StrokeType.OUTSIDE);
		year.setFont(new Font(null, 22.0));
			
		//month(Text) : fx:id="month" strokeType="OUTSIDE" strokeWidth="0.0" text="####">
		month.setStrokeWidth(0.0);
		month.setStrokeType(StrokeType.OUTSIDE);
		month.setFont(new Font(null, 22.0));
			
		//calendar(FlowPane) : fx:id="calendar" hgap="10.0" layoutX="14.0" layoutY="116.0" prefHeight="498.0" prefWidth="716.0" vgap="5.0" />
		calendar.setHgap(10.0);
		calendar.setVgap(5.0);
		calendar.setLayoutX(14.0);
		calendar.setLayoutY(116.0);
		calendar.setPrefHeight(498.0);
		calendar.setPrefWidth(716.0);
		calendar.setBackground(Background.fill(Paint.valueOf("#FFFFF9")));
		
			
		//AnchorPane
	    root1.setPrefHeight(700.0);
	    root1.setPrefWidth(750.0);
	    root1.setStyle("#000000");
	    root1.setBackground(Background.fill(Paint.valueOf("#ADD8E6")));
	        
	    //HBox
	    ribbon.setAlignment(Pos.CENTER);
	    ribbon.setLayoutX(163.0);
		ribbon.setLayoutY(14.0);
		ribbon.setPrefSize(419, 44);
		ribbon.setSpacing(5);
			
		//Buttons
		forward.setMnemonicParsing(false);
		backward.setMnemonicParsing(false);
		forward.setPrefSize(50, 50);
		backward.setPrefSize(50, 50);
			
		//refresh button
		ImageView img = new ImageView(new Image("refr.png"));
		img.setPreserveRatio(true);
		img.setFitHeight(50);
		img.setFitWidth(50);
		refr.setPadding(Insets.EMPTY);
		refr.setGraphic(img);
		refr.setPrefSize(50, 50);
		refr.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				calendar.getChildren().clear();
				drawCalendar();
					
			}
				
		});
			
			
			
		forward.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				dateVal = dateVal.plusMonths(1);
				calendar.getChildren().clear();
				drawCalendar();
					
			}
				
		});
			
		backward.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				dateVal = dateVal.minusMonths(1);
				calendar.getChildren().clear();
				drawCalendar();
					
			}
				
		});
			
		//WeekDays and HBox
		Su.setStrokeType(StrokeType.OUTSIDE);
		Mo.setStrokeType(StrokeType.OUTSIDE);
		Tu.setStrokeType(StrokeType.OUTSIDE);
		We.setStrokeType(StrokeType.OUTSIDE);
		Tr.setStrokeType(StrokeType.OUTSIDE);
		Fr.setStrokeType(StrokeType.OUTSIDE);
		Sa.setStrokeType(StrokeType.OUTSIDE);
			
		Mo.setLayoutX(213);
		Tu.setLayoutX(222);
		We.setLayoutX(232);
		Tr.setLayoutX(241);
		Fr.setLayoutX(251);
		Sa.setLayoutX(266);
			
		Mo.setLayoutY(37);
		Tu.setLayoutY(37);
		We.setLayoutY(37);
		Tr.setLayoutY(37);
		Fr.setLayoutY(37);
		Sa.setLayoutY(37);
			
		Su.setStrokeWidth(0);
		Mo.setStrokeWidth(0);
		Tu.setStrokeWidth(0);
		We.setStrokeWidth(0);
		Tr.setStrokeWidth(0);
		Fr.setStrokeWidth(0);
		Sa.setStrokeWidth(0);
			
		Su.setTextAlignment(TextAlignment.CENTER);
		Mo.setTextAlignment(TextAlignment.CENTER);
		Tu.setTextAlignment(TextAlignment.CENTER);
		We.setTextAlignment(TextAlignment.CENTER);
		Tr.setTextAlignment(TextAlignment.CENTER);
		Fr.setTextAlignment(TextAlignment.CENTER);
		Sa.setTextAlignment(TextAlignment.CENTER);
			
		weekDays.setAlignment(Pos.CENTER);
		weekDays.setLayoutX(14);
		weekDays.setLayoutY(78);
		weekDays.setPrefSize(716, 44);
		weekDays.setSpacing(88.75);
		weekDays.getChildren().addAll(Su,Mo,Tu,We,Tr,Fr,Sa);
			
			
			
	        
	    ribbon.getChildren().addAll(refr,backward,month,year,forward);
		root1.getChildren().addAll(ribbon,weekDays,calendar);
	}
	
	//SETS UP THE CALENDAR WINDOW TO DEPLOY BY CALLING ALL FUNCTIONS
		
	private void setup() {
		nodeSetup(); 
		dateVal = ZonedDateTime.now();
		today = ZonedDateTime.now();
		drawCalendar();
			 
	}
	
	//DRAWS THE CALENDAR AND OUTLINES THE CURRENT DATE WITH A RED BOX, CHECKS FOR ANY TASKS IN THE LIST AND PLACES CIRCLES ON THEIR RESPECTIVE DUE DATES
	
	private void drawCalendar(){
		monCol = String.valueOf(dateVal.getMonth());
		
		year.setText(String.valueOf(dateVal.getYear()));
		month.setText(String.valueOf(dateVal.getMonth()));
		
		switch(monCol) {
		case "JANUARY":
			month.setFill(Paint.valueOf("#0000FF"));
			break; 
			
		case "FEBRUARY":
			month.setFill(Paint.valueOf("#800080"));
			break; 
			
		case "MARCH":
			month.setFill(Paint.valueOf("#008000"));
			break; 
			
		case "APRIL":
			month.setFill(Paint.valueOf("#FFA500"));
			break; 
			
		case "MAY":
			month.setFill(Paint.valueOf("#FFFF00"));
			break; 
			
		case "JUNE":
			month.setFill(Paint.valueOf("#FF0000"));
			break; 
			
		case "JULY":
			month.setFill(Paint.valueOf("#FF4500"));
			break; 
			
		case "AUGUST":
			month.setFill(Paint.valueOf("#00FFFF"));
			break; 
			
		case "SEPTEMBER":
			month.setFill(Paint.valueOf("#8B4513"));
			break; 
			
		case "OCTOBER":
			month.setFill(Paint.valueOf("#FF1493"));
			break; 
			
		case "NOVEMBER":
			month.setFill(Paint.valueOf("#800000"));
			break; 
			
		case "DECEMBER":
			month.setFill(Paint.valueOf("#228B22"));
			break; 
		}

		double calendarWidth = calendar.getPrefWidth();
		double calendarHeight = calendar.getPrefHeight();
		double strokeWidth = 1;
		double spacingH = calendar.getHgap();
		double spacingV = calendar.getVgap();

		int monthMaxdateVal = dateVal.getMonth().maxLength();
		        
		//CHECKS FOR LEAP YEAR
		        
		if(dateVal.getYear() % 4 != 0 && monthMaxdateVal == 29){
				monthMaxdateVal = 28;
			}
		int dateValOffset = ZonedDateTime.of(dateVal.getYear(), dateVal.getMonthValue(), 1,0,0,0,0,dateVal.getZone()).getDayOfWeek().getValue();
		        
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				StackPane stackPane = new StackPane();
		        FlowPane f = new FlowPane();
		        f.setPrefWidth((calendarWidth/7) - strokeWidth - spacingH);
		        f.setPrefHeight((calendarHeight/6) - strokeWidth - spacingV);
		                
		        stackPane.getChildren().add(fp(f));

		        int calculateddateVal = (j+1)+(7*i);
		        if(calculateddateVal > dateValOffset){
		        	int currentdateVal = calculateddateVal - dateValOffset;
		        	if(currentdateVal <= monthMaxdateVal){
		        		Text dateVal = new Text(String.valueOf(currentdateVal));
		                double textTranslationY = - (gp.getPrefHeight() / 2) * 0.75;
		                dateVal.setTranslateY(textTranslationY);
		                for(int a=1;a<dps.size();a++) {
		                	ZonedDateTime g = dps.get(a).getValue().atStartOfDay(ZoneId.systemDefault());
		                        	
		                    if(g.getDayOfMonth() == currentdateVal && g.getMonth().toString().equalsIgnoreCase(month.getText()) && String.valueOf(g.getYear()).equalsIgnoreCase(year.getText())) {	
		                    	if(taskcreator(slids.get(a),dps.get(a)).getOpacity()!=0.0) {
		                    		f.getChildren().add(taskcreator(slids.get(a),dps.get(a)));
		                        }
		                        			
		                     }
		                  }
		                        
		                        stackPane.getChildren().add(dateVal);
		             }
		             if(today.getYear() == dateVal.getYear() && today.getMonth() == dateVal.getMonth() && today.getDayOfMonth() == currentdateVal){
		            	 stackPane.setBorder(new Border(new BorderStroke(Color.RED,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
		             }
		         }
		     calendar.getChildren().add(stackPane);
		     }
		  }
	}
	
	//CREATES TASK CIRCLES ON CALENDAR AND ADDS THEM TO A FLOWPANE IN THE CALENDAR BOXES
	
	private FlowPane fp(FlowPane f) {
		f.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.DOTTED,CornerRadii.EMPTY,BorderWidths.DEFAULT)));

		return f;
	}

	private Rectangle taskcreator(Slider h, DatePicker g) {
		Rectangle ts = new Rectangle();
		ts.setHeight(25);
		ts.setWidth(25);
		ts.setArcHeight(40);
		ts.setArcWidth(40);
				
		Image tasF = new Image("tasF.png");
				
		BorderPane apc = new BorderPane();
		Scene nsc = new Scene(apc);
		Stage s1 = new Stage();
		s1.setTitle("TASK FOCUS");
		s1.setAlwaysOnTop(true);
		s1.getIcons().add(tasF);
		s1.setScene(nsc);
				
		s1.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				apc.getChildren().clear();
						
			}
					
		});

		for(int i=1;i<slids.size();i++) {
			if(slids.get(i).getValue() == 5.0 && slids.get(i) == h ) {
						
				ts.setFill(Paint.valueOf("#dc143c"));
				if(cbs.get(i).isSelected()) {
					ts.setOpacity(0.0);
				}
				ts.setOnMouseClicked(new EventHandler<MouseEvent>() {
							
					@Override
					public void handle(MouseEvent e) {

								
								
						Text temp = new Text();
						for(int j=0;j<tasks.size();j++) {
							if(tasks.get(j) == h.getParent().getParent()) {
										
								temp.setText(lbs.get(j).getText());
								apc.setCenter(temp);
								s1.setResizable(false);
								s1.setHeight(140);
								s1.setWidth(350);
								s1.show();
							}
										
						}
									
										
								
					}
							
				});
				return ts;
			}
	}
			for(int i=1;i<slids.size();i++) {
				if(slids.get(i).getValue() == 4.0&& slids.get(i) == h) {
					ts.setFill(Paint.valueOf("#ffd700"));
					if(cbs.get(i).isSelected()) {
						ts.setOpacity(0.0);
					}
					ts.setOnMouseClicked(new EventHandler<MouseEvent>() {
						
						@Override
						public void handle(MouseEvent e) {

							
							
							Text temp = new Text();
							for(int j=0;j<tasks.size();j++) {
								if(tasks.get(j) == h.getParent().getParent()) {
									
									temp.setText(lbs.get(j).getText());
									apc.setCenter(temp);
									s1.setResizable(false);
									s1.setHeight(140);
									s1.setWidth(350);
									s1.show();
									
								}
							}
								
									
					
				}
						
			});
					return ts;
		}
	}
			for(int i=1;i<slids.size();i++) {
				if(slids.get(i).getValue() == 3.0&& slids.get(i) == h) {
					ts.setFill(Paint.valueOf("#32cd32"));
					if(cbs.get(i).isSelected()) {
						ts.setOpacity(0.0);
					}
					ts.setOnMouseClicked(new EventHandler<MouseEvent>() {
						
						@Override
						public void handle(MouseEvent e) {

							
							
							Text temp = new Text();
							for(int j=0;j<tasks.size();j++) {
								if(tasks.get(j) == h.getParent().getParent()) {
									
									temp.setText(lbs.get(j).getText());
									apc.setCenter(temp);
									s1.setResizable(false);
									s1.setHeight(140);
									s1.setWidth(350);
									s1.show();
									
								}
							}
								
									
							
						}
						
					});
					return ts;
				}
		}
			for(int i=1;i<slids.size();i++) {
				if(slids.get(i).getValue() == 2.0&& slids.get(i) == h) {

					ts.setFill(Paint.valueOf("#87ceeb"));
					if(cbs.get(i).isSelected()) {
						ts.setOpacity(0.0);
					}
					ts.setOnMouseClicked(new EventHandler<MouseEvent>() {
						
						@Override
						public void handle(MouseEvent e) {

							
							
							Text temp = new Text();
							for(int j=0;j<tasks.size();j++) {
								if(tasks.get(j) == h.getParent().getParent()) {
									
									temp.setText(lbs.get(j).getText());
									apc.setCenter(temp);
									s1.setResizable(false);
									s1.setHeight(140);
									s1.setWidth(350);
									s1.show();
									
								}
								
							}	
						
					}
						
				});
					return ts;
			}
	}
			for(int i=1;i<slids.size();i++) {
				if(slids.get(i).getValue() == 1.0&& slids.get(i) == h) {
					ts.setFill(Paint.valueOf("#c8c8c8"));
					if(cbs.get(i).isSelected()) {
						ts.setOpacity(0.0);
					}
					ts.setOnMouseClicked(new EventHandler<MouseEvent>() {
						
						@Override
						public void handle(MouseEvent e) {

							
						
							Text temp = new Text();
							for(int j=0;j<tasks.size();j++) {
								if(tasks.get(j) == h.getParent().getParent()) {
									
									temp.setText(lbs.get(j).getText());
									apc.setCenter(temp);
									s1.setResizable(false);
									s1.setHeight(140);
									s1.setWidth(350);
									s1.show();
									
								}
							}
									
						
					}
						
				});
					return ts;
			}
		}

			
			return null;
			
			
			
			
			
			
		 }
		
}
	
