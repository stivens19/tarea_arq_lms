package com.tecsup.lms.comments.domain;

import com.tecsup.lms.shared.domain.event.DomainEvent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class CourseComment {

    private String courseId;
    private String userId;
    private String commentId;
    private String comment;
    private int rating;

    private final List<DomainEvent> changes = new ArrayList<>();

    // Constructor privado para reconstrucción
    private CourseComment() {}

    // Factory method para crear un nuevo comentario
    public static CourseComment add(String courseId, String userId, String comment, int rating) {
        CourseComment courseComment = new CourseComment();
        courseComment.apply(new CommentAddedEvent(courseId, userId, UUID.randomUUID().toString(), comment, rating));
        return courseComment;
    }

    // Método para editar comentario
    public void edit(String comment, int rating) {
        apply(new CommentEditedEvent(this.commentId, comment, rating));
    }

    // Método para aplicar eventos y reconstruir el estado
    private void apply(DomainEvent event) {
        if (event instanceof CommentAddedEvent e) {
            this.courseId = e.getCourseId();
            this.userId = e.getUserId();
            this.commentId = e.getCommentId();
            this.comment = e.getComment();
            this.rating = e.getRating();
        } else if (event instanceof CommentEditedEvent e) {
            this.comment = e.getComment();
            this.rating = e.getRating();
        }
        changes.add(event);
    }

    public static CourseComment reconstruct(List<DomainEvent> events) {
        CourseComment courseComment = new CourseComment();
        events.forEach(event -> {
            courseComment.applyChange(event);
        });
        courseComment.changes.clear();
        return courseComment;
    }

    private void applyChange(DomainEvent event) {
         if (event instanceof CommentAddedEvent e) {
            this.courseId = e.getCourseId();
            this.userId = e.getUserId();
            this.commentId = e.getCommentId();
            this.comment = e.getComment();
            this.rating = e.getRating();
        } else if (event instanceof CommentEditedEvent e) {
            this.comment = e.getComment();
            this.rating = e.getRating();
        }
    }

    public List<DomainEvent> getUncommittedChanges() {
        return new ArrayList<>(changes);
    }

    public void markChangesAsCommitted() {
        changes.clear();
    }
}
