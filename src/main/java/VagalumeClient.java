import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class VagalumeClient {
  private static final Logger LOGGER = Logger.getLogger(VagalumeClient.class.getName());
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private static final String ARTIST_PATH = "https://www.vagalume.com.br/%s/index.js";
  private static final String LYRICS_SEARCH_PATH = "https://api.vagalume.com.br/search.php";
  private static final String LYRICS_SEARCH_ARTIST_PARAM_NAME = "art";
  private static final String LYRICS_SEARCH_MUSIC_PARAM_NAME = "mus";
  private static final String LYRICS_SEARCH_EXTRA_PARAM_ALBUM_VALUE = "alb";
  private static final String EXTRA_PARAM_NAME = "extra";


  private Client client;

  public VagalumeClient() {
    this.client = ClientBuilder.newClient();
  }

  public List<SongBasic> getAllSongs(final String artist) {
    final Response response = client.target(String.format(ARTIST_PATH, artist))
        .request(MediaType.APPLICATION_JSON)
        .get();
    if (!Response.Status.Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
      LOGGER.log(Level.SEVERE, response.getStatusInfo() + ";" + response.readEntity(String.class));
      return null;
    }
    try {
      JsonNode node = MAPPER.readTree(response.readEntity(String.class));
      ArrayNode songsArrayNode = (ArrayNode) node.get("artist").get("lyrics").get("item");
      return MAPPER.convertValue(songsArrayNode,
          TypeFactory.defaultInstance().constructCollectionType(List.class, SongBasic.class));
    } catch (IOException e) {
      throw new RuntimeException("Not possible to parse the response");
    }
  }

  public List<Song> searchSong(final String artist, final String songName) {
    final Response response = client.target(LYRICS_SEARCH_PATH)
        .queryParam(LYRICS_SEARCH_ARTIST_PARAM_NAME, artist)
        .queryParam(LYRICS_SEARCH_MUSIC_PARAM_NAME, songName)
        .queryParam(EXTRA_PARAM_NAME, LYRICS_SEARCH_EXTRA_PARAM_ALBUM_VALUE)
        .request(MediaType.APPLICATION_JSON)
        .get();

    if (!Response.Status.Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily())) {
      LOGGER.log(Level.SEVERE, response.getStatusInfo() + ";" + response.readEntity(String.class));
      return null;
    }
    try {
      JsonNode node = MAPPER.readTree(response.readEntity(String.class));
      JsonNode musicNode = node.get("mus");
      return MAPPER.convertValue(musicNode,
          TypeFactory.defaultInstance().constructCollectionType(List.class, Song.class));
    } catch (IOException e) {
      throw new RuntimeException("Not possible to parse the response." + response.readEntity(String.class));
    }
  }
}
