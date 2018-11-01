package github.io.volong.chapter03;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class CreateStringField {

    public static void main(String[] args) throws IOException {
        
        FSDirectory dir = FSDirectory.open(new File("indexPath"));
        
        StandardAnalyzer analyzer = new StandardAnalyzer();
        
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        
        IndexWriter indexWriter = new IndexWriter(dir, config);
        
        Document document = new Document();
        
        StringField telphoneNo = new StringField("telephone_number", "12345678912", Store.YES);
        document.add(telphoneNo);
        
        StringField areaCode = new StringField("area_code", "", Store.YES);
        document.add(areaCode);
        
        indexWriter.addDocument(document);
        
        
    }
}
