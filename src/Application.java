import BinBaum.BinBaum;
import BinBaum.BE;
import BinBaum.IndexAlreadyExistsException;
import BinBaum.IndexNotFoundException;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


/**
 * Created by Jegoni on 15.01.2016.
 */
public class Application extends javafx.application.Application {

    private GraphicsContext gc;
    private BinBaum binBaum;
    private Canvas canvas;
    private IntegerProperty tiefeProp;
    private IntegerProperty knotenProp;

    @Override
    public void start(Stage primaryStage) throws Exception {
        tiefeProp = new SimpleIntegerProperty(0);
        knotenProp = new SimpleIntegerProperty(0);
        Scene scene = new Scene(createGUI(primaryStage));

        primaryStage.setTitle("Binär Baum blabalabla");
        primaryStage.setScene(scene);
        primaryStage.setHeight(480);
        primaryStage.setWidth(640);
        primaryStage.setMinHeight(480);
        primaryStage.setMinWidth(640);
        primaryStage.setMaximized(true);
        /*Alternativ:
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        */
        primaryStage.show();

        binBaum = new BinBaum();

        reload();
    }

    private void reload() {
        tiefeProp.setValue(binBaum.tiefe());
        knotenProp.setValue(binBaum.knotenanzahl());
        int baumTiefe = binBaum.tiefe() + 1; //+1 Wegen Endobjekten!
        int baumBreite = zweiHoch(baumTiefe);
        int breitePixel = baumBreite * 100 + (baumBreite - 1) * 20;
        canvas.setWidth(breitePixel);
        canvas.setHeight(baumTiefe * 50 + baumTiefe * 20);
        //Hintergrund:
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        //Starte das hinkritzeln:
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.GREEN);
        gc.fillText("ROOT", breitePixel / 2, 10);
        if(binBaum.getRoot().isLeaf())
            maleEnde(1, 1, breitePixel, breitePixel / 2, 0);
        else
            maleKnoten(binBaum.getRoot(), 1, 1, breitePixel, breitePixel / 2, 0);
    }
    
    private void maleKnoten(BE be, int tiefe, int breite, final int gesamtbreite, int lx, int ly){
        int breitePunkt = gesamtbreite * (breite * 2 - 1) / zweiHoch(tiefe);
        int tiefePunkt = (tiefe - 1) * 50 + tiefe * 10;
        gc.strokeLine(lx, ly, breitePunkt, tiefePunkt);
        gc.strokeRoundRect(breitePunkt - 50, tiefePunkt, 100, 50, 5, 5);
        gc.setFill(Color.WHITE);
        gc.fillRoundRect(breitePunkt - 49, tiefePunkt + 1, 98, 48, 5, 5);
        gc.setFill(Color.BLACK);
        gc.fillText(be.getIndex(), breitePunkt, tiefePunkt + 15);
        gc.fillText(be.getInfo(), breitePunkt, tiefePunkt + 35);
        if(be.getLinks().isLeaf())
            maleEnde(tiefe + 1, breite * 2 - 1, gesamtbreite, breitePunkt, tiefePunkt + 50);
        else
            maleKnoten(be.getLinks(), tiefe + 1, breite * 2 - 1, gesamtbreite, breitePunkt, tiefePunkt + 50);
        if(be.getRechts().isLeaf())
            maleEnde(tiefe + 1, breite * 2, gesamtbreite, breitePunkt, tiefePunkt + 50);
        else
            maleKnoten(be.getRechts(), tiefe + 1, breite * 2, gesamtbreite, breitePunkt, tiefePunkt + 50);
    }

    private void maleEnde(int tiefe, int breite, final int gesamtbreite, int lx, int ly){
        int breitePunkt = gesamtbreite * (breite * 2 - 1) / zweiHoch(tiefe);
        int tiefePunkt = (tiefe - 1) * 50 + tiefe * 10;
        gc.strokeLine(lx, ly, breitePunkt, tiefePunkt);
        gc.setFill(Color.RED);
        gc.fillOval(breitePunkt - 25, tiefePunkt, 50, 50);
        gc.setFill(Color.BLACK);
        gc.fillText("ENDE", breitePunkt, tiefePunkt + 25);
    }

    private int zweiHoch(int exponent) {
        if(exponent < 0) {
            System.err.println("Exponent darf nicht kleiner als Null sein!");
            return - 1;
        }
        int ergebnis = 1;
        for(int i = 0; i < exponent; i++){
            ergebnis = ergebnis * 2;
        }
        return ergebnis;
    }

    private Parent createGUI(Stage stage){
        BorderPane layout = new BorderPane();
        //MenuBar:
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("Datei");
        MenuItem exit = new MenuItem("Beenden");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
        MenuItem reset = new MenuItem("Reset");
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                binBaum = new BinBaum();
                reload();
            }
        });

        menuFile.getItems().addAll(reset, new SeparatorMenuItem(), exit);
        menuBar.getMenus().addAll(menuFile);

        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        canvas.setHeight(400);
        canvas.setWidth(400);
        StackPane stackPane = new StackPane(canvas);
        stackPane.setAlignment(Pos.CENTER);
        ScrollPane scrollPane = new ScrollPane(stackPane);

        /*final double SKALIERUNG = 1.1; //Geschwindigkeit des Zoomens
        stackPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                event.consume();
                if (event.getDeltaY() == 0)
                    return;
                double skalierungsFaktor = (event.getDeltaY() > 0) ? SKALIERUNG : 1 / SKALIERUNG;

                stackPane.setScaleX(skalierungsFaktor * stackPane.getScaleX());
                stackPane.setScaleY(skalierungsFaktor * stackPane.getScaleY());
            }
        });*/

        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        TitledPane tpBaum = tpBaum();
        TitledPane chat = new TitledPane("Chat", new Pane());
        Accordion tools = new Accordion(tpBaum, chat);
        tools.setExpandedPane(tpBaum);
        tpBaum.setCollapsible(false);
        tools.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {
            @Override
            public void changed(ObservableValue<? extends TitledPane> observable, TitledPane oldValue, TitledPane newValue) {
                if (oldValue != null) oldValue.setCollapsible(true);
                if (newValue != null) Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        newValue.setCollapsible(false);
                    }
                });
            }
        });
        layout.setTop(menuBar);
        layout.setCenter(scrollPane);
        layout.setRight(tools);
        return layout;
    }

    private TitledPane tpBaum(){
        GridPane layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        Font ueberschrift = Font.font(null, FontWeight.BOLD, 15);

        //Einfuegen:
        Label einfuegen = new Label("Einfügen:");
        einfuegen.setFont(ueberschrift);
        final TextField einfuegenTF1 = new TextField("Index");
        final TextField einfuegenTF2 = new TextField("Info");
        final Button einfuegenB = new Button("Einfügen");
        layout.add(einfuegen, 0, 1);
        layout.add(einfuegenTF1, 0, 2);
        layout.add(einfuegenTF2, 0, 3);
        layout.add(einfuegenB, 1, 3);
        final StringProperty einfuegenT1 = new SimpleStringProperty();
        final StringProperty einfuegenT2 = new SimpleStringProperty();
        einfuegenTF1.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                    einfuegenTF1.setText(einfuegenT1.getValue());
                else {
                    einfuegenT1.setValue(einfuegenTF1.getText());
                    if(einfuegenT1.getValue() == null)
                        einfuegenTF1.setText("Index");
                    else if(einfuegenT1.getValue().equals(""))
                        einfuegenTF1.setText("Index");
                }
            }
        });
        einfuegenTF2.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                    einfuegenTF2.setText(einfuegenT2.getValue());
                else {
                    einfuegenT2.setValue(einfuegenTF2.getText());
                    if(einfuegenT2.getValue() == null)
                        einfuegenTF2.setText("Info");
                    else if(einfuegenT2.getValue().equals(""))
                        einfuegenTF2.setText("Info");
                }
            }
        });
        einfuegenTF1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                einfuegenTF2.requestFocus();
            }
        });
        einfuegenTF2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                einfuegenB.requestFocus();
            }
        });
        einfuegenB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if(einfuegenT1.getValue() != null && einfuegenT2.getValue() != null) {
                        if(!einfuegenT1.getValue().replaceAll("//s+", "").equals("") && !einfuegenT1.getValue().replaceAll("//s+", "").equals("")) {
                            binBaum.einfuegen(einfuegenT1.getValue(), einfuegenT2.getValue());
                            einfuegenT1.setValue(null);
                            einfuegenT2.setValue(null);
                            einfuegenTF1.setText("Index");
                            einfuegenTF2.setText("Info");
                            reload();
                        }
                    }
                } catch (IndexAlreadyExistsException e) {
                    e.printStackTrace(); //todo fehlermeldung
                }
            }
        });
        einfuegenB.defaultButtonProperty().bind(einfuegenB.focusedProperty());

        //Löschen:
        layout.add(new Separator(Orientation.HORIZONTAL), 0, 4, 2, 1);
        Label loeschen = new Label("Löschen:");
        loeschen.setFont(ueberschrift);
        final TextField loeschenTF = new TextField("Index");
        final Button loeschenB = new Button("Löschen");
        layout.add(loeschen, 0, 5);
        layout.add(loeschenTF, 0, 6);
        layout.add(loeschenB, 1, 6);
        final StringProperty loeschenT = new SimpleStringProperty();
        loeschenTF.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue)
                    loeschenTF.setText(loeschenT.getValue());
                else {
                    loeschenT.setValue(loeschenTF.getText());
                    if(loeschenT.getValue() == null)
                        loeschenTF.setText("Index");
                    else if(loeschenT.getValue().equals(""))
                        loeschenTF.setText("Index");
                }
            }
        });
        loeschenTF.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loeschenB.requestFocus();
            }
        });
        loeschenB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if(loeschenT.getValue() != null) {
                        if(!loeschenT.getValue().replaceAll("//s+", "").equals("")) {
                            binBaum.loeschen(loeschenT.getValue());
                            loeschenT.setValue(null);
                            loeschenTF.setText("Löschen");
                            reload();
                        }
                    }
                } catch (IndexNotFoundException e) {
                    e.printStackTrace(); //todo fehlermeldung
                }
            }
        });
        loeschenB.defaultButtonProperty().bind(loeschenB.focusedProperty());

        //Stats:
        layout.add(new Separator(Orientation.HORIZONTAL), 0, 7, 2, 1);
        Label stats = new Label("Swag n Stats:");
        stats.setFont(ueberschrift);
        Label tiefe = new Label("Tiefe: 0");
        Label knoten = new Label("Knoten: 0");
        layout.add(stats, 0, 8, 2, 1);
        layout.add(tiefe, 0, 9, 2, 1);
        layout.add(knoten, 0, 10, 2, 1);
        tiefeProp.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                tiefe.setText("Tiefe: " + newValue);
            }
        });
        knotenProp.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                knoten.setText("Knoten: " + newValue);
            }
        });



        return new TitledPane("Binärbaum", layout);
    }

    public static void main(String[] args){
        launch(args);
    }
}