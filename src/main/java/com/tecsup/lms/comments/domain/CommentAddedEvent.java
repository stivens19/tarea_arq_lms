package com.tecsup.lms.comments.domain;

import com.tecsup.lms.shared.domain.event.DomainEvent;
import lombok.Getter;

@Getter
public class CommentAddedEvent extends DomainEvent {

    private final String courseId;
    private final String userId;
    private final String commentId;
    private final String comment;
    private final int rating;

    public CommentAddedEvent(String courseId, String userId, String commentId, String comment, int rating) {
        super();
        this.courseId = courseId;
        this.userId = userId;
        this.commentId = commentId;
        this.comment = comment;
        this.rating = rating;
    }
}
