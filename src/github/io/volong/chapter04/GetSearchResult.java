package github.io.volong.chapter04;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
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

public class GetSearchResult {

	public static void main(String[] args) throws IOException, ParseException {
		
		StandardAnalyzer analyzer = new StandardAnalyzer();
		RAMDirectory dir = new RAMDirectory();
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
		IndexWriter indexWriter = new IndexWriter(dir, config);
		
		Document doc = new Document();
		TextField textField = new TextField("content", "", Store.YES);
		
		String[] contents = {"Humpty Dumpty sat on a wall,",
				"Humpty Dumpty had a great fall.",
				"All the king's horses and all the king's men",
				"Couldn't put Humpty together again."};
		
		
		for (String content : contents) {
			textField.setStringValue(content);
			doc.removeField("content");
			doc.add(textField);
			indexWriter.addDocument(doc);
		}
		
		indexWriter.commit();
		
		// 获取文件中的索引
		DirectoryReader reader = DirectoryReader.open(dir);
		// 新建一个搜索器
		IndexSearcher searcher = new IndexSearcher(reader);
		// 新建一个查询
		QueryParser queryParser = new QueryParser("content", analyzer);
		Query query = queryParser.parse("humpty dumpty");
		// 获取查询结果
		TopDocs topDocs = searcher.search(query, 2);
		
		System.out.println("top hits:" + topDocs.totalHits);
		
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			doc = reader.document(scoreDoc.doc);
			System.out.println(doc.getField("content").stringValue() + ":" + scoreDoc.score);
		}
	}
}
