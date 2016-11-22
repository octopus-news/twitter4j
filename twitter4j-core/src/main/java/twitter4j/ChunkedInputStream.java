package twitter4j;

import java.io.*;

/**
 * In iterator providing access to n segments, when last byte of underlying stream is read, whole stream is marked as
 * finished and consequent call to close closes it.
 */
public class ChunkedInputStream extends InputStream {

    private final InputStream source;

    private long offset;
    private final long segmentSize;
    private int segmentIndex;
    private boolean finished;

    public ChunkedInputStream(InputStream source, long segmentSize) throws FileNotFoundException {
        this.source = source;
        this.segmentSize = segmentSize;
        segmentIndex = -1;
        offset = 0;
    }

    @Override
    public int read() throws IOException {
        if (offset >= segmentSize) {
            return -1;
        }
        int read = source.read();
        if (read != -1) {
            offset++;
        } else {
            finished = true;
        }
        return read;
    }

    public int getSegmentIndex() {
        return segmentIndex;
    }

    public boolean nextSegment() {
        if (finished) {
            return false;
        }
        offset = 0;
        segmentIndex ++;
        return true;
    }

    public void finish() {
        finished = true;
    }

    @Override
    public void close() throws IOException {
        if (finished) {
            source.close();
        }
    }

}
