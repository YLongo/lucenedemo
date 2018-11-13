package github.io.volong.chapter04;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

public class UseFieldCache {

    public static void main(String[] args) throws IOException {
        
        StandardAnalyzer analyzer = new StandardAnalyzer();
        RAMDirectory dir = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        
        Document doc = new Document();
        StringField stringField = new StringField("name", "", Store.YES);
        String[] contents = {"alpha", "bravo", "charlie", "delta", "echo", "foxtrot"};
        
        for (String content : contents) {
            stringField.setStringValue(content);
            doc.removeField("name");
            doc.add(stringField);
            indexWriter.addDocument(doc);
        }
        
        indexWriter.commit();
        
        IndexReader reader = DirectoryReader.open(dir);
        BinaryDocValues cache = FieldCache.DEFAULT.getTerms(SlowCompositeReaderWrapper.wrap(reader), "name", true);
        
        for (int i = 0; i < reader.maxDoc(); i++) {
            BytesRef bytesRef = cache.get(i);
            System.out.println(i + ":" + bytesRef.utf8ToString());
        }
    }
}
