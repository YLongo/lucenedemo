package github.io.volong.lucenedemo.chapter01;

import java.io.IOException;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class LuceneTest {

	public static void main(String[] args) throws IOException, ParseException {
		
		/*
		 *  索引文档 
		 */
		WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
		
		Directory directory = new RAMDirectory();
		
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
		
		IndexWriter indexWriter = new IndexWriter(directory, config);
		
		Document doc = new Document();
		
		String text = "Lucene is an Information Retrieval library written in Java.";
		
		doc.add(new TextField("Content", text, Field.Store.YES));
		
		indexWriter.addDocument(doc);
		
		/*
		 * 删除文档
		 */
		
		// 删除字段 id 中，值为 1 的文档 (此 id 只是一个字段的名字，而不是 docId)
		indexWriter.deleteDocuments(new Term("id", "1"));
		
		
		// 将会提交索引并关闭所有相关的文件以及释放 write.lock
		indexWriter.close();
		
		/*
		 * 查询文档
		 */
		IndexReader reader = DirectoryReader.open(directory);
		
		IndexSearcher indexSearcher = new IndexSearcher(reader);
		
		QueryParser parse = new QueryParser("Content", analyzer);
		
		Query query = parse.parse("Lucene");
		
		int hitsPerPage = 10;
		
		TopDocs docs = indexSearcher.search(query, hitsPerPage);
		
		ScoreDoc[] hits = docs.scoreDocs;
		
		int end = Math.min(docs.totalHits, hitsPerPage);
		
		System.out.println("Total Hits: " + docs.totalHits);
		
		System.out.println("Results: ");
		
		for (int i = 0; i < end; i++) {
			Document d = indexSearcher.doc(hits[i].doc);
			System.out.println("Content: " + d.get("Content"));
		}
		
	}
}
