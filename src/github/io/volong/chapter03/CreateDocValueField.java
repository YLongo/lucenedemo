package github.io.volong.chapter03;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.SortedDocValues;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.apache.lucene.util.ByteBlockPool.DirectAllocator;

public class CreateDocValueField {

    public static void main(String[] args) throws IOException {
        
        
        StandardAnalyzer analyzer = new StandardAnalyzer();
        RAMDirectory dir = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        
        Document doc = new Document();
        
        doc.add(new SortedDocValuesField("sorted_string", new BytesRef("hello")));
        indexWriter.addDocument(doc);
        
        doc = new Document();
        doc.add(new SortedDocValuesField("sorted_string", new BytesRef("world")));
        indexWriter.addDocument(doc);
        
        indexWriter.commit();
        indexWriter.close();
        
        // ----------------------
        
        DirectoryReader reader = DirectoryReader.open(dir);
        Document doc0 = reader.document(0);
        System.out.println("doc_0:" + doc0.toString());
        
        Document doc1 = reader.document(1);
        System.out.println("doc_1:" + doc1.toString());
        
        for (AtomicReaderContext context : reader.leaves()) {
            AtomicReader atomicReader = context.reader();
            SortedDocValues sorted = DocValues.getSorted(atomicReader, "sorted_string");
            System.out.println("Value count:" + sorted.getValueCount());
            
            System.out.println("doc_0 sorted_string:" + sorted.get(0).utf8ToString());
            
            System.out.println("doc_1 sorted_string:" + sorted.get(1).utf8ToString());
        }
        
        reader.close();
    }
}
