import org.apache.commons.math3.distribution.PoissonDistribution;

import javax.tools.Tool;
import java.util.Arrays;
import java.util.stream.IntStream;

public class BioSystem {

    private int N_alive;
    private double c, beta, tau, timeElapsed;
    private Microhabitat microhab;

    public BioSystem(int N_alive, double c, double beta, double tau){
        this.N_alive = N_alive;
        this.c = c;
        this.beta = beta;
        this.timeElapsed = 0.;
        this.tau = tau;
        microhab = new Microhabitat(N_alive, c, beta);
    }

    public double getTimeElapsed(){return timeElapsed;}

    public int getN_alive(){return microhab.getN_alive();}
    public int getN_dead(){return microhab.getN_dead();}



    public void performAction(){
        double tau_step = tau;
        int current_pop = getN_alive();
        int n_replications;
        int n_deaths;

        //only bother doing stuff if there's bacteria left alive
        if(current_pop > 0){

            whileloop:
            while(true){
                n_replications = new PoissonDistribution(current_pop*microhab.gRate()*tau_step).sample();
                n_deaths = 0;

                //only work out deaths if the antimicrobial concn is high enough
                if(microhab.lethalConcentration()) n_deaths = new PoissonDistribution(current_pop*Math.abs(microhab.dRate())*tau_step).sample();
                //avoid negative populations
                if(n_deaths > current_pop){
                    tau_step/=2.;
                    continue whileloop;
                }

                break whileloop;
            }

            int new_pop = current_pop + n_replications - n_deaths;
            microhab.setN_alive(new_pop);
            microhab.updateDeathCount(n_deaths);
        }

        timeElapsed += tau_step;
    }


    public void performActionIndividually(){
        double tau_step = tau;
        int current_pop = getN_alive();
        int n_replications;
        int n_deaths;

        if(current_pop > 0){

            whileloop:
            while(true){
                n_replications = 0;
                n_deaths = 0;

                for(int i = 0; i < current_pop; i++){
                    //replications
                    int indv_rep = new PoissonDistribution(microhab.gRate()*tau_step).sample();

                    int indv_dth = 0;
                    if(microhab.lethalConcentration()){
                        indv_dth = new PoissonDistribution(Math.abs(microhab.dRate())*tau_step).sample();
                        if(indv_dth > 0) indv_rep = 0;
                        if(indv_dth > 1){
                            tau_step/=2.;
                            continue whileloop;
                        }
                    }
                    n_replications += indv_rep;
                    n_deaths += indv_dth;
                }

                break whileloop;
            }
            int new_pop = current_pop + n_replications - n_deaths;
            microhab.setN_alive(new_pop);
            microhab.updateDeathCount(n_deaths);
        }
        timeElapsed += tau_step;
    }



    public static void varyingMIC(int N, double c){

        double minMIC = 1., maxMIC = 10.;
        int nMICsMeasured = 20;
        double MIC_increment = (maxMIC-minMIC)/nMICsMeasured;

        double duration = 24.;
        int nTimeMeasurements = 50;
        int nReps = 24;

        DataBox[] all_measurements = new DataBox[nMICsMeasured+1];
        String filename = "Pyrithione-MIC-varying-tau-rescaled";

        for(int i = 0; i <= nMICsMeasured; i++){
            double beta_i = minMIC + i*MIC_increment;

            all_measurements[i] = BioSystem.varyingMICSubroutine(nReps, duration, nTimeMeasurements, N, c, beta_i);
        }


        Toolbox.writeDataboxArrayToFile(filename, all_measurements);


    }


    public static DataBox varyingMICSubroutine(int nReps, double duration, int nMeasurements, int N, double c, double beta){
        //this performs several reps of the biosystem for a given duration at a given c and beta
        //it returns an array containing the population over time, sampled at regular intervals

        DataBox[] sub_results = new DataBox[nReps];

        double[][] collated_times = new double[nReps][];
        double[][] collated_popSizes = new double[nReps][];

        IntStream.range(0, nReps).parallel().forEach(i -> sub_results[i] = BioSystem.varyingMICSubSubroutine(duration, nMeasurements, N, c, beta, i));

        for(int i = 0; i < nReps; i++){
            collated_times[i] = sub_results[i].getTimes();
            //System.out.println(Arrays.toString(collated_times[i]));
            collated_popSizes[i] = Toolbox.convertIntsToDoubles(sub_results[i].getPopSizes());
        }

        double[] avg_times = Toolbox.averagedResults(collated_times);
        double[] avg_popSizes = Toolbox.averagedResults(collated_popSizes);

        return new DataBox(beta, avg_times, avg_popSizes);

    }


    public static DataBox varyingMICSubSubroutine(double duration, int nMeasurements, int N, double c, double beta, int rep){

        double tau = 0.001;
        double t_interval = duration/nMeasurements;
        System.out.println(t_interval);
        //System.exit(8);
        double[] times = new double[nMeasurements+1];
        int[] pop_sizes = new int[nMeasurements+1];

        BioSystem bs = new BioSystem(N, c, beta, tau);
        boolean alreadyRecorded = false;
        int measurement_counter = 0;


        while(bs.getTimeElapsed() <= duration+(0.1*t_interval)){

            if(bs.getTimeElapsed()%t_interval < 1.e-3 && !alreadyRecorded){

                System.out.println("\nMIC: "+beta+"\trep: "+rep+"\ttime: "+bs.getTimeElapsed()+"\tN alive: "+bs.getN_alive());

                times[measurement_counter] = bs.getTimeElapsed();
                pop_sizes[measurement_counter] = bs.getN_alive();
                measurement_counter++;
                alreadyRecorded = true;
            }
            //System.out.println("time: "+bs.getTimeElapsed()+"\t"+bs.getTimeElapsed()%t_interval);
            if(bs.getTimeElapsed()%t_interval > 0.1*t_interval) alreadyRecorded = false;

            bs.performAction();
        }
        //System.out.println("t interval: "+t_interval);

        return new DataBox(times, pop_sizes);
    }








}
