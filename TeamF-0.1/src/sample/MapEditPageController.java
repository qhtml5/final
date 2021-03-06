package sample;

import com.jfoenix.controls.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapEditPageController implements Initializable, Data, ITimed{

    //private TimeoutController timeoutController;

    //private Timer atimer;

    //fxml components
    @FXML
    private StackPane stackPane;
    @FXML
    private Group scrollContent;
    @FXML
    private JFXToggleButton nodeEdgeEdit;
    @FXML
    private ScrollPane scrollMap;
    @FXML
    private ImageView map;
    @FXML
    private JFXListView threeList, twoList, oneList, groundList, lowerTwoList, lowerOneList;
    @FXML
    private JFXTabPane tabPane;
    @FXML
    private RadioMenuItem chooseAStar, chooseDepth, chooseBreadth, chooseDijk, chooseBeam, chooseBest;
    @FXML
    private SplitMenuButton algoMenu;
    @FXML
    private JFXButton editNodeBtn, editEdgeBtn, loginButton;
    @FXML
    private Canvas pathCanvas;
    @FXML
    private Canvas pathCanvas1;
    @FXML
    private Tab floorThree, floorTwo, floorOne, floorLowerTwo, floorLowerOne, floorGround;
    @FXML
    private JFXCheckBox allNodes;
    @FXML
    private JFXCheckBox allEdges;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private ContextMenu contextMenu2;
    @FXML
    private JFXListView<String> startAuto;
    @FXML
    private JFXTextField startLabel;

    private ObservableList<String> allEntries;
   /* public final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty<>();
*/
    @FXML
    private JFXButton helpButton;

    //other variables
    private Main mainController;

    public Node selectedNode; // Used for opening map editing windows

    public double calcX; // Used for opening map editing windows

    public double calcY; // Used for opening map editing windows

    boolean editEdges;

    @FXML // This is the method that gets called everywhere in the fxml files.
    public void someAction() //  throws IOException, InterruptedException
    {
//        try
//        {
//            timeoutController.doTimer();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            System.out.println("Could not start timer.");
//        }
    }

    //initialization
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //startAuto.setVisible(false);
//
//        timeoutController = new TimeoutController();
//        atimer = new Timer();
//        timeoutController.updateDelay(30); // 30 per steph request.
//        timeoutController.setTimer(atimer, false); // This is not a popup

        updateNodes();
        updateEdges();
        Data.data.gc1 = pathCanvas1.getGraphicsContext2D();
        Data.data.gc2 = pathCanvas.getGraphicsContext2D();

        scrollMap.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollMap.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //popluating list view -- three
        ObservableList<String> threeItems = FXCollections.observableArrayList (
                "Bridge to Dana-Farber Cancer Institute", "Brigham Circle Medical Associates", "Center for Infertility and Reproductive Surgery",
                "Clinical Trials", "Conference Center","Dialysis,", "Dialysis Waiting Room", "Fetal Med & Genetics", "General Surgical Specialties Suite A",
                "General Surgical Specialties Suite B", "Gynecology", "Gyencology Oncology MIGS", "Innovation Hub", "Maternal Fetal Practice",
                "MICU 3B/C Waiting Room", "OB/GYN Blood Lab", "Obstetrics", "The Porch", "Reproductive Endocrine Labs", "Urology", "Watkins Clinic C");
        //threeList.setItems(threeItems);

        //populating list view -- second
        ObservableList<String> twoItems = FXCollections.observableArrayList("Bridge to Children's", "Brigham Health", "Carrie M. Hall Conference Center",
                "Chest Diseases", "Coffee Connection", "Comprehensive Breast Health", "Conference Center", "Duncan Reid Conference Room", "Ear, Nose, & Throat",
                "Endoscopy", "Garden Cafe", "Gift Shop", "Jen Center for Primary Care", "Lee Bell Breast Center", "Louis Bornstein Family Amphitheater",
                "Medical Surgical Specialties", "MRI Associates", "Oral Medicine and Dentistry", "Orthopedics and Rhematology", "Outpatient Specimen Collection",
                "Pat's Place", "Patient Financial Services", "Plastic Surgery", "Thoracic Surgery Clinic", "Vascular Diagnostic Lab", "Watkins A", "Watkins B",
                "Weiner Center for Preoperative Evaluation");
        //twoList.setItems(twoItems);

        //populating list view -- first
        ObservableList<String> oneItems = FXCollections.observableArrayList("Ambulatory X-Ray", "Asthma Research Center", "Au Bon Pain",
                "Bretholtz Center for Patients and Families", "CART Waiting", "Connor's Center Security Desk", "CPE Classroom", "International Patient Center",
                "Kessler Library", "MS Waiting", "Multifaith Chapel", "Neuroscience Waiting Room", "Obstetrics Admitting", "Occupational Health", "Partner's Shuttle",
                "Rehabilitation Services", "Shapiro Board Room", "Sharf Admitting Center", "Spiritual Care Office", "Wound Care Center Ambulatory Treatment Room",
                "Zinner Breakout Room");
        //oneList.setItems(oneItems);

        //populating list view -- lower two
        ObservableList<String> lowerTwoItems = FXCollections.observableArrayList("Cardiac Stress Test Lab", "Cardiovascular Imaging Center", "CVRR",
                "Interpreter Services", "MRI/CT Scan Imaging", "Radiation Oncology", "Radiation Oncology Conference Room", "Radiation Oncology T/X Suite");
        //lowerTwoList.setItems(lowerTwoItems);

        //populating list view -- lower one
        ObservableList<String> lowerOneItems = FXCollections.observableArrayList("Abrams Conference Room", "Anesthesia Conference Room", "CSIR MRI",
                "Day Surgery Family Waiting", "Helen Hogan Conference Room", "Medical Records Conference Room", "Medical Records Film Library", "Nuclear Medicine",
                "Outpatient Fluoroscopy", "Pre-OP PACU", "Ultrasound", "Volunteers");
        //lowerOneList.setItems(lowerOneItems);

        //populating list -- ground
        ObservableList<String> groundItems = FXCollections.observableArrayList("Infusion", "Neuro Testing", "Outpatient Plebotomy");
        //groundList.setItems(groundItems);
        tabPane.getSelectionModel().select(floorOne);
        map.setImage(data.firstFloor);//new Image(getClass().getResourceAsStream("/sample/UI/Icons/01_thefirstfloor.png")));


        allEntries = FXCollections.observableArrayList(testEmbeddedDB.getAllLongNames());
            /*
            * AStar -1
            * Breadth -2
            * Depth -3
            * Dijk -4
            * Beam -5
            * Best -6
            * */

        //default is Astar
        algoMenu.setText(chooseAStar.getText());
        chooseAStar.setSelected(true);

        chooseAStar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.navigationPageController.setCurrentAlgo(1);
                algoMenu.setText(chooseAStar.getText());
            }
        });

        chooseBreadth.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.navigationPageController.setCurrentAlgo(2);
                algoMenu.setText(chooseBreadth.getText());
            }
        });

        chooseDepth.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.navigationPageController.setCurrentAlgo(3);
                algoMenu.setText(chooseDepth.getText());
            }
        });

        chooseDijk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.navigationPageController.setCurrentAlgo(4);
                algoMenu.setText(chooseDijk.getText());
            }
        });

        chooseBeam.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.navigationPageController.setCurrentAlgo(5);
                algoMenu.setText(chooseBeam.getText());
            }
        });

        chooseBest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.navigationPageController.setCurrentAlgo(6);
                algoMenu.setText(chooseBest.getText());
            }
        });

