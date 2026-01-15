package com.tecsup.lms.comments.application;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddCommentCommand {
    private String courseId;
    private String userId;
    private String comment;
    private int rating;
}
