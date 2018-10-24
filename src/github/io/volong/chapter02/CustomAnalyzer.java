package github.io.volong.chapter02;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LetterTokenizer;

/**
 * 自定义分析器 
 */
public class CustomAnalyzer extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        LetterTokenizer letterTokenizer = new LetterTokenizer(reader);
        
        TokenStream filter = new CustomTokenFilter(letterTokenizer);
        
        return new TokenStreamComponents(letterTokenizer, filter);
    }

}
