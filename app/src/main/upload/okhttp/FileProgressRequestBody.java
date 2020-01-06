package okhttp;

import java.io.File;
import java.io.IOException;

import listener.SliceListener;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class FileProgressRequestBody extends RequestBody {

    private static final int SEGMENT_SIZE = 2 * 1024; // okio.Segment.SIZE

    private final File file;
    private final String contentType;

    private final SliceListener listener;

    public FileProgressRequestBody(File file,
                                   String contentType,
                                   SliceListener listener) {
        this.file = file;
        this.contentType = contentType;
        this.listener = listener;
    }

    @Override
    public long contentLength() {
        return file.length();
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse(contentType);
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(file);
            long total = 0;
            long read;

            while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
                total += read;
                sink.flush();
                this.listener.onTransferred(total);

            }
        } finally {
            Util.closeQuietly(source);
        }
    }

}