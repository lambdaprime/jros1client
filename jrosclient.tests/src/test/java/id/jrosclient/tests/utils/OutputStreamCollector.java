package id.jrosclient.tests.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class OutputStreamCollector extends OutputStream {

    public List<String> output = new ArrayList<>();
    
    @Override
    public void write(int i) throws IOException {
        var s = String.format("%2s", Integer.toHexString(i));
        s = s.replace(" ", "0");
        output.add(s);
    }
}
