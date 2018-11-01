package github.io.volong.chapter03;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class CreateTextField {

    public static void main(String[] args) throws IOException {
        
        FSDirectory dir = FSDirectory.open(new File("indexPath"));
        
        StandardAnalyzer analyzer = new StandardAnalyzer();
        
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        
        IndexWriter indexWriter = new IndexWriter(dir, config);
        
        Document doc = new Document();
        
        String text = "Lucene is an Information Retrieval library written in Java.";
        
        doc.add(new TextField("text", text, Store.YES));
        
        indexWriter.addDocument(doc);
        
        indexWriter.commit();
    }
}
