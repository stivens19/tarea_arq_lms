package com.tecsup.lms.comments.application;

import com.tecsup.lms.comments.domain.CourseComment;
import com.tecsup.lms.shared.domain.event.DomainEvent;
import com.tecsup.lms.shared.infrastructure.eventsourcing.EventStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentCommandHandler {

    private final EventStore eventStore;

    public CommentCommandHandler(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public String handle(AddCommentCommand command) {
        CourseComment courseComment = CourseComment.add(
                command.getCourseId(),
                command.getUserId(),
                command.getComment(),
                command.getRating()
        );

        courseComment.getUncommittedChanges().forEach(
                event -> eventStore.save(courseComment.getCommentId(), event)
        );

        courseComment.markChangesAsCommitted();
        
        return courseComment.getCommentId();
    }

    public void handle(EditCommentCommand command) {
        List<DomainEvent> events = eventStore.getEvents(command.getCommentId());
        
        if (events == null || events.isEmpty()) {
            throw new RuntimeException("Comment not found with ID: " + command.getCommentId());
        }

        CourseComment courseComment = CourseComment.reconstruct(events);
        
        courseComment.edit(command.getComment(), command.getRating());

        courseComment.getUncommittedChanges().forEach(
                event -> eventStore.save(courseComment.getCommentId(), event)
        );

        courseComment.markChangesAsCommitted();
    }
}
