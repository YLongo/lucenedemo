package github.io.volong.lucenedemo;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class IndexWriter01 {
	
	public static void main(String[] args) throws IOException {
		
		Analyzer analyzer = new WhitespaceAnalyzer();
		
		Directory directory = new RAMDirectory();
		
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
		
		IndexWriter indexWriter = new IndexWriter(directory, config);
		
		
	}

}
