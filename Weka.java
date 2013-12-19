import weka.attributeSelection.InfoGainAttributeEval;
// import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.*;
import weka.core.*;
import weka.core.converters.ConverterUtils.*;
import weka.classifiers.*;
import weka.filters.*;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.Evaluation;
import weka.classifiers.Classifier;
import java.util.Random;
import java.util.List;

public class Weka {
    private static Instances m_Data = null;
    private static StringToWordVector m_Filter = new StringToWordVector();
    private static AttributeSelection m_Attr_Filter = new AttributeSelection();
    private static Classifier m_Classifier = new NaiveBayes();

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
        m_Filter.input(sentence_instance);
        Instance s_Filtered_1 = m_Filter.output();
        m_Attr_Filter.input(s_Filtered_1);
        Instance s_Filtered_2 = m_Attr_Filter.output();


        // Get index of predicted value
        // double predicted = m_Classifier.classifyInstance(s_Filtered_2);
        double[] dist = m_Classifier.distributionForInstance(s_Filtered_2);
        double predicted = m_Classifier.classifyInstance(s_Filtered_2);


        System.out.println("---------------------------------------");
        System.out.println("Probabilities:");
        System.out.println("Horror: " + dist[1]);
        System.out.println("Other : " + dist[2]);


        System.out.println("Prediction value: " + predicted);
        System.out.println("Classified as : " +
               m_Data.classAttribute().value((int)predicted));
        System.out.println("---------------------------------------");
        
        // Experimental:
        // Check if horror when considering prior distribution
        if (dist[2] > dist[1]) {
            dist[2] = dist[2]*0.79;
            if (dist[1] > dist[2]) { // Change to horror
                System.out.println("Weighted classification: ");
                System.out.println("Weighted sentence classified as : " +
                       m_Data.classAttribute().value(1));
                System.out.println("---------------------------------------");
            }
        }

        // Evaluation eTest = new Evaluation(sentence_instance);
        // eTest.crossValidateModel(classifier, m_DataFiltered, 5,  new Random(1));
        // // eTest.crossValidateModel(classifier, , 5,  new Random(1));
        // String strSummary = eTest.toSummaryString();
        // System.out.println(strSummary);
    }

    public static void initializeModel() throws Exception{

        System.out.println("Initializing model...");
        DataSource source = new DataSource("data/sentences/sentences.arff");
        m_Data = source.getDataSet();
        // setting class attribute if the m_Data format does not provide this information
        // For example, the XRFF format saves the class attribute information as well
        if (m_Data.classIndex() == -1)
            // {dummy, horror, other}. Horror set (I think)
            m_Data.setClassIndex(1);

        // STWV
        String[] options = new String[7];
        options[0] = "-W";      // Keep 400 words
        options[1] = "100";
        options[2] = "-stopwords";      // use stoplist
        options[3] = "englishST.txt";
        options[4] = "-L";      // use lowercase tokens
        options[5] = "-stemmer";    // use stemming
        options[6] = "weka.core.stemmers.IteratedLovinsStemmer";
        m_Filter.setOptions(options);
        m_Filter.setInputFormat(m_Data);
        System.out.println("Applying STWV...");
        Instances dataFiltered = Filter.useFilter(m_Data, m_Filter);
        System.out.println("STWV applied!");

        
        // Attribute filter
        InfoGainAttributeEval eval = new InfoGainAttributeEval();
        Ranker search = new Ranker();
        // search.setThreshold(0)
        m_Attr_Filter.setEvaluator(eval);
        m_Attr_Filter.setSearch(search);
        m_Attr_Filter.setInputFormat(dataFiltered);
        // generate new data
        System.out.println("Applying attribute selection...");
        Instances newData = Filter.useFilter(dataFiltered, m_Attr_Filter);
        System.out.println("Attribute selection applied!");
        // Prints number arrays and their class attribute
        // System.out.println(newData);

        // Build classifier
        System.out.println("Building classifier from data...");
        m_Classifier.buildClassifier(newData);
        System.out.println("Finished building classifier!");

        // Evaluation eTest = new Evaluation(newData);
        // eTest.crossValidateModel(m_Classifier, newData, 5,  new Random(1));
        // String strSummary = eTest.toSummaryString();
        // System.out.println(strSummary);
        System.out.println("Model initialized!");
    }

    /**
    * Method that converts a text message into an instance.
    */
    private static Instance makeInstance(String text, Instances data) {
        // Create instance of length two.
        Instance instance = new Instance(2);

        Attribute messageAtt = data.attribute(0);
        instance.setValue(messageAtt, messageAtt.addStringValue(text));

        // Give instance access to attribute information from the dataset.
        instance.setDataset(data);
        return instance;
    }

}
