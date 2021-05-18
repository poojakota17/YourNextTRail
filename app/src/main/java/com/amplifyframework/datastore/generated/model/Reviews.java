package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;

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

/** This is an auto generated class representing the Reviews type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Reviews")
public final class Reviews implements Model {
  public static final QueryField ID = field("Reviews", "id");
  public static final QueryField REVIEW = field("Reviews", "review");
  public static final QueryField SENITMENT = field("Reviews", "senitment");
  public static final QueryField USER = field("Reviews", "reviewsUserId");
  public static final QueryField TRAIL = field("Reviews", "reviewsTrailId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String review;
  private final @ModelField(targetType="String", isRequired = true) String senitment;
  private final @ModelField(targetType="User") @BelongsTo(targetName = "reviewsUserId", type = User.class) User user;
  private final @ModelField(targetType="Trail") @BelongsTo(targetName = "reviewsTrailId", type = Trail.class) Trail trail;
  public String getId() {
      return id;
  }
  
  public String getReview() {
      return review;
  }
  
  public String getSenitment() {
      return senitment;
  }
  
  public User getUser() {
      return user;
  }
  
  public Trail getTrail() {
      return trail;
  }
  
  private Reviews(String id, String review, String senitment, User user, Trail trail) {
    this.id = id;
    this.review = review;
    this.senitment = senitment;
    this.user = user;
    this.trail = trail;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Reviews reviews = (Reviews) obj;
      return ObjectsCompat.equals(getId(), reviews.getId()) &&
              ObjectsCompat.equals(getReview(), reviews.getReview()) &&
              ObjectsCompat.equals(getSenitment(), reviews.getSenitment()) &&
              ObjectsCompat.equals(getUser(), reviews.getUser()) &&
              ObjectsCompat.equals(getTrail(), reviews.getTrail());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getReview())
      .append(getSenitment())
      .append(getUser())
      .append(getTrail())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Reviews {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("review=" + String.valueOf(getReview()) + ", ")
      .append("senitment=" + String.valueOf(getSenitment()) + ", ")
      .append("user=" + String.valueOf(getUser()) + ", ")
      .append("trail=" + String.valueOf(getTrail()))
      .append("}")
      .toString();
  }
  
  public static ReviewStep builder() {
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
  public static Reviews justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Reviews(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      review,
      senitment,
      user,
      trail);
  }
  public interface ReviewStep {
    SenitmentStep review(String review);
  }
  

  public interface SenitmentStep {
    BuildStep senitment(String senitment);
  }
  

  public interface BuildStep {
    Reviews build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep user(User user);
    BuildStep trail(Trail trail);
  }
  

  public static class Builder implements ReviewStep, SenitmentStep, BuildStep {
    private String id;
    private String review;
    private String senitment;
    private User user;
    private Trail trail;
    @Override
     public Reviews build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Reviews(
          id,
          review,
          senitment,
          user,
          trail);
    }
    
    @Override
     public SenitmentStep review(String review) {
        Objects.requireNonNull(review);
        this.review = review;
        return this;
    }
    
    @Override
     public BuildStep senitment(String senitment) {
        Objects.requireNonNull(senitment);
        this.senitment = senitment;
        return this;
    }
    
    @Override
     public BuildStep user(User user) {
        this.user = user;
        return this;
    }
    
    @Override
     public BuildStep trail(Trail trail) {
        this.trail = trail;
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
    private CopyOfBuilder(String id, String review, String senitment, User user, Trail trail) {
      super.id(id);
      super.review(review)
        .senitment(senitment)
        .user(user)
        .trail(trail);
    }
    
    @Override
     public CopyOfBuilder review(String review) {
      return (CopyOfBuilder) super.review(review);
    }
    
    @Override
     public CopyOfBuilder senitment(String senitment) {
      return (CopyOfBuilder) super.senitment(senitment);
    }
    
    @Override
     public CopyOfBuilder user(User user) {
      return (CopyOfBuilder) super.user(user);
    }
    
    @Override
     public CopyOfBuilder trail(Trail trail) {
      return (CopyOfBuilder) super.trail(trail);
    }
  }
  
}
