import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class VagalumeClientTest {

  private VagalumeClient vagalumeClient;

  @Before
  public void setUp() {
    vagalumeClient = new VagalumeClient();
  }

  @Test
  public void testGetAllSongs() {
    List<SongBasic> allSongs = vagalumeClient.getAllSongs("angra");
    assertNotNull(allSongs);
  }

  @Test
  public void testSearchSong() {
    List<Song> song = vagalumeClient.searchSong("angra", "zitto");
    assertNotNull(song);
  }
}

