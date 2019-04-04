package neo.script.gorm.general.graphql.schema

import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLInputType
import graphql.schema.GraphQLList
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLTypeReference
import neo.script.gorm.general.service.MenuNode
import neo.script.gorm.graphql.schema.GraphQLTypeRegister
import org.grails.gorm.graphql.types.GraphQLTypeManager
import org.springframework.stereotype.Component

import static graphql.schema.GraphQLFieldDefinition.*

@Component
class GormGeneralTypeRegister implements GraphQLTypeRegister {
    @Override
    void doRegister(GraphQLTypeManager typeManager) {
        def fields = [newFieldDefinition().name('menu').type(new GraphQLTypeReference('Menu')).build(),
                      newFieldDefinition().name("subMenus")
                              .type(GraphQLList.list(new GraphQLTypeReference("MenuNode"))).build()]
        GraphQLObjectType menuNodeType = new MenuNodeType("MenuNode", "菜单树", fields)
        typeManager.registerType(MenuNode, menuNodeType)
    }
}

/**
 * 下面这个方法存在问题，一定要求GraphQLInputType，不然可以直接用GraphQLObjectType#newObject
 * @see org.grails.gorm.graphql.entity.dsl.helpers.Typed#resolveType
 * @see graphql.schema.GraphQLObjectType#newObject()
 */
class MenuNodeType extends GraphQLObjectType implements GraphQLInputType {
    MenuNodeType(String name, String description, List<GraphQLFieldDefinition> fieldDefinitions) {
        super(name, description, fieldDefinitions, [])
    }
}
