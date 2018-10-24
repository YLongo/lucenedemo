package github.io.volong.chapter02.customattribute;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import github.io.volong.chapter02.customattribute.GenderAttribute.Gender;

public class GenderFilter extends TokenFilter {

    private GenderAttribute genderAttr = addAttribute(GenderAttribute.class);
    
    private CharTermAttribute charTermAttr = addAttribute(CharTermAttribute.class);
    
    protected GenderFilter(TokenStream input) {
        super(input);
    }

    @Override
    public boolean incrementToken() throws IOException {
        
        if (!input.incrementToken()) {
            return false;
        }
        
        genderAttr.setGender(determineGender(charTermAttr.toString()));
        
        return false;
    }
    
    protected Gender determineGender(String term) {
        
        if ("mr".equalsIgnoreCase(term) || "mister".equalsIgnoreCase(term)) {
            return Gender.Male;
        } else if ("mrs".equalsIgnoreCase(term) || "misters".equalsIgnoreCase(term)) {
            return Gender.Female;
        }
        
        return Gender.Undefined;
    }

}
