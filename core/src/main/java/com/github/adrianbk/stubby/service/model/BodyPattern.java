package com.github.adrianbk.stubby.service.model;

import com.github.adrianbk.stubby.model.StubMessage;

// all instances should be imutable (so we can safely pass them to scripts etc.)
public abstract class BodyPattern { // Note: using abstract class so we can force override of equals()
    
    public abstract MatchField matches(StubMessage request);

    @Override
    public abstract boolean equals(Object object); // force implementors to override
    
    @Override
    public abstract int hashCode(); // force implementors to override

}
