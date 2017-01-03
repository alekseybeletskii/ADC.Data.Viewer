package adc.data.viewer.ADCreader;


import adc.data.viewer.MainApp;
import adc.data.viewer.controllers.TextFileDataController;

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
 * "saveToFile" method to convert data from the binary to the text format
 */


public class DataParser {
    private DataPaths dataPaths;
    private DataParams dataParams;
    private DataFormatsDetect dataFormatsDetect;
    private double[] [] signals;
    private int sigCount=-1;
    private String [] signalLabels;
    private Path [] signalFullPath;
    private int [] fileNumbers;
    private MainApp mainApp;


    /**
     * The constructor takes a list of files in the "List<File>" format after the button "Open" is pressed
     * @param filesList
     * a list of files to be processed
     */

    public DataParser(List<File> filesList, MainApp mainApp) {

        this.mainApp=mainApp;
        mainApp.setDataPars(this);
//        TextFileDataController.setDataParser(this);

        File[] filesListToProcess = filesList.toArray(new File[filesList.size()]);

        dataPaths = new DataPaths(filesListToProcess);  //produce Paths from an input list of files
        dataParams = new DataParams(filesListToProcess.length); //This object will contain parameters from all files that have been opened
        for ( DataTypesList frmt : DataTypesList.values()) frmt.getDataType().setDataParser(this);
        dataFormatsDetect = new DataFormatsDetect(this, mainApp); //infers data format of a current file

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
            {mainApp.getTextFileDataController().setData(i,sigCount);
                i++;
            continue;}

            DataTypesList.valueOf(formatName).getDataType().setData(i,sigCount);

            i++;

        }
    }

    /**
     * This method fills all the output arrays and will be invoked by a corresponding ADC-type class
     *
     * @param signal
     * a signal extracted from binary file by corresponding ADC-type-class
     * @param sigCount
     * an extracted signal's sequence number in the "signals" array
     * @param fNum
     * a number of a processed source file
     * @param sigAdcNum
     * an ADC channel number of extracted signal
     */

    public void setSignals(double [] signal, int sigCount, int fNum, int sigAdcNum) {
        this.signals[sigCount] = signal.clone();
        this.signalLabels[sigCount] = dataPaths.getFileName()[fNum]+ "_#"+sigAdcNum;
        this.signalFullPath[sigCount] = Paths.get(dataPaths.getDataFilePath()[fNum].getParent() +System.getProperty("file.separator")+ dataPaths.getFileName()[fNum] + "_#" + sigAdcNum);
        this.sigCount = sigCount;
        this.fileNumbers[sigCount] = fNum;
    }

    /**
     * The method saves avery signal as a separate text file to the "txt" subdirectory
     */
    public void saveToFile (){
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




    public void setDataParams(DataParams dataParams) {
        this.dataParams = dataParams;
    }

    public DataParams getDataParams() {
        return dataParams;
    }

    public void setDataPaths(DataPaths dataPaths) {
        this.dataPaths = dataPaths;
    }

    public DataPaths getDataPaths() {
        return dataPaths;
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

