package github.io.volong.chapter03;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class ObtainIndexWriter {
    
    public static void main(String[] args) throws IOException {
        
        // 指定索引所在的文件夹
        FSDirectory directory = FSDirectory.open(new File("indexPath"));
        
        StandardAnalyzer analyzer = new StandardAnalyzer();
        
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        
        // 设置创建索引的方式
        config.setOpenMode(OpenMode.CREATE);
        
        /*
         * 以下两个条件同时设置时，哪个先满足就使用哪个
         */
        
        // 当内存中数据大小为 64MB 时，刷新到文件夹中。默认值为 16MB
        config.setRAMBufferSizeMB(64);
        
        // 当内存中的文档数达到 4000 时，生成一个新的段。默认值为 1000
        config.setMaxBufferedDocs(4000);
        
        IndexWriter indexWriter = new IndexWriter(directory, config);
        
        // 索引更改只有再调用如下两个方法之一后，才会可见
//        indexWriter.close();
        indexWriter.commit();
        
    }
}
