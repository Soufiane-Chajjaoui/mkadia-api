package fr.mkadia.mkadiaapi.services.review;

import fr.mkadia.mkadiaapi.dtos.ElementsOfPageDTO;
import fr.mkadia.mkadiaapi.dtos.ReviewDTO;
import fr.mkadia.mkadiaapi.entities.Product;
import fr.mkadia.mkadiaapi.entities.Review;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.exceptions.EntityNotFoundException;
import fr.mkadia.mkadiaapi.mappers.ReviewMapper;
import fr.mkadia.mkadiaapi.models.ResponseMessage;
import fr.mkadia.mkadiaapi.repositories.ProductRepository;
import fr.mkadia.mkadiaapi.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ProductRepository productRepository;
    public ElementsOfPageDTO<ReviewDTO> getReviews(int productId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(productId, pageRequest);
        ElementsOfPageDTO<ReviewDTO> reviewsDTO = ElementsOfPageDTO.<ReviewDTO>
                builder()
                .elementsDTO(reviews.stream().map(reviewMapper::fromEntity).toList())
                .pageSize(pageRequest.getPageSize())
                .totalPages(reviews.getTotalPages())
                .currentPage(pageRequest.getPageNumber())
                .totalRecords(reviews.getTotalElements())
                .build();
        return reviewsDTO;
    }

    public ReviewDTO saveReview(ReviewDTO reviewDTO, User user) {

        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(()-> new EntityNotFoundException("Product not found"));

        Review review = reviewMapper.fromDTO(reviewDTO);

        review.setProduct(product);
        review.setUser(user);

        Review reviewSaved = reviewRepository.save(review);
        return reviewMapper.fromEntity(reviewSaved);
    }

    public ResponseMessage deleteReview(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
        return ResponseMessage.builder()
                .message("Review has been deleted")
                .status(HttpStatus.OK.value())
                .build();    }
}
