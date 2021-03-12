package utils;

import org.junit.jupiter.engine.JupiterTestEngine;
import org.junit.platform.engine.*;

import java.util.Optional;

public class VOTestEngine implements TestEngine {
    public TestEngine delegate;
    protected String rootNameId;

    public VOTestEngine(TestEngine delegate, String rootNameId) {
        this.delegate = delegate;
        this.rootNameId = rootNameId;
    }

    @Override
    public String getId() {
        return rootNameId;
    }

    @Override
    public TestDescriptor discover(EngineDiscoveryRequest engineDiscoveryRequest, UniqueId uniqueId) {
        return delegate.discover(engineDiscoveryRequest, uniqueId);
    }

    @Override
    public void execute(ExecutionRequest executionRequest) {
        delegate.execute(executionRequest);
    }

    @Override
    public Optional<String> getGroupId() {
        return delegate.getGroupId();
    }

    @Override
    public Optional<String> getArtifactId() {
        return delegate.getArtifactId();
    }

    @Override
    public Optional<String> getVersion() {
        return delegate.getVersion();
    }
}
