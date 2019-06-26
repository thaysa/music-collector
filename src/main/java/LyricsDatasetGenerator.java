import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

public class LyricsDatasetGenerator {
  private static final Logger LOGGER = Logger.getLogger(LyricsDatasetGenerator.class.getName());
  private VagalumeClient vagalumeClient;

  public LyricsDatasetGenerator() {
    vagalumeClient = new VagalumeClient();
  }

  public void generateDataset(final String artist) throws IOException, InterruptedException {
    Path basePath = Paths.get(artist);
    List<SongBasic> allSongs = vagalumeClient.getAllSongs(artist);
    for (SongBasic songBasic : allSongs) {
      LOGGER.info( "processing: " + songBasic.getDesc() + "->" + songBasic.getUrl());
      Thread.sleep(10000); // to prevent many request error
      final String songName = extractNormalizedSongName(songBasic);
      List<Song> songs = vagalumeClient.searchSong(artist, songName);
      if (songs == null) {
        LOGGER.warning( "Song name not found: " + songName);
        continue;
      }
      exportMusicData(basePath, songs);
    }
  }

  private void exportMusicData(final Path basePath, final List<Song> songs) throws IOException {

    for (Song song : songs) {
      File songFolder = createFolderIfNeeded(basePath, song);
      LOGGER.info("Song folder:" + songFolder.getAbsolutePath());
      Files.write(
          Paths.get(songFolder.getCanonicalPath(), extractNormalizedSongName(song) + ".txt"),
          song.getText() == null ? "".getBytes() : song.getText().getBytes());
      exportTranslation(song, songFolder);
    }
  }

  private void exportTranslation(final Song song, final File songFolder) throws IOException {
    if (song.getTranslate() == null) {
      LOGGER.warning("Translation not found:" + song.getName());
      return;
    }

    for (Translate translation : song.getTranslate()) {
      if (translation.getLang() == 1) {
        Files.write(
            Paths.get(songFolder.getCanonicalPath(),
                extractNormalizedSongName(song) + "_pt.txt"),
            translation.getText().getBytes());
      }
    }
  }

  private File createFolderIfNeeded(final Path basePath, final Song song) {
    File dir = null;
    if (song.getAlbum() == null) {
      LOGGER.warning("Album not found for :" + song.getName());
      dir = basePath.toFile();
    } else {
      dir = new File(basePath.toFile(),
          song.getAlbum().getYear() + "_" + extractNormalizedAlbumName(song.getAlbum()));
    }
    if (!dir.exists()) {
      dir.mkdirs();
    }
    return dir;
  }


  private static String extractNormalizedSongName(final SongBasic songBasic) {
    final String url = songBasic.getUrl();
    return extractNormalizedName(url);
  }

  private static String extractNormalizedSongName(final Song song) {
    final String url = song.getUrl();
    return extractNormalizedName(url);
  }

  private static String extractNormalizedAlbumName(final Album album) {
    final String url = album.getUrl();
    return extractNormalizedName(url);
  }

  /**
   * Extract song name
   * Following input url:SongBasic: //https://www.vagalume.com.br/angra/carry-on.html
   * will return carry-on
   * @param url
   * @return normalized song name for search
   */
  private static String extractNormalizedName(final String url) {
    int i = url.lastIndexOf("/");
    return url.substring(i).replace(".html", "").replace("/", "");
  }
}
