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

package adc.data.viewer.parser;


import adc.data.viewer.ui.MainApp;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.List;
import java.util.stream.IntStream;



/**
 * This class produces an object that will incapsulate three output arrays:
 * -"signals" with all signals
 * -"signalLabels" with labels to be shown in table view for convenient signals selection
 * -"signalFullPath" full paths to every signal used when saving as separate text files
 * "saveToText" method to convert data from the binary to the text format
 */


public class DataParser {
    private DataPaths dataPaths;
    private DataParams dataParams;
    private DataFormatsDetect dataFormatsDetect;
    private double[] [] signals;
    private int signalIndex;
    private String [] signalLabels;
    private Path [] signalFullPath;
    private int [] fileNumbers;
    private MainApp mainApp;




    public DataParser( MainApp mainApp) {
        this.mainApp=mainApp;
        dataFormatsDetect = new DataFormatsDetect(mainApp);
    }

    public synchronized void parseNewList(List<File> filesList) {
        signalIndex =-1;
        File[] filesListToProcess = filesList.toArray(new File[filesList.size()]);
        dataPaths = new DataPaths(filesListToProcess);  //produce Paths from an input list of files
        dataParams = new DataParams(filesListToProcess.length); //This object will contain parameters from all files that have been opened
        for ( DataTypesList frmt : DataTypesList.values()) frmt.getDataType().setDataParser(this);



        dataFormatsDetect.detectFormat();


        setParam();

        signals = new double[IntStream.of(dataParams.getRealChannelsQuantity()).sum()] [];
        signalLabels = new String[signals.length];
        signalFullPath = new Path[signals.length];
        fileNumbers = new int[signals.length];

        if(dataParams.isDataParamsValid()) setData();
    }

    private void setParam() {

        String formatName;
        int i=0;
        while (i < dataPaths.getFileName().length)

        {
            formatName = dataParams.getDataFormatStr()[i].toUpperCase();

            if (!formatName.equals("TEXTFILE"))

                DataTypesList.valueOf(formatName).getDataType().setParam(i);

            i++;
        }
    }


    private  void setData (){
        String formatName;
        int i=0;
        while (i < dataPaths.getFileName().length)

        {
            formatName = dataParams.getDataFormatStr()[i].toUpperCase();

            if (formatName.equals("TEXTFILE"))
            {mainApp.getTextFileDataController().setData(i,signalIndex);
                i++;
                continue;}

            DataTypesList.valueOf(formatName).getDataType().setData(i,signalIndex);

            i++;

        }
    }

    /**
     * This method fills all the output arrays and will be invoked by a corresponding ADC-type class
     *
     * @param signal
     * a signal extracted from binary file by corresponding ADC-type-class
     * @param signalIndex
     * an extracted signal's sequence number in the "signals" array
     * @param fileIndex
     * a number of a processed source file
     * @param sigAdcNum
     * an ADC channel number of extracted signal
     */

    public void setSignals(double [] signal, int signalIndex, int fileIndex, int sigAdcNum) {
        this.signals[signalIndex] = signal.clone();
        this.signalLabels[signalIndex] = dataPaths.getFileName()[fileIndex]+ "_#"+sigAdcNum;
        this.signalFullPath[signalIndex] = Paths.get(dataPaths.getDataFilePath()[fileIndex].getParent() +System.getProperty("file.separator")+ dataPaths.getFileName()[fileIndex] + "_#" + sigAdcNum);
        this.signalIndex = signalIndex;
        this.fileNumbers[signalIndex] = fileIndex;
    }

    /**
     * The method saves avery signal as a separate text file to the "txt" subdirectory
     */
    public void saveToText(){
        int i=0;
        byte [] stringBytes;
        while (i< signalLabels.length){

            Path   outTxtPath =    getSignalFullPath()[i].getParent().resolve(Paths.get("txt"));
            try {
                Files.createDirectories(outTxtPath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try ( FileChannel fWrite = (FileChannel)Files.newByteChannel(outTxtPath.resolve(signalLabels[i] +".txt"),StandardOpenOption.WRITE,StandardOpenOption.READ, StandardOpenOption.CREATE))

            {
                MappedByteBuffer wrBuf = fWrite.map(FileChannel.MapMode.READ_WRITE, 0, signals[i].length*16);
                int j=0;
                while(j<signals[i].length) {
                    stringBytes = String.format("%15.7f", signals[i][j]).getBytes("UTF-8");
                    wrBuf.put(stringBytes);
                    wrBuf.put((byte)System.getProperty("line.separator").charAt(0));

                    j++;
                }


            } catch(InvalidPathException e) {
                System.out.println("Path Error " + e);
            } catch (IOException e) {
                System.out.println("I/O Error " + e);
            }

            i++;
        }

    }

    public DataParams getDataParams() {
        return dataParams;
    }

    public void setDataParams(DataParams dataParams) {
        this.dataParams = dataParams;
    }

    public DataPaths getDataPaths() {
        return dataPaths;
    }

    public void setDataPaths(DataPaths dataPaths) {
        this.dataPaths = dataPaths;
    }

    /**
     *
     * @return
     * return array of file numbers
     */
    public int[] getFileNumbers() {
        return fileNumbers;
    }

    /**
     * @return
     * return array of signals
     */
    public double[][] getSignals() {
        return signals;
    }

    /**
     * @return
     *  return an array of labels as strings to be shown in table view
     */
    public String[] getSignalLabels() {
        return signalLabels;
    }

    /**
     * @return
     * return an array of signals' full paths as strings to be used when saving as text files
     */
    private Path[] getSignalFullPath() {
        return signalFullPath;
    }
}


