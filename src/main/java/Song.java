import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Song {

  private String id;
  private String name;
  private String url;
  private Integer lang;
  private String text;
  @JsonProperty("alb")
  private Album album;
  private List<Translate> translate = null;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Integer getLang() {
    return lang;
  }

  public void setLang(Integer lang) {
    this.lang = lang;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Album getAlbum() {
    return album;
  }

  public void setAlbum(Album album) {
    this.album = album;
  }

  public List<Translate> getTranslate() {
    return translate;
  }

  public void setTranslate(List<Translate> translate) {
    this.translate = translate;
  }
}
