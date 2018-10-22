package github.io.volong.chapter02;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * 将礼貌用语简短的单词扩展为完整的单词
 */
public class CustomTokenFilter extends TokenFilter {

    private Map<String, String> courtesyTitleFitler = new HashMap<>();
    
    private CharTermAttribute termArr;
    
    protected CustomTokenFilter(TokenStream input) {
        super(input);
        termArr = addAttribute(CharTermAttribute.class);
        courtesyTitleFitler.put("Dr", "doctor");
        courtesyTitleFitler.put("Mr", "mister");
        courtesyTitleFitler.put("Mrs", "miss");
    }

    @Override
    public boolean incrementToken() throws IOException {
        
        if (!input.incrementToken()) {
            return false;
        }
        
        String small = termArr.toString();
        if (courtesyTitleFitler.containsKey(small)) {
            termArr.setEmpty().append(courtesyTitleFitler.get(small));
        }
        
        return true;
    }

    
}
