package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.HasMany;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Trail type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Trails")
public final class Trail implements Model {
  public static final QueryField ID = field("Trail", "id");
  public static final QueryField TITLE = field("Trail", "title");
  public static final QueryField DESCRIPTION = field("Trail", "description");
  public static final QueryField LEVEL = field("Trail", "level");
  public static final QueryField IMAGE = field("Trail", "image");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String title;
  private final @ModelField(targetType="String", isRequired = true) String description;
  private final @ModelField(targetType="String", isRequired = true) String level;
  private final @ModelField(targetType="String", isRequired = true) String image;
  private final @ModelField(targetType="TrailAttribute") @HasMany(associatedWith = "trail", type = TrailAttribute.class) List<TrailAttribute> attributes = null;
  private final @ModelField(targetType="Reviews") @HasMany(associatedWith = "trail", type = Reviews.class) List<Reviews> reviews = null;
  public String getId() {
      return id;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getDescription() {
      return description;
  }
  
  public String getLevel() {
      return level;
  }
  
  public String getImage() {
      return image;
  }
  
  public List<TrailAttribute> getAttributes() {
      return attributes;
  }
  
  public List<Reviews> getReviews() {
      return reviews;
  }
  
  private Trail(String id, String title, String description, String level, String image) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.level = level;
    this.image = image;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Trail trail = (Trail) obj;
      return ObjectsCompat.equals(getId(), trail.getId()) &&
              ObjectsCompat.equals(getTitle(), trail.getTitle()) &&
              ObjectsCompat.equals(getDescription(), trail.getDescription()) &&
              ObjectsCompat.equals(getLevel(), trail.getLevel()) &&
              ObjectsCompat.equals(getImage(), trail.getImage());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTitle())
      .append(getDescription())
      .append(getLevel())
      .append(getImage())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Trail {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("level=" + String.valueOf(getLevel()) + ", ")
      .append("image=" + String.valueOf(getImage()))
      .append("}")
      .toString();
  }
  
  public static TitleStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static Trail justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Trail(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      title,
      description,
      level,
      image);
  }
  public interface TitleStep {
    DescriptionStep title(String title);
  }
  

  public interface DescriptionStep {
    LevelStep description(String description);
  }
  

  public interface LevelStep {
    ImageStep level(String level);
  }
  

  public interface ImageStep {
    BuildStep image(String image);
  }
  

  public interface BuildStep {
    Trail build();
    BuildStep id(String id) throws IllegalArgumentException;
  }
  

  public static class Builder implements TitleStep, DescriptionStep, LevelStep, ImageStep, BuildStep {
    private String id;
    private String title;
    private String description;
    private String level;
    private String image;
    @Override
     public Trail build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Trail(
          id,
          title,
          description,
          level,
          image);
    }
    
    @Override
     public DescriptionStep title(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }
    
    @Override
     public LevelStep description(String description) {
        Objects.requireNonNull(description);
        this.description = description;
        return this;
    }
    
    @Override
     public ImageStep level(String level) {
        Objects.requireNonNull(level);
        this.level = level;
        return this;
    }
    
    @Override
     public BuildStep image(String image) {
        Objects.requireNonNull(image);
        this.image = image;
        return this;
    }
    
    /** 
     * WARNING: Do not set ID when creating a new object. Leave this blank and one will be auto generated for you.
     * This should only be set when referring to an already existing object.
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     * @throws IllegalArgumentException Checks that ID is in the proper format
     */
    public BuildStep id(String id) throws IllegalArgumentException {
        this.id = id;
        
        try {
            UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
        } catch (Exception exception) {
          throw new IllegalArgumentException("Model IDs must be unique in the format of UUID.",
                    exception);
        }
        
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String title, String description, String level, String image) {
      super.id(id);
      super.title(title)
        .description(description)
        .level(level)
        .image(image);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
    
    @Override
     public CopyOfBuilder level(String level) {
      return (CopyOfBuilder) super.level(level);
    }
    
    @Override
     public CopyOfBuilder image(String image) {
      return (CopyOfBuilder) super.image(image);
    }
  }
  
}
