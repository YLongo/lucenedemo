package github.io.volong.chapter02;

import java.io.Reader;

import org.apache.lucene.analysis.util.CharTokenizer;

/**
 * 自定义分词器。
 * 通过空白符分割字符串
 */
public class CustomTokenizer extends CharTokenizer {

    public CustomTokenizer(Reader input) {
        super(input);
    }

    @Override
    protected boolean isTokenChar(int c) {
        return !Character.isSpaceChar(c);
    }

}
