package com.lehang.web.module.cms.service.impl;

import java.io.File;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lehang.web.module.cms.dao.INewsDao;
import com.lehang.web.module.cms.entity.News;
import com.lehang.web.module.cms.service.INewsService;

import net.eulerform.common.BeanTool;
import net.eulerform.common.FileReader;
import net.eulerform.common.GlobalProperties;
import net.eulerform.common.GlobalPropertyReadException;
import net.eulerform.common.StringTool;
import net.eulerform.web.core.base.entity.PageResponse;
import net.eulerform.web.core.base.entity.QueryRequest;
import net.eulerform.web.core.base.service.impl.BaseService;
import net.eulerform.web.core.exception.MultipartFileSaveException;
import net.eulerform.web.core.util.WebFileTool;

@Service
public class NewsService extends BaseService implements INewsService {

    @Resource INewsDao newsDao;
    
    @Override
    public void saveNews(News news, MultipartFile img) throws MultipartFileSaveException {
        BeanTool.clearEmptyProperty(news);
        
        if(!StringTool.isNull(news.getId())){
            News tmp = this.newsDao.load(news.getId());
            if(tmp != null) {
                if(img != null && img.getSize() > 0){
                    String uploadPath;
                    try {
                        uploadPath = this.getServletContext().getRealPath(GlobalProperties.get(GlobalProperties.UPLOAD_PATH));
                    } catch (GlobalPropertyReadException e) {
                        throw new RuntimeException(e);
                    }
                    
                    //删除旧图片
                    if(news != null) {
                        String filePath = uploadPath+"/"+tmp.getImageFileName();
                        FileReader.deleteFile(new File(filePath));
                    }

                    File savedFile = WebFileTool.saveMultipartFile(img);
                    news.setImageFileName(savedFile.getName()); 
                } else {
                    news.setImageFileName(tmp.getImageFileName());
                }
            }
        }
        
        if(news.getPubDate() == null)
            news.setPubDate(new Date());

        if(news.getTop() == null)
            news.setTop(false);
        
        this.newsDao.saveOrUpdate(news);
    }

    @Override
    public PageResponse<News> findNewsByPage(QueryRequest queryRequest, int pageIndex, int pageSize, boolean loadText) {
        return this.newsDao.findNewsByPage(queryRequest, pageIndex, pageSize, loadText);
    }

    @Override
    public void deleteNews(String[] idArray) {
        this.newsDao.deleteByIds(idArray);
    }

}
