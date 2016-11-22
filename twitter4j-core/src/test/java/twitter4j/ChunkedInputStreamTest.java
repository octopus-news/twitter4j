package twitter4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.fail;
import org.junit.Test;

public class ChunkedInputStreamTest {

    private static final byte[] CONTENT = "0123456789abcdefghij54321qwerty".getBytes();
    private static final int CHUNK_SIZE = 10;

    @Test
    public void testRead() {
        try {
            ChunkedInputStream is = new ChunkedInputStream(new ByteArrayInputStream(CONTENT) {
                @Override
                public void close() throws IOException {
                    System.out.println("Underlying stream closed");
                }
            }, CHUNK_SIZE);
            while (is.nextSegment()) {
                byte[] buffer = new byte[CHUNK_SIZE];
                int read = is.read(buffer, 0, buffer.length);
                is.close();

                System.out.println("Read: " + new String(buffer, 0, read) + ", part: " + is.getSegmentIndex());
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
