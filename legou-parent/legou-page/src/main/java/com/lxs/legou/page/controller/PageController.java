package com.lxs.legou.page.controller;

import com.lxs.legou.page.service.PageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/page")
@CrossOrigin
public class PageController {

    private final PageService pageService;

    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    @RequestMapping("/createHtml/{id}")
    public ResponseEntity<String> createHtml(@PathVariable(name = "id") Long id) {
        pageService.createPageHtml(id);
        return ResponseEntity.ok("生成成功");
    }

    @DeleteMapping("/deleteHtml/{id}")
    public ResponseEntity<String> deleteHtml(@PathVariable(name = "id") Long id) {
        pageService.removePageHtml(id);
        return ResponseEntity.ok("删除成功");
    }

}
