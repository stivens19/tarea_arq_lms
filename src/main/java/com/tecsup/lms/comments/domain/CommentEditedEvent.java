package com.tecsup.lms.comments.domain;

import com.tecsup.lms.shared.domain.event.DomainEvent;
import lombok.Getter;

@Getter
public class CommentEditedEvent extends DomainEvent {

    private final String commentId;
    private final String comment;
    private final int rating;

    public CommentEditedEvent(String commentId, String comment, int rating) {
        super();
        this.commentId = commentId;
        this.comment = comment;
        this.rating = rating;
    }
}
