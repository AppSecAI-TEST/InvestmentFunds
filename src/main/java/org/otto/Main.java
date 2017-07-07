package org.otto;

import org.otto.simulator.Simulator;

/**
 * Created by tomek on 2016-10-07.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        final String inputFileName = (args.length >= 1) ? args[0] : null;
        final String outputFileName = (args.length >= 2) ? args[1] : "out.xml";
        final int times = (args.length >= 3) ? Integer.valueOf(args[2]) : 20;

        Simulator simulator = new Simulator();
        simulator.runSimulation(times, inputFileName, outputFileName);

        System.out.println("Done!");
        System.exit(0);
    }

}
