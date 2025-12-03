package fr.mkadia.mkadiaapi.controllers.user;

import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.dtos.ReviewDTO;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.services.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ElementsOfPageDTO<ReviewDTO>> getReviews(@RequestParam Long productId,
                                                                       @RequestParam(name = "page" , defaultValue = "0") int page,
                                                                       @RequestParam(name = "size" , defaultValue = "5")int size) {
        return ResponseEntity.ok(reviewService.getReviews(productId, page, size));
    }
    @PostMapping
    public ResponseEntity<?> saveReview(@RequestBody ReviewDTO reviewDTO, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(reviewService.saveReview(reviewDTO, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.deleteReview(id));
    }
}
