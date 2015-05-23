package co.phoenixlab.dds.decoder;

import java.util.Iterator;
import java.util.stream.Stream;

public interface FormatDecoder {

    /**
     * Decodes a line, returning an array of 0xAARRGGBB ints, or null if no lines remain to be decoded.
     * @return An array of ARGB ints, containing the pixels in a single line, or null if all lines have been decoded.
     */
    int[] decodeLine();

    /**
     * Returns an iterator over lazily decoded lines.
     * @return An iterator of ARGB int arrays
     * @see #decodeLine()
     */
    Iterator<int[]> lineIterator();

    /**
     * Returns a stream over lazily decoded lines.
     * @return A Stream of ARGB int arrays
     */
    Stream<int[]> lineStream();
}
