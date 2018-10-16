package github.io.volong.lucenedemo.chapter02;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

public class ObtainTokenAttributeValue {

    public static void main(String[] args) throws IOException {
        
        StringReader reader = new StringReader("Lucene is mainly used for information retrieval and you can read more about it at lucene.apache.org.");
        StandardAnalyzer analyzer = new StandardAnalyzer();
        TokenStream tokenStream = analyzer.tokenStream("field", reader);
        
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        
        // 所有操作进行之前都要进行的操作。用于清除 tokenStream 所有的状态
        tokenStream.reset();
        
        while (tokenStream.incrementToken()) {
            String token = charTermAttribute.toString();
            System.out.println("[" + token + "]");
            System.out.println("start offset:" + offsetAttribute.startOffset());
            System.out.println("end offset  :" + offsetAttribute.endOffset());
        }
        tokenStream.end();
        
        tokenStream.close();
        analyzer.close();
    }
}
