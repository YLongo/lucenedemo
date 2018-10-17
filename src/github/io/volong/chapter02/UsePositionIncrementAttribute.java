package github.io.volong.chapter02;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

public class UsePositionIncrementAttribute extends TokenFilter {

    private CharTermAttribute charTermAttr;
    
    private PositionIncrementAttribute posIncrAtt;
    
    protected UsePositionIncrementAttribute(TokenStream input) {
        super(input);
        charTermAttr = addAttribute(CharTermAttribute.class);
        posIncrAtt = addAttribute(PositionIncrementAttribute.class);
    }

    @Override
    public boolean incrementToken() throws IOException {
        
        int extraIncrement = 0;
        boolean returnValue = false;
        
        while (input.incrementToken()) {
            // 对停顿词过滤
            if (StopAnalyzer.ENGLISH_STOP_WORDS_SET.contains(charTermAttr.toString())) {
                extraIncrement++;
                continue;
            }
            
            returnValue = true;
            
            // 作者书上有这个 break，但是我觉得这样·写会导致停顿词被漏掉
            // break;
        }
        
        if (extraIncrement > 0) {
            posIncrAtt.setPositionIncrement(posIncrAtt.getPositionIncrement() + extraIncrement);
        }
        
        return returnValue;
    }

    
}
