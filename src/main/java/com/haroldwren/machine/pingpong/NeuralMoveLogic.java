package com.haroldwren.machine.pingpong;

import com.haroldwren.machine.PAppletProxy;
import org.encog.ml.MLRegression;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.NEATPopulation;
import org.encog.neural.neat.PersistNEATPopulation;
import org.encog.neural.networks.BasicNetwork;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import static processing.core.PApplet.dist;
import static processing.core.PApplet.norm;

/**
 * Created by jack on 2017.04.09..
 */
public class NeuralMoveLogic {
    private final static int DRAW_WIDTH = PAppletProxy.getInstance(null).getWidth(),
            DRAW_HEIGHT = PAppletProxy.getInstance(null).getHeight();
    private NEATNetwork network;
    private static final String networkFileName = "file/temp/perfect-pong01.eg";
    private final static double SENSE = DRAW_HEIGHT /2;
    private static final float MOVE_STEP = 10f;

    /**
     * create neuralMoveLogic
     */
    public NeuralMoveLogic() {
        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(networkFileName);
        if(null!= resource) {
            File networkFile = new File(resource.getFile());
            PersistNEATPopulation pnp = new PersistNEATPopulation();
            try {
                InputStream inputStream = new FileInputStream(networkFile);
                NEATPopulation pop = (NEATPopulation) pnp.read(inputStream);
                network = (NEATNetwork)pop.getCODEC().decode(pop.getBestGenome());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * do the logic
     *
     * @param bat
     * @param ball
     * @param network
     */
    public void doNeuralLogic(Bat bat, Ball ball, MLRegression network) {
        Float batYPoint = bat.getyPoint();
        Float batXPoint = bat.getyPoint();
        Float ballPreviousXPoint = ball.getxPreviousPoint();
        Float ballPreviousYPoint = ball.getyPreviousPoint();
        Float ballXPoint = ball.getxPoint();
        Float ballYPoint = ball.getyPoint();

        double distanceFromP = dist(ballXPoint, ballYPoint, bat.getxPoint(), batYPoint);

        //float distanceX = ballXPoint - bat.getxPoint();
        //float distanceY = ballYPoint - batYPoint;
        //double distance = Math.sqrt(Math.pow(distanceX, 2)+Math.pow(distanceY, 2));
        if(distanceFromP>SENSE) {
            ballXPoint = 0f;
            ballYPoint = 0f;
            ballPreviousYPoint = 0f;
            ballPreviousXPoint = 0f;
        }
        Float normBatYPoint = norm(batYPoint, 0, DRAW_HEIGHT);
        Float normBatXPoint = norm(batXPoint, 0, DRAW_WIDTH);
        Float normBallPreviousXPoint = norm(ballPreviousXPoint, 0, DRAW_WIDTH);
        Float normBallPreviousYPoint = norm(ballPreviousYPoint, 0, DRAW_HEIGHT);
        Float normBallXPoint = norm(ballXPoint, 0, DRAW_WIDTH);
        Float normBallYPoint = norm(ballYPoint, 0, DRAW_HEIGHT);

        double[] neuralInput = {normBatYPoint, normBallXPoint, normBallYPoint, normBallPreviousYPoint,1};
        //double[] neuralInput = {normBatYPoint, normBatXPoint, normBallXPoint, normBallYPoint, normBallPreviousYPoint};
        BasicMLData input = new BasicMLData(neuralInput);

        MLData output;
        if(network != null) {
            if(network.getClass() == NEATNetwork.class) {
                output = this.compute((NEATNetwork) network, input);
            } else {
                output = this.compute((BasicNetwork) network, input);
            }
        } else {
            output = this.compute(this.network, input);
        }
        double neuralOuput = output.getData(0);
        if(neuralOuput<0.33) {
            bat.move(MOVE_STEP);
        } else if(neuralOuput > 0.66){
            bat.move(-MOVE_STEP);
        }
    }

    /**
     * compute output, for input data
     *
     * @param network
     * @param input
     * @return
     */
    private MLData compute(NEATNetwork network,BasicMLData input) {
        return network.compute(input);
    }

    private MLData compute(BasicNetwork network,BasicMLData input) {
        return network.compute(input);
    }

}
