package github.io.volong.chapter04.constructquery;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class UseTermQuery {

	public static void main(String[] args) throws IOException {
		
		FSDirectory dir = FSDirectory.open(new File("indexPath"));
		DirectoryReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		Query query = new TermQuery(new Term("content", "alpha"));
		TopDocs topDocs = searcher.search(query, 100);
		
	}
}
