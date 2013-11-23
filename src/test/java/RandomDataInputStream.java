import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Random;

import org.uncommons.maths.random.XORShiftRNG;

   public class RandomDataInputStream extends InputStream {
        private final DecimalFormat fmt = new DecimalFormat("000.00");
        private final long size;
        private final Random rng = new XORShiftRNG();
        private long pos = 0;
        private long lastPublishMillis=System.currentTimeMillis();

        public RandomDataInputStream(long size) {
            super();
            this.size = size;
        }

        @Override
        public int read() throws IOException {
            if (pos++ > size) {
                return -1;
            }
            publishProcess();
            return rng.nextInt();
        }

        @Override
        public int read(byte[] b) throws IOException {
            int idx = 0;
            while (size > pos && idx < b.length) {
                b[idx++] = (byte) read();
            }
            if (idx == 0) {
                return -1;
            }
            publishProcess();
            return idx;
        }

        private void publishProcess() {
            if (System.currentTimeMillis() - lastPublishMillis > 1000 || pos == 1 || pos == size) {
                lastPublishMillis = System.currentTimeMillis();
                System.out.print("\rread " + fmt.format(pos * 100f / size) + " % of the stream");
            }
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (b.length > size - pos) {
            }
            int idx = 0;
            pos+=off;
            while (size > pos && idx < b.length) {
                b[idx++] = (byte) read();
            }
            if (idx == 0) {
                return -1;
            }
            publishProcess();
            return idx;
        }

        /* (non-Javadoc)
         * @see java.io.InputStream#close()
         */
        @Override
        public void close() throws IOException {
            System.out.println();
        }

    }
