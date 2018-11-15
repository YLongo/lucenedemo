package github.io.volong.chapter04.constructquery;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class UseQueryParser {

	public static void main(String[] args) throws IOException, ParseException {
		
		FSDirectory dir = FSDirectory.open(new File("indexPath"));
		StandardAnalyzer analyzer = new StandardAnalyzer();
		DirectoryReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser queryParser = new QueryParser("content", analyzer);
		Query query = queryParser.parse("alpha beta");
		TopDocs topDocs = searcher.search(query, 100);
	}
}
