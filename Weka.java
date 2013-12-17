import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.AttributeSelection;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.attributeSelection.Ranker;
import weka.core.*;
import weka.core.converters.ConverterUtils.*;
import weka.classifiers.*;
import weka.classifiers.meta.*;
import weka.classifiers.trees.*;
import weka.filters.*;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.Evaluation;
import weka.classifiers.Classifier;
import java.util.Random;
import java.util.List;

public class Weka {

    private static Instances m_Data = null;
    private static StringToWordVector m_Filter = new StringToWordVector();
    private static AttributeSelectedClassifier m_Classifier = new AttributeSelectedClassifier();

    public static void main(String[] args) throws Exception {
        initializeModel();
        String sentence;
        while(true) {
            sentence = System.console().readLine("Enter a sentence: ");
            classify(sentence);
        }

    }

    public static void classify(String sentence)  throws Exception {
        // Create a separate test set for the sentence
        Instances testset = m_Data.stringFreeStructure();
        Instance sentence_instance = makeInstance(sentence, testset);

        // Get index of predicted value
        double predicted = m_Classifier.classifyInstance(sentence_instance);

        // Output class value.
        System.out.println("Sentence classified as : " +
               m_Data.classAttribute().value((int)predicted));

        // Evaluation eTest = new Evaluation(sentence_instance);
        // eTest.crossValidateModel(classifier, m_DataFiltered, 5,  new Random(1));
        // // eTest.crossValidateModel(classifier, , 5,  new Random(1));
        // String strSummary = eTest.toSummaryString();
        // System.out.println(strSummary);
    }

    public static void initializeModel() throws Exception{

        DataSource source = new DataSource("data/sentences/sentences.arff");
        m_Data = source.getDataSet();
        // setting class attribute if the m_Data format does not provide this information
        // For example, the XRFF format saves the class attribute information as well
        if (m_Data.classIndex() == -1)
            // {dummy, horror, other}. Horror set (I think)
            m_Data.setClassIndex(1);



        // STWV
        String[] options = new String[4];
        options[0] = "-W";      // Keep 200 words
        options[1] = "200";
        options[2] = "-S";      // use stoplist
        options[3] = "1";
        m_Filter.setOptions(options);
        m_Filter.setInputFormat(m_Data);
        Instances dataFiltered = Filter.useFilter(m_Data, m_Filter);

        
        // Meta-classifier. Attribute selection before presented to classifier.
        AttributeSelectedClassifier m_Classifier = new AttributeSelectedClassifier();
        InfoGainAttributeEval eval = new InfoGainAttributeEval();
        Ranker search = new Ranker();
        NaiveBayes nb = new NaiveBayes();
        m_Classifier.setClassifier(nb);
        m_Classifier.setEvaluator(eval);
        m_Classifier.setSearch(search);
        m_Classifier.buildClassifier(dataFiltered);

        Evaluation eTest = new Evaluation(dataFiltered);
        eTest.crossValidateModel(m_Classifier, dataFiltered, 5,  new Random(1));
        // String strSummary = eTest.toSummaryString();
        // System.out.println(strSummary);
    }

    /**
    * Method that converts a text message into an instance.
    */
    private static Instance makeInstance(String text, Instances data) {
        System.out.println("Sentence: " + text);

        // Create instance of length two.
        Instance instance = new Instance(3);

        // Set value for message attribute
        Attribute messageAtt = data.attribute(0);
        instance.setValue(messageAtt, messageAtt.addStringValue(text));

        // Give instance access to attribute information from the dataset.
        instance.setDataset(data);
        return instance;
    }

}
