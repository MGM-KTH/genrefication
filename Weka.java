import weka.attributeSelection.*;
import weka.core.*;
import weka.core.converters.ConverterUtils.*;
import weka.classifiers.*;
import weka.classifiers.meta.*;
import weka.classifiers.trees.*;
import weka.filters.*;

public class Weka {
    
    public static void main(String[] args) throws Exception{
     DataSource source = new DataSource("data/horror_other.arff");
     Instances data = source.getDataSet();
     // setting class attribute if the data format does not provide this information
     // For example, the XRFF format saves the class attribute information as well
     if (data.classIndex() == -1)
       data.setClassIndex(0);
    }
}