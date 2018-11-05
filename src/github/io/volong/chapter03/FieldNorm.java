package github.io.volong.chapter03;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class FieldNorm {

    public static void main(String[] args) throws IOException {
        
        StandardAnalyzer analyzer = new StandardAnalyzer();
        RAMDirectory dir = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, config);
        
        Document doc = new Document();
        final TextField textField = new TextField("name", "", Store.YES);
        doc.add(textField);
        
        float boost = 1f;
        String[] names = {"John R Smith", "Mary Smith", "Peter Smith"};
        
        for (String name : names) {
            boost *= 1.1;
            textField.setStringValue(name);
            textField.setBoost(boost);
            doc.removeField("name");
            doc.add(textField);
            indexWriter.addDocument(doc);
        }

        indexWriter.commit();
        
        DirectoryReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        Query termQuery = new TermQuery(new Term("name", "smith"));
        
        TopDocs topDocs = searcher.search(termQuery, 100);
        System.out.println("search smith");
        
        for (ScoreDoc d : topDocs.scoreDocs) {
            doc = reader.document(d.doc);
            System.out.println(doc.getField("name").stringValue());
        }
    }
}
