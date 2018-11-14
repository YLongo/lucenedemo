package github.io.volong.chapter04;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

public class UseIndexSearcher {

    public static void main(String[] args) throws IOException {
        
        FSDirectory dir = FSDirectory.open(new File("indexFile"));
        DirectoryReader reader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(reader);
    }
}
