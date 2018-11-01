package github.io.volong.chapter03;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.FieldType.NumericType;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;

public class CreateNumericField {

    public static void main(String[] args) {
        
        IntField intField = new IntField("int_value", 100, Store.YES);
        
        LongField longField = new LongField("long_value", 100L, Store.YES);
        
        FloatField floatField = new FloatField("float_value", 100.0F, Store.YES);
        
        DoubleField doubleField = new DoubleField("double_value", 100.0D, Store.YES);
        
        FieldType fieldType = new FieldType();
        fieldType.setNumericType(NumericType.INT);
        fieldType.setNumericPrecisionStep(Integer.MAX_VALUE);
        fieldType.setStored(false);
        fieldType.setIndexed(true);

        IntField intFieldSort = new IntField("int_value_sort", 100, fieldType);
        
        Document doc = new Document();
        doc.add(intField);
        doc.add(longField);
        doc.add(floatField);
        doc.add(doubleField);
        doc.add(intFieldSort);
        
        
    }
}
