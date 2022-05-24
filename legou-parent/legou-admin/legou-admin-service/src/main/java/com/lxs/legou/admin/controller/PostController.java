package com.lxs.legou.admin.controller;

import com.github.pagehelper.PageInfo;
import com.lxs.legou.admin.po.Post;
import com.lxs.legou.admin.service.IPostService;
import com.lxs.legou.core.controller.BaseController;
import com.lxs.legou.core.json.JSON;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController extends BaseController<IPostService, Post> {

    /**
     * 演示使用JSON注解过滤属性
     * @return
     */
    @ApiOperation(value="查询", notes="查询所有")
    @RequestMapping(value = "/list", method = {RequestMethod.POST, RequestMethod.GET})
    @JSON(type = Post.class ,filter = "desc")
    public List<Post> list(Post post) {
        return service.list(post);
    }

    /**
     * 分页查询
     * @param entity
     * @param page
     * @param rows
     * @return
     */
    @ApiOperation(value="分页查询", notes="分页查询")
    @PostMapping("/list-page")
//    @PreAuthorize("hasRole('ADMIN')")
    public PageInfo<Post> listPage(Post entity,
                                   @RequestParam(name = "page", defaultValue = "1", required = false) int page,
                                   @RequestParam(name = "rows", defaultValue = "10", required = false) int rows) {
        return service.listPage(entity, page, rows);
    }

}
