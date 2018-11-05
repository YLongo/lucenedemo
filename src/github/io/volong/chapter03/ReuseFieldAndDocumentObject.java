package github.io.volong.chapter03;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class ReuseFieldAndDocumentObject {

    public static void main(String[] args) throws IOException {
        
        StandardAnalyzer analyzer = new StandardAnalyzer();
        RAMDirectory dir = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        
        Document doc = new Document();
        StringField stringField = new StringField("name", "Rose", Store.YES);
        
        String[] names = {"John", "Mary", "Peter"};
        for (String name : names) {
            stringField.setStringValue(name);
            doc.removeField("name");
            doc.add(stringField);
            indexWriter.addDocument(doc);
        }
        
        indexWriter.commit();
        
        DirectoryReader reader = DirectoryReader.open(dir);
        for (int i = 0; i < names.length; i++) {
            doc = reader.document(i);
            System.out.println("docid:" + i + ", name:" + doc.getField("name").stringValue());
        }
    }
}
