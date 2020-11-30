package com.bagguo.mydamai.ui.topic;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Entity：表明这个实体类会在数据库中生成一个与之相对应的表，其中可配置项：
 *
 * nameInDb：可以自定义表名，表明该实体对应数据库中的那张表，默认为实体类名；
 *
 * indexes：定义索引，这里可跨越多个列；
 *
 * createInDb：如果是有多个实体都关联这个表，可以把多余的实体里面设置为false避免重复创建（默认是true）；
 *
 * schema：一个项目中有多个schema时，表明要让这个dao属于哪个schema；
 *
 * active：是否应该生成更新/删除/刷新方法。如果Entity定义了 @ToOne 或 @ToMany关系，那么独立于该值是有效的。意为是否支持实体类之间update，refresh，delete等操作。
 */
@Entity
public class FeedArticleBean {
    /**
     * apkLink: "",
     * audit: 1,
     * author: "",
     * canEdit: false,
     * chapterId: 502,
     * chapterName: "自助",
     * collect: false,
     * courseId: 13,
     * desc: "",
     * descMd: "",
     * envelopePic: "",
     * fresh: true,
     * id: 16049,
     * link: "https://mp.weixin.qq.com/s/fEgSymIZMm82T9CrLueIKA",
     * niceDate: "2小时前",
     * niceShareDate: "2小时前",
     * origin: "",
     * prefix: "",
     * projectLink: "",
     * publishTime: 1605143247000,
     * realSuperChapterId: 493,
     * selfVisible: 0,
     * shareDate: 1605143247000,
     * shareUser: "飞洋",
     * superChapterId: 494,
     * superChapterName: "广场Tab",
     * tags:[],
     * title: "🔥“终于懂了“系列：Jetpack AAC完整解析（-）Lifecycle 完全掌握！",
     * type: 0,
     * userId: 31360,
     * visible: 1,
     * zan: 0
     */

    @Id(autoincrement = true)
    private Long id;

    @Index(unique = true)//设置唯一性
    private String key;//url
    private Long time;//时间

    private String apkLink;
    private int audit;
    private String title;
    private String link;
    private String niceDate;

    @Generated(hash = 831611858)
    public FeedArticleBean(Long id, String key, Long time, String apkLink,
            int audit, String title, String link, String niceDate) {
        this.id = id;
        this.key = key;
        this.time = time;
        this.apkLink = apkLink;
        this.audit = audit;
        this.title = title;
        this.link = link;
        this.niceDate = niceDate;
    }

    @Generated(hash = 211181177)
    public FeedArticleBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getApkLink() {
        return apkLink;
    }

    public void setApkLink(String apkLink) {
        this.apkLink = apkLink;
    }

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNiceDate() {
        return niceDate;
    }

    public void setNiceDate(String niceDate) {
        this.niceDate = niceDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
