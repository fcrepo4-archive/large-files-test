import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.RepositoryConfiguration;


public class SingleConfigLargeFileIngestTest {

    private final ModeShapeEngine mode = new ModeShapeEngine();

    @Before
    public void setup() throws Exception{
        RepositoryConfiguration cfg = RepositoryConfiguration.read("repository-single.json");
        mode.start();
        mode.deploy(cfg);
    }

    @Test
    public void testIngestTwoMegabyte() throws Exception{
        long binSize = 1024l * 1024l * 2l;
        Repository repo = mode.getRepository("repo");
        Session sess = repo.login();
        Node fileNode = sess.getRootNode().addNode("large-node-" + UUID.randomUUID(), "nt:file");
        Node dataNode = fileNode.addNode("jcr:content", "nt:resource");
        InputStream src = new RandomDataInputStream(binSize);
        Binary bin = sess.getValueFactory().createBinary(src);
        assertEquals(binSize,bin.getSize());
        dataNode.setProperty("jcr:data", bin);
        dataNode.setProperty("jcr:lastModified", Calendar.getInstance());
        sess.save();
        sess.logout();
    }

    @Test
    @Ignore
    public void testIngestTwoGigabyte() throws Exception{
        long binSize = 1024l * 1024l * 1024l * 2l;
        Repository repo = mode.getRepository("repo");
        Session sess = repo.login();
        Node fileNode = sess.getRootNode().addNode("large-node-" + UUID.randomUUID(), "nt:file");
        Node dataNode = fileNode.addNode("jcr:content", "nt:resource");
        InputStream src = new RandomDataInputStream(binSize);
        Binary bin = sess.getValueFactory().createBinary(src);
        assertEquals(binSize,bin.getSize());
        dataNode.setProperty("jcr:data", bin);
        dataNode.setProperty("jcr:lastModified", Calendar.getInstance());
        sess.save();
        sess.logout();
    }

    @After
    public void teardown() throws Exception {
        mode.shutdown().get();
    }
}
