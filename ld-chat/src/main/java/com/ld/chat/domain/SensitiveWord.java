/*
 * THIS FILE IS PART OF Zhejiang LiShi Technology CO.,LTD.
 * Copyright (c) 2019-2023  Zhejiang LiShi Technology CO.,LTD.
 * It is forbidden to distribute or copy the code under this software without the consent of the Zhejiang LiShi Technology
 *
 *     https://www.lishiots.com/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.ld.chat.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
    * 敏感词信息明细表
    */
@TableName("saas_sensitive_word")
public class SensitiveWord {
    
    /**
    * 主键
    */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
    * 敏感词
    */
    private String name;
    
    /**
    * 排序字段,默认0
    */
    private Integer sort;
    
    /**
    * 创建人
    */
    private Long creator;
    
    /**
    * 创建时间
    */
    private Date createTime;
    
    /**
    * 修改人
    */
    private Long modifier;
    
    /**
    * 修改时间
    */
    private Date modifiedTime;
    
    /**
    * 租户id
    */
    private String tenantId;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getSort() {
        return sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    
    public Long getCreator() {
        return creator;
    }
    
    public void setCreator(Long creator) {
        this.creator = creator;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Long getModifier() {
        return modifier;
    }
    
    public void setModifier(Long modifier) {
        this.modifier = modifier;
    }
    
    public Date getModifiedTime() {
        return modifiedTime;
    }
    
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
    
    public String getTenantId() {
        return tenantId;
    }
    
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}