package com.tecsup.lms.comments.application;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditCommentCommand {
    private String commentId;
    private String comment;
    private int rating;
}
