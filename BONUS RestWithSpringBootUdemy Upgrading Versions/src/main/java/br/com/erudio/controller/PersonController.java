package br.com.erudio.controller;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.services.PersonServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//@Api(value = "Person Endpoint", description = "Description for person", tags = {"Person Endpoint"})
//@CrossOrigin
@Tag(name = "Person Endpoint")
@RestController
@RequestMapping("/api/person/v1")
public class PersonController {

    @Autowired
    private PersonServices service;

    @Operation(summary = "Find all people")
    @GetMapping(produces =  { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity<CollectionModel<PersonVO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "12") int limit,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));

        Page<PersonVO> persons = service.findAll(pageable);
        persons.stream()
               .forEach(p -> p.add(
                       linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()
                    )
               );

        Link findAllLink = linkTo(methodOn(PersonController.class).findAll(page, limit, direction)).withSelfRel();
        return ResponseEntity.ok(CollectionModel.of(persons, findAllLink));
    }

    @Operation(summary = "Find person by first name")
    @GetMapping(value = "/findPersonByName/{firstName}", produces =  { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity<CollectionModel<PersonVO>> findPersonByName(
            @PathVariable("firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "12") int limit,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));

        Page<PersonVO> persons = service.findPersonByName(firstName, pageable);
        persons.stream()
                .forEach(p -> p.add(
                        linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()
                        )
                );

        return ResponseEntity.ok(CollectionModel.of(persons));
    }

//    @CrossOrigin(origins = "http://localhost:8080")
    @Operation(summary = "Find a person by ID")
    @GetMapping(value = "/{id}", produces =  { "application/json", "application/xml", "application/x-yaml" })
    public PersonVO findById(@PathVariable("id") Long id) {
        PersonVO personVO = service.findById(id);
        personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return personVO;
    }

//    @CrossOrigin(origins = {"http://localhost:8080", "http://www.erudio.com.br"})
    @Operation(summary = "Create a person")
    @PostMapping(produces = { "application/json", "application/xml", "application/x-yaml" },
                 consumes = { "application/json", "application/xml", "application/x-yaml" })
    public PersonVO create(@RequestBody PersonVO person) {
        PersonVO personVO = service.create(person);
        personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());
        return personVO;
    }

    @Operation(summary = "Update a person")
    @PutMapping(produces = { "application/json", "application/xml", "application/x-yaml" },
                consumes = { "application/json", "application/xml", "application/x-yaml" })
    public PersonVO update(@RequestBody PersonVO person) {
        PersonVO personVO = service.update(person);
        personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());
        return personVO;
    }

    @Operation(summary = "Disable a person by ID")
    @PatchMapping(value = "/{id}", produces =  { "application/json", "application/xml", "application/x-yaml" })
    public PersonVO disablePerson(@PathVariable("id") Long id) {
        PersonVO personVO = service.disablePerson(id);
        personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return personVO;
    }

    @Operation(summary = "Delete a person by ID")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
