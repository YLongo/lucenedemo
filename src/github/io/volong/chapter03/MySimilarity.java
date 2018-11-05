package github.io.volong.chapter03;

import java.io.IOException;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.NumericDocValues;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.util.BytesRef;

public class MySimilarity extends Similarity {

    private Similarity sim;
    
    public MySimilarity(Similarity sim) {
        this.sim = sim;
    }
    
    
    @Override
    public long computeNorm(FieldInvertState state) {
        return sim.computeNorm(state);
    }

    @Override
    public SimWeight computeWeight(float queryBoost, CollectionStatistics collectionStats, TermStatistics... termStats) {
        return sim.computeWeight(queryBoost, collectionStats, termStats);
    }

    @Override
    public SimScorer simScorer(SimWeight weight, AtomicReaderContext context) throws IOException {
        
        SimScorer scorer = sim.simScorer(weight, context);
        NumericDocValues values = context.reader().getNumericDocValues("ranking");
        
        return new SimScorer() {
            
            @Override
            public float score(int doc, float freq) {

                return values.get(doc) * scorer.score(doc, freq);
            }
            
            @Override
            public float computeSlopFactor(int distance) {
                return scorer.computeSlopFactor(1);
            }
            
            @Override
            public float computePayloadFactor(int doc, int start, int end, BytesRef payload) {
                return scorer.computePayloadFactor(doc, start, end, payload);
            }
        };
    }

    
}
