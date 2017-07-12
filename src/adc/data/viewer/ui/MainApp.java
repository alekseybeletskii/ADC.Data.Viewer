/*
 * 	********************* BEGIN LICENSE BLOCK *********************************
 * 	ADCDataViewer
 * 	Copyright (c) 2016 onward, Aleksey Beletskii  <beletskiial@gmail.com>
 * 	All rights reserved
 *
 * 	github: https://github.com/alekseybeletskii
 *
 * 	The ADCDataViewer software serves for visualization and simple processing
 * 	of any data recorded with Analog Digital Converters in binary or text form.
 *
 * 	Commercial support is available. To find out more contact the author directly.
 *
 * 	Redistribution and use in source and binary forms, with or without
 * 	modification, are permitted provided that the following conditions are met:
 *
 * 	  1. Redistributions of source code must retain the above copyright notice, this
 * 	     list of conditions and the following disclaimer.
 * 	  2. Redistributions in binary form must reproduce the above copyright notice,
 * 	     this list of conditions and the following disclaimer in the documentation
 * 	     and/or other materials provided with the distribution.
 *
 * 	The software is distributed to You under terms of the GNU General Public
 * 	License. This means it is "free software". However, any program, using
 * 	ADCDataViewer _MUST_ be the "free software" as well.
 * 	See the GNU General Public License for more details
 * 	(file ./COPYING in the root of the distribution
 * 	or website <http://www.gnu.org/licenses/>)
 *
 * 	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * 	ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * 	WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * 	DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * 	ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * 	(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * 	LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * 	ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * 	(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * 	SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * 	********************* END LICENSE BLOCK ***********************************
 */

package adc.data.viewer.ui;

