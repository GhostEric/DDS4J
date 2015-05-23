package co.phoenixlab.dds.decoder;

import co.phoenixlab.dds.Dds;
import co.phoenixlab.dds.DdsHeader;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class AbstractBasicDecoder implements FormatDecoder {

    protected Dds dds;
    protected int currLine;
    protected final int numLines;
    protected final int rawLineByteWidth;
    protected final ByteBuffer rawLineCache;
    protected final int lineWidth;
    protected int arrayPos;

    public AbstractBasicDecoder(Dds dds) {
        this.dds = dds;
        this.currLine = 0;
        DdsHeader header = dds.getHeader();
        this.numLines = header.getDwHeight();
        this.lineWidth = header.getDwWidth();
        this.rawLineByteWidth = header.getDdspf().getDwRGBBitCount() / 8 * header.getDwWidth();
        this.rawLineCache = ByteBuffer.allocate(rawLineByteWidth);
        this.arrayPos = 0;
    }

    protected void loadNextLineIntoCache() {
        rawLineCache.rewind();
        rawLineCache.put(dds.getBdata(), arrayPos, rawLineByteWidth);
        rawLineCache.flip();
        arrayPos += rawLineByteWidth;
    }

    public Stream<int[]> lineStream() {
        Spliterator<int[]> spliterator = Spliterators.spliterator(lineIterator(), numLines,
                Spliterator.SIZED | Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED);
        return StreamSupport.stream(spliterator, false);
    }

    public Iterator<int[]> lineIterator() {
        return new LineIterator();
    }

    class LineIterator implements Iterator<int[]> {
        public boolean hasNext() {
            return currLine < numLines;
        }

        public int[] next() {
            return decodeLine();
        }
    }
}