/*        try {
            clickNearestNodeSelected();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        zoom();


        contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Edit Node");
        MenuItem item2 = new MenuItem("Add Node");
        contextMenu.getItems().addAll(item1, item2);

        contextMenu2 = new ContextMenu();
        MenuItem item3 = new MenuItem("Set Start Node");
        MenuItem item4 = new MenuItem("Set End Node");
        contextMenu2.getItems().addAll(item3, item4);

        scrollMap.setPannable(true);
    }

    public static void updateNodes() {
        Data.data.firstFloorNodes = testEmbeddedDB.getNodesByFloor(3);
        Data.data.groundFloorNodes = testEmbeddedDB.getNodesByFloor(2);
        Data.data.lowerLevel01FloorNodes = testEmbeddedDB.getNodesByFloor(1);
        Data.data.lowerLevel02FloorNodes = testEmbeddedDB.getNodesByFloor(0);
        Data.data.secondFloorNodes = testEmbeddedDB.getNodesByFloor(4);
        Data.data.thirdFloorNodes = testEmbeddedDB.getNodesByFloor(5);
    }

    public static void updateEdges() {
        Data.data.thirdFloorEdges = testEmbeddedDB.getEdgesByFloor(5);
        Data.data.secondFloorEdges = testEmbeddedDB.getEdgesByFloor(4);
        Data.data.firstFloorEdges = testEmbeddedDB.getEdgesByFloor(3);
        Data.data.groundFloorEdges = testEmbeddedDB.getEdgesByFloor(2);
        Data.data.lower1FloorEdges = testEmbeddedDB.getEdgesByFloor(1);
        Data.data.lower2FloorEdges = testEmbeddedDB.getEdgesByFloor(0);
    }
    //getter and setters
    public void setMainController(Main main){
        this.mainController = main;
    }

    public void clearCanvas(GraphicsContext gcIn, Canvas pathCanvasIn) {
        double y = pathCanvasIn.getHeight();
        double x = pathCanvasIn.getWidth();
        if(gcIn != null)
            gcIn.clearRect(0,0,x,y);
        else {
            //System.out.println("IT WAS NULL" + gcIn);
        }
    }

    @FXML
    public void changeFloorL1() {
        clearCanvas(data.gc2,pathCanvas);
        clearCanvas(data.gc1,pathCanvas1);
        map.setImage(Data.data.L1Floor);
    }

    @FXML
    public void changeFloorL12() {
        System.out.println("in here");
        //alignNodes();
    }

    @FXML
    public void changeFloor1() {
        clearCanvas(data.gc2,pathCanvas);
        clearCanvas(data.gc1,pathCanvas1);
        map.setImage(Data.data.firstFloor);
    }

    @FXML
    public void changeFloor2() {
        clearCanvas(data.gc2,pathCanvas);
        clearCanvas(data.gc1,pathCanvas1);
        map.setImage(Data.data.secondFloor);
    }

    @FXML
    public void changeFloor3() {
        clearCanvas(data.gc2, pathCanvas);
        clearCanvas(data.gc1,pathCanvas1);
        map.setImage(Data.data.thirdFloor);
    }

    @FXML
    public void changeFloorG() {
        clearCanvas(data.gc2,pathCanvas);
        clearCanvas(data.gc1,pathCanvas1);
        map.setImage(Data.data.GFloor);
    }

    @FXML
    public void changeFloorL2() {
        clearCanvas(data.gc2,pathCanvas);
        clearCanvas(data.gc1,pathCanvas1);
        map.setImage(Data.data.L2Floor);
    }


    //functions to open pages
    @FXML
    public void editNodes(){
        Main.nodeEditScreen(editNodeBtn);
    }

    @FXML
    public void editEdges(){
        Main.edgeEditScreen(editEdgeBtn);
    }

    @FXML
    public void openMapScreen() throws IOException, InterruptedException {
        Main.mapScreen();
    }

    @FXML
    public void help(){Main.setHelpScreenServiceRequestScreen();}

    @FXML
    public void logout() throws IOException, InterruptedException{
        AuthenticationInfo clearAuth = new AuthenticationInfo("guest", AuthenticationInfo.Privilege.USER);
        SettingSingleton.getSettingSingleton().setAuthProperty(clearAuth);

        Main.logOutUser();
        Main.mapScreen();
        clear();
    }

    @FXML
    public void back() throws IOException, InterruptedException{Main.mapScreen();}

    /**
     * Convert from the map image (5000x3400) to the anchorpane drawing/ canvas pathcanvas
     * All the constants and window sizes are stored inside data holder
     * @param x X coordinate to be converted
     * @param y Y coordinate to be converted
     * @return A point representing the two converted values as a point
     */
    public Point2D convertFromImage(double x, double y){
        updateImageCoordinates();
        updateCanvasCoordinates();
        //System.out.println("This is the image view: " + data.imageViewX + ", " + data.imageViewY);
        //System.out.println("This is the map image: " + data.MapX + ", " + data.MapY);
        //System.out.println("This is the input point: " + x + ", " + y);
        double returnX = x / ((data.imageViewX / data.canvasX) * (data.MapX/data.imageViewX));
        double returnY = y / ((data.imageViewY / data.canvasY) * (data.MapY/data.imageViewY));
        //System.out.println("This is the point: " + returnX + " ," + returnY);
        return new Point2D(returnX,returnY);
    }

    /**
     * Convert an input point from the anchorpane / canvas to the image (5000x3400)
     * @param x Input from anchorpane or canvas
     * @param y Input from anchorpane or canvas
     * @return return a point representing the converted point
     */
    public Point2D convertToImage(double x, double y){
        updateImageCoordinates();
        updateCanvasCoordinates();
        //System.out.println("This is the image view: " + data.canvasX + ", " + data.canvasY);
        //System.out.println("This is the map image: " + data.MapX + ", " + data.MapY);
        //System.out.println("This is the input point: " + x + ", " + y);
        double returnX = x * ((data.imageViewX / data.canvasX) * (data.MapX/data.imageViewX));
        double returnY = (y) * ((data.imageViewY / data.canvasY) * (data.MapY/data.imageViewY));
        //System.out.println("This is the point: " + returnX + " ," + returnY);
        return new Point2D(returnX,returnY);
    }

    /**
     * Update the stored sizes for the image view object
     */
    public void updateImageCoordinates() {
        data.imageViewX = map.getLayoutBounds().getWidth();
        data.imageViewY = map.getLayoutBounds().getHeight();
    }

    /**
     * Update the stored sizes for the pathcanvs object
     */
    public void updateCanvasCoordinates() {
        data.canvasX = pathCanvas1.getWidth();
        data.canvasY = pathCanvas1.getHeight();
    }

    //other functions
    @FXML
    public void importCSV(){
        //testEmbeddedDB.fillEdgesTable();
        //testEmbeddedDB.fillNodesTable();

        //nodes is false, edges is true

        //System.out.println(nodeEdgeEdit.isSelected());
        if(nodeEdgeEdit.isSelected()){
            System.out.println("edge");
            importEdgesCSV();
        } else {
            System.out.println("nodes");
            importNodesCSV();
        }

    }

    public void importEdgesCSV(){
        final FileChooser fileChooser = new FileChooser();
        Path path;
        String p = new String();
        List<File> filelist;
        ArrayList<String> paths = new ArrayList<>();

        //File file = fileChooser.showOpenDialog(new Stage());
        filelist = fileChooser.showOpenMultipleDialog(new Stage());

        for(File f : filelist){
            if (f != null) {
                p = f.getAbsolutePath();
                paths.add(p);
                System.out.println(p);
                testEmbeddedDB.loadEdgesFile(p);

            }
        }

//        testEmbeddedDB.fillEdgesTable();
//        testEmbeddedDB.fillNodesTable();

    }

    public void importNodesCSV(){
        final FileChooser fileChooser = new FileChooser();
        Path path;
        String p = new String();
        List<File> filelist;
        ArrayList<String> paths = new ArrayList<>();

        //File file = fileChooser.showOpenDialog(new Stage());
        filelist = fileChooser.showOpenMultipleDialog(new Stage());

        for(File f : filelist){
            if (f != null) {
                p = f.getAbsolutePath();
                paths.add(p);
                System.out.println(p);
                testEmbeddedDB.loadNodesFile(p);
            }
        }

//        testEmbeddedDB.fillEdgesTable();
//        testEmbeddedDB.fillNodesTable();

    }



    @FXML
    public void exportCSV(){
        final DirectoryChooser dirChooser = new DirectoryChooser();
        String s = new String();

        File dir = dirChooser.showDialog(new Stage());

        if(dir != null){
            s = dir.getAbsolutePath();
            System.out.println(s);
            testEmbeddedDB.writeToEdgesCSV(s);
            testEmbeddedDB.writeToNodesCSV(s);
        }

        //testEmbeddedDB.writeToCSV();
    }


    /**
     * function to draw the nodes of the  floors
     * @param FloorNodes
     */
    public void drawNodesCircle(Vector<Node> FloorNodes)
    {
        if(FloorNodes != null) {
            int length = FloorNodes.size();

            Data.data.gc1.setStroke(Color.BLUE);
            Data.data.gc1.stroke();
            // Iterate through all the given nodes to draw the node
            for (int i = 0; i < length; i++)
            {
                Node nodesMap = FloorNodes.get(i);
                double divisionCst = 4.15;
                int offset = 0;
                Point2D point = convertFromImage(nodesMap.getxCoordinate(), nodesMap.getyCoordinate());
                data.gc1.strokeOval(point.getX(),point.getY(), 4.0, 4.0);
                data.gc1.fillOval(point.getX() ,point.getY(), 4.0, 4.0);
            }
        }
    }

    /**
     * function to draw all the edges of the floor
     */
    public void drawEdges(Vector<Edge> floorEdges) {
        if(floorEdges != null) {
            Data.data.gc1.setStroke(Color.RED);
            Data.data.gc1.stroke();
            Data.data.gc1.setLineWidth(4);
            for (Edge edge:floorEdges) {
                double divisionCst = 4.15;
                Node start = edge.getStart();
                Node stop = edge.getEnd();
                Point2D startPoint = convertFromImage(start.getxCoordinate(), start.getyCoordinate());
                Point2D stopPoint = convertFromImage(stop.getxCoordinate(), stop.getyCoordinate());
                data.gc1.strokeLine(startPoint.getX(),startPoint.getY(), stopPoint.getX(), stopPoint.getY());
            }
        }
    }

    /**
     * mouse click location done!
     * @throws IOException
     */

    public void clickNearestNodeSelected() throws IOException {


            pathCanvas1.setOnMousePressed((javafx.scene.input.MouseEvent e) -> {
                e.consume();
                Data.data.gc1.clearRect(0, 0, 1143, 783);

                double newX1 = (e.getX());
                double newY1 = (e.getY());

                double divisionCst = 4.15;
                int offset = 1;
                Point2D point = convertToImage(newX1, newY1);
                if (floorLowerTwo.isSelected()) {
                    selectedNode = mousePosition(point.getX(), point.getY(), Data.data.lowerLevel02FloorNodes);
                } else if (floorLowerOne.isSelected()) {
                    selectedNode = mousePosition(point.getX(), point.getY(), Data.data.lowerLevel01FloorNodes);
                } else if (floorGround.isSelected()) {
                    selectedNode = mousePosition(point.getX(), point.getY(), Data.data.groundFloorNodes);
                } else if (floorOne.isSelected()) {
                    selectedNode = mousePosition(point.getX(), point.getY(), Data.data.firstFloorNodes);
                } else if (floorTwo.isSelected()) {
                    selectedNode = mousePosition(point.getX(), point.getY(), Data.data.secondFloorNodes);
                } else if (floorThree.isSelected()) {
                    selectedNode = mousePosition(point.getX(), point.getY(), Data.data.thirdFloorNodes);
                }

                if (e.isPrimaryButtonDown()) {
                    //e.consume();
                    Data.data.gc1.setStroke(Color.RED);
                    Data.data.gc1.stroke();
                    double xSel = selectedNode.getxCoordinate();
                    double ySel = selectedNode.getyCoordinate();
                    Point2D point2 = convertFromImage(xSel,ySel);
                    Data.data.gc1.strokeOval(point2.getX(), point2.getY(), 7.0, 7.0);
                    Data.data.gc1.fillOval(point2.getX(), point2.getY(), 7.0, 7.0);
                    Main.nodeEditScreenClick(selectedNode, editNodeBtn);

                    /*if(data.nodeAlign.size()>=3){
                        data.nodeAlign.remove(2);
                    }
                    data.nodeAlign.add(0,selectedNode);
                    */

                    drawFloorNodes();
                } else if (e.isSecondaryButtonDown()) {
                    if(editEdges) {
                        contextMenu2.show(pathCanvas1, newX1 - 40, newY1);
                        contextMenu2.getItems().get(0).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                // Event handling for the first item, open the edit edge screen
                                Main.edgeStartEditScreen(editEdgeBtn, selectedNode);
                                drawFloorEdges();
                            }
                        });
                        contextMenu2.getItems().get(1).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                Main.edgeEndEditScreen(editEdgeBtn, selectedNode);
                                drawFloorEdges();
                            }
                        });
                    } else {
                        contextMenu.show(pathCanvas1, newX1 - 40, newY1);
                        contextMenu.getItems().get(0).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                // Event handling for the first item, open the edit node screen
                                Main.nodeEditScreenClick(selectedNode, editNodeBtn);
                                drawFloorNodes();
                            }
                        });
                        contextMenu.getItems().get(1).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                // Event handling for the second item, open add node screen
                                calcX = (int) (e.getSceneX() - data.offset) * divisionCst;
                                calcY = (e.getSceneY() - data.offset) * divisionCst;
                                Main.nodeAddEditScreenClick(editNodeBtn, calcX, calcY);
                                drawFloorNodes();
                            }
                        });
                    }
                }
            });
        }



    public void alignNodes(){
        //nodeAlign na = new nodeAlign(data.nodeAlign.get(0), data.nodeAlign.get(1), data.nodeAlign.get(2));
        nodeAlign na = new nodeAlign();
        int newX = na.setGoodX(data.nodeAlign.get(0), data.nodeAlign.get(1), data.nodeAlign.get(2));
        int newY = na.setGoodY(data.nodeAlign.get(0), data.nodeAlign.get(1), data.nodeAlign.get(2));
        testEmbeddedDB.updateNodeXCoord(data.nodeAlign.get(0).getNodeID(), newX);
        testEmbeddedDB.updateNodeYCoord(data.nodeAlign.get(0).getNodeID(), newY);
        MapEditPageController.updateNodes();
        Data.data.graph = testEmbeddedDB.dbBuildMap();

        drawFloorNodes();
    }

