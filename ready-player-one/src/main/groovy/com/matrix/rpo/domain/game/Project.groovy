package com.matrix.rpo.domain.game

import com.matrix.rpo.domain.crm.Customer
import grails.gorm.annotation.Entity

/**
 * 游戏项目
 *
 * 一个游戏项目对应一个训练模型
 * 一个项目可以多次发起活动
 */
@Entity
class Project {
    String id
    String name
    Customer customer
}
