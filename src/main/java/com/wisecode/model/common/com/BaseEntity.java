package com.wisecode.model.common.com;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Entity支持类
 */
@MappedSuperclass
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    // 删除标记（0：正常；1：删除）
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";

    // 显示/隐藏
    public static final String SHOW = "1";
    public static final String HIDE = "0";

    // 是/否
    public static final String YES = "1";
    public static final String NO = "0";

    // 状态状态（0：发布；1：作废；2：审核；）
    public static final String STATUS_RELEASE = "0";
    public static final String STATUS_DELETE = "1";
    public static final String STATUS_AUDIT = "2";

}
