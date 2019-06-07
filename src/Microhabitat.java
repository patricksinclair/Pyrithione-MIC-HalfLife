public class Microhabitat {

    private int N_alive;
    private double c, beta;

    int N_dead;


    public Microhabitat(int N_alive, double c, double beta){
        this.N_alive = N_alive;
        this.c = c;
        this.beta = beta;

        this.N_dead = 0;
    }



    public int getN(){return N_alive + N_dead;}
    public int getN_alive(){return N_alive;}
    public void setN_alive(int N_alive){this.N_alive = N_alive;}
    public int getN_dead(){return N_dead;}
    public double getC(){
        return c;
    }

    public void updateDeathCount(int death_toll){this.N_dead += death_toll;}

    public boolean lethalConcentration(){
        return c > beta();
    }

    public void replicateABActerium(){N_alive++;}

    public void killABacterium(){
        if(N_alive > 0){
            N_alive--;
            N_dead++;
        }
    }

    public double beta(){
        return beta;
    }


    // set the growth rate to consistently be 0.1 here.
    // this is slightly different to the nutrients sims where phi(c) determined
    // growth and death rates
    public double gRate(){
        return 0.083;
    }


    public double dRate(){
        double cB = c/beta();
        double phi_c = 1. - (6.*cB*cB)/(5. + cB*cB);
        return 0.083*phi_c;
    }

    public double phi_c(){
        double cB = c/beta();
        return 1. - (6.*cB*cB)/(5. + cB*cB);
    }

    public double replicationOrDeathRate(){
        double phi_c_scaled = 0.083*(phi_c());
        double return_val2 = (phi_c() > 0.) ? gRate() : phi_c_scaled;
        return return_val2;
    }


















}
