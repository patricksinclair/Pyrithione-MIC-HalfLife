import javax.xml.crypto.Data;

public class DataBox {

    private int[] popSizes;
    private double[] times;

    private double MIC;
    private double[] avg_times;
    private double[] avg_popSizes;

    public DataBox(double[] times, int[] popSizes){
        this.times = times;
        this.popSizes = popSizes;
    }


    public DataBox(double MIC, double[] avg_times, double[] avg_popSizes){
        this.MIC = MIC;
        this.avg_times = avg_times;
        this.avg_popSizes = avg_popSizes;
    }



    public double[] getTimes(){return times;}
    public int[] getPopSizes(){return popSizes;}

    public double getMIC(){return MIC;}
    public double[] getAvg_times(){return avg_times;}
    public double[] getAvg_popSizes(){return avg_popSizes;}
}
