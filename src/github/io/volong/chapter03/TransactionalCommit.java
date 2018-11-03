package github.io.volong.chapter03;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexCommit;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.NoDeletionPolicy;
import org.apache.lucene.index.SnapshotDeletionPolicy;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class TransactionalCommit {

    public static void main(String[] args) throws IOException {
        
        StandardAnalyzer analyzer = new StandardAnalyzer();
        RAMDirectory dir = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);

        // 所有的提交将会被保留，之前的提交也不会被移除
        SnapshotDeletionPolicy policy = new SnapshotDeletionPolicy(NoDeletionPolicy.INSTANCE);
        
        config.setIndexDeletionPolicy(policy);
        
        IndexWriter indexWriter = new IndexWriter(dir, config);
        
        Document doc = new Document();
        indexWriter.addDocument(doc);
        indexWriter.commit(); // 第一次提交
        IndexCommit snapshot = policy.snapshot();
        
        doc = new Document();
        indexWriter.addDocument(doc);
        indexWriter.commit(); // 第二次提交
        snapshot = policy.snapshot();
        
        doc = new Document();
        indexWriter.addDocument(doc);
        indexWriter.rollback(); // 回滚
        
        indexWriter.close();
        
        List<IndexCommit> commits = DirectoryReader.listCommits(dir);
        System.out.println("commits count:" + commits.size());
        
        for (IndexCommit commit : commits) {
            DirectoryReader reader = DirectoryReader.open(commit);
            System.out.println("       commit:" + commit.getSegmentCount());
            System.out.println("  num of docs:" + reader.numDocs());
        }
        
        System.out.println("\nsnapshot counts:" + policy.getSnapshotCount() + "\n");
        
        List<IndexCommit> snapshots = policy.getSnapshots();
        for (IndexCommit snap : snapshots) {
            DirectoryReader reader = DirectoryReader.open(snap);
            System.out.println("snapshot counts:" + snap.getSegmentCount());
            System.out.println("    num of docs:" + reader.numDocs());
        }
        
        policy.release(snapshot);
        
        System.out.println("\nsnapshot counts:" + policy.getSnapshotCount());
    }
}
