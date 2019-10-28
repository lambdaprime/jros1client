package id.functions;

public interface ThrowingSupplier<R, E extends Exception> {
    R run() throws E;
}
