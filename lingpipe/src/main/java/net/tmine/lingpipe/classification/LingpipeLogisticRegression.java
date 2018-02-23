package net.tmine.lingpipe.classification;

import com.aliasi.matrix.SparseFloatVector;
import com.aliasi.stats.AnnealingSchedule;
import com.aliasi.stats.LogisticRegression;
import com.aliasi.stats.RegressionPrior;
import java.io.IOException;
import java.util.Vector;
import net.tmine.classification.TextClassifier;
import net.tmine.entities.Document;
import net.tmine.entities.DocumentSet;
import net.tmine.utils.Serialization;


/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class LingpipeLogisticRegression implements TextClassifier {

    private LogisticRegression model;

    public LingpipeLogisticRegression(String file) throws ClassNotFoundException, IOException {
        model = (LogisticRegression) Serialization.deserialize(file);
    }

    @Override
    public String classify(Document d) {
        Vector<Double> features = d.getFeatures();
        int[] keys = new int[features.size()];
        float[] values = new float[features.size()];
        int dimensions = features.size();
        for (int j = 0; j < features.size(); j++) {
            keys[j] = j;
            values[j] = features.get(j).floatValue();
        }
        com.aliasi.matrix.Vector v = new SparseFloatVector(keys, values, dimensions);
        double[] prob = model.classify(v);
        int index = -1;
        double max = 0;
        for (int i = 0; i < prob.length; i++)
            if (max < prob[i]) {
                max = prob[i];
                index = i;
            }
        return "" + index;
    }
    
    @Override
    public void trainClassifier(DocumentSet col, String modelFile) throws IOException {
        com.aliasi.matrix.Vector[] xs = new com.aliasi.matrix.Vector[col.size()];
        int[] cs = new int[col.size()];
        RegressionPrior prior = RegressionPrior.gaussian(1.0, true);
        AnnealingSchedule annealingSchedule = AnnealingSchedule.exponential(0.00025, 0.999);
        double minImprovement = 0.000000001;
        int minEpochs = 100;
        int maxEpochs = 20000;
        for (int i = 0; i < col.size(); i++) {
            Document d = col.get(i);
            Vector<Double> features = d.getFeatures();
            int[] keys = new int[features.size()];
            float[] values = new float[features.size()];
            int dimensions = features.size();
            for (int j = 0; j < features.size(); j++) {
                keys[j] = j;
                values[j] = features.get(j).floatValue();
            }
            xs[i] = new SparseFloatVector(keys, values, dimensions);
            cs[i] = (int) (Double.parseDouble(d.getClassification()));
        }
        LogisticRegression lrmodel = LogisticRegression.estimate(xs, cs, prior, 
                annealingSchedule, null, minImprovement, minEpochs, maxEpochs);
        Serialization.serialize(lrmodel, modelFile);
    }


}
