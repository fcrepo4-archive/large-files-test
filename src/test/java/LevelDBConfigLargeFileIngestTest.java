import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Session;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.modeshape.jcr.JcrRepository;
import org.modeshape.jcr.JcrRepositoryFactory;
import org.modeshape.jcr.api.RepositoryFactory;


public class LevelDBConfigLargeFileIngestTest {

    private JcrRepository repo;

    @Before
    public void setup() throws Exception{
        Map<String,String> params = new HashMap<String, String>();
        params.put(RepositoryFactory.URL, this.getClass().getResource("repository-leveldb.json").toURI().toASCIIString());
        repo = (JcrRepository) new JcrRepositoryFactory().getRepository(params);
    }

    @Test
    public void testIngestTwoMegabyte() throws Exception{
        long binSize = 1024l * 1024l * 2l;
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
    public void testIngestTwoGigabyte() throws Exception{
        long binSize = 1024l * 1024l * 1024l * 5l;
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
    }
}