//    public Node returnNearestNodeSelected() throws IOException {
//
//
//        pathCanvas1.setOnMousePressed((javafx.scene.input.MouseEvent e) -> {
//            e.consume();
//            Data.data.gc1.clearRect(0, 0, 1143, 783);
//
//            double newX1 = (e.getSceneX());
//            double newY1 = (e.getSceneY());
//
//            double divisionCst = 4.15;
//            int offset = 1;
//            if (floorLowerTwo.isSelected()) {
//                selectedNode = mousePosition((newX1 - 2) * divisionCst + offset, (newY1 - 2) * divisionCst + offset, Data.data.lowerLevel02FloorNodes);
//            } else if (floorLowerOne.isSelected()) {
//                selectedNode = mousePosition((newX1 - 2) * divisionCst + offset, (newY1 - 2) * divisionCst + offset, Data.data.lowerLevel01FloorNodes);
//            } else if (floorGround.isSelected()) {
//                selectedNode = mousePosition((newX1 - 2) * divisionCst + offset, (newY1 - 2) * divisionCst + offset, Data.data.groundFloorNodes);
//            } else if (floorOne.isSelected()) {
//                selectedNode = mousePosition((newX1 - 2) * divisionCst + offset, (newY1 - 2) * divisionCst + offset, Data.data.firstFloorNodes);
//            } else if (floorTwo.isSelected()) {
//                selectedNode = mousePosition((newX1 - 2) * divisionCst + offset, (newY1 - 2) * divisionCst + offset, Data.data.secondFloorNodes);
//            } else if (floorThree.isSelected()) {
//                selectedNode = mousePosition((newX1 - 2) * divisionCst + offset, (newY1 - 2) * divisionCst + offset, Data.data.thirdFloorNodes);
//            }
//        });
//
//        return selectedNode;
//    }

    @FXML
    public void drawFloorNodes(){
            if (floorLowerTwo.isSelected()) {
                drawNodesCircle(data.lowerLevel02FloorNodes);
            } else if (floorLowerOne.isSelected()) {
                drawNodesCircle(data.lowerLevel01FloorNodes);
            } else if (floorGround.isSelected()) {
                drawNodesCircle(data.groundFloorNodes);
            } else if (floorOne.isSelected()) {
                drawNodesCircle(data.firstFloorNodes);
            } else if (floorTwo.isSelected()) {
                drawNodesCircle(data.secondFloorNodes);
            } else if (floorThree.isSelected()) {
                drawNodesCircle(data.thirdFloorNodes);
            }
    }

    @FXML
    public void drawFloorEdges() {
            if (floorLowerTwo.isSelected()) {
                drawEdges(data.lower2FloorEdges);
            } else if (floorLowerOne.isSelected()) {
                drawEdges(data.lower1FloorEdges);
            } else if (floorGround.isSelected()) {
                drawEdges(data.groundFloorEdges);
            } else if (floorOne.isSelected()) {
                drawEdges(data.firstFloorEdges);
            } else if (floorTwo.isSelected()) {
                drawEdges(data.secondFloorEdges);
            } else if (floorThree.isSelected()) {
                drawEdges(data.thirdFloorEdges);
            }
    }


    /**
     * Function to calculate the nearest node depend on the mouse click location.
     * @param x
     * @param y
     * @param NodesOfTheFloor
     * @return
     */
    public Node mousePosition (double x, double y,Vector<Node> NodesOfTheFloor) {
        double newMouseX = x;
        double newMouseY = y;
        Node GoodOne = null;
        double MinDist = 100000000.0;
        double dist;



        for (Node i: NodesOfTheFloor) {
            dist = data.graph.MouseNodeDist(newMouseX,newMouseY,i);
            if(dist < MinDist) {
                MinDist = dist;
                GoodOne = i;
            }
        }
        return GoodOne;

    }

    @FXML
    public void updateBoolean() {
        editEdges = nodeEdgeEdit.isSelected();
    }

    @FXML
    public void clear() {
        clearCanvas(Data.data.gc1,pathCanvas1);
        clearCanvas(Data.data.gc,pathCanvas);
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    @FXML
    public void chat(){
        //Main.setHelpScreenServiceRequestScreen();
        try{
            /*messenger.API m = new messenger.API();
            m.run(6,6,600,600,
                    "/src/UI/style.css", "test", "test", "sip:HELP@130.215.213.204:6969");*/
        } catch (Exception e){
            System.out.println("API ERROR: " + e.getLocalizedMessage());
        }
    }

    @FXML
    public void about(){Main.aboutWindow(helpButton);}

    @FXML
    public void zoom() {
        scrollMap.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
                System.out.println("this happened too");
                stackPane.setMinSize(newValue.getWidth(), newValue.getHeight());
            }
        });


        scrollContent.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                //event.consume();
                System.out.println("SCROLLING HAPPENED");
                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor = (event.getDeltaY() > 0) ? 1.03 : 1/1.03;
                double extraWidth = scrollContent.getLayoutBounds().getWidth() - scrollMap.getViewportBounds().getWidth();
                double hScrollProportion = (scrollMap.getHvalue() - scrollMap.getHmin()) / (scrollMap.getHmax() - scrollMap.getHmin());
                double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
                double extraHeight = scrollContent.getLayoutBounds().getHeight() - scrollMap.getViewportBounds().getHeight();
                double vScrollProportion = (scrollMap.getVvalue() - scrollMap.getVmin()) / (scrollMap.getVmax() - scrollMap.getVmin());
                double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);

                Point2D scrollOffset = new Point2D(scrollXOffset, scrollYOffset);

                if (!(scaleFactor * stackPane.getScaleX() < 1)) {
                    stackPane.setScaleX(stackPane.getScaleX() * scaleFactor);
                    stackPane.setScaleY(stackPane.getScaleY() * scaleFactor);
                }

                double scrollXOffset2 = scrollOffset.getX();

                double scrollYOffset2 = scrollOffset.getY();

                double extraWidth2 = scrollContent.getLayoutBounds().getWidth() - scrollMap.getViewportBounds().getWidth();

                if (extraWidth2 > 0) {
                    double halfWidth = scrollMap.getViewportBounds().getWidth() / 2 ;
                    double newScrollXOffset = (scaleFactor - 1) *  halfWidth + scaleFactor * scrollXOffset2;
                    scrollMap.setHvalue(scrollMap.getHmin() + newScrollXOffset * (scrollMap.getHmax() - scrollMap.getHmin()) / extraWidth2);
                } else {
                    scrollMap.setHvalue(scrollMap.getHmin());
                }

                double extraHeight2 = scrollContent.getLayoutBounds().getHeight() - scrollMap.getViewportBounds().getHeight();

                if (extraHeight2 > 0) {
                    double halfHeight = scrollMap.getViewportBounds().getHeight() / 2 ;
                    double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset2;
                    scrollMap.setVvalue(scrollMap.getVmin() + newScrollYOffset * (scrollMap.getVmax() - scrollMap.getVmin()) / extraHeight2);
                } else {
                    scrollMap.setHvalue(scrollMap.getHmin());
                }
            }
        });

        final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty<Point2D>();
            pathCanvas1.setOnMousePressed((javafx.scene.input.MouseEvent event) -> {

                double newX1 = (event.getX());
                double newY1 = (event.getY());

                //Data.data.gc1.strokeOval(newX1, newY1, 7.0, 7.0);
                //Data.data.gc1.fillOval(newX1, newY1, 7.0, 7.0);

                double divisionCst = 4.15;
                int offset = 1;
                Point2D point = convertToImage(newX1, newY1);
                if (floorLowerTwo.isSelected()) {
                    selectedNode = mousePosition(point.getX(), point.getY(), Data.data.lowerLevel02FloorNodes);
                } else if (floorLowerOne.isSelected()) {
                    selectedNode = mousePosition(point.getX(), point.getY(), Data.data.lowerLevel01FloorNodes);
                } else if (floorGround.isSelected()) {
                    selectedNode = mousePosition(point.getX(), point.getY(), Data.data.groundFloorNodes);
                } else if (floorOne.isSelected()) {
                    selectedNode = mousePosition(point.getX(), point.getY(), Data.data.firstFloorNodes);
                } else if (floorTwo.isSelected()) {
                    selectedNode = mousePosition(point.getX(), point.getY(), Data.data.secondFloorNodes);
                } else if (floorThree.isSelected()) {
                    selectedNode = mousePosition(point.getX(), point.getY(), Data.data.thirdFloorNodes);
                }

                if (event.isSecondaryButtonDown()) {
                    //event.consume();
                    Point2D point2 = convertFromImage(selectedNode.getxCoordinate(), selectedNode.getyCoordinate());
                    Data.data.gc1.strokeOval(point2.getX(), point2.getY(), 7.0, 7.0);
                    Data.data.gc1.fillOval(point2.getX(), point2.getY(), 7.0, 7.0);
                    if (editEdges) {
                        contextMenu2.show(pathCanvas1, newX1 - 40, newY1);
                        contextMenu2.getItems().get(0).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                // Event handling for the first item, open the edit edge screen
                                Main.edgeStartEditScreen(editEdgeBtn, selectedNode);
                                drawFloorEdges();
                            }
                        });
                        contextMenu2.getItems().get(1).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                Main.edgeEndEditScreen(editEdgeBtn, selectedNode);
                                drawFloorEdges();
                            }
                        });
                    } else {
                        contextMenu.show(pathCanvas1, newX1 - 40, newY1);
                        contextMenu.getItems().get(0).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                // Event handling for the first item, open the edit node screen
                                Main.nodeEditScreenClick(selectedNode, editNodeBtn);
                                drawFloorNodes();
                            }
                        });
                        contextMenu.getItems().get(1).setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent even) {
                                // Event handling for the second item, open add node screen
                                calcX = (int) (event.getX() - data.offset) * divisionCst;
                                calcY = (event.getY() - data.offset) * divisionCst;
                                Main.nodeAddEditScreenClick(editNodeBtn, calcX, calcY);
                                drawFloorNodes();
                            }
                        });
                    }
                }
                lastMouseCoordinates.set(new Point2D(event.getX(), event.getY()));
            });

        scrollMap.setOnMouseDragged(new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {

                System.out.println("These are last mouse coordinates X: " + lastMouseCoordinates.get().getX() + " Y: " + lastMouseCoordinates.get().getY());

                double deltaX = event.getX() - lastMouseCoordinates.get().getX();
                double extraWidth = scrollContent.getLayoutBounds().getWidth() - scrollMap.getViewportBounds().getWidth();
                double deltaH = deltaX * ((scrollMap.getHmax() - scrollMap.getHmin()) / extraWidth);
                double desiredH = scrollMap.getHvalue() - deltaH;

                System.out.println("This is the scrollmap HMax: " + scrollMap.getHmax() + " This is the desired H: " + desiredH);

                if(deltaX > 0 ) {
                    System.out.println("This is the set value: " + Math.max(0, Math.min(scrollMap.getHmax(), desiredH)));
                    scrollMap.setHvalue(Math.max(0, Math.min(scrollMap.getHmax(), desiredH)));
                    event.consume();
                } else {
                    System.out.println("FILTERED");
                }

                double deltaY = event.getY() - lastMouseCoordinates.get().getY();
                double extraHeight = scrollContent.getLayoutBounds().getHeight() - scrollMap.getViewportBounds().getHeight();
                double deltaV = deltaY * ((scrollMap.getHmax() - scrollMap.getHmin()) / extraHeight);
                double desiredV = scrollMap.getVvalue() - deltaV;

                if ( deltaY > 0 ) {
                    System.out.println("This is the scrollmap VMax: " + scrollMap.getVmax() + " This is the desired V: " + desiredV);
                        scrollMap.setVvalue(Math.max(0, Math.min(scrollMap.getVmax(), desiredV)));
                    event.consume();
                } else {
                    System.out.println("FILTERED");
                }
            }
        });
    }

    public Point2D figureScrollOffset(Group scrollContent, ScrollPane scroller) {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
    }

    public void repositionScroller(Group scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
        double scrollXOffset = scrollOffset.getX();
        double scrollYOffset = scrollOffset.getY();
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        if (extraWidth > 0) {
            double halfWidth = scroller.getViewportBounds().getWidth() / 2 ;
            double newScrollXOffset = (scaleFactor - 1) *  halfWidth + scaleFactor * scrollXOffset;
            scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        if (extraHeight > 0) {
            double halfHeight = scroller.getViewportBounds().getHeight() / 2 ;
            double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
            scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
    }

    // reset to the top left:
    private void reset(ImageView imageView, double width, double height) {
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
    }

    private void resetStack(ScrollPane pane, double width, double height) {
        pane.setViewportBounds(new BoundingBox(0,0,width,height));
    }

    private double clamp(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    // convert mouse coordinates in the imageView to coordinates in the actual image:
    private Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
        double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();
        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProportion * viewport.getWidth(),
                viewport.getMinY() + yProportion * viewport.getHeight());
    }

    @FXML
    public void startAutoClose(){
        startLabel.setText(startAuto.getItems().get(0));
        startAuto.setVisible(false);
    }

    @FXML
    public void startAutoComplete(){
        startAuto.setVisible(true);
        ObservableList<String> filteredEntries = FXCollections.observableArrayList("empty");
        //filtering
        startLabel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                LinkedList<String> searchResult = new LinkedList<>();
                //Check if the entered Text is part of some entry
                String text = startLabel.getText();
                Pattern pattern;
                pattern = Pattern.compile(".*" + text + ".*",
                        Pattern.CASE_INSENSITIVE);

                for (String entry : allEntries) {
                    Matcher matcher = pattern.matcher(entry);
                    if (matcher.matches()) {
                        searchResult.add(entry);
                    }
                }

                if (allEntries.size() > 0) {
                    filteredEntries.clear();
                    filteredEntries.addAll(searchResult);
                }
                startAuto.setItems(filteredEntries);
            }
        });

        startAuto.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                startLabel.setText(newValue);
            }
        });
    }

    @FXML
    public void setDefaultStart(){
        data.kiosk = SearchEngine.SearchNode(startLabel.getText());
    }
}