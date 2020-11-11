package ru.outs.chain;

/**
 * @author sergey
 * created on 11.09.18.
 */
abstract class ApplProcessor {
    private ApplProcessor next;

    private ApplProcessor getNext() {
        return next;
    }

    void setNext(ApplProcessor next) {
        this.next = next;
    }

    void process(Application application) {
        before();
        processInternal(application);
        after();
        if (getNext() != null) {
            getNext().process(application);
        }
    }

    protected abstract void processInternal(Application application);

    public abstract String getProcessorName();

    private void before() {
        System.out.println("before:" + getProcessorName());
    }

    private void after() {
        System.out.println("after:" + getProcessorName());
    }

}
