package github.io.volong.chapter02.customattribute;

import org.apache.lucene.util.Attribute;

/**
 * 自定义属性。
 * 
 */
public interface GenderAttribute extends Attribute {

    enum Gender {
        Male,
        
        Female,
        
        Undefined
    }
    
    void setGender(Gender gender);
    
    Gender getGender();
}
