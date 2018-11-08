package github.io.volong.chapter04;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

public class ObtainIndexReader {

    public static void main(String[] args) throws IOException {
        
        // 打开索引所在的文件夹
        FSDirectory dir = FSDirectory.open(new File("indexPath"));

        // 获取 reader
        DirectoryReader dirReader = DirectoryReader.open(dir);
        
        // 获取 AtomicReader 列表
        List<AtomicReaderContext> leaves = dirReader.leaves();
        
        AtomicReader firstAtomicReader = leaves.get(0).reader();
        
        // 如果的旧的 dirReader 产生改变则获取一个新的
        DirectoryReader newDirReader = DirectoryReader.openIfChanged(dirReader);
        
        if (newDirReader != null) {
            IndexSearcher searcher = new IndexSearcher(newDirReader);
            dirReader.close();
        }
    }
}
