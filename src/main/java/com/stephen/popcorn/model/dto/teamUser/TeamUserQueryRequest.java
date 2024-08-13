package com.stephen.popcorn.model.dto.teamUser;

import com.stephen.popcorn.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 查询队伍-用户请求
 *
 * @author stephen qiu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TeamUserQueryRequest extends PageRequest implements Serializable {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 用户id
     */
    private Long userId;
    
    /**
     * 队伍id
     */
    private Long teamId;
    

    private static final long serialVersionUID = 1L;
}