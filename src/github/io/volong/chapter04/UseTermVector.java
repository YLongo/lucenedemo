package github.io.volong.chapter04;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsAndPositionsEnum;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

public class UseTermVector {

    public static void main(String[] args) throws IOException {
        
        StandardAnalyzer analyzer = new StandardAnalyzer();
        RAMDirectory dir = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        
        IndexWriter indexWriter = new IndexWriter(dir, config);
        
        FieldType fieldType = new FieldType();
        fieldType.setIndexed(true);
        fieldType.setTokenized(true);
        fieldType.setStored(true);
        fieldType.setStoreTermVectors(true);
        fieldType.setStoreTermVectorPositions(true);
        fieldType.setStoreTermVectorOffsets(true);
        
        Document doc = new Document();
        
        Field field = new Field("content", "", fieldType);
        
        String[] contents = {
                "Humpty Dumpty sat on a wall,",
                "Humpty Dumpty had a great fall.",
                "All the king's horses and all the king's men",
                "Couldn't put Humpty together again."};
        
        for (String content : contents) {
            field.setStringValue(content);
            doc.removeField("content");
            doc.add(field);
            indexWriter.addDocument(doc);
        }
        
        indexWriter.commit();
        
        DirectoryReader reader = DirectoryReader.open(dir);
        
        TermsEnum termsEnum = null;
        BytesRef term = null;
        DocsAndPositionsEnum docsAndPositions = null;
        
        for (int i = 0; i < reader.maxDoc(); i++) {

            // 获取当前文档中的词
            Terms termVector = reader.getTermVector(i, "content");
            
            // 获取迭代器
            termsEnum = termVector.iterator(termsEnum);
            
            while ((term = termsEnum.next()) != null) {
                
                String termValue = term.utf8ToString();
                System.out.println("docid:" + i);
                System.out.println("  term:" + termValue);
                System.out.println("  length:" + term.length);
                
                // 获取词的位置信息
                docsAndPositions = termsEnum.docsAndPositions(null, docsAndPositions);
                
                if (docsAndPositions.nextDoc() >= 0) {
                    int freq = docsAndPositions.freq();
                    System.out.println("  freq:" + freq);
                    
                    // 同一个词可能会在文档中出现多次
                    for (int j = 0; j < freq; j++) {
                        System.out.print("  position:" + docsAndPositions.nextPosition());
                        System.out.print("  offset start:" + docsAndPositions.startOffset());
                        System.out.println("  offset end:" + docsAndPositions.endOffset());
                    }
                }
            }
        }
    }
}
