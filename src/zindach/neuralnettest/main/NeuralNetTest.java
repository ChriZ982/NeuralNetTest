//package zindach.neuralnettest.main;
//
//import zindach.neuralnetlib.main.NeuralNetwork;
//import zindach.neuralnetlib.main.NeuronCalcFunction;
//
//public class NeuralNetTest {    
//    
//    private static final int ITERATIONS = 2;
//
//    private static final int IN = 2;
//    private static final int OUT = 1;
//    private static final int HIDDEN = 3;
//    private static final int LAYERS = 1;
//    private static final double BIAS = 0;
//    private static final NeuronCalcFunction FUNC = NeuronCalcFunction.SIGMOID;
//
//    public static void main(String[] args) {
//        NeuralNetwork nn = new NeuralNetwork(IN, OUT, HIDDEN, LAYERS, BIAS, FUNC);
//        long time = 0;
//        for (int i = 0; i < ITERATIONS; i++) {
//            double[] in = new double[IN];
//            for (int j = 0; j < IN; j++) {
//                in[j] = Math.random();
//                System.out.print(in[j]+ " ");
//            }
//            time -= System.nanoTime();
//            nn.recalculate(in);
//            time += System.nanoTime();
//            nn.print();
//        }
//        System.out.println("Took: " + time/1000000 + "ms");
//        Frame frame = new Frame();
//    }
//
//}
