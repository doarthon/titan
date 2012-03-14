package com.thinkaurelius.titan.blueprints;


import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.thinkaurelius.titan.blueprints.TitanVertex;
import com.thinkaurelius.titan.blueprints.util.TitanVertexSequence;
import com.thinkaurelius.titan.core.PropertyType;
import com.tinkerpop.blueprints.pgm.AutomaticIndex;
import com.tinkerpop.blueprints.pgm.CloseableSequence;
import com.tinkerpop.blueprints.pgm.Element;
import com.tinkerpop.blueprints.pgm.Index;

import java.util.Set;

public class TitanIndex<T extends Element> implements AutomaticIndex<T> {

    private final String indexName;
    private final TitanGraph db;
    private final Set<String> keys;
    private final Class<T> indexClass;
    
    TitanIndex(TitanGraph db, String indexName, Set<String> keys, Class<T> indexClass) {
        this.db=db;
        this.indexName=indexName;
        ImmutableSet.Builder<String> b = ImmutableSet.builder();
        b.addAll(keys);
        this.keys=b.build();
        this.indexClass=indexClass;
    }
    
    @Override
    public Set<String> getAutoIndexKeys() {
        return keys;
    }

    @Override
    public String getIndexName() {
        return indexName;
    }

    @Override
    public CloseableSequence<T> get(String s, Object o) {
        return new TitanVertexSequence(db.indexRetrieval(s,o));
    }

    @Override
    public long count(String s, Object o) {
        return db.indexRetrieval(s,o).size();
    }

    @Override
    public Class<T> getIndexClass() {
        return indexClass;
    }

    @Override
    public Type getIndexType() {
        return Type.AUTOMATIC;
    }

    @Override
    public void put(String s, Object o, T titanVertex) {
        throw new UnsupportedOperationException("Cannot manually update an automatic index");
    }

    @Override
    public void remove(String s, Object o, T titanVertex) {
        throw new UnsupportedOperationException("Cannot manually update an automatic index");
    }
}
