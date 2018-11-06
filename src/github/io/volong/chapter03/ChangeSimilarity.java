package github.io.volong.chapter03;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class ChangeSimilarity {

    public static void main(String[] args) throws IOException {
        
        Analyzer analyzer = new StandardAnalyzer();
        RAMDirectory dir = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        
        MySimilarity mySimilarity = new MySimilarity(new DefaultSimilarity());
        config.setSimilarity(mySimilarity);
        
        IndexWriter indexWriter = new IndexWriter(dir, config);
        
        Document doc = new Document();
        TextField textField = new TextField("name", "", Store.YES);
        
        NumericDocValuesField rankingField = new NumericDocValuesField("ranking", 1);
        
        long ranking = 1L;
        
        String[] names = {"John R Smith", "Mary Smith", "Peter Smith"};
        for (String name : names) {
            ranking *= 2;
            textField.setStringValue(name);
            rankingField.setLongValue(ranking);
            doc.removeField("name");
            doc.removeField("ranking");
            doc.add(textField);
            doc.add(rankingField);
            indexWriter.addDocument(doc);
        }
        indexWriter.commit();
        
        DirectoryReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(mySimilarity);
        
        TermQuery termQuery = new TermQuery(new Term("name", "smith"));
        TopDocs topDocs = searcher.search(termQuery, 100);
        
        System.out.println("-----searching smith-----");
        
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document document = reader.document(scoreDoc.doc);
            System.out.println(document.getField("name").stringValue());
        }
    }
}
