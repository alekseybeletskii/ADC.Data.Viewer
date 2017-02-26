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


import adc.data.viewer.plotter.Plotter;
import adc.data.viewer.processing.SavitzkyGolayFilter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class PlotterController {

    private MainApp mainApp;
    private Plotter plotter;



    @FXML
    Label legend;
    @FXML
    private AnchorPane plotsLayout;
    @FXML
    private Label xyLabel;
    @FXML
    private Button subtractSGFilter;

    public Plotter getPlotter() {
        return plotter;
    }
    public Label getXyLabel() {
        return xyLabel;
    }
    public AnchorPane getPlotsLayout() {
        return plotsLayout;
    }
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    public void setPlotsOnPane() {

        plotter = new Plotter(mainApp, plotsLayout, this);

    }

    public void setLegend(Label legend) {
        this.legend = legend;
    }
    public Label getLegend() {
        return legend;
    }

    @FXML
    public void handleRaw(ActionEvent actionEvent) {

        plotter.getCanvasData().setSGfilter(null);
        plotter.getCanvasData().setPlotType("Raw");
        plotter.getCanvasData().drawData();
    }

    @FXML
    public void handleRawAndSGFilter(ActionEvent actionEvent) {
        plotter.getCanvasData().setSGfilter(new SavitzkyGolayFilter(PlotterSettingController.sgleft, PlotterSettingController.sgright, PlotterSettingController.sgorder));
        plotter.getCanvasData().setPlotType("RawAndSGFilter");
        plotter.getCanvasData().drawData();
    }

    @FXML
    public void handleSGFiltered(ActionEvent actionEvent) {
        plotter.getCanvasData().setSGfilter(new SavitzkyGolayFilter(PlotterSettingController.sgleft, PlotterSettingController.sgright, PlotterSettingController.sgorder));
        plotter.getCanvasData().setPlotType("SGFiltered");
        plotter.getCanvasData().drawData();
    }

    @FXML
    public void handleSGFilter(ActionEvent actionEvent) {
        plotter.getCanvasData().setSGfilter(new SavitzkyGolayFilter(PlotterSettingController.sgleft, PlotterSettingController.sgright, PlotterSettingController.sgorder));
        plotter.getCanvasData().setPlotType("SGFilter");
        plotter.getCanvasData().drawData();
    }

    @FXML
    public void handlePlotterSettings(ActionEvent actionEvent) {
//        mainApp.setPlotterController(this);
        mainApp.setPlotterSetting(this);
    }



    @FXML
    private void initialize() {
    subtractSGFilter.setText("\u2013SGfilter");
    }

}