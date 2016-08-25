package com.lehang.web.module.cms.service;

import org.springframework.web.multipart.MultipartFile;

import com.lehang.web.module.cms.entity.News;

import net.eulerform.web.core.base.entity.PageResponse;
import net.eulerform.web.core.base.entity.QueryRequest;
import net.eulerform.web.core.base.service.IBaseService;
import net.eulerform.web.core.exception.MultipartFileSaveException;

public interface INewsService extends IBaseService {

    public void saveNews(News news, MultipartFile img) throws MultipartFileSaveException;

    public PageResponse<News> findNewsByPage(QueryRequest queryRequest, int pageIndex, int pageSize);

    public void deleteNews(String[] idArray);

}