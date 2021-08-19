package com.exampleebook1.springboot.controller;

import com.exampleebook1.springboot.model.ProdutoModel;
import com.exampleebook1.springboot.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProdutoController {

    @Autowired
    ProdutoRepository produtoRepository;

    @GetMapping("/produtos")
    public ResponseEntity <List<ProdutoModel>> getAllProdutos(){
        List <ProdutoModel> produtosList = produtoRepository.findAll();
        if (produtosList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            for (ProdutoModel produto : produtosList){
                Long id = produto.getId();
                produto.add(linkTo(methodOn(ProdutoController.class).getOneProdutos(id)).withSelfRel());
            }
            return new ResponseEntity<List<ProdutoModel>>(produtosList,HttpStatus.OK);
        }
    }

    @GetMapping("/produtos/{id}")
    public ResponseEntity <ProdutoModel> getOneProdutos(@PathVariable(value = "id")Long id){
        Optional<ProdutoModel> produtoO = produtoRepository.findById(id);
        if (!produtoO.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            produtoO.get().add(linkTo(methodOn(ProdutoController.class).getAllProdutos()).withRel("Lista de Produtos"));
            return new ResponseEntity<ProdutoModel>(produtoO.get(),HttpStatus.OK);

        }
    }

    @PostMapping("/produtos")
    public ResponseEntity <ProdutoModel> saveProduto(@RequestBody @Valid ProdutoModel produto){
        return new ResponseEntity<ProdutoModel>(produtoRepository.save(produto),HttpStatus.CREATED);
    }

    @DeleteMapping("/produtos/{id}")
    public ResponseEntity <ProdutoModel> deleteProdutos(@PathVariable(value = "id")Long id){
        Optional<ProdutoModel> produtoO = produtoRepository.findById(id);
        if (!produtoO.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            produtoRepository.delete(produtoO.get());
            return new ResponseEntity<>(HttpStatus.OK);

        }
    }
    @PutMapping("produtos/{id}")
    public ResponseEntity <ProdutoModel> updateProdutos(@PathVariable(value = "id")Long id,
                                                        @RequestBody @Valid ProdutoModel produto){
        Optional<ProdutoModel> produtoO = produtoRepository.findById(id);
        if (!produtoO.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            produto.setId((produtoO.get().getId()));
            return new ResponseEntity<ProdutoModel>(produtoRepository.save(produto),HttpStatus.OK);

        }
    }


}
