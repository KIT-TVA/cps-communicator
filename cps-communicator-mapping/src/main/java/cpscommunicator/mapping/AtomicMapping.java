package cpscommunicator.mapping;

public class AtomicMapping<S> {
    private Class<?> classOfDeltaOperation;
    private S changeDescriptor;

    public AtomicMapping() {
    }

    public AtomicMapping(Class<?> classOfDeltaOperation, S changeDescription) {
        this.classOfDeltaOperation = classOfDeltaOperation;
        this.changeDescriptor = changeDescription;
    }

    public S getChangeDescriptor() {
        return changeDescriptor;
    }

    public void setChangeDescriptor(S changeDescriptor) {
        this.changeDescriptor = changeDescriptor;
    }

    public Class<?> getClassOfDeltaOperation() {
        return classOfDeltaOperation;
    }

    public void setClassOfDeltaOperation(Class<?> classOfDeltaOperation) {
        this.classOfDeltaOperation = classOfDeltaOperation;
    }
}
