package com.matrix.rpo.domain.game

import grails.gorm.annotation.Entity
import neo.script.gorm.general.initializer.InitializeDomian
import org.grails.gorm.graphql.entity.dsl.GraphQLMapping


@Entity
@InitializeDomian(profiles = 'dev', depends = Project)
class Scene {
    String id
    String name
    String code
    Boolean enabled

    static belongsTo = [project: Project]
    static hasMany = [gameScenes: GameScene]

    static mapping = {
        project fetch: 'join', lazy: false
    }

    static graphql = GraphQLMapping.build {
        description "游戏场景 - Project包含多个Scene，Game发起时可选取enabled=true的多个"
        code description: '场景代码，需和训练模型做对应'
        project description: '场景代码，需和训练模型做对应'
    }

    static initList = [
            new Scene(name: '测试场景001', code: 't001', enabled: true, project: Project.TEST_PROJECT),
            new Scene(name: '测试场景002', code: 't002', enabled: true, project: Project.TEST_PROJECT),
            new Scene(name: '测试场景003', code: 't003', enabled: true, project: Project.TEST_PROJECT),
            new Scene(name: '测试场景004', code: 't004', enabled: true, project: Project.TEST_PROJECT),
            new Scene(name: '测试场景005', code: 't005', enabled: true, project: Project.TEST_PROJECT)
    ]
}
