#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import ${package}.data.vo.v1.PersonVO;
import ${package}.services.PersonServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Api(value = "Person Endpoint", description = "Description for person", tags = {"Person Endpoint"})
//@CrossOrigin
@Api(tags = "PersonEndpoint")
@RestController
@RequestMapping("/api/person/v1")
public class PersonController {

    @Autowired
    private PersonServices service;

    @Autowired
    PagedResourcesAssembler<PersonVO> assembler;

    @ApiOperation(value = "Find all people")
    @GetMapping(produces =  { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity<?> findAll(
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

        PagedResources<?> resources = assembler.toResource(persons);

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @ApiOperation(value = "Find person by first name")
    @GetMapping(value = "/findPersonByName/{firstName}", produces =  { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity<?> findPersonByName(
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

        PagedResources<?> resources = assembler.toResource(persons);

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

//    @CrossOrigin(origins = "http://localhost:8080")
    @ApiOperation(value = "Find a person by ID")
    @GetMapping(value = "/{id}", produces =  { "application/json", "application/xml", "application/x-yaml" })
    public PersonVO findById(@PathVariable("id") Long id) {
        PersonVO personVO = service.findById(id);
        personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return personVO;
    }

//    @CrossOrigin(origins = {"http://localhost:8080", "http://www.erudio.com.br"})
    @ApiOperation(value = "Create a person")
    @PostMapping(produces = { "application/json", "application/xml", "application/x-yaml" },
                 consumes = { "application/json", "application/xml", "application/x-yaml" })
    public PersonVO create(@RequestBody PersonVO person) {
        PersonVO personVO = service.create(person);
        personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());
        return personVO;
    }

    @ApiOperation(value = "Update a person")
    @PutMapping(produces = { "application/json", "application/xml", "application/x-yaml" },
                consumes = { "application/json", "application/xml", "application/x-yaml" })
    public PersonVO update(@RequestBody PersonVO person) {
        PersonVO personVO = service.update(person);
        personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());
        return personVO;
    }

    @ApiOperation(value = "Disable a person by ID")
    @PatchMapping(value = "/{id}", produces =  { "application/json", "application/xml", "application/x-yaml" })
    public PersonVO disablePerson(@PathVariable("id") Long id) {
        PersonVO personVO = service.disablePerson(id);
        personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return personVO;
    }

    @ApiOperation(value = "Delete a person by ID")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
