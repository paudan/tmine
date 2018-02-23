package net.tmine.lingpipe.sentiment;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.classify.LMClassifier;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.stats.MultivariateEstimator;
import com.aliasi.util.AbstractExternalizable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;
import net.tmine.entities.Document;
import net.tmine.entities.DocumentSet;
import net.tmine.entities.Sentence;

/**
 * @author Reinald Kim Amplayo & Min Song
 */
public class LingpipeSentiment {

    private LMClassifier<NGramProcessLM, MultivariateEstimator> mClassifier;

    @SuppressWarnings("unchecked")
    public LingpipeSentiment(String model) throws Exception {
        mClassifier = (LMClassifier<NGramProcessLM, MultivariateEstimator>) AbstractExternalizable.readObject(new File(model));
    }

    public double getSentiment(Sentence s) {
        String sentence = s.getSentence();
        Classification classification = mClassifier.classify(sentence);
        return Double.parseDouble(classification.bestCategory());
    }

    public void createLingpipeSentimentModel(DocumentSet col) throws Exception {
        Vector<String> comment = new Vector<>();
        Vector<String> polarity = new Vector<>();
        for (int i = 0; i < col.size(); i++) {
            Document doc = col.get(i);
            comment.add(doc.getDocument());
            polarity.add(doc.getClassification());
        }
        String[] mCategories = {"0.0", "1.0"};
        DynamicLMClassifier<NGramProcessLM> mClassifier = DynamicLMClassifier.createNGramProcess(mCategories, 8);
        for (int i = 0; i < col.size(); i++) {
            CharSequence document = comment.get(i);
            Classification classification = new Classification("" + polarity.get(i));
            Classified<CharSequence> classified = new Classified<>(document, classification);
            mClassifier.handle(classified);
        }
        FileOutputStream fos = new FileOutputStream("model/lingpipe/sentiment.model");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        mClassifier.compileTo(oos);
        oos.close();
        fos.close();
    }

}
