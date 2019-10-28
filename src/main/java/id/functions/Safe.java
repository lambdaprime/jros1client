package id.functions;

public class Safe {

    public static <R, E extends Exception> R run(ThrowingSupplier<R, E> s) {
        try {
            return s.run();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <E extends Exception> void run(ThrowingRunnable<E> s) {
        try {
            s.run();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
