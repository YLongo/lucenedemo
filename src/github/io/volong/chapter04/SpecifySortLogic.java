package github.io.volong.chapter04;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class SpecifySortLogic {

    /**
     * 
     * 
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        RAMDirectory dir = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        
        IndexWriter indexWriter = new IndexWriter(dir, config);
        
        Document doc = new Document();
        StringField stringField = new StringField("name", "", Store.YES);
        
        String[] contents = {"foxtrot", "echo", "delta", "charlie", "bravo", "alpha"};
        
        for (String content : contents) {
            stringField.setStringValue(content);
            doc.removeField("name");
            doc.add(stringField);
            indexWriter.addDocument(doc);
        }
        
        indexWriter.commit();
        
        
        DirectoryReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        WildcardQuery query = new WildcardQuery(new Term("name", "*"));
        
        SortField sortField = new SortField("name", SortField.Type.STRING);
        Sort sort = new Sort(sortField);
        TopFieldDocs topDocs = searcher.search(query, null, 100, sort);
//        TopDocs topDocs = searcher.search(query, 100);
        
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            doc = reader.document(scoreDoc.doc);
            System.out.println(doc.getField("name").stringValue() + " : " + scoreDoc.score);
        }
    }
}