import adc.data.viewer.model.ADCDataRecords;
import adc.data.viewer.parser.DataParser;
import adc.data.viewer.plotter.Plotter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public  class MainApp extends Application {


    public void setDirectoryWatcher(WatchDirectory directoryWatcher) {
        this.directoryWatcher = directoryWatcher;
    }

    public WatchDirectory getDirectoryWatcher() {
        return directoryWatcher;
    }

    private  WatchDirectory directoryWatcher;

    public static void main(String[] args) {
        launch(args);
    }

    public void setListOfFiles(List<File> listOfFiles) {
        this.listOfFiles = listOfFiles;
    }

    private List<File> listOfFiles;

    public boolean isNewFileCreated() {
        return newFileCreated;
    }

    public void setNewFileCreated(boolean newFileCreated) {
        this.newFileCreated = newFileCreated;
    }

    private boolean newFileCreated;
    private  boolean redrawAllowed;

    public void setNextSignalToDraw(ADCDataRecords nextSignalToDraw) {
        this.nextSignalToDraw = nextSignalToDraw;
    }

    private final Image logo = new Image("images/logo.png");
    private ADCDataRecords nextSignalToDraw;
    private String defaultPlotsLayoutType;
    private Stage primaryStage;
    private BorderPane mainLayout;
    private DataParser dataParser;
    private static final Preferences appPreferences = Preferences.userRoot().node(MainApp.class.getName());

    private ObservableList<ADCDataRecords> adcDataRecords = FXCollections.observableArrayList();
    private TextFileParamController textFileParamController;
    private int nextSignalToDrawIndex;
    private int howManyPlots;

    private double defaultWidthOfLine;
    private String defaultPlotStyle;
    private String defaultPlotType;
    private int [] defaultSGFilterSettings;
    private double [] defaultFixZeroShiftRange;
    private double [] savedAxesRange;
    private boolean defaultFixZeroShift;
    private boolean useSavedAxesRange;

    public List<PlotterController> getPlotterControllerlist() {
        return plotterControllerlist;
    }
    private List<PlotterController> plotterControllerlist =new ArrayList<>();
    private SignalsOverviewController signalsOverviewController;
    public String getDefaultPlotsLayoutType() {
        return defaultPlotsLayoutType;
    }
    public void setDefaultPlotsLayoutType(String defaultPlotsLayoutType) {
        this.defaultPlotsLayoutType = defaultPlotsLayoutType;
    }
    public ADCDataRecords getNextSignalToDraw() {
        return nextSignalToDraw;
    }
    public SignalsOverviewController getSignalsOverviewController() {
        return signalsOverviewController;
    }
    public TextFileParamController getTextFileParamController() {
        return textFileParamController;
    }
    public ObservableList<ADCDataRecords> getAdcDataRecords() {
        return adcDataRecords;
    }
    public void setAdcDataRecords(ObservableList<ADCDataRecords> adcDataRecords) {
        this.adcDataRecords = adcDataRecords;
    }
    Stage getPrimaryStage() {
        return primaryStage;
    }
    public DataParser getDataParser() {
        return dataParser;
    }
    public double getHowManyPlots() {
        return howManyPlots;
    }
    public String getDefaultPlotType() {
        return defaultPlotType;
    }
    public void setDefaultPlotType(String defaultPlotType) {
        this.defaultPlotType = defaultPlotType;
    }
    public void setDefaultWidthOfLine(double defaultWidthOfLine) {
        this.defaultWidthOfLine = defaultWidthOfLine;
    }
    public double getDefaultWidthOfLine() {
        return defaultWidthOfLine;
    }
    public String getDefaultPlotStyle() {
        return defaultPlotStyle;
    }
    public void setDefaultPlotStyle(String defaultPlotStyle) {
        this.defaultPlotStyle = defaultPlotStyle;
    }
    public int[] getDefaultSGFilterSettings() {
        return defaultSGFilterSettings;
    }
    public void setDefaultSGFilterSettings(int[] defaultSGFilterSettings) {
        this.defaultSGFilterSettings = defaultSGFilterSettings;
    }
    public double[] getDefaultFixZeroShiftRange() {
        return defaultFixZeroShiftRange;
    }
    public void setDefaultFixZeroShiftRange(double[] defaultFixZeroShiftRange) {
        this.defaultFixZeroShiftRange = defaultFixZeroShiftRange;
    }
    public boolean getDefaultFixZeroShift() {
        return defaultFixZeroShift;
    }
    public void setDefaultFixZeroShift(boolean defaultFixZeroShift) {
        this.defaultFixZeroShift = defaultFixZeroShift;
    }
    public double[] getSavedAxesRange() {
        return savedAxesRange;
    }
    public void setSavedAxesRange(double[] savedAxesRange) {
        this.savedAxesRange = savedAxesRange;
    }
    public boolean isUseSavedAxesRange() {
        return useSavedAxesRange;
    }
    public void setUseSavedAxesRange(boolean useSavedAxesRange) {
        this.useSavedAxesRange = useSavedAxesRange;
    }


    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setOnCloseRequest(e ->
        {Platform.exit();
         System.exit(0);});

        nextSignalToDrawIndex =-1;
        this.dataParser =new DataParser(this);
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("ADC Signal Viewer");
        this.primaryStage.getIcons().add(logo);
        initMainLayout();
        showSignalsOverview();
        defaultWidthOfLine =1.0;
        defaultPlotType="Raw";
        defaultPlotStyle = "line";
        defaultPlotsLayoutType = "AllPlots";
        defaultSGFilterSettings = new int [] {50,50,1};
        defaultFixZeroShiftRange = new double [] {0,0};
        savedAxesRange = new double [] {0.0,1.0,-1.0,1.0};
        defaultFixZeroShift = false;
        useSavedAxesRange =false;
        PlotterSettingController.setSGFilterSettingsDefault(defaultSGFilterSettings[0],defaultSGFilterSettings[1],defaultSGFilterSettings[2]);
        PlotterSettingController.setSpectrogramSettingsDefault(256,"Hanning",50);
        PlotterSettingController.setFixZeroShiftDefault(defaultFixZeroShiftRange[0], defaultFixZeroShiftRange[1], defaultFixZeroShift);
        PlotterSettingController.setWidthOfLineDefault(defaultWidthOfLine);
        PlotterSettingController.setChosenLineOrScatter(defaultPlotStyle);
    }

    private void initMainLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("MainLayout.fxml"));
            mainLayout =  loader.load();
            Scene scene = new Scene(mainLayout);
            primaryStage.setScene(scene);
            MainLayoutController controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showSignalsOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("SignalsOverview.fxml"));
            AnchorPane signalsOverview = loader.load();
            mainLayout.setCenter(signalsOverview);
            signalsOverviewController  = loader.getController();
            signalsOverviewController.setMainApp(this);
            signalsOverviewController.setTableItems();
            signalsOverviewController.getPlotsScrollPane().setVisible(false);
            signalsOverviewController.getSignalsOverviewSplitPane().setVisible(false);
            setKeyPressedAction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    synchronized void drawPlots() {
        Plotter.setPlotterObjectsCounter(0);

        clearPlots();
        switch (defaultPlotsLayoutType){
            case "AllPlots":
                signalsOverviewController.getPlotsScrollPane().setFitToHeight(true);
                nextSignalToDraw=null;
                layoutLoader();
                plotterControllerlist.get(plotterControllerlist.size()-1)
                        .getPlotsLayout().prefHeightProperty()
                        .bind(signalsOverviewController.getPlotsVBox().heightProperty());
                break;
            case "AllPlotsByOne":
                signalsOverviewController.getPlotsScrollPane().setFitToHeight(true);
                for (ADCDataRecords sm: adcDataRecords) {
                    if (sm.getSignalSelected()) {
                        nextSignalToDraw=sm;
                        layoutLoader();
                        plotterControllerlist.get(plotterControllerlist.size()-1)
                                .getPlotsLayout().prefHeightProperty()
                                .bind(signalsOverviewController.getPlotsVBox().heightProperty());
                    }
                }
                break;
            case "AllPlotsByOneScroll":
                signalsOverviewController.getPlotsScrollPane().setFitToHeight(false);
                for (ADCDataRecords sm: adcDataRecords) {
                    if (sm.getSignalSelected()) {
                        nextSignalToDraw=sm;
                        layoutLoader();

                        plotterControllerlist.get(plotterControllerlist.size()-1)
                                .getPlotsLayout().setPrefHeight
                                (signalsOverviewController.getSignalsOverviewRightPane().getHeight());
                    }
                }
                break;
            default:
                break;
        }

        howManyPlots=0;
        for (PlotterController pc:plotterControllerlist){

            if(defaultPlotsLayoutType.equals("AllPlotsByOne")){
               AnchorPane.setBottomAnchor(pc.getPlotter(),25.0);
            }
            howManyPlots++;
;
            pc.getPlotter().getCanvasData().widthProperty().addListener(it -> pc.getPlotter().getCanvasData().drawData());
            pc.getPlotter().getCanvasData().heightProperty().addListener(it -> pc.getPlotter().getCanvasData().drawData());
            signalsOverviewController.getPlotsVBox().getChildren().add(pc.getPlotsLayout());
        }


        signalsOverviewController.getPlotsScrollPane().setVisible(true);
        signalsOverviewController.getPlotsScrollPane().requestFocus();
    }

    private void layoutLoader() {
        PlotterController plotterController;
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("Plotter.fxml"));
            loader.load();
            plotterController = loader.getController();
            plotterController.setMainApp(this);
            plotterController.setPlotsOnPane();
            plotterController.getPlotter().getCanvasData().setPlotterController(plotterController);
            plotterController.getPlotter().getCanvasData().setNextSignalToDraw(nextSignalToDraw);
            plotterControllerlist.add(plotterController);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTextFileParams(int fileIndex) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("TextFileParam.fxml"));
            BorderPane textFileParams = loader.load();
            textFileParamController = loader.getController();
            textFileParamController.getNextFileName().setText(dataParser.getFileName()[fileIndex]);
            Stage textFileParamsStage = new Stage();
            textFileParamsStage.initStyle(StageStyle.UNDECORATED);
            textFileParamsStage.getIcons().add(logo);
            textFileParamsStage.initOwner(primaryStage);
            textFileParamsStage.setResizable(false);
            textFileParamsStage.toFront();
            textFileParamController.setMainApp(this);
            textFileParamController.setDataParser(dataParser);
            textFileParamController.setTextFileParamsStage(textFileParamsStage);
            textFileParamController.setFileNumber(fileIndex);
            textFileParamsStage.setTitle("Set siganl parameters");
            Scene scene = new Scene(textFileParams);
            textFileParamsStage.setScene(scene);
            textFileParamsStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     void setPlotterSetting(PlotterController pc) {
            PlotterSettingController plotterSettingController;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("PlotterSetting.fxml"));
            BorderPane plotterSetting = loader.load();
            plotterSettingController = loader.getController();
            Stage plotterSettingStage = new Stage();
            plotterSettingStage.setResizable(false);
            plotterSettingStage.setAlwaysOnTop(true);
            plotterSettingController.setMainApp(this);
            plotterSettingController.setPlotterController(pc);
            plotterSettingController.setPlotterSettingStage(plotterSettingStage);
            plotterSettingStage.setTitle("Plotter Settings");
            Scene scene = new Scene(plotterSetting);
            plotterSettingStage.setScene(scene);
            plotterSettingStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     void showReadme(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("Readme.fxml"));
            TextArea aReadme = loader.load();
            ReadmeController controller =loader.getController();
            Stage aReadmeStage = new Stage();
            controller.setStageReadme(aReadmeStage);
            aReadmeStage.setResizable(false);
            aReadmeStage.setTitle("Description. Double left-click to close");
            Scene scene = new Scene(aReadme);
            aReadmeStage.setScene(scene);
            aReadmeStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     void clearAll(){
         nextSignalToDrawIndex=-1;
             adcDataRecords.clear();
             signalsOverviewController.getPlotsVBox().getChildren().clear();
             plotterControllerlist.clear();
             signalsOverviewController.getPlotsScrollPane().setVisible(false);
    }

    private void clearPlots(){
        if(defaultPlotsLayoutType.equals("AllPlots")) {
            redrawAllowed=true;
            signalsOverviewController.getPlotsVBox().getChildren().clear();
            plotterControllerlist.clear();
            signalsOverviewController.getPlotsScrollPane().setVisible(false);
        }
        if(defaultPlotsLayoutType.equals("AllPlotsByOne")){
            redrawAllowed=false;
            signalsOverviewController.getPlotsVBox().getChildren().clear();
            plotterControllerlist.clear();
            signalsOverviewController.getPlotsScrollPane().setVisible(false);
        }
        if(defaultPlotsLayoutType.equals("AllPlotsByOneScroll")){
            redrawAllowed=false;
            signalsOverviewController.getPlotsVBox().getChildren().clear();
            plotterControllerlist.clear();
            signalsOverviewController.getPlotsScrollPane().setVisible(false);
        }
    }

    private void signalMarkerAddListeners() {

        for (ADCDataRecords sgmrk : adcDataRecords){
            sgmrk.signalSelectedProperty().addListener((observable, oldValue, newValue) -> {
                        if (!plotterControllerlist.isEmpty() && redrawAllowed) {
                            plotterControllerlist.get(0).getPlotter().getCanvasData().drawData();
                        }
                    }
            );

            sgmrk.signalColorProperty().addListener((observable, oldValue, newValue) -> {
                    if (!plotterControllerlist.isEmpty() && redrawAllowed) {
                        plotterControllerlist.get(0).getPlotter().getCanvasData().drawData();
                    }
                }
        );
    }
    }

    private void setKeyPressedAction(){




        signalsOverviewController.getPlotsScrollPane().setOnKeyPressed(k -> {

            if(k.getCode().getName().equals("F")){
                double oldDividerPos = signalsOverviewController.getSignalsOverviewSplitPane().getDividerPositions()[0];
                double newDividerPosition = oldDividerPos>0.05?0:0.2;
                signalsOverviewController.getSignalsOverviewSplitPane().setDividerPosition(0,newDividerPosition);
            }



            if(defaultPlotsLayoutType.equals("AllPlots")) {
                if (k.getCode() == KeyCode.HOME && k.isShortcutDown()) {
                    nextSignalToDraw=null;
                    redrawAllowed=false;
                    adcDataRecords.forEach(sig -> sig.setSignalSelected(true));
                    plotterControllerlist.get(0).getPlotter().getAxes().obtainDataAndTimeMargins(nextSignalToDraw);
                    plotterControllerlist.get(0).getPlotter().getAxes().setAxesBasicSetup();
                    nextSignalToDrawIndex = -1;
                    plotterControllerlist.get(0).getPlotter().getCanvasData().drawData();
                    redrawAllowed=true;
                } else
                    switch (k.getCode()) {
                        case HOME:
                            adcDataRecords.forEach(sig -> sig.setSignalSelected(false));
                            nextSignalToDrawIndex = 0;
                            nextSignalToDraw= adcDataRecords.get(nextSignalToDrawIndex);
                            plotterControllerlist.get(0).getPlotter().getCanvasData().setNextSignalToDraw(nextSignalToDraw);
                            plotterControllerlist.get(0).getPlotter().getAxes().obtainDataAndTimeMargins(nextSignalToDraw);
                            plotterControllerlist.get(0).getPlotter().getAxes().setAxesBasicSetup();
                            adcDataRecords.get(nextSignalToDrawIndex).setSignalSelected(true);
                            break;
                        case END:
                            adcDataRecords.forEach(sig -> sig.setSignalSelected(false));
                            nextSignalToDrawIndex = adcDataRecords.size() - 1;
                            nextSignalToDraw= adcDataRecords.get(nextSignalToDrawIndex);
                            plotterControllerlist.get(0).getPlotter().getCanvasData().setNextSignalToDraw(nextSignalToDraw);
                            plotterControllerlist.get(0).getPlotter().getAxes().obtainDataAndTimeMargins(nextSignalToDraw);
                            plotterControllerlist.get(0).getPlotter().getAxes().setAxesBasicSetup();
                            adcDataRecords.get(nextSignalToDrawIndex).setSignalSelected(true);
                            break;
                        case DOWN:
                            adcDataRecords.forEach(sig -> sig.setSignalSelected(false));
                            nextSignalToDrawIndex++;
                            nextSignalToDrawIndex = nextSignalToDrawIndex > adcDataRecords.size() - 1 ? 0 : nextSignalToDrawIndex;
                            nextSignalToDraw= adcDataRecords.get(nextSignalToDrawIndex);
                            plotterControllerlist.get(0).getPlotter().getCanvasData().setNextSignalToDraw(nextSignalToDraw);
                            plotterControllerlist.get(0).getPlotter().getAxes().obtainDataAndTimeMargins(nextSignalToDraw);
                            plotterControllerlist.get(0).getPlotter().getAxes().setAxesBasicSetup();
                            adcDataRecords.get(nextSignalToDrawIndex).setSignalSelected(true);
                            break;
                        case UP:
                            adcDataRecords.forEach(sig -> sig.setSignalSelected(false));
                            nextSignalToDrawIndex--;
                            nextSignalToDrawIndex = nextSignalToDrawIndex < 0 ? adcDataRecords.size() - 1 : nextSignalToDrawIndex;
                            nextSignalToDraw= adcDataRecords.get(nextSignalToDrawIndex);
                            plotterControllerlist.get(0).getPlotter().getCanvasData().setNextSignalToDraw(nextSignalToDraw);
                            plotterControllerlist.get(0).getPlotter().getAxes().obtainDataAndTimeMargins(nextSignalToDraw);
                            plotterControllerlist.get(0).getPlotter().getAxes().setAxesBasicSetup();
                            adcDataRecords.get(nextSignalToDrawIndex).setSignalSelected(true);
                            break;
                        default:
                            break;
                    }
            }

        });

    }








    public  void parse (List<Path> inpList) {

        int dotIndex = inpList.get(0).getFileName().toString().lastIndexOf(".");
        String fileExtension = inpList.get(0).getFileName().toString().substring(dotIndex + 1).toLowerCase();

        if ((fileExtension.equals("dat") || fileExtension.equals("txt")) && inpList.get(0).toFile().length() > 0) {
            clearAll();
            setDefaultPlotsLayoutType("AllPlots");
            redrawAllowed=true;
            dataParser.parseNewList(inpList);
            signalMarkerAddListeners();
            signalsOverviewController.getSignalsOverviewSplitPane().setVisible(true);
            drawPlots();
        }
        synchronized (this){
            if(directoryWatcher!=null)directoryWatcher.resumeWatcher();
        }
    }




}
