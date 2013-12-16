import weka.attributeSelection.*;
import weka.core.*;
import weka.core.converters.ConverterUtils.*;
import weka.classifiers.*;
import weka.classifiers.meta.*;
import weka.classifiers.trees.*;
import weka.filters.*;

public class Weka {
    public static void main(String[] args) throws Exception{
        DataSource source = new DataSource("horror_other.arff");
        Instances data = source.getDataSet();
        // setting class attribute if the data format does not provide this information
        // For example, the XRFF format saves the class attribute information as well
        if (data.classIndex() == -1)
            data.setClassIndex(0);

        // TODO: Fix code that works. STWV code copied from example.
        StringToWordVector filter = new StringToWordVector();
        filter.setInputFormat(data);
        Instances dataFiltered = Filter.useFilter(data, filter);
        System.out.println("\n\nFiltered data:\n\n" + dataFiltered);

        // train J48 and output model
        J48 classifier = new J48();
        classifier.buildClassifier(dataFiltered);
        System.out.println("\n\nClassifier model:\n\n" + classifier);
    }
}
