package com.tecsup.lms.comments.infrastructure;

import com.tecsup.lms.comments.application.AddCommentCommand;
import com.tecsup.lms.comments.application.CommentCommandHandler;
import com.tecsup.lms.comments.application.EditCommentCommand;
import com.tecsup.lms.comments.domain.CourseComment;
import com.tecsup.lms.shared.domain.event.DomainEvent;
import com.tecsup.lms.shared.infrastructure.eventsourcing.EventStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentCommandHandler commentCommandHandler;
    private final EventStore eventStore;

    public CommentController(CommentCommandHandler commentCommandHandler, EventStore eventStore) {
        this.commentCommandHandler = commentCommandHandler;
        this.eventStore = eventStore;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> addComment(@RequestBody AddCommentCommand command) {
        String commentId = commentCommandHandler.handle(command);
        return ResponseEntity.ok(Map.of(
            "message", "Comment added successfully",
            "commentId", commentId
        ));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Map<String, String>> editComment(
            @PathVariable String commentId,
            @RequestBody EditCommentCommand command) {
        
        EditCommentCommand finalCommand = new EditCommentCommand(commentId, command.getComment(), command.getRating());
        
        commentCommandHandler.handle(finalCommand);
        return ResponseEntity.ok(Map.of("message", "Comentario agregado exitosamente"));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CourseComment> getComment(@PathVariable String commentId) {
        List<DomainEvent> events = eventStore.getEvents(commentId);
        if (events == null || events.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CourseComment comment = CourseComment.reconstruct(events);
        return ResponseEntity.ok(comment);
    }
}
