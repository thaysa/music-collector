import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextFileAppender {
  public static void main(String[] args) throws  Exception{
    String rootFolder = "/root/src/emotion/angra_studio";
    StringBuilder sb = new StringBuilder();
    File file = new File(rootFolder);

    if (file.isDirectory()) {
      File[] files = file.listFiles();
      for (File innerFile : files) {
        if (innerFile.isDirectory()) {
           for (File songFile : innerFile.listFiles()) {
            if (songFile.getName().endsWith(".txt") && !songFile.getName().endsWith("pt.txt")){
              String fileContent = new String(Files.readAllBytes(Paths.get(songFile.toURI())));
              if (!fileContent.toLowerCase().startsWith("instrumental"))
              sb.append(fileContent).append("\n");
            }
          }

        }
      }
    }
    Files.write(Paths.get("/root/src/emotion/all_angra_studio_song.txt"), sb.toString().getBytes());
    System.out.println(sb);
  }

}
