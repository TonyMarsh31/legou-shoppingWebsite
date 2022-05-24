package com.lxs.legou.admin.service.impl;

import com.lxs.legou.admin.po.Post;
import com.lxs.legou.admin.service.IPostService;
import com.lxs.legou.core.service.impl.CrudServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl extends CrudServiceImpl<Post> implements IPostService {

}
