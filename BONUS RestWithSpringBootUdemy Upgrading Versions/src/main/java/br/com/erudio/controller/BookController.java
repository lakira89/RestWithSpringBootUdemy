package br.com.erudio.controller;

import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.services.BookServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//@Api(value = "Book Endpoint", description = "Description for book", tags = {"Book Endpoint"})
@Tag(name = "Book Endpoint")
@RestController
@RequestMapping("/api/book/v1")
public class BookController {

    @Autowired
    private BookServices service;

    @Operation(summary = "Find all books")
    @GetMapping(produces =  { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity<CollectionModel<BookVO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "12") int limit,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "title"));

        Page<BookVO> books = service.findAll(pageable);
        books.stream()
               .forEach(b -> b.add(
                       linkTo(methodOn(BookController.class).findById(b.getKey())).withSelfRel()
                    )
               );

        return ResponseEntity.ok(CollectionModel.of(books));
    }

    @Operation(summary = "Find book by title")
    @GetMapping(value = "/findBookByTitle/{title}", produces =  { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity<CollectionModel<BookVO>> findBookByTitle(
            @PathVariable(value = "title") String title,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "12") int limit,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "title"));

        Page<BookVO> books = service.findBookByTitle(title, pageable);
        books.stream()
                .forEach(b -> b.add(
                        linkTo(methodOn(BookController.class).findById(b.getKey())).withSelfRel()
                        )
                );

        return ResponseEntity.ok(CollectionModel.of(books));
    }

    @Operation(summary = "Find a book by ID")
    @GetMapping(value = "/{id}", produces =  { "application/json", "application/xml", "application/x-yaml" })
    public BookVO findById(@PathVariable("id") Long id) {
        BookVO bookVO = service.findById(id);
        bookVO.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return bookVO;
    }

    @Operation(summary = "Create a book")
    @PostMapping(produces = { "application/json", "application/xml", "application/x-yaml" },
                 consumes = { "application/json", "application/xml", "application/x-yaml" })
    public BookVO create(@RequestBody BookVO book) {
        BookVO bookVO = service.create(book);
        bookVO.add(linkTo(methodOn(BookController.class).findById(bookVO.getKey())).withSelfRel());
        return bookVO;
    }

    @Operation(summary = "Update a book")
    @PutMapping(produces = { "application/json", "application/xml", "application/x-yaml" },
                consumes = { "application/json", "application/xml", "application/x-yaml" })
    public BookVO update(@RequestBody BookVO book) {
        BookVO bookVO = service.update(book);
        bookVO.add(linkTo(methodOn(BookController.class).findById(bookVO.getKey())).withSelfRel());
        return bookVO;
    }

    @Operation(summary = "Delete a book by ID")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
