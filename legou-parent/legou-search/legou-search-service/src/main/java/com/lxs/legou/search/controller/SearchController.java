package com.lxs.legou.search.controller;

import com.lxs.legou.search.po.SearchRequest;
import com.lxs.legou.search.po.SearchResult;
import com.lxs.legou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/query")
    public ResponseEntity<SearchResult> queryGoodsByPage(@RequestBody SearchRequest searchRequest) {
        SearchResult result = searchService.search(searchRequest);

        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(result);
    }

}
