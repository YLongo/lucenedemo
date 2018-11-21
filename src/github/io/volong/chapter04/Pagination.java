package github.io.volong.chapter04;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class Pagination {

    public static void main(String[] args) throws IOException, ParseException {
        
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        RAMDirectory dir = new RAMDirectory();
        
        IndexWriter indexWriter = new IndexWriter(dir, config);
        
        Document doc = new Document();
        TextField textField = new TextField("content", "", Store.YES);
        
        String[] contents = {"Humpty Dumpty sat on a wall,",
                "Humpty Dumpty had a great fall.",
                "All the king's horses and all the king's men",
                "Couldn't put Humpty together again."};
        
        for (String content : contents) {
            doc.removeField("content");
            textField.setStringValue(content);
            doc.add(textField);
            indexWriter.addDocument(doc);
        }
        
        indexWriter.commit();
        
        DirectoryReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        
        QueryParser queryParser = new QueryParser("content", analyzer);
        Query query = queryParser.parse("humpty dumpty");
        
        TopDocs topDocs = searcher.search(query, 2);
        
        System.out.println("total hits:" + topDocs.totalHits);
        
        int page = 1;
        ScoreDoc lastScoreDoc = null;
        
        while (true) {
            
            System.out.println("page:" + page);
            
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                doc = reader.document(scoreDoc.doc);
                System.out.println(scoreDoc.score + ":" + doc.getField("content").stringValue());
                lastScoreDoc = scoreDoc;
            }
            
            topDocs = searcher.searchAfter(lastScoreDoc, query, 2);
            if (topDocs.scoreDocs.length == 0) {
                break;
            }
            page ++;
        }
        
    }
}
