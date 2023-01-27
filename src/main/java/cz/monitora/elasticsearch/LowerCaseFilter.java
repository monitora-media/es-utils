package cz.monitora.elasticsearch;

import org.apache.lucene.analysis.CharacterUtils;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

import java.io.IOException;

// based on org.apache.lucene.analysis.LowerCaseFilter

/**
 * Generate lowercase tokens.
 * If  `preserveOriginal` is true, then keep also the original token, containing upper case letters.
 */
public class LowerCaseFilter extends TokenFilter {
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final PositionIncrementAttribute posIncAttr = addAttribute(PositionIncrementAttribute.class);
    private final boolean preserveOriginal;
    private State state;

    public LowerCaseFilter(TokenStream in, boolean preserveOriginal) {
        super(in);
        this.preserveOriginal = preserveOriginal;
    }

    @Override
    public final boolean incrementToken() throws IOException {
        if (state != null) {
            restoreState(state);
            posIncAttr.setPositionIncrement(0);
            state = null;
            return true;
        }

        if (input.incrementToken()) {
            final char[] buffer = termAtt.buffer();
            final int length = termAtt.length();

            if (preserveOriginal) {
                boolean isAnyUpper = false;
                for (int i = 0; i < length && !isAnyUpper; ++i) {
                    isAnyUpper = Character.isUpperCase(buffer[i]);
                }
                if (isAnyUpper) {
                    state = captureState();
                }
            }
            CharacterUtils.toLowerCase(termAtt.buffer(), 0, termAtt.length());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        state = null;
    }
}