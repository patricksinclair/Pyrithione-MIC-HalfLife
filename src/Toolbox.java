import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Toolbox {

    public static double[] convertIntsToDoubles(int[] source){
        double[] result = new double[source.length];
        for(int i = 0; i < source.length; i++){
            result[i] = source[i];
        }
        return result;
    }



    public static double averageOfArrayList(ArrayList<Double> listo){

        if(listo.size() > 0) {
            double sum = 0.;

            for(Double d : listo) {
                sum += d;
            }

            return sum/(double) listo.size();
        }else{
            return 0.;
        }
    }

    public static double[] averageAndStDevOfArray(double[] results){
        double sum = 0.;
        for(double d : results){
            sum += d;
        }
        double mean = sum/results.length;

        double sumSq = 0.;
        for(double d : results){
            sumSq +=(d-mean)*(d-mean);
        }
        double stDev = Math.sqrt(sumSq/(results.length - 1.));

        return new double[]{mean, stDev};
    }


    public static double stDevOfArrayList(ArrayList<Double> listo){

        if(listo.size() > 0) {
            double mean = Toolbox.averageOfArrayList(listo);
            double sumSq = 0.;

            for(Double d : listo) {
                sumSq += (d-mean)*(d-mean);
            }
            return Math.sqrt(sumSq/(listo.size()-1));
        }else{
            return 0.;
        }
    }

    public static double[] averagedResults(double[][] inputData){

        int nReps = inputData.length;
        int nMeasurements = inputData[0].length;

        double[] averagedResults = new double[nMeasurements];

        //iterate over the counters, checking all the reps, then moving to next counter
        for(int c = 0; c < nMeasurements; c++){
            double runningTotal = 0.;
            for(int r = 0; r < nReps; r++){
                runningTotal += inputData[r][c];
            }
            averagedResults[c] = runningTotal/nReps;
        }
        return averagedResults;
    }

    //modified this to handle zeroes
    public static double[][] averagedResults(double[][][] inputData){

        int nReps = inputData.length;
        int nTimes = inputData[0].length;
        int L = inputData[0][0].length;

        double[][] averagedResults = new double[nTimes][L];

        for(int t = 0; t < nTimes; t++){

            for(int l = 0; l < L; l++){

                double runningTotal = 0.;
                int repCounter = 0;
                for(int r = 0; r < nReps; r++){
                    if(inputData[r][t][l]!=0){
                        runningTotal += inputData[r][t][l];
                        repCounter++;
                    }

                }
                averagedResults[t][l] = (repCounter > 0) ? runningTotal/repCounter : 0.;
            }
        }
        return averagedResults;
    }



    public static void writeHistoArrayToFile(String filename, int[] inputData){
        try {
            File file = new File(filename+".txt");
            if(!file.exists()) file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            int nReadings = inputData.length;

            for(int i = 0; i < nReadings-1; i++){

                String output = String.format("%d", inputData[i]);

                bw.write(output);
                bw.newLine();
            }
            String output = String.format("%d", inputData[nReadings-1]);
            bw.write(output);
            bw.close();
        }catch (IOException e){}
    }


    public static void writeAveragedDistbsToFile(String filename, double[][] inputData){

        try {
            File file = new File(filename+".txt");
            if(!file.exists()) file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            int nTimes = inputData.length;
            int L = inputData[0].length;

            for(int l = 0; l < L; l++){
                String output = String.valueOf(l)+" ";
                for(int t = 0; t < nTimes; t++){
                    output += String.format("%.6f ", inputData[t][l]);
                }
                bw.write(output);
                bw.newLine();
            }
            bw.close();
        }catch (IOException e){}
    }




    public static void writeAveragedArrayToFile(String filename, double[] data){

        try{
            File file = new File(filename+".txt");
            if(!file.exists()) file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            String headerString = "#";
            String dataString = "";

            for(int i = 0; i < data.length; i++){
                bw.write(String.valueOf(data[i]));
                bw.newLine();
            }
            bw.close();

        }catch (IOException e){}

    }


    public static void writeThreeArraysToFile(String filename, double[] arr_a, double[] arr_b, double[] arr_c){

        try{
            File file = new File(filename+".txt");
            if(!file.exists()) file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);


            for(int i = 0; i < arr_a.length; i++){
                String output = String.format("%.3f, %.3f, %.3f", arr_a[i], arr_b[i], arr_c[i]);
                bw.write(output);
                bw.newLine();
            }
            bw.close();

        }catch (IOException e){}

    }


    public static void writeMultipleColumnsToFile(String filename, double[][] results){

        try{
            File file = new File(filename+".txt");
            if(!file.exists()) file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            int ncols = results.length;


            for(int i = 0; i < results[0].length; i++){

                String output = "";

                for(int nc = 0; nc < ncols-1; nc++){
                    output += String.format("%.4f, ", results[nc][i]);
                }
                output += String.format("%.4f", results[ncols-1][i]);

                bw.write(output);
                bw.newLine();
            }
            bw.close();

        }catch (IOException e){}
    }



    public static void writeDataboxArrayToFile(String filename, DataBox[] data_boxes){

        try{
            File file = new File(filename+".txt");
            if(!file.exists()) file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            int string_length = 12;
            ////////////////// ALL THIS TO WRITE A HEADER ////////////////////
            String t_string = "#time, ";
            String header = String.format("%-"+string_length+"s", t_string);
            for(int i = 0; i < data_boxes.length-1; i++){
                String c_string = "MIC="+String.format("%.4f",data_boxes[i].getMIC())+", ";
                header += String.format("%-"+string_length+"s", c_string);
            }
            String c_string = "MIC="+data_boxes[data_boxes.length-1].getMIC();
            header += String.format("%-"+string_length+"s", c_string);
            bw.write(header);
            bw.newLine();
            /////////////////////////////////////////////////////////////////

            for(int t = 0; t < data_boxes[0].getAvg_times().length; t++){

                String t_string_col = String.format("%.4E", data_boxes[0].getAvg_times()[t])+",";
                String output = String.format("%-"+string_length+"s", t_string_col);

                for(int MIC = 0; MIC < data_boxes.length-1; MIC++){
                    String pop_string = String.format("%.4E", data_boxes[MIC].getAvg_popSizes()[t])+",";
                    output += String.format("%-"+string_length+"s", pop_string);
                }
                String pop_string = String.format("%.4E", data_boxes[data_boxes.length-1].getAvg_popSizes()[t]);
                output += String.format("%-"+string_length+"s", pop_string);

                bw.write(output);
                bw.newLine();
            }

            bw.close();

        }catch (IOException e){}
    }



    public static String millisToShortDHMS(long duration) {
        String res = "";
        long days  = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration)
                - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        if (days == 0) {
            res = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        else {
            res = String.format("%dd%02d:%02d:%02d", days, hours, minutes, seconds);
        }
        return res;
    }


}
