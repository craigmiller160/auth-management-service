package io.craigmiller160.authmanagementservice.config

import graphql.kickstart.tools.SchemaParser
import graphql.kickstart.tools.SchemaParserBuilder
import graphql.schema.GraphQLSchema
import graphql.schema.idl.SchemaGenerator
import io.craigmiller160.authmanagementservice.graphql.mutation.RoleMutationResolver
import io.craigmiller160.authmanagementservice.graphql.query.ClientQueryResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class GraphqlConfig {

    // TODO delete if not needed

//    @Autowired
//    private lateinit var clientQueryResolver: ClientQueryResolver
//
//    @Autowired
//    private lateinit var roleResolver: RoleMutationResolver
//
//    @Bean
//    fun schema(): GraphQLSchema {
//        return SchemaParserBuilder()
//                .files("graphql/schema.graphqls")
//                .resolvers(
//                        clientQueryResolver,
//                        roleResolver
//                )
//                .build()
//                .makeExecutableSchema()
//    }

}
