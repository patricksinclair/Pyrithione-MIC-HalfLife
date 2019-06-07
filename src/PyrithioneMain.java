import java.util.Arrays;

public class PyrithioneMain {

    //this is the program used to vary the MIC in order to determine the mean for the log-normal distribution
    //this is being redone here with scaled growth and death rates.


    public static void main(String[] args){
        int N = (int)6e6;
        double c = 10.;

        //System.out.println("bla");
        //BioSystem.popSizeOverTime_v2(N, c);
        System.out.println("testo");
        BioSystem.varyingMIC(N, c);

        /*double x = 1234567.;
        String o = String.format("%-12.4E", x)+"g";
        System.out.println(o);
        int string_length = 10;
        String t_string = "#time";
        String header = String.format("%-"+string_length+"s", t_string)+"g";
        System.out.println(header);*/
    }
}