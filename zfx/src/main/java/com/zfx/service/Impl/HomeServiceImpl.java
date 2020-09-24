package com.zfx.service.Impl;

import com.github.pagehelper.PageHelper;
import com.macro.mall.mapper.*;
import com.macro.mall.model.*;
import com.zfx.dao.HomeDao;
import com.zfx.domain.FlashPromotionProduct;
import com.zfx.domain.HomeContentResult;
import com.zfx.domain.HomeFlashPromotion;
import com.zfx.service.HomeService;
import com.zfx.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private SmsHomeAdvertiseMapper advertiseMapper;
    @Autowired
    private HomeDao homeDao;
    @Autowired
    private SmsFlashPromotionMapper flashPromotionMapper;
    @Autowired
    private SmsFlashPromotionSessionMapper flashPromotionSessionMapper;
    @Autowired
    private PmsProductCategoryMapper  productCategoryMapper;
    @Autowired
    private    CmsSubjectMapper cmsSubjectMapper;
    @Override
    public HomeContentResult content() {

        HomeContentResult result = new HomeContentResult();
        result.setAdvertiseList(getHomeAdvertise());
        //推荐品牌
         result.setBrandList(homeDao.getRecommendBrandList(0,5));
        //当前秒杀场次
          result.setHomeFlashPromotion(getHomeFlashPromotion());
        //新品推荐
          result.setNewProductList(homeDao.getNewProductList(0,5));
        //人气推荐
         result.setHotProductList(homeDao.getHotProductList(0,5));
        //推荐专题
         result.setSubjectList(homeDao.getRecommendSubjectList(0,5));
        return result;
    }

    private HomeFlashPromotion getHomeFlashPromotion() {
        Date now = new Date();
        HomeFlashPromotion homeFlashPromotion = new HomeFlashPromotion();
        SmsFlashPromotion flashPromotion = getFlashPromotion(now);//当前日期
        if (flashPromotion != null) {
            //获取当前秒杀场次
            SmsFlashPromotionSession flashPromotionSession = getFlashPromotionSession(now);
            if (flashPromotionSession != null) {
                homeFlashPromotion.setStartTime(flashPromotionSession.getStartTime());
                homeFlashPromotion.setEndTime(flashPromotionSession.getEndTime());
                //获取下一个秒杀场次
                SmsFlashPromotionSession nextSession = getNextFlashPromotionSession(homeFlashPromotion.getStartTime());
                if(nextSession!=null){
                    homeFlashPromotion.setNextStartTime(nextSession.getStartTime());
                    homeFlashPromotion.setNextEndTime(nextSession.getEndTime());
                }
                //获取秒杀商品
                List<FlashPromotionProduct> flashProductList = homeDao.getFlashProductList(flashPromotion.getId(), flashPromotionSession.getId());
                homeFlashPromotion.setProductList(flashProductList);

    }
        }
                return homeFlashPromotion;
    }

    private SmsFlashPromotionSession getNextFlashPromotionSession(Date date) {
        SmsFlashPromotionSessionExample sessionExample = new SmsFlashPromotionSessionExample();
        sessionExample.createCriteria()
                .andStartTimeGreaterThan(date);
        sessionExample.setOrderByClause("start_time asc");
        List<SmsFlashPromotionSession> promotionSessionList = flashPromotionSessionMapper.selectByExample(sessionExample);
        if (!CollectionUtils.isEmpty(promotionSessionList)) {
            return promotionSessionList.get(0);
        }
        return null;
    }

    private SmsFlashPromotionSession getFlashPromotionSession(Date now) {
        Date currDate= DateUtil.getTime(now);//得到当前date的时分秒
        SmsFlashPromotionSessionExample example = new SmsFlashPromotionSessionExample();
        example.createCriteria()
                .andStartTimeLessThanOrEqualTo(currDate)
                .andEndTimeGreaterThanOrEqualTo(currDate);
        List<SmsFlashPromotionSession> FlashPromotionSessions = flashPromotionSessionMapper
                .selectByExample(example);
        if (!CollectionUtils.isEmpty(FlashPromotionSessions)) {
            return FlashPromotionSessions.get(0);
        }
        return null;

    }

    private SmsFlashPromotion getFlashPromotion(Date now) {
        SmsFlashPromotionExample example = new SmsFlashPromotionExample();
        example.createCriteria().andStartDateLessThanOrEqualTo(now)
                .andEndDateGreaterThanOrEqualTo(now).andStatusEqualTo(1);
        List<SmsFlashPromotion> smsFlashPromotions = flashPromotionMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(smsFlashPromotions)){
            return smsFlashPromotions.get(0);
        }
        return null;
    }


    private List<SmsHomeAdvertise> getHomeAdvertise() {
        SmsHomeAdvertiseExample example = new SmsHomeAdvertiseExample();
        example.createCriteria().andStatusEqualTo(1).andTypeEqualTo(1);
        example.setOrderByClause("sort desc");
        return advertiseMapper.selectByExample(example);
    }

    @Override
    public List<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        return homeDao.getHotProductList(0,Integer.MAX_VALUE);
    }

    @Override
    public List<PmsProductCategory> getProductCateList(Long parentId) {
        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.createCriteria()
                .andShowStatusEqualTo(1)
                .andParentIdEqualTo(parentId);
        example.setOrderByClause("sort desc");
        return productCategoryMapper.selectByExample(example);

    }

    @Override
    public List<CmsSubject> getSubjectList(Long cateId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        CmsSubjectExample cmsSubjectExample = new CmsSubjectExample();
        cmsSubjectExample.createCriteria().andShowStatusEqualTo(1)
                .andCategoryIdEqualTo(cateId);

        return cmsSubjectMapper.selectByExample(cmsSubjectExample);
    }
}
