package github.io.volong.chapter02;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

public class UsePerFieldAnalyzerWrapper {

    public static void main(String[] args) throws IOException {
        
        Map<String, Analyzer> analyzerPerField = new HashMap<>();
        // 建立字段与 analyzer 的映射关系
        analyzerPerField.put("myfield", new WhitespaceAnalyzer());
        
        // 指定默认的 analyzer 为 StandardAnalyzer
        try(PerFieldAnalyzerWrapper defanalyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerPerField)) {
            
            // 字段为 myfield，则在映射关系中找到了 analyzer 为 WhitespaceAnalyzer
            TokenStream tokenStream = defanalyzer.tokenStream("myfield", new StringReader("lucene.apache.org AB-978"));
            CharTermAttribute charAttr = tokenStream.addAttribute(CharTermAttribute.class);
            OffsetAttribute offsetAttr = tokenStream.addAttribute(OffsetAttribute.class);
            tokenStream.reset();
            
            System.out.println("== 使用 WhitespaceAnalyzer 处理字段 ==");
            
            while (tokenStream.incrementToken()) {
                System.out.println(charAttr.toString() + " 开始位置:" + offsetAttr.startOffset() + " 结束位置:" + offsetAttr.endOffset());
            }
            tokenStream.end();
            
            // 字段为 content，在映射关系中没有找到相应的 analyzer。则使用默认的 StandardAnalyzer
            tokenStream = defanalyzer.tokenStream("content", new StringReader("lucene.apache.org AB-978"));
            offsetAttr = tokenStream.addAttribute(OffsetAttribute.class);
            charAttr = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            
            System.out.println();
            System.out.println("== 使用 StandardAnalyzer 处理字段 ==");
            
            while (tokenStream.incrementToken()) {
                System.out.println(charAttr.toString() + " 开始位置:" + offsetAttr.startOffset() + " 结束位置:" + offsetAttr.endOffset());
            }
            tokenStream.end();
        }
        
    }
}
