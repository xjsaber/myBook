package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xjsaber
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null){
            if (StringUtils.isNotBlank(product.getSubImages())){
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0){
                    product.setMainImage(subImageArray[0]);
                }
            }

            if (product.getId() != null){
                int row = productMapper.updateByPrimaryKey(product);
                if (row > 0){
                    ServerResponse.createBySuccess("更新产品成功");
                }
                else {
                    ServerResponse.createBySuccess("更新产品失败");
                }
            } else {
                int row = productMapper.insert(product);
                if (row > 0){
                    ServerResponse.createBySuccess("新增产品成功");
                }
                else {
                    ServerResponse.createBySuccess("新增产品失败");
                }
            }
        }
        return ServerResponse.createByErrorMessage("新增或更新产品参数不正确");
    }

    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setId(status);
        int row = productMapper.updateByPrimaryKey(product);
        if (row > 0){
            return ServerResponse.createBySuccessMessage("修改产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品销售状态失败");
    }

    @Override
    public ServerResponse<List<Product>> list(int categoryId, String keyword, int pageNum, int pageSize, String orderBy) {
        return null;
    }

    @Override
    public ServerResponse<ProductDetailVo> managerProductDetail(Integer productId) {
        if (productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }
        // VO对象--value object
        // pojo->bo(business object)->vo(view object)
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setName(product.getName());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setSubTitle(product.getSubtitle());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImage(product.getSubImages());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        // imageHost;
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        // parentCategoryId
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null){
            productDetailVo.setParentCategoryId(0);
        }
        else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        // createTime
        // updateTime

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }

    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        // startPage -- start
        // 填充自己的SQL查询逻辑
        // pageHelper -- 收尾
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectList();

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem : productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVoList);
        return ServerResponse.createBySuccess(pageResult);
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setName(product.getName());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubTitle(product.getSubtitle());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setStatus(product.getStatus());
        productListVo.setStock(product.getStock());

        return productListVo;
    }

}
