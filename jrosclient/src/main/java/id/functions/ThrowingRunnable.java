package id.functions;

public interface ThrowingRunnable<E extends Exception> {
    void run() throws E;
}
