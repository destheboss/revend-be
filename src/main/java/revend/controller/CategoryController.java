package revend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import revend.domain.Category;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @GetMapping
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = Arrays.stream(Category.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(categories);
    }
}