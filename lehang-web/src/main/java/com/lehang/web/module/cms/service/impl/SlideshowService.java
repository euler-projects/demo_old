package com.lehang.web.module.cms.service.impl;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lehang.web.module.cms.dao.ISlideshowDao;
import com.lehang.web.module.cms.entity.Slideshow;
import com.lehang.web.module.cms.service.ISlideshowService;

import net.eulerform.common.FileReader;
import net.eulerform.common.GlobalProperties;
import net.eulerform.common.GlobalPropertyReadException;
import net.eulerform.common.StringTool;
import net.eulerform.web.core.base.service.impl.BaseService;
import net.eulerform.web.core.exception.MultipartFileSaveException;
import net.eulerform.web.core.util.WebFileTool;

@Service
public class SlideshowService extends BaseService implements ISlideshowService {
    
    @Resource private ISlideshowDao slideshowDao;

    @Override
    public List<Slideshow> loadSlideshow() {
        List<Slideshow> data = this.slideshowDao.loadSlideshow();
        return data;
    }

    @Override
    public void saveSlideshow(List<MultipartFile> img, List<String> url) {
        for(int i = 0; i < img.size(); i++) {
            this.saveSlideshowByOrder(img.get(i), url.get(i), i);
            
        }
    }
    
    private void saveSlideshowByOrder(MultipartFile img, String url, int order){
        Slideshow slideshow = this.slideshowDao.findSlideshowByOrder(order);
        
        if(slideshow == null) {
            slideshow = new Slideshow();
            slideshow.setOrder(order);
        }
        slideshow.setUrl(url);
        
        if(img != null && img.getSize() > 0){
            
            try {
                String uploadPath = this.getServletContext().getRealPath(GlobalProperties.get(GlobalProperties.UPLOAD_PATH));
                
                //删除旧图片
                if(!StringTool.isNull(slideshow.getId())) {
                    String filePath = uploadPath+"/"+slideshow.getImgFileName();
                    FileReader.deleteFile(new File(filePath));
                }
                
                File savedFile = WebFileTool.saveMultipartFile(img);
                slideshow.setImgFileName(savedFile.getName());
            } catch (MultipartFileSaveException | GlobalPropertyReadException e) {
                throw new RuntimeException(e);
            }
        }

        this.slideshowDao.saveOrUpdate(slideshow);
    }
}